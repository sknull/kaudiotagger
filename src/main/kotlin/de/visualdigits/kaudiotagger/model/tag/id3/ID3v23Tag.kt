package de.visualdigits.kaudiotagger.model.tag.id3

import de.visualdigits.kaudiotagger.model.exceptions.EmptyFrameException
import de.visualdigits.kaudiotagger.model.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.exceptions.InvalidFrameIdentifierException
import de.visualdigits.kaudiotagger.model.exceptions.PaddingException
import de.visualdigits.kaudiotagger.model.exceptions.TagNotFoundException
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyIPLS
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTDAT
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTDRC
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTIME
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTIPL
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTMCL
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTYER
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23PreferredFrameOrderComparator
import de.visualdigits.kaudiotagger.model.frame.id3.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.frame.id3.ID3v23Frame
import de.visualdigits.kaudiotagger.model.frame.id3.ID3v24Frame
import de.visualdigits.kaudiotagger.model.kframe.ID3v23KFrame
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
import de.visualdigits.kaudiotagger.model.tag.AbstractTag
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.FileConstants
import de.visualdigits.kaudiotagger.util.ID3SyncSafeInteger
import de.visualdigits.kaudiotagger.util.ID3Unsynchronization
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.File
import java.nio.ByteBuffer
import kotlin.experimental.and
import kotlin.experimental.or

class ID3v23Tag : AbstractID3v2Tag {

    companion object {

        /**
         * ID3v2.3 Header bit mask
         */
        val MASK_V23_UNSYNCHRONIZATION: Byte = FileConstants.BIT7

        /**
         * ID3v2.3 Header bit mask
         */
        val MASK_V23_EXTENDED_HEADER: Byte = FileConstants.BIT6

        /**
         * ID3v2.3 Header bit mask
         */
        val MASK_V23_EXPERIMENTAL: Byte = FileConstants.BIT5

        /**
         * ID3v2.3 Extended Header bit mask
         */
        val MASK_V23_CRC_DATA_PRESENT: Byte = FileConstants.BIT7

        /**
         * ID3v2.3 RBUF frame bit mask
         */
        val MASK_V23_EMBEDDED_INFO_FLAG: Byte = FileConstants.BIT1

        const val RELEASE: Byte = 2
        const val MAJOR_VERSION: Byte = 3
        const val REVISION: Byte = 0
        const val TYPE_CRCDATA: String = "crcdata"
        const val TYPE_EXPERIMENTAL: String = "experimental"
        const val TYPE_EXTENDED: String = "extended"
        const val TYPE_PADDINGSIZE: String = "paddingsize"
        const val TYPE_UNSYNCHRONISATION: String = "unsyncronisation"

        var TAG_EXT_HEADER_LENGTH: Int = 10
        var TAG_EXT_HEADER_CRC_LENGTH: Int = 4
        var FIELD_TAG_EXT_SIZE_LENGTH: Int = 4
        var TAG_EXT_HEADER_DATA_LENGTH: Int = TAG_EXT_HEADER_LENGTH - FIELD_TAG_EXT_SIZE_LENGTH
    }

    /**
     * CRC Checksum calculated
     */
    var crcDataFlag: Boolean = false

    /**
     * Experiemntal tag
     */
    var experimental: Boolean = false

    /**
     * Contains extended header
     */
    var extended: Boolean = false

    /**
     * All frames in the tag uses unsynchronisation
     */
    var unsynchronization: Boolean = false

    /**
     * The tag is compressed
     */
    var compression: Boolean = false

    /**
     * Crcdata Checksum in extended header
     */
    private var crc32 = 0

    /**
     * Tag padding
     */
    private var paddingSize = 0

    /**
     * Creates a new empty ID3v2_3 datatype.
     */
    constructor()

    /**
     * Copy Constructor, creates a new ID3v2_3 Tag based on another ID3v2_3 Tag
     *
     * @param copyObject
     */
    constructor(copyObject: ID3v23Tag) {
        log.debug("Creating tag from another tag of same type")
        copyPrimitives(copyObject)
        copyFrames(copyObject)
    }

    /**
     * Constructs a new tag based upon another tag of different version/type
     *
     * @param mp3tag
     */
    constructor(mp3tag: AbstractTag) {
        log.debug("Creating tag from a tag of a different version")

        val convertedTag: ID3v24Tag?
        //Should use simpler copy constructor
        if (mp3tag is ID3v23Tag) {
            throw UnsupportedOperationException(
                "Copy Constructor not called. Please type cast the argument"
            )
        }
        if (mp3tag is ID3v24Tag) {
            convertedTag = mp3tag
        } else {
            convertedTag = ID3v24Tag(mp3tag)
        }
        //Copy Primitives
        copyPrimitives(convertedTag!!)
        //Copy Frames
        copyFrames(convertedTag)
        log.debug("Created tag from a tag of a different version")
    }

    /**
     * Creates a new ID3v2_3 datatype.
     *
     * @param buffer
     * @throws TagException
     */
    constructor(buffer: ByteBuffer) {
        this.read(buffer)
    }

    /**
     * @return textual tag identifier
     */
    override fun getIdentifier(): String? {
        return "ID3v2.30"
    }

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

    override fun addFrame(frame: AbstractID3v2Frame) {
        try {
            if (frame is ID3v23Frame) {
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
        if ((frame.getIdentifier() == ID3v24KFrame.YEAR.id) &&
            (frame.frameBody is FrameBodyTDRC)
        ) {
            val tmpBody = frame.frameBody as FrameBodyTDRC
            //TODO will overwrite any existing TYER or TIME frame, do we ever want multiples of these
            tmpBody.findMatchingMaskAndExtractV3Values()
            var newFrame: ID3v23Frame
            if (tmpBody.year != "") {
                newFrame = ID3v23Frame(ID3v23KFrame.TYER.id)
                (newFrame.frameBody as FrameBodyTYER).setText(tmpBody.year)
                frames.add(newFrame)
            }
            if (tmpBody.date != "") {
                newFrame = ID3v23Frame(ID3v23KFrame.TDAT.id)
                (newFrame.frameBody as FrameBodyTDAT).setText(tmpBody.date)
                (newFrame.frameBody as FrameBodyTDAT).isMonthOnly = tmpBody.monthOnly
                frames.add(newFrame)
            }
            if (!tmpBody.time.equals("")) {
                newFrame = ID3v23Frame(ID3v23KFrame.TIME.id)
                (newFrame.frameBody as FrameBodyTIME).setText(tmpBody.time)
                (newFrame.frameBody as FrameBodyTIME).hoursOnly = tmpBody.hoursOnly
                frames.add(newFrame)
            }
        } else if ((frame.getIdentifier() == ID3v24KFrame.INVOLVED_PEOPLE.id) &&
            (frame.frameBody is FrameBodyTIPL)
        ) {
            val pairs = (frame.frameBody as FrameBodyTIPL).getPairing()?.mapping
            val ipls: AbstractID3v2Frame = ID3v23Frame(
                frame as ID3v24Frame,
                ID3v23KFrame.INVOLVED_PEOPLE.id
            )
            val iplsBody = FrameBodyIPLS(
                frame.frameBody?.getTextEncoding() ?: 0.toByte(),
                pairs ?: error("No mapping")
            )
            ipls.frameBody = iplsBody
            frames.add(ipls)
        } else if ((frame.getIdentifier() == ID3v24KFrame.MUSICIAN_CREDITS.id) &&
            (frame.frameBody is FrameBodyTMCL)
        ) {
            val pairs = (frame.frameBody as FrameBodyTMCL).getPairing()?.mapping
            val ipls: AbstractID3v2Frame = ID3v23Frame(
                frame as ID3v24Frame,
                ID3v23KFrame.INVOLVED_PEOPLE.id
            )
            val iplsBody = FrameBodyIPLS(
                frame.frameBody?.getTextEncoding() ?: 0.toByte(),
                pairs ?: error("No mapping")
            )
            ipls.frameBody = iplsBody
            frames.add(ipls)
        } else {
            frames.add(ID3v23Frame(frame))
        }
        return frames
    }

    /**
     * Return frame size based upon the sizes of the tags rather than the physical
     * no of bytes between start of ID3Tag and start of Audio Data.
     *
     *
     * TODO this is incorrect, because of subclasses
     *
     * @return size of tag
     */
    override fun getSize(): Int {
        var size = TAG_HEADER_LENGTH
        if (extended) {
            size += TAG_EXT_HEADER_LENGTH
            if (crcDataFlag) {
                size += TAG_EXT_HEADER_CRC_LENGTH
            }
        }
        size += super.getSize()
        return size
    }

    /**
     * Write tag to file
     *
     *
     * TODO:we currently never write the Extended header , but if we did the size calculation in this
     * method would be slightly incorrect
     *
     * @param file The file to write to
     */
    override fun write(file: File, audioStartLocation: Long): Long {
        log.debug("Writing tag to file:")

        //Write Body Buffer
        var bodyByteBuffer: ByteArray = writeFramesToBuffer().toByteArray()
        log.debug(
            "bodybytebuffer:sizebeforeunsynchronisation:" +
                    bodyByteBuffer.size
        )

        // Unsynchronize if option enabled and unsync required
        unsynchronization =
            TagOptionSingleton.unsyncTags &&
                    ID3Unsynchronization.requiresUnsynchronization(bodyByteBuffer)
        if (unsynchronization) {
            bodyByteBuffer = ID3Unsynchronization.unsynchronize(bodyByteBuffer)
            log.debug(
                "bodybytebuffer:sizeafterunsynchronisation:${bodyByteBuffer.size}"
            )
        }

        val sizeIncPadding: Int = calculateTagSize(
            bodyByteBuffer.size + TAG_HEADER_LENGTH,
            audioStartLocation.toInt()
        )
        val padding = sizeIncPadding - (bodyByteBuffer.size + TAG_HEADER_LENGTH)
        log.debug(
            "Current audiostart:$audioStartLocation"
        )
        log.debug(
            "Size including padding:$sizeIncPadding"
        )
        log.debug("Padding:$padding")

        val headerBuffer = writeHeaderToBuffer(
            padding,
            bodyByteBuffer.size
        )
        writeBufferToFile(
            file,
            headerBuffer,
            bodyByteBuffer,
            padding,
            sizeIncPadding,
            audioStartLocation
        )
        return sizeIncPadding.toLong()
    }

    /**
     * Write the ID3 header to the ByteBuffer.
     *
     *
     * TODO Calculate the CYC Data Check
     * TODO Reintroduce Extended Header
     *
     * @param padding is the size of the padding portion of the tag
     * @param size    is the size of the body data
     * @return ByteBuffer
     */
    private fun writeHeaderToBuffer(padding: Int, size: Int): ByteBuffer {
        // Flags,currently we never calculate the CRC
        // and if we dont calculate them cant keep orig values. Tags are not
        // experimental and we never createField extended header to keep things simple.
        extended = false
        experimental = false
        crcDataFlag = false

        // Create Header Buffer,allocate maximum possible size for the header
        val headerBuffer = ByteBuffer.allocate(
            TAG_HEADER_LENGTH + TAG_EXT_HEADER_LENGTH + TAG_EXT_HEADER_CRC_LENGTH
        )

        //TAGID
        headerBuffer.put(TAG_ID)

        //Major Version
        headerBuffer.put(getMajorVersion())

        //Minor Version
        headerBuffer.put(getRevision())

        //Flags
        var flagsByte: Byte = 0
        if (unsynchronization) {
            flagsByte = flagsByte or MASK_V23_UNSYNCHRONIZATION
        }
        if (extended) {
            flagsByte = flagsByte or MASK_V23_EXTENDED_HEADER
        }
        if (experimental) {
            flagsByte = flagsByte or MASK_V23_EXPERIMENTAL
        }
        headerBuffer.put(flagsByte)

        //Additional Header Size,(for completeness we never actually write the extended header)
        var additionalHeaderSize = 0
        if (extended) {
            additionalHeaderSize += TAG_EXT_HEADER_LENGTH
            if (crcDataFlag) {
                additionalHeaderSize += TAG_EXT_HEADER_CRC_LENGTH
            }
        }

        //Size As Recorded in Header, don't include the main header length
        headerBuffer.put(
            ID3SyncSafeInteger.valueToBuffer(padding + size + additionalHeaderSize)
        )

        //Write Extended Header
        if (extended) {
            var extFlagsByte1: Byte = 0
            val extFlagsByte2: Byte = 0

            //Contains CRCData
            if (crcDataFlag) {
                headerBuffer.putInt(
                    TAG_EXT_HEADER_DATA_LENGTH + TAG_EXT_HEADER_CRC_LENGTH
                )
                extFlagsByte1 = extFlagsByte1 or MASK_V23_CRC_DATA_PRESENT
                headerBuffer.put(extFlagsByte1)
                headerBuffer.put(extFlagsByte2)
                headerBuffer.putInt(paddingSize)
                headerBuffer.putInt(crc32)
            } else {
                headerBuffer.putInt(TAG_EXT_HEADER_DATA_LENGTH)
                headerBuffer.put(extFlagsByte1)
                headerBuffer.put(extFlagsByte2)
                //Newly Calculated Padding As Recorded in Extended Header
                headerBuffer.putInt(padding)
            }
        }

        headerBuffer.flip()

        return headerBuffer
    }

    /**
     * {@inheritDoc}
     */
    public override fun read(buffer: ByteBuffer) {
        val size: Int
        if (!seek(buffer)) {
            throw TagNotFoundException(getIdentifier() + " tag not found")
        }
        log.debug("Reading ID3v23 tag")

        readHeaderFlags(buffer)

        // Read the size, this is size of tag not including the tag header
        size = ID3SyncSafeInteger.bufferToValue(buffer)
        log.debug(ErrorMessage.ID_TAG_SIZE.getMsg(size))

        //Extended Header
        if (extended) {
            readExtendedHeader(buffer, size)
        }

        //Slice Buffer, so position markers tally with size (i.e do not include tagHeader)
        var bufferWithoutHeader = buffer.slice()
        //We need to synchronize the buffer
        if (unsynchronization) {
            bufferWithoutHeader = ID3Unsynchronization.synchronize(
                bufferWithoutHeader
            )
        }

        readFrames(bufferWithoutHeader, size)
        log.debug(
            "Loaded Frames,there are:" +
                    frameMap.size
        )
    }

    /**
     * Read header flags
     *
     *
     * Log info messages for flags that have been set and log warnings when bits have been set for unknown flags
     *
     * @param buffer
     */
    private fun readHeaderFlags(buffer: ByteBuffer) {
        //Allowable Flags
        val flags = buffer.get()
        unsynchronization = (flags and MASK_V23_UNSYNCHRONIZATION) != 0.toByte()
        extended = (flags and MASK_V23_EXTENDED_HEADER) != 0.toByte()
        experimental = (flags and MASK_V23_EXPERIMENTAL) != 0.toByte()

        //Not allowable/Unknown Flags
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
                    FileConstants.BIT0
                )
            )
        }

        if (unsynchronization) {
            log.debug(
                ErrorMessage.ID3_TAG_UNSYNCHRONIZED.getMsg()
            )
        }

        if (extended) {
            log.debug(ErrorMessage.ID3_TAG_EXTENDED.getMsg())
        }

        if (experimental) {
            log.debug(
                ErrorMessage.ID3_TAG_EXPERIMENTAL.getMsg()
            )
        }
    }

    /**
     * Read the optional extended header
     *
     * @param buffer
     * @param size
     */
    private fun readExtendedHeader(buffer: ByteBuffer, size: Int) {
        // Int is 4 bytes.
        var size = size
        val extendedHeaderSize = buffer.getInt()
        // Extended header without CRC Data
        if (extendedHeaderSize == TAG_EXT_HEADER_DATA_LENGTH) {
            //Flag should not be setField , if is log a warning
            val extFlag = buffer.get()
            crcDataFlag = (extFlag and MASK_V23_CRC_DATA_PRESENT) != 0.toByte()
            if (crcDataFlag) {
                log.warn(
                    ErrorMessage.ID3_TAG_CRC_FLAG_SET_INCORRECTLY.getMsg()
                )
            }
            //2nd Flag Byte (not used)
            buffer.get()

            //Take padding and ext header size off the size to be read
            paddingSize = buffer.getInt()
            if (paddingSize > 0) {
                log.debug(
                    ErrorMessage.ID3_TAG_PADDING_SIZE.getMsg(
                        paddingSize
                    )
                )
            }
            size = size - (paddingSize + TAG_EXT_HEADER_LENGTH)
        } else if (extendedHeaderSize ==
            TAG_EXT_HEADER_DATA_LENGTH + TAG_EXT_HEADER_CRC_LENGTH
        ) {
            log.debug(ErrorMessage.ID3_TAG_CRC.getMsg())

            //Flag should be setField, if nor just act as if it is
            val extFlag = buffer.get()
            crcDataFlag = (extFlag and MASK_V23_CRC_DATA_PRESENT) != 0.toByte()
            if (!crcDataFlag) {
                log.warn(
                    ErrorMessage.ID3_TAG_CRC_FLAG_SET_INCORRECTLY.getMsg()
                )
            }
            //2nd Flag Byte (not used)
            buffer.get()
            //Take padding size of size to be read
            paddingSize = buffer.getInt()
            if (paddingSize > 0) {
                log.debug(
                    ErrorMessage.ID3_TAG_PADDING_SIZE.getMsg(
                        paddingSize
                    )
                )
            }
            size =
                size -
                        (paddingSize + TAG_EXT_HEADER_LENGTH + TAG_EXT_HEADER_CRC_LENGTH)
            //CRC Data
            crc32 = buffer.getInt()
            log.debug(
                ErrorMessage.ID3_TAG_CRC_SIZE.getMsg(crc32)
            )
        } else {
            log.warn(
                ErrorMessage.ID3_EXTENDED_HEADER_SIZE_INVALID.getMsg(
                    extendedHeaderSize
                )
            )
            buffer.position(buffer.position() - FIELD_TAG_EXT_SIZE_LENGTH)
        }
    }

    /**
     * Read the frames
     *
     *
     * Read from byteBuffer upto size
     *
     * @param byteBuffer
     * @param size
     */
    fun readFrames(byteBuffer: ByteBuffer, size: Int) {
        //Now start looking for frames
        var next: ID3v23Frame?
        frameMap = mutableMapOf()
        encryptedFrameMap = mutableMapOf()

        //Read the size from the Tag Header
        this.fileReadSize = size
        log.debug(
            "Start of frame body at:${byteBuffer.position()},frames data size is:$size"
        )

        // Read the frames until got to up to the size as specified in header or until
        // we hit an invalid frame identifier or padding
        while (byteBuffer.position() < size) {
            val id: String?
            try {
                //Read Frame
                val posBeforeRead = byteBuffer.position()
                log.debug(
                    "Looking for next frame at:$posBeforeRead"
                )
                next = ID3v23Frame(byteBuffer)
                id = next.getIdentifier()
                log.debug(
                    "Found " +
                            id +
                            " at frame at:" +
                            posBeforeRead
                )
                loadFrameIntoMap(id, next)
            } catch (ex: PaddingException) { //Found Padding, no more frames
                log.debug(
                    "Found padding starting at:${byteBuffer.position()}"
                )
                break
            } catch (ex: EmptyFrameException) { //Found Empty Frame, log it - empty frames should not exist
                log.warn("Empty Frame:${ex.message}")
                this.emptyFrameBytes += ID3v23Frame.FRAME_HEADER_SIZE
            } catch (ifie: InvalidFrameIdentifierException) {
                log.warn(
                    "Invalid Frame Identifier:${ifie.message}"
                )
                this.invalidFrames++
                //Don't try and find any more frames
                break
            } //and we have reached padding //Problem trying to find frame, often just occurs because frameHeader includes padding
            catch (ife: InvalidFrameException) {
                log.warn(
                    "Invalid Frame:${ife.message}"
                )
                this.invalidFrames++
                //Don't try and find any more frames
                break
            } //in case we can read the next frame //Failed reading frame but may just have invalid data but correct length so lets carry on
            catch (idete: InvalidDataTypeException) {
                log.warn(
                    "Corrupt Frame:${idete.message}"
                )
                this.invalidFrames++
                continue
            }
        }
    }

    /**
     * @return comparator used to order frames in preferred order for writing to file
     * so that most important frames are written first.
     */
    override fun getPreferredFrameOrderComparator(): Comparator<String> {
        return ID3v23PreferredFrameOrderComparator.instance
    }
}