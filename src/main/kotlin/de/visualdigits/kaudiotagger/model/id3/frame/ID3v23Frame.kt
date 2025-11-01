package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.exceptions.EmptyFrameException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyDeprecated
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyUnsupported
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23EncodingFlags
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23StatusFlags
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24StatusFlags
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.util.ID3Compression
import de.visualdigits.kaudiotagger.util.ID3Tags
import de.visualdigits.kaudiotagger.util.ID3TextEncodingConversion
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class ID3v23Frame: AbstractID3v2Frame {

    companion object {

        const val FRAME_ID_SIZE: Int = 4
        const val FRAME_FLAGS_SIZE: Int = 2
        const val FRAME_SIZE_SIZE: Int = 4
        const val FRAME_COMPRESSION_UNCOMPRESSED_SIZE: Int = 4
        const val FRAME_ENCRYPTION_INDICATOR_SIZE: Int = 1
        const val FRAME_GROUPING_INDICATOR_SIZE: Int = 1
        val FRAME_HEADER_SIZE: Int = FRAME_ID_SIZE + FRAME_SIZE_SIZE + FRAME_FLAGS_SIZE

        val validFrameIdentifier: Pattern = Pattern.compile("[A-Z][0-9A-Z]{3}")


    }

    /**
     * If the frame is encrypted then the encryption method is stored in this byte
     */
    var encryptionMethod = 0

    /**
     * If the frame belongs in a group with other frames then the group identifier byte is stored
     */
    var groupIdentifier = 0

    constructor()

    /**
     * Creates a new ID3v23 Frame of type identifier.
     *
     *
     * An empty body of the correct type will be automatically created.
     * This constructor should be used when wish to create a new
     * frame from scratch using user data.
     *
     * @param identifier
     */
    constructor(identifier: String?): super(identifier) {
        statusFlags = ID3v23StatusFlags()
        encodingFlags = ID3v23EncodingFlags()
    }

    /**
     * Copy Constructor
     *
     *
     * Creates a new v23 frame  based on another v23 frame
     *
     * @param frame
     */
    constructor(frame: ID3v23Frame): super(frame) {
        statusFlags = ID3v23StatusFlags(this, frame.statusFlags?.originalFlags ?: 0)
        encodingFlags = ID3v23EncodingFlags(this, frame.encodingFlags?.flags?:0)
    }
    /**
     * Partially construct ID3v24 Frame form an IS3v23Frame
     *
     *
     * Used for Special Cases
     *
     * @param frame
     * @param identifier
     */
    constructor(frame: ID3v24Frame, identifier: String): this(identifier) {
        statusFlags = ID3v23StatusFlags(
            this,
            frame.statusFlags as? ID3v24StatusFlags
        )
        encodingFlags = ID3v23EncodingFlags(this, frame.encodingFlags?.flags?:0)
    }

    constructor(frameBody: AbstractID3v2FrameBody): super(frameBody)

    /**
     * Creates a new ID3v23Frame  based on another frame of a different version.
     *
     * @param frame
     */
    constructor(frame: AbstractID3v2Frame) {
        log.debug("Creating frame from a frame of a different version")
        when (frame) {
            is ID3v23Frame -> {
                throw UnsupportedOperationException("Copy Constructor not called. Please type cast the argument")
            }

            is ID3v22Frame -> {
                statusFlags = ID3v23StatusFlags(this)
                encodingFlags = ID3v23EncodingFlags(this)
            }

            is ID3v24Frame -> {
                statusFlags = ID3v23StatusFlags(this, frame.statusFlags as ID3v24StatusFlags)
                encodingFlags = ID3v23EncodingFlags(this, frame.encodingFlags?.flags ?: 0)
            }
        }

        if (frame is ID3v24Frame) {
            // Unknown Frame e.g NCON, also protects when known id but has unsupported frame body
            when {
                frame.frameBody is FrameBodyUnsupported -> {
                    frameBody = FrameBodyUnsupported(frame.frameBody as? FrameBodyUnsupported)
                    frameBody?.header = this
                    setIdentifier(frame.getIdentifier())
                    log.debug("UNKNOWN:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}")
                }
                frame.frameBody is FrameBodyDeprecated -> {
                    // Was it valid for this tag version, if so try and reconstruct
                    if (ID3Tags.isID3v23FrameIdentifier(frame.getIdentifier())) {
                        frameBody = (frame.frameBody as? FrameBodyDeprecated)?.originalFrameBody
                        frameBody?.header = this
                        frameBody?.setTextEncoding(
                            ID3TextEncodingConversion.getTextEncoding(
                                this,
                                frameBody?.getTextEncoding() ?: TextEncoding.ISO_8859_1.id
                            )
                        )
                        setIdentifier(frame.getIdentifier())
                        log.debug("DEPRECATED:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}")
                    } else {
                        frameBody = FrameBodyDeprecated(frame.frameBody as? FrameBodyDeprecated)
                        frameBody?.header = this
                        frameBody?.setTextEncoding(
                            ID3TextEncodingConversion.getTextEncoding(
                                this,
                                frameBody?.getTextEncoding() ?: TextEncoding.ISO_8859_1.id
                            )
                        )

                        setIdentifier(frame.getIdentifier())
                        log.debug("DEPRECATED:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}")
                    }
                }
                ID3Tags.isID3v24FrameIdentifier(frame.getIdentifier()) -> {
                    log.debug("isID3v24FrameIdentifier")
                    // Version between v4 and v3
                    setIdentifier(ID3Tags.convertFrameID24To23(frame.getIdentifier()))
                    if (getIdentifier() != null) {
                        log.debug("V4:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}")
                        frameBody = ID3Tags.copyObject(frame.frameBody) as? AbstractTagFrameBody
                        frameBody?.header = this
                        frameBody?.setTextEncoding(
                            ID3TextEncodingConversion.getTextEncoding(
                                this,
                                frameBody?.getTextEncoding() ?: TextEncoding.ISO_8859_1.id
                            )
                        )
                    } else {
                        // Is it a known v4 frame which needs forcing to v3 frame e.g. TDRC - TYER,TDAT
                        setIdentifier(ID3Tags.forceFrameID24To23(frame.getIdentifier()))
                        when {
                            getIdentifier() != null -> {
                                log.debug("V4:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}")
                                frameBody = readBody(getIdentifier(), frame.frameBody as? AbstractID3v2FrameBody)
                                frameBody?.header = this
                                frameBody?.setTextEncoding(
                                    ID3TextEncodingConversion.getTextEncoding(
                                        this,
                                        frameBody?.getTextEncoding() ?: TextEncoding.ISO_8859_1.id
                                    )
                                )
                            }
                            else -> {
                                val baos = ByteArrayOutputStream()
                                (frame.frameBody as? AbstractID3v2FrameBody)?.write(baos)

                                setIdentifier(frame.getIdentifier())
                                frameBody = FrameBodyUnsupported(getIdentifier(), baos.toByteArray())
                                frameBody?.header = this
                                log.debug("V4:Orig id is:${frame.getIdentifier()}:New Id Unsupported is:${getIdentifier()}")
                            }
                        }
                    }
                }
                else -> {
                    log.error("Orig id is:${frame.getIdentifier()}Unable to create Frame Body")
                    throw InvalidFrameException("Orig id is:${frame.getIdentifier()}Unable to create Frame Body")
                }
            }
        } else if (frame is ID3v22Frame) {
            if (ID3Tags.isID3v22FrameIdentifier(frame.getIdentifier())) {
                setIdentifier(ID3Tags.convertFrameID22To23(frame.getIdentifier())?.id)
                if (getIdentifier() != null) {
                    log.debug("V3:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}")
                    frameBody = ID3Tags.copyObject(frame.frameBody) as? AbstractTagFrameBody
                    frameBody?.header = this
                } else if (ID3Tags.isID3v22FrameIdentifier(frame.getIdentifier())) {
                    // Force v2 to v3
                    setIdentifier(ID3Tags.forceFrameID22To23(frame.getIdentifier()))
                    if (getIdentifier() != null) {
                        log.debug("V22Orig id is:${frame.getIdentifier()}New id is:${getIdentifier()}")
                        frameBody = readBody(getIdentifier(), frame.frameBody as? AbstractID3v2FrameBody)
                        frameBody?.header = this
                    }
                    else {
                        frameBody = FrameBodyDeprecated(frame.frameBody as? AbstractID3v2FrameBody)
                        frameBody?.header = this
                        setIdentifier(frame.getIdentifier())
                        log.debug("Deprecated:V22:orig id id is:${frame.getIdentifier()}:New id is:${getIdentifier()}")
                    }
                }
            } else {
                frameBody = FrameBodyUnsupported(frame.frameBody as? FrameBodyUnsupported)
                frameBody?.header = this
                setIdentifier(frame.getIdentifier())
                log.debug("UNKNOWN:Orig id is:${frame.getIdentifier()}:New id is:${getIdentifier()}")
            }
        }

        log.warn("Frame is unknown version:" + frame.javaClass)
    }

    /**
     * Creates a new ID3v23Frame dataType by reading from byteBuffer.
     *
     * @param byteBuffer      to read from
     */
    constructor(byteBuffer: ByteBuffer) {
        read(byteBuffer)
    }
    /**
     * Read the frame from a byteBuffer
     *
     * @param byteBuffer buffer to read from
     */
    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null) {
            return false
        }
        val identifier = readIdentifier(byteBuffer)
        if (!isValidID3v2FrameIdentifier(identifier)) {
            log.debug("Invalid identifier:$identifier")
            byteBuffer.position(byteBuffer.position() - (getFrameIdSize() - 1))
        }
        // Read the size field (as Big Endian Int - byte buffers always initialised to Big Endian order)
        frameSize = byteBuffer.getInt()// Update position of main buffer, so no attempt is made to reread these bytes
        // Create Buffer that only contains the body of this frame rather than the remainder of tag
        // it should be deprecated, but what about if somehow a V24Frame has been put into a V23 Tag, shouldn't
        // it then be created as FrameBodyUnsupported
        // Probably corrupt so treat as a standard frame

        // Work out the real size of the frameBody data
        // Read the body data
        // Read the Grouping byte, but do nothing with it
        // Consume the encryption byte
        // Read the Decompressed Size
        // It is a valid v23 identifier so should be able to find a
        //  frame body for it.

        // Read extra bits appended to frame header for various encodings
        // These are not included in header size but are included in frame size but won't be read when we actually
        // try to read the frame body data
        when {
            frameSize < 0 -> {
                log.warn("Invalid Frame Size:$frameSize:$identifier")
                throw InvalidFrameException("$identifier is invalid frame:$frameSize")
            }

            frameSize == 0 -> {
                log.warn("Empty Frame Size:$identifier")
                // We don't process this frame or add to frameMap because contains no useful information
                // Skip the two flag bytes so in correct position for subsequent frames
                byteBuffer.get()
                byteBuffer.get()
                throw EmptyFrameException("$identifier is empty frame")
            }

            frameSize > byteBuffer.remaining() -> {
                log.warn("Invalid Frame size of $frameSize larger than size of${byteBuffer.remaining()} before mp3 audio:$identifier")
                throw InvalidFrameException("$identifier is invalid frame:$frameSize larger than size of${byteBuffer.remaining()} before mp3 audio:$identifier")
            }

            // Read the flag bytes

            // If this identifier is a valid v24 identifier or easily converted to v24

            // Cant easily be converted to v24 but is it a valid v23 identifier
            else -> {
                statusFlags = ID3v23StatusFlags(this, byteBuffer.get().toInt())
                encodingFlags = ID3v23EncodingFlags(this, byteBuffer.get().toInt())
                var id: String?

                // If this identifier is a valid v24 identifier or easily converted to v24
                id = ID3Tags.convertFrameID23To24(identifier)

                // Cant easily be converted to v24 but is it a valid v23 identifier
                if (id == null) {
                    // It is a valid v23 identifier so should be able to find a
                    //  frame body for it.
                    if (ID3Tags.isID3v23FrameIdentifier(identifier)) {
                        id = identifier
                    } else {
                        id = UNSUPPORTED_ID
                    }
                }
                log.debug("Identifier was:$identifier reading using:${id}with frame size:$frameSize")

                // Read extra bits appended to frame header for various encodings
                // These are not included in header size but are included in frame size but won't be read when we actually
                // try to read the frame body data
                var extraHeaderBytesCount = 0
                var decompressedFrameSize = -1

                if ((encodingFlags as? ID3v23EncodingFlags)?.isCompression() == true) {
                    // Read the Decompressed Size
                    decompressedFrameSize = byteBuffer.getInt()
                    extraHeaderBytesCount = FRAME_COMPRESSION_UNCOMPRESSED_SIZE
                    log.debug(
                        "Decompressed frame size is:$decompressedFrameSize"
                    )
                }

                if ((encodingFlags as? ID3v23EncodingFlags)?.isEncryption() == true) {
                    // Consume the encryption byte
                    extraHeaderBytesCount += FRAME_ENCRYPTION_INDICATOR_SIZE
                    encryptionMethod = byteBuffer.get().toInt()
                }

                if ((encodingFlags as? ID3v23EncodingFlags)?.isGrouping() == true) {
                    // Read the Grouping byte, but do nothing with it
                    extraHeaderBytesCount += FRAME_GROUPING_INDICATOR_SIZE
                    groupIdentifier = byteBuffer.get().toInt()
                }

                if ((encodingFlags as? ID3v23EncodingFlags)?.isNonStandardFlags() == true) {
                    // Probably corrupt so treat as a standard frame
                    log.error(
                        "InvalidEncodingFlags:${encodingFlags?.flags?.toHexString()}"
                    )
                }

                if ((encodingFlags as? ID3v23EncodingFlags)?.isCompression() == true && decompressedFrameSize > (100 * frameSize)) {
                    throw InvalidFrameException("$identifier is invalid frame, frame size $frameSize cannot be:$decompressedFrameSize when uncompressed")
                }

                // Work out the real size of the frameBody data
                val realFrameSize = frameSize - extraHeaderBytesCount

                if (realFrameSize <= 0) {
                    throw InvalidFrameException("$identifier is invalid frame, realframeSize is:$realFrameSize")
                }

                val frameBodyBuffer: ByteBuffer
                // Read the body data
                try {
                    if ((encodingFlags as? ID3v23EncodingFlags)?.isCompression() == true) {
                        frameBodyBuffer = ID3Compression.uncompress(
                            identifier,
                            byteBuffer,
                            decompressedFrameSize,
                            realFrameSize
                        )
                        if ((encodingFlags as? ID3v23EncodingFlags)?.isEncryption() == true) {
                            frameBody = readEncryptedBody(
                                id,
                                frameBodyBuffer,
                                decompressedFrameSize
                            )
                        } else {
                            frameBody = readBody(id, frameBodyBuffer, decompressedFrameSize)
                        }
                    } else if ((encodingFlags as? ID3v23EncodingFlags)?.isEncryption() == true) {
                        frameBodyBuffer = byteBuffer.slice()
                        frameBodyBuffer.limit(frameSize)
                        frameBody = readEncryptedBody(identifier, frameBodyBuffer, frameSize)
                    } else {
                        // Create Buffer that only contains the body of this frame rather than the remainder of tag
                        frameBodyBuffer = byteBuffer.slice()
                        frameBodyBuffer.limit(realFrameSize)
                        frameBody = readBody(id, frameBodyBuffer, realFrameSize)
                    }
                    if (frameBody !is ID3v23FrameBody) {
                        log.debug("Converted frameBody with:$identifier to deprecated frameBody")
                        this.frameBody = frameBody?.let { fb -> FrameBodyDeprecated(fb as? FrameBodyDeprecated) }
                    }
                } finally {
                    // Update position of main buffer, so no attempt is made to reread these bytes
                    byteBuffer.position(byteBuffer.position() + realFrameSize)
                }

                return true
            }
        }
    }

    override fun getFrameIdSize(): Int {
        return FRAME_ID_SIZE
    }

    /**
     * Does the frame identifier meet the syntax for a idv3v2 frame identifier.
     * must start with a capital letter and only contain capital letters and numbers
     *
     * @param identifier to be checked
     * @return whether the identifier is valid
     */
    fun isValidID3v2FrameIdentifier(identifier: String?): Boolean {
        val m = validFrameIdentifier.matcher(identifier)
        return m.matches()
    }

    override fun getFrameSizeSize(): Int {
        return FRAME_SIZE_SIZE
    }

    override fun getFrameHeaderSize(): Int {
        return FRAME_HEADER_SIZE
    }

    /**
     * Return size of frame
     *
     * @return int frame size
     */
    override fun getSize(): Int {
        return (frameBody?.getSize()?:0) + FRAME_HEADER_SIZE
    }

    /**
     * Write the frame to bufferOutputStream
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        log.debug("Writing frame to buffer:" + getIdentifier())
        // This is where we will write header, move position to where we can
        // write body
        val headerBuffer = ByteBuffer.allocate(FRAME_HEADER_SIZE)

        // Write Frame Body Data
        val bodyOutputStream = ByteArrayOutputStream()
        (frameBody as? AbstractID3v2FrameBody)?.write(bodyOutputStream)
        // Write Frame Header write Frame ID
        if (getIdentifier()?.length == 3) {
            setIdentifier(getIdentifier() + ' ')
        }
        headerBuffer.put(
            getIdentifier()?.toByteArray(StandardCharsets.ISO_8859_1),
            0,
            FRAME_ID_SIZE
        )
        // Write Frame Size
        val size = frameBody?.getSize()
        log.debug("Frame Size Is:" + size)
        headerBuffer.putInt(frameBody?.getSize()?:0)

        // Write the Flags
        // Status Flags:leave as they were when we read
        headerBuffer.put((statusFlags?.writeFlags?:0).toByte())

        // Remove any non standard flags
        (encodingFlags as? ID3v23EncodingFlags)?.unsetNonStandardFlags()

        // Unset Compression flag if previously set because we uncompress previously compressed frames on write.
        (encodingFlags as? ID3v23EncodingFlags)?.unsetCompression()
        headerBuffer.put((encodingFlags?.flags?:0).toByte())

        try {
            // Add header to the Byte Array Output Stream
            tagBuffer.write(headerBuffer.array())

            if ((encodingFlags as? ID3v23EncodingFlags)?.isEncryption() == true) {
                tagBuffer.write(encryptionMethod)
            }

            if ((encodingFlags as? ID3v23EncodingFlags)?.isGrouping() == true) {
                tagBuffer.write(groupIdentifier)
            }

            // Add body to the Byte Array Output Stream
            tagBuffer.write(bodyOutputStream.toByteArray())
        } catch (ioe: IOException) {
            // This could never happen coz not writing to file, so convert to RuntimeException
            throw RuntimeException(ioe)
        }
    }

    /**
     * @return true if considered a common frame
     */
    override fun isCommon(): Boolean {
        return ID3v23FrameId.isCommon(getIdentifier())
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
        statusFlags?.createStructure()
        encodingFlags?.createStructure()
        frameBody?.createStructure()
        MP3File.tagFormatter?.closeHeadingElement(TYPE_FRAME)
    }
}