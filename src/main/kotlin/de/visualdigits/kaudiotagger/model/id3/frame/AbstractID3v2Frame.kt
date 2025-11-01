package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidTagException
import de.visualdigits.kaudiotagger.model.common.field.TagTextField
import de.visualdigits.kaudiotagger.model.common.frame.AbstractTagFrame
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyEncrypted
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyUnsupported
import de.visualdigits.kaudiotagger.util.EncodingFlags
import de.visualdigits.kaudiotagger.util.StatusFlags
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.ByteArrayOutputStream
import java.lang.reflect.Constructor
import java.lang.reflect.InvocationTargetException
import java.nio.ByteBuffer
import java.nio.charset.Charset

abstract class AbstractID3v2Frame: AbstractTagFrame, TagTextField {

    companion object {
        const val TYPE_FRAME: String = "frame"
        const val TYPE_FRAME_SIZE: String = "frameSize"
        const val UNSUPPORTED_ID: String = "Unsupported"

    }

    // Frame identifier
    private var identifier: String? = ""

    // Frame Size
    var frameSize: Int = 0

    /**
     * This holds the Status flags (not supported in v2.20
     */
    var statusFlags: StatusFlags? = null

    /**
     * This holds the Encoding flags (not supported in v2.20)
     */
    var encodingFlags: EncodingFlags? = null

    constructor() {
        frameBody?.header = this
    }

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
    constructor(identifier: String?) {
        this.identifier = identifier
        log.debug("Creating empty frame of type$identifier")

        // Use reflection to map id to frame body, which makes things much easier
        // to keep things up to date.
        try {
            val c = Class.forName("${AbstractID3v2FrameBody.FRAME_BODY_PACKAGE}.FrameBody$identifier") as Class<AbstractID3v2FrameBody>
            frameBody = c.getDeclaredConstructor().newInstance()
        } catch (cnfe: ClassNotFoundException) {
            log.error(cnfe.message)
            frameBody = FrameBodyUnsupported(identifier)
        } catch (ie: InstantiationException) { // Instantiate Interface/Abstract should not happen
            log.error("InstantiationException:$identifier", ie)
            throw java.lang.RuntimeException(ie)
        } catch (iae: IllegalAccessException) { // Private Constructor shouild not happen
            log.error("IllegalAccessException:$identifier", iae)
            throw java.lang.RuntimeException(iae)
        }
        
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
    @Suppress("UNCHECKED_CAST")    fun readBody(
        identifier: String?,
        body: AbstractID3v2FrameBody
    ): AbstractID3v2FrameBody {
        /* Use reflection to map id to frame body, which makes things much easier
         * to keep things up to date, although slight performance hit.
         */
        val frameBody: AbstractID3v2FrameBody
        try {
            val c = Class.forName("${AbstractID3v2FrameBody.FRAME_BODY_PACKAGE}.FrameBody$identifier") as Class<AbstractID3v2FrameBody>
            val constructorParameterTypes = arrayOf<Class<*>>(body.javaClass)
            val constructorParameterValues = arrayOf<Any?>(body)
            val construct: Constructor<AbstractID3v2FrameBody> = c.getConstructor(*constructorParameterTypes)
            frameBody = (construct.newInstance(*constructorParameterValues))
        } catch (_: ClassNotFoundException) {
            log.debug("Identifier not recognised:$identifier unable to create framebody")
            throw InvalidFrameException("FrameBody$identifier does not exist")
        } catch (sme: NoSuchMethodException) { // If suitable constructor does not exist
            log.error("No such method:" + sme.message, sme)
            throw InvalidFrameException("FrameBody$identifier does not have a constructor that takes:${body.javaClass.getName()}")
        } catch (ite: InvocationTargetException) {
            log.error("An error occurred within abstractID3v2FrameBody")
            log.error("Invocation target exception", ite.cause)
            throw InvalidFrameException(ite.cause?.message)
        } catch (ie: InstantiationException) { // Instantiate Interface/Abstract should not happen
            log.error("Instantiation exception", ie)
            throw RuntimeException(ie.message)
        } catch (iae: IllegalAccessException) { // Private Constructor shouild not happen
            log.error("Illegal access exception ", iae)
            throw RuntimeException(iae.message)
        }

        log.debug("frame Body created${frameBody.getIdentifier()}")
        frameBody.header = this

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

    /**
     * @param b
     */
    override fun isBinary(b: Boolean) {
        // do nothing because whether or not a field is binary is defined by its id and is immutable
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
    override fun getEncoding(): Charset? {
        val textEncoding = frameBody?.getTextEncoding()
        return TextEncoding.fromId(textEncoding?.toInt()?:0)?.charSet
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
    ): AbstractID3v2FrameBody {
        // Use reflection to map id to frame body, which makes things much easier
        // to keep things up to date,although slight performance hit.
        log.debug("Creating framebody:start")

        var frameBody: AbstractID3v2FrameBody
        try {
            val c = Class.forName("${AbstractID3v2FrameBody.FRAME_BODY_PACKAGE}.FrameBody$identifier") as Class<AbstractID3v2FrameBody>
            val constructorParameterTypes = arrayOf<Class<*>>(
                Class.forName("java.nio.ByteBuffer"),
                Integer.TYPE,
            )
            val constructorParameterValues = arrayOf<Any>(byteBuffer, frameSize)
            log.debug("constructorParameterTypes '{}': {}", identifier, constructorParameterTypes.toList())
            log.debug("constructorParameterValues '{}': {}", identifier, constructorParameterValues.toList())
            val construct: Constructor<AbstractID3v2FrameBody> = c.getConstructor(*constructorParameterTypes)
            frameBody = (construct.newInstance(*constructorParameterValues))
        } catch (_: ClassNotFoundException) { // No class defined for this frame type,use FrameUnsupported
            log.error("Identifier not recognised: '$identifier' using FrameBodyUnsupported")
            try {
                frameBody = FrameBodyUnsupported(byteBuffer, frameSize)
            } // read method to declare it can throw InvalidtagException // Should only throw InvalidFrameException but unfortunately legacy hierachy forces
            catch (ife: InvalidFrameException) {
                throw ife
            } catch (te: InvalidTagException) {
                throw InvalidFrameException(te.message)
            }
        } // propagate it up otherwise mark this frame as invalid // An error has occurred during frame instantiation, if underlying cause is an unchecked exception or error
        catch (e: InvocationTargetException) {
            throw InvalidFrameException("Could not invoke constructor", e)
        } catch (e: Exception) { // No Such Method should not happen
            throw IllegalStateException("Could not construct frame", e)
        }
        log.debug("Created framebody:end${frameBody.getIdentifier()}")
        frameBody.header = this
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