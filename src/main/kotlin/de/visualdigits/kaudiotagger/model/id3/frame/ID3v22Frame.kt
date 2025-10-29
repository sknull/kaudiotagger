package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.exceptions.EmptyFrameException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameIdentifierException
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyDeprecated
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyUnsupported
import de.visualdigits.kaudiotagger.model.id3.types.FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.util.ID3Tags
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.math.BigInteger
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class ID3v22Frame: AbstractID3v2Frame {

    companion object {
        const val FRAME_ID_SIZE: Int = 3
        const val FRAME_SIZE_SIZE: Int = 3
        val FRAME_HEADER_SIZE: Int = FRAME_ID_SIZE + FRAME_SIZE_SIZE
        val validFrameIdentifier: Pattern = Pattern.compile("[A-Z][0-9A-Z]{2}")


    }

    constructor()

    constructor(frameBody: AbstractID3v2FrameBody): super(frameBody)

    /**
     * Creates a new ID3v22Frame datatype by reading from byteBuffer.
     *
     * @param byteBuffer      to read from
     */
    constructor(byteBuffer: ByteBuffer): this() {
        read(byteBuffer)
    }

    /**
     * Creates a new ID3v22 Frame of type identifier.
     *
     *
     * An empty body of the correct type will be automatically created. This constructor should be used when wish to
     * create a new frame from scratch using user values
     *
     * @param identifier
     */
    constructor(identifier: String): super(identifier) {
        log.debug("Creating empty frame of type$identifier")
        var bodyIdentifier = identifier

        //If dealing with v22 identifier (Note this constructor is used by all three tag versions)
        if (ID3Tags.isID3v22FrameIdentifier(bodyIdentifier)) {
            //Does it have its own framebody (PIC,CRM) or are we using v23/v24 body (the normal case)
            if (ID3Tags.forceFrameID22To23(bodyIdentifier) != null) {
                //Do not convert
            } else if (bodyIdentifier == "CRM") {
                //Do not convert.
                //TODO we don't have a way of converting this to v23 which is why its not in the ForceMap
            } else if ((bodyIdentifier == ID3v22FrameId.TYER.id) ||
                (bodyIdentifier == ID3v22FrameId.TIME.id)
            ) {
                bodyIdentifier = ID3v24FrameId.YEAR.id
            } else if (ID3Tags.isID3v22FrameIdentifier(bodyIdentifier)) {
                bodyIdentifier = ID3Tags.convertFrameID22To23(bodyIdentifier)?.id?:UNSUPPORTED_ID
            }
        }

        // Use reflection to map id to frame body, which makes things much easier
        // to keep things up to date.
        try {
            val c = Class.forName("${AbstractID3v2FrameBody.FRAME_BODY_PACKAGE}.FrameBody$bodyIdentifier") as Class<AbstractID3v2FrameBody>
            frameBody = c.newInstance()
        } catch (cnfe: ClassNotFoundException) {
            log.error(cnfe.message, cnfe)
            frameBody = FrameBodyUnsupported(identifier)
        } catch (ie: InstantiationException) { //Instantiate Interface/Abstract should not happen
            log.error(ie.message, ie)
            throw java.lang.RuntimeException(ie)
        } catch (iae: IllegalAccessException) { //Private Constructor shouild not happen
            log.error(iae.message, iae)
            throw java.lang.RuntimeException(iae)
        }
        frameBody?.header = this
        log.debug(
            "Created empty frame of type${this.getIdentifier()}with frame body of$bodyIdentifier"
        )
    }

    /**
     * Creates a new ID3v22 Frame from another frame of a different tag version
     *
     * @param frame to construct the new frame from
     */
    constructor(frame: AbstractID3v2Frame): this() {
        log.debug("Creating frame from a frame of a different version")
        if (frame is ID3v22Frame) {
            throw UnsupportedOperationException("Copy Constructor not called. Please type cast the argument")
        }

        // If it is a v24 frame is it possible to convert it into a v23 frame, and then convert from that
        if (frame is ID3v24Frame) {
            val v23Frame = ID3v23Frame(frame)
            createV22FrameFromV23Frame(v23Frame)
        } else if (frame is ID3v23Frame) {
            createV22FrameFromV23Frame(frame)
        }
        this.frameBody?.header = this
        log.debug("Created frame from a frame of a different version")
    }

    private fun createV22FrameFromV23Frame(frame: ID3v23Frame) {
        setIdentifier(ID3Tags.convertFrameID23To22(frame.getIdentifier())?:error("No identifier"))
        if (getIdentifier() != null) {
            log.debug(
                "V2:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}"
            )
            this.frameBody = ID3Tags.copyObject(frame.frameBody) as AbstractID3v2FrameBody
        } else if (ID3Tags.isID3v23FrameIdentifier(frame.getIdentifier())) {
            setIdentifier(ID3Tags.forceFrameID23To22(frame.getIdentifier())?:error("No identifier"))
            if (getIdentifier() != null) {
                log.debug(
                    "V2:Force:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}"
                )
                this.frameBody = readBody(
                    getIdentifier(),
                    (frame.frameBody as? AbstractID3v2FrameBody)?:error("No body")
                )
            } else {
                throw InvalidFrameException(
                    "Unable to convert v23 frame:${frame.getIdentifier()} to a v22 frame"
                )
            }
        } else if (frame.frameBody is FrameBodyDeprecated) {
            //Was it valid for this tag version, if so try and reconstruct
            if (ID3Tags.isID3v22FrameIdentifier(frame.getIdentifier())) {
                this.frameBody = frame.frameBody
                setIdentifier(frame.getIdentifier())
                log.debug(
                    "DEPRECATED:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}"
                )
            } else {
                this.frameBody = FrameBodyDeprecated(
                    frame.frameBody as FrameBodyDeprecated
                )
                setIdentifier(frame.getIdentifier())
                log.debug(
                    "DEPRECATED:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}"
                )
            }
        } else {
            this.frameBody = FrameBodyUnsupported(
                frame.frameBody as FrameBodyUnsupported
            )
            setIdentifier(frame.getIdentifier())
            log.debug(
                "v2:UNKNOWN:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}"
            )
        }
    }

    /**
     * @return true if considered a common frame
     */
    override fun isBinary(): Boolean {
        return ID3v22FrameId.isBinary(getIdentifier())
    }

    /**
     * @return true if considered a common frame
     */
    override fun isCommon(): Boolean {
        return ID3v22FrameId.isCommon(getIdentifier())
    }

    /**
     * Return size of frame
     *
     * @return int size of frame
     */
    override fun getSize(): Int {
        return (frameBody?.getSize()?:0) + getFrameHeaderSize()
    }

    /**
     * Read frame from file.
     * Read the frame header then delegate reading of data to frame body.
     *
     * @param byteBuffer
     */
    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null) {
            return false
        }
        val identifier = readIdentifier(byteBuffer)?:error("No ioentifier")

        val buffer = ByteArray(getFrameSizeSize())

        // Is this a valid identifier?
        if (!isValidID3v2FrameIdentifier(identifier)) {
            log.debug("Invalid identifier:$identifier")
            byteBuffer.position(byteBuffer.position() - (getFrameIdSize() - 1))
            throw InvalidFrameIdentifierException(
                "$identifier:is not a valid ID3v2.20 frame"
            )
        }
        //Read Frame Size (same size as Frame Id so reuse buffer)
        byteBuffer.get(buffer, 0, getFrameSizeSize())
        frameSize = decodeSize(buffer)
        if (frameSize < 0) {
            throw InvalidFrameException(
                "$identifier has invalid size of:$frameSize"
            )
        } else if (frameSize == 0) {
            //We dont process this frame or add to framemap becuase contains no useful information
            log.warn("Empty Frame:$identifier")
            throw EmptyFrameException(identifier + " is empty frame")
        } else if (frameSize > byteBuffer.remaining()) {
            log.warn(
                "Invalid Frame size larger than size before mp3 audio:$identifier"
            )
            throw InvalidFrameException(identifier + " is invalid frame")
        } else {
            log.debug("Frame Size Is:$frameSize")
            //Convert v2.2 to v2.4 id just for reading the data
            var id: FrameId? = ID3Tags.convertFrameID22To24(identifier)
            if (id == null) {
                //OK,it may be convertable to a v.3 id even though not valid v.4
                id = ID3Tags.convertFrameID22To23(identifier)
                if (id == null) {
                    // Is it a valid v22 identifier so should be able to find a
                    // frame body for it.
                    if (ID3Tags.isID3v22FrameIdentifier(identifier)) {
                        id = ID3v22FrameId.fromId(identifier)
                    } else {
                        id = null
                    }
                }
            }
            log.debug("Identifier was:{} reading using:{}", identifier, id)

            //Create Buffer that only contains the body of this frame rather than the remainder of tag
            val frameBodyBuffer = byteBuffer.slice()
            frameBodyBuffer.limit(frameSize)

            try {
                frameBody = readBody(id?.id?:UNSUPPORTED_ID, frameBodyBuffer, frameSize)
            } finally {
                //Update position of main buffer, so no attempt is made to reread these bytes
                byteBuffer.position(byteBuffer.position() + frameSize)
            }
        }

        return true
    }

    /**
     * Read Frame Size, which has to be decoded
     *
     * @param buffer
     * @return
     */
    private fun decodeSize(buffer: ByteArray): Int {
        val bi = BigInteger(buffer)
        val tmpSize = bi.toInt()
        if (tmpSize < 0) {
            log.warn(
                "Invalid Frame Size of:" +
                        tmpSize +
                        "Decoded from bin:" +
                        Integer.toBinaryString(tmpSize) +
                        "Decoded from hex:" +
                        Integer.toHexString(tmpSize)
            )
        }
        return tmpSize
    }

    /**
     * Write Frame raw data
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        log.debug("Write Frame to Buffer" + getIdentifier())
        //This is where we will write header, move position to where we can
        //write body
        val headerBuffer = ByteBuffer.allocate(getFrameHeaderSize())

        //Write Frame Body Data
        val bodyOutputStream = ByteArrayOutputStream()
        (frameBody as AbstractID3v2FrameBody).write(bodyOutputStream)

        //Write Frame Header
        //Write Frame ID must adjust can only be 3 bytes long
        headerBuffer.put(
            getIdentifier()?.toByteArray(StandardCharsets.ISO_8859_1),
            0,
            getFrameIdSize()
        )
        encodeSize(headerBuffer, frameBody?.getSize()?:0)

        //Add header to the Byte Array Output Stream
        try {
            tagBuffer.write(headerBuffer.array())

            //Add body to the Byte Array Output Stream
            tagBuffer.write(bodyOutputStream.toByteArray())
        } catch (ioe: IOException) {
            //This could never happen coz not writing to file, so convert to RuntimeException
            throw RuntimeException(ioe)
        }
    }

    /**
     * Write Frame Size (can now be accurately calculated, have to convert 4 byte int
     * to 3 byte format.
     *
     * @param headerBuffer
     * @param size
     */
    private fun encodeSize(headerBuffer: ByteBuffer, size: Int) {
        headerBuffer.put(((size and 0x00FF0000) shr 16).toByte())
        headerBuffer.put(((size and 0x0000FF00) shr 8).toByte())
        headerBuffer.put((size and 0x000000FF).toByte())
        log.debug(
            "Frame Size Is Actual:" +
                    size +
                    ":Encoded bin:" +
                    Integer.toBinaryString(size) +
                    ":Encoded Hex" +
                    Integer.toHexString(size)
        )
    }

    /**
     * Does the frame identifier meet the syntax for a idv3v2 frame identifier.
     * must start with a capital letter and only contain capital letters and numbers
     *
     * @param identifier
     * @return
     */
    fun isValidID3v2FrameIdentifier(identifier: String): Boolean {
        val m = validFrameIdentifier.matcher(identifier)
        return m.matches()
    }

    override fun getFrameHeaderSize(): Int {
        return FRAME_HEADER_SIZE
    }

    override fun getFrameIdSize(): Int {
        return FRAME_ID_SIZE
    }

    override fun getFrameSizeSize(): Int {
        return FRAME_SIZE_SIZE
    }

    /**
     * Return String Representation of body
     */
    override fun createStructure() {
        MP3File.tagFormatter?.openHeadingElement(
            TYPE_FRAME,
            getIdentifier() ?:""
        )
        MP3File.tagFormatter?.addElement(TYPE_FRAME_SIZE, frameSize)
        frameBody?.createStructure()
        MP3File.tagFormatter?.closeHeadingElement(TYPE_FRAME)
    }
}