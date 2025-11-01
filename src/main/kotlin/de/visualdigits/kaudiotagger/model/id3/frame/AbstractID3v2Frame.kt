package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidTagException
import de.visualdigits.kaudiotagger.model.common.field.TagTextField
import de.visualdigits.kaudiotagger.model.common.frame.AbstractTagFrame
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyEncrypted
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyUnsupported
import de.visualdigits.kaudiotagger.util.EncodingFlags
import de.visualdigits.kaudiotagger.util.ReflectionUtils.callByteBufferConstructor
import de.visualdigits.kaudiotagger.util.ReflectionUtils.callCopyConstructor
import de.visualdigits.kaudiotagger.util.ReflectionUtils.callDefaultConstructor
import de.visualdigits.kaudiotagger.util.StatusFlags
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer
import java.nio.charset.Charset

abstract class AbstractID3v2Frame: AbstractTagFrame, TagTextField {

    companion object {

        const val TYPE_FRAME: String = "frame"
        const val TYPE_FRAME_SIZE: String = "frameSize"
        const val UNSUPPORTED_ID: String = "Unsupported"

    }

    private var identifier: String? = ""

    var frameSize: Int = 0

    /**
     * This holds the Status flags (not supported in v2.20
     */
    var statusFlags: StatusFlags? = null

    /**
     * This holds the Encoding flags (not supported in v2.20)
     */
    var encodingFlags: EncodingFlags? = null

    constructor()

    /**
     * Create a frame based on another frame
     *
     * @param frame
     */
    constructor(frame: AbstractID3v2Frame): super(frame)

    /**
     * Create a frame based on a body
     *
     * @param body
     */
    constructor(body: AbstractID3v2FrameBody) {
        this.frameBody = body
        this.frameBody?.header = this
    }
    /**
     * Create a new frame with empty body based on identifier
     *
     * @param identifier
     */
    // TODO the identifier checks should be done in the relevent subclasses
    @Suppress("UNCHECKED_CAST")
    constructor(identifier: String? = null) {
        this.identifier = identifier
        frameBody?.header = this

        log.debug("Creating empty frame of type$identifier")

        // Use reflection to map id to frame body, which makes things much easier
        // to keep things up to date.
        frameBody = callDefaultConstructor(identifier)
        if (frameBody == null) frameBody = FrameBodyUnsupported(identifier)
        frameBody?.header = this
        
        if (this is ID3v24Frame) {
            frameBody?.setTextEncoding(
                TagOptionSingleton.id3v24DefaultTextEncoding.id
            )
        } else if (this is ID3v23Frame) {
            frameBody?.setTextEncoding(
                TagOptionSingleton.id3v23DefaultTextEncoding.id
            )
        }

        log.debug("Created empty frame of type$identifier")
    }

    /**
     * Get the next frame id, throwing an exception if unable to do this and check against just having padded data
     *
     * @param byteBuffer
     * @return
     */
    fun readIdentifier(byteBuffer: ByteBuffer): String? {
        val buffer = ByteArray(getFrameIdSize())

        // Read the Frame Identifier
        if (getFrameIdSize() <= byteBuffer.remaining()) {
            byteBuffer[buffer, 0, getFrameIdSize()]
        }

        if (isPadding(buffer)) {
            log.debug("invalid frame '$identifier' - only padding found")
        }

        if ((getFrameHeaderSize() - getFrameIdSize()) > byteBuffer.remaining()) {
            log.warn(
                    "No space to find another frame:"
            )
            throw InvalidFrameException(
                "No space to find another frame"
            )
        }

        identifier = String(buffer)
        log.debug("Identifier is${this.getIdentifier()}")

        return this.getIdentifier()
    }

    /**
     * Return the frame identifier
     *
     * @return the frame identifier
     */
    override fun getIdentifier(): String? {
        return identifier
    }

    fun setIdentifier(identifier: String?) {
        this.identifier = identifier
    }

    /**
     * Read the frameBody when frame marked as encrypted
     *
     * @param identifier
     * @param byteBuffer
     * @param frameSize
     * @return
     */
    fun readEncryptedBody(
        identifier: String?,
        byteBuffer: ByteBuffer,
        frameSize: Int
    ): AbstractID3v2FrameBody {
        try {
            val frameBody: AbstractID3v2FrameBody = FrameBodyEncrypted(
                identifier,
                byteBuffer,
                frameSize
            )
            frameBody.header = this
            return frameBody
        } catch (ite: InvalidTagException) {
            throw InvalidDataTypeException(cause = ite)
        }
    }

    /**
     * This creates a new body based of type identifier but populated by the data
     * in the body. This is a different type to the body being created which is why
     * TagUtility.copyObject() can't be used. This is used when converting between
     * different versions of a tag for frames that have a non-trivial mapping such
     * as TYER in v3 to TDRC in v4. This will only work where appropriate constructors
     * exist in the frame body to be created, for example a FrameBodyTYER requires a constructor
     * consisting of a FrameBodyTDRC.
     *
     *
     * If this method is called and a suitable constructor does not exist then an InvalidFrameException
     * will be thrown
     *
     * @param identifier to determine type of the frame
     * @param body
     * @return newly created framebody for this type
     */
    @Suppress("UNCHECKED_CAST")
    fun readBody(
        identifier: String?,
        body: AbstractID3v2FrameBody?
    ): AbstractTagFrameBody? {
        if (body == null) {
            return null
        }
        /* Use reflection to map id to frame body, which makes things much easier
         * to keep things up to date, although slight performance hit.
         */
        val frameBody = callCopyConstructor(identifier, body)
        frameBody?.header = this

        log.debug("frame Body created${frameBody?.getIdentifier()}")

        return frameBody
    }


    override fun getRawContent(): ByteArray? {
        val baos = ByteArrayOutputStream()
        write(baos)
        return baos.toByteArray()
    }

    abstract fun write(tagBuffer: ByteArrayOutputStream)

    /**
     * Returns the content of the field.
     *
     *
     * For frames consisting of different fields, this will return the value deemed to be most
     * likely to be required
     *
     * @return Content
     */
    override fun getContent(): String? {
        return frameBody?.getUserFriendlyValue()
    }

    override fun isEmpty(): Boolean {
        return frameBody == null
    }

    /**
     * Sets the content of the field.
     *
     * @param content fields content.
     */
    override fun setContent(content: String) {
        throw UnsupportedOperationException(
            "Not implemented please use the generic tag methods for setting content"
        )
    }

    /**
     * Returns the current used charset encoding.
     *
     * @return Charset encoding.
     */
    override fun getEncoding(): Charset {
        return TextEncoding.fromId(frameBody?.getTextEncoding()?.toInt())?.charSet?: TextEncoding.ISO_8859_1.charSet
    }

    override fun setEncoding(encoding: Charset) {
        throw java.lang.UnsupportedOperationException("Not Implemented Yet")
    }

    /**
     * @return size in bytes of the frameid field
     */
    abstract fun getFrameIdSize(): Int

    /**
     * @return the size in bytes of the frame size field
     */
    abstract fun getFrameSizeSize(): Int

    /**
     * @return the size in bytes of the frame header
     */
    abstract fun getFrameHeaderSize(): Int

    open fun isPadding(buffer: ByteArray): Boolean {
        return (buffer.size >= 4 &&
                (buffer[0] == '\u0000'.code.toByte()) &&
                (buffer[1] == '\u0000'.code.toByte()) &&
                (buffer[2] == '\u0000'.code.toByte()) &&
                (buffer[3] == '\u0000'.code.toByte())
                )
    }

    /**
     * Read the frame body from the specified file via the buffer
     *
     * @param identifier the frame identifier
     * @param byteBuffer to read the frame body from
     * @param frameSize
     *
     * @return a newly created FrameBody
     */
    @Suppress("UNCHECKED_CAST")
    fun readBody(
        identifier: String?,
        byteBuffer: ByteBuffer,
        frameSize: Int
    ): AbstractTagFrameBody {
        // Use reflection to map id to frame body, which makes things much easier
        // to keep things up to date,although slight performance hit.
        log.debug("Creating framebody:start")

        var frameBody = callByteBufferConstructor(identifier, byteBuffer, frameSize)
        if (frameBody == null) frameBody = FrameBodyUnsupported(byteBuffer, frameSize)
        frameBody.header = this
        log.debug("Created framebody:end${frameBody.getIdentifier()}")

        return frameBody
    }

    /**
     * Return String Representation of frame
     */
    open fun createStructure() {
        MP3File.tagFormatter?.openHeadingElement(
            TYPE_FRAME,
            getIdentifier() ?:""
        )
        MP3File.tagFormatter?.closeHeadingElement(TYPE_FRAME)
    }
}