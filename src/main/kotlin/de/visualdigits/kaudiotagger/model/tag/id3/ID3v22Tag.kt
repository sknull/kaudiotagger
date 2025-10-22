package de.visualdigits.kaudiotagger.model.tag.id3

import de.visualdigits.kaudiotagger.model.exceptions.EmptyFrameException
import de.visualdigits.kaudiotagger.model.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.exceptions.InvalidFrameIdentifierException
import de.visualdigits.kaudiotagger.model.exceptions.PaddingException
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractFrameBodyTextInfo
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTCON
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTDRC
import de.visualdigits.kaudiotagger.model.frame.framebody.ID3v22PreferredFrameOrderComparator
import de.visualdigits.kaudiotagger.model.frame.id3.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.frame.id3.ID3v22Frame
import de.visualdigits.kaudiotagger.model.kframe.ID3v22KFrame
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.FileConstants
import de.visualdigits.kaudiotagger.util.ID3SyncSafeInteger
import de.visualdigits.kaudiotagger.util.ID3Unsynchronization
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.File
import java.nio.ByteBuffer
import kotlin.experimental.and
import kotlin.experimental.or

class ID3v22Tag(
    buffer: ByteBuffer
) : AbstractID3v2Tag(buffer) {

    companion object {

        /**
         * Bit mask to indicate tag is Unsychronization
         */
        val MASK_V22_UNSYNCHRONIZATION: Byte = FileConstants.BIT7
        /**
         * Bit mask to indicate tag is compressed, although compression is not
         * actually defined in v22 so just ignored
         */
        val MASK_V22_COMPRESSION: Byte = FileConstants.BIT6

        const val RELEASE: Byte = 2
        const val MAJOR_VERSION: Byte = 2
        const val REVISION: Byte = 0
        const val TYPE_COMPRESSION: String = "compression"
        const val TYPE_UNSYNCHRONISATION: String = "unsyncronisation"
    }

    /**
     * The tag is compressed, although no compression scheme is defined in ID3v22
     */
    var compression: Boolean = false

    /**
     * If set all frames in the tag uses unsynchronisation
     */
    var unsynchronization: Boolean = false

    /**
     * Retrieve the Release
     */
    override fun getRelease(): Byte {
        return RELEASE
    }

    /**
     * Retrieve the Major Version
     */
    override fun getMajorVersion(): Byte {
        return MAJOR_VERSION
    }

    /**
     * Retrieve the Revision
     */
    override fun getRevision(): Byte {
        return REVISION
    }

    /**
     * @return an indentifier of the tag type
     */
    override fun getIdentifier(): String? {
        return "ID3v2_2.20"
    }

    /**
     * Return frame size based upon the sizes of the frames rather than the size
     * including padding recorded in the tag header
     *
     * @return size
     */
    override fun getSize(): Int {
        var size = TAG_HEADER_LENGTH
        size += super.getSize()
        return size
    }

    /**
     * {@inheritDoc}
     */
    override fun read(byteBuffer: ByteBuffer) {
        val size: Int
        if (!seek(byteBuffer)) {
            error("ID3v2.20 tag not found")
        }
        log.debug("Reading tag from file")

        //Read the flags
        readHeaderFlags(byteBuffer)

        // Read the size
        size = ID3SyncSafeInteger.bufferToValue(byteBuffer)

        //Slice Buffer, so position markers tally with size (i.e do not include tagheader)
        var bufferWithoutHeader = byteBuffer.slice()

        //We need to synchronize the buffer
        if (unsynchronization) {
            bufferWithoutHeader = ID3Unsynchronization.synchronize(bufferWithoutHeader)
        }
        readFrames(bufferWithoutHeader, size)
        log.debug(
            "Loaded Frames,there are:${frameMap.size}"
        )
    }

    /**
     * Does a tag of the correct version exist in this file.
     *
     * @param byteBuffer to search through
     * @return true if tag exists.
     */
    override fun seek(byteBuffer: ByteBuffer): Boolean {
        byteBuffer.rewind()
        log.debug(
            "ByteBuffer pos:${byteBuffer.position()}:limit${byteBuffer.limit()}:cap${byteBuffer.capacity()}"
        )

        val tagIdentifier = ByteArray(FIELD_TAGID_LENGTH)
        byteBuffer.get(tagIdentifier, 0, FIELD_TAGID_LENGTH)
        if (!(tagIdentifier.contentEquals(TAG_ID))) {
            return false
        }
        //Major Version
        val major = byteBuffer.get()
        if (major != MAJOR_VERSION) {
            return false
        }
        //Minor Version
        val minor = byteBuffer.get()
        return minor == REVISION
    }

    /**
     * Read tag Header Flags
     *
     * @param byteBuffer
     */
    private fun readHeaderFlags(byteBuffer: ByteBuffer) {
        //Flags
        val flags = byteBuffer.get()
        unsynchronization = (flags and MASK_V22_UNSYNCHRONIZATION) != 0.toByte()
        compression = (flags and MASK_V22_COMPRESSION) != 0.toByte()

        if (unsynchronization) {
            log.debug(
                ErrorMessage.ID3_TAG_UNSYNCHRONIZED.getMsg()
            )
        }

        if (compression) {
            log.debug(
                ErrorMessage.ID3_TAG_COMPRESSED.getMsg()
            )
        }

        //Not allowable/Unknown Flags
        if ((flags and FileConstants.BIT5) != 0.toByte()) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT5
                )
            )
        }
        if ((flags and FileConstants.BIT4) != 0.toByte()) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT4
                )
            )
        }
        if ((flags and FileConstants.BIT3) != 0.toByte()) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT3
                )
            )
        }
        if ((flags and FileConstants.BIT2) != 0.toByte()) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT2
                )
            )
        }
        if ((flags and FileConstants.BIT1) != 0.toByte()) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT1
                )
            )
        }
        if ((flags and FileConstants.BIT0) != 0.toByte()) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT3
                )
            )
        }
    }

    /**
     * Read frames from tag
     *
     * @param byteBuffer
     * @param size
     */
    fun readFrames(byteBuffer: ByteBuffer, size: Int) {
        //Now start looking for frames
        var next: ID3v22Frame
        frameMap = LinkedHashMap()
        encryptedFrameMap = LinkedHashMap()

        //Read the size from the Tag Header
        this.fileReadSize = size
        log.debug(
            "Start of frame body at:" +
                    byteBuffer.position() +
                    ",frames sizes and padding is:" +
                    size
        )
        
        /* todo not done yet. Read the first Frame, there seems to be quite a
         ** common case of extra data being between the tag header and the first
         ** frame so should we allow for this when reading first frame, but not subsequent frames
         */
        // Read the frames until got to upto the size as specified in header
        while (byteBuffer.position() < size) {
            try {
                //Read Frame
                log.debug(
                    "looking for next frame at:" +
                            byteBuffer.position()
                )
                next = ID3v22Frame(byteBuffer)
                val id = next.getIdentifier()
                loadFrameIntoMap(id, next)
            } catch (ex: PaddingException) { //Found Padding, no more frames
                log.debug(
                    "Found padding starting at:" +
                            byteBuffer.position()
                )
                break
            } catch (ex: EmptyFrameException) { //Found Empty Frame
                log.warn(
                    "Empty Frame:" + ex.message
                )
                this.emptyFrameBytes += ID3v22Frame.FRAME_HEADER_SIZE
            } catch (ifie: InvalidFrameIdentifierException) {
                log.debug(
                    "Invalid Frame Identifier:" +
                            ifie.message
                )
                this.invalidFrames++
                //Dont try and find any more frames
                break
            } catch (ife: InvalidFrameException) { //Problem trying to find frame
                log.warn(
                    "Invalid Frame:" + ife.message
                )
                this.invalidFrames++
                //Dont try and find any more frames
                break
            } //in case we can read the next frame //Failed reading frame but may just have invalid data but correct length so lets carry on
            catch (idete: InvalidDataTypeException) {
                log.warn(
                    "Corrupt Frame:" + idete.message
                )
                this.invalidFrames++
                continue
            }
        }
    }

    override fun loadFrameIntoMap(frameId: String?, next: AbstractID3v2Frame) {
        (next.frameBody as? FrameBodyTCON)?.also { fb -> fb.setV23Format() }
        super.loadFrameIntoMap(frameId, next)
    }

    override fun addFrame(frame: AbstractID3v2Frame) {
        try {
            if (frame is ID3v22Frame) {
                copyFrameIntoMap(frame.getIdentifier(), frame)
            } else {
                val frames: MutableList<AbstractID3v2Frame> = convertFrame(frame)
                for (next in frames) {
                    copyFrameIntoMap(next.getIdentifier(), next)
                }
            }
        } catch (ife: InvalidFrameException) {
            log.error("Unable to convert frame:" + frame.getIdentifier())
        }
    }

    override fun convertFrame(frame: AbstractID3v2Frame): MutableList<AbstractID3v2Frame> {
        val frames = mutableListOf<AbstractID3v2Frame>()
        val tmpBody = frame.frameBody
        if ((frame.getIdentifier() == ID3v24KFrame.YEAR.id) && (tmpBody is FrameBodyTDRC)) {
            var newFrame: ID3v22Frame
            if (tmpBody.year.isNotEmpty()) {
                //Create Year frame (v2.2 id,but uses v2.3 body)
                newFrame = ID3v22Frame(ID3v22KFrame.TYER.id)
                (newFrame.frameBody as AbstractFrameBodyTextInfo).setText(tmpBody.year)
                frames.add(newFrame)
            }
            if (tmpBody.time.isNotEmpty()) {
                //Create Time frame (v2.2 id,but uses v2.3 body)
                newFrame = ID3v22Frame(ID3v22KFrame.TIME.id)
                (newFrame.frameBody as AbstractFrameBodyTextInfo).setText(tmpBody.time)
                frames.add(newFrame)
            }
        } else {
            frames.add(ID3v22Frame(frame))
        }
        return frames
    }

    /**
     * @return comparator used to order frames in preferred order for writing to file
     * so that most important frames are written first.
     */
    override fun getPreferredFrameOrderComparator(): Comparator<String> {
        return ID3v22PreferredFrameOrderComparator.instance
    }

    /**
     * {@inheritDoc}
     */
    override fun write(file: File, audioStartByte: Long): Long {
        log.debug("Writing tag to file:")

        // Write Body Buffer
        var bodyByteBuffer = writeFramesToBuffer().toByteArray()

        // Unsynchronize if option enabled and unsync required
        unsynchronization =
            TagOptionSingleton.unsyncTags &&
                    ID3Unsynchronization.requiresUnsynchronization(bodyByteBuffer)
        if (unsynchronization) {
            bodyByteBuffer = ID3Unsynchronization.unsynchronize(bodyByteBuffer)
            log.debug(
                "bodybytebuffer:sizeafterunsynchronisation:" +
                        bodyByteBuffer.size
            )
        }

        val sizeIncPadding = calculateTagSize(
            bodyByteBuffer.size + TAG_HEADER_LENGTH,
            audioStartByte.toInt()
        )
        val padding = sizeIncPadding - (bodyByteBuffer.size + TAG_HEADER_LENGTH)
        log.debug(
            "Current audiostart:" + audioStartByte
        )
        log.debug(
            "Size including padding:" + sizeIncPadding
        )
        log.debug("Padding:" + padding)

        val headerBuffer: ByteBuffer = writeHeaderToBuffer(
            padding,
            bodyByteBuffer.size
        )
        writeBufferToFile(
            file!!,
            headerBuffer,
            bodyByteBuffer,
            padding,
            sizeIncPadding,
            audioStartByte
        )
        return sizeIncPadding.toLong()
    }

    /**
     * Write the ID3 header to the ByteBuffer.
     *
     * @param padding
     * @param size
     * @return ByteBuffer
     */
    private fun writeHeaderToBuffer(padding: Int, size: Int): ByteBuffer {
        compression = false

        //Create Header Buffer
        val headerBuffer = ByteBuffer.allocate(TAG_HEADER_LENGTH)

        //TAGID
        headerBuffer.put(TAG_ID)
        //Major Version
        headerBuffer.put(getMajorVersion())
        //Minor Version
        headerBuffer.put(getRevision())

        //Flags
        var flags = 0.toByte()
        if (unsynchronization) {
            flags = flags or MASK_V22_UNSYNCHRONIZATION
        }
        if (compression) {
            flags = flags or MASK_V22_COMPRESSION
        }

        headerBuffer.put(flags)

        //Size As Recorded in Header, don't include the main header length
        headerBuffer.put(ID3SyncSafeInteger.valueToBuffer(padding + size))
        headerBuffer.flip()

        return headerBuffer
    }
}