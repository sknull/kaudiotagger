package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.exceptions.EmptyFrameException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameIdentifierException
import de.visualdigits.kaudiotagger.model.common.exceptions.KeyNotFoundException
import de.visualdigits.kaudiotagger.model.common.exceptions.PaddingException
import de.visualdigits.kaudiotagger.model.common.field.TagField
import de.visualdigits.kaudiotagger.model.common.field.TagTextField
import de.visualdigits.kaudiotagger.model.common.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.SupportedTag
import de.visualdigits.kaudiotagger.model.id3.frame.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyAPIC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyIPLS
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTCON
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTDAT
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTDRC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIME
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIPL
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTMCL
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTYER
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.model.id3.types.PictureTypes
import de.visualdigits.kaudiotagger.model.images.Artwork
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.FileConstants
import de.visualdigits.kaudiotagger.util.ID3SyncSafeInteger
import de.visualdigits.kaudiotagger.util.ID3Unsynchronization
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.File
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class ID3v23Tag : AbstractID3v2Tag {

    companion object {

        /**
         * ID3v2.3 Header bit mask
         */
        val MASK_V23_UNSYNCHRONIZATION: Int = FileConstants.BIT7

        /**
         * ID3v2.3 Header bit mask
         */
        val MASK_V23_EXTENDED_HEADER: Int = FileConstants.BIT6

        /**
         * ID3v2.3 Header bit mask
         */
        val MASK_V23_EXPERIMENTAL: Int = FileConstants.BIT5

        /**
         * ID3v2.3 Extended Header bit mask
         */
        val MASK_V23_CRC_DATA_PRESENT: Int = FileConstants.BIT7

        /**
         * ID3v2.3 RBUF frame bit mask
         */
        val MASK_V23_EMBEDDED_INFO_FLAG: Int = FileConstants.BIT1

        const val RELEASE: Int = 2
        const val MAJOR_VERSION: Int = 3
        const val REVISION: Int = 0
        const val TYPE_CRCDATA: String = "crcdata"
        const val TYPE_EXPERIMENTAL: String = "experimental"
        const val TYPE_EXTENDED: String = "extended"
        const val TYPE_PADDINGSIZE: String = "paddingsize"
        const val TYPE_UNSYNCHRONISATION: String = "unsyncronisation"

        var TAG_EXT_HEADER_LENGTH: Int = 10
        var TAG_EXT_HEADER_CRC_LENGTH: Int = 4
        var FIELD_TAG_EXT_SIZE_LENGTH: Int = 4
        var TAG_EXT_HEADER_DATA_LENGTH: Int = TAG_EXT_HEADER_LENGTH - FIELD_TAG_EXT_SIZE_LENGTH


        fun read(byteBuffer: ByteBuffer?): ID3v23Tag? {
            val tag = ID3v23Tag()

            return if (tag.read(byteBuffer)) tag else null
        }
    }

    /**
     * CRC Checksum calculated
     */
    var isCrcDataFlag: Boolean = false

    /**
     * Experiemntal tag
     */
    var isExperimental: Boolean = false

    /**
     * Contains extended header
     */
    var isExtended: Boolean = false

    /**
     * All frames in the tag uses unsynchronisation
     */
    var isUnsynchronization: Boolean = false

    /**
     * The tag is compressed
     */
    var isCompression: Boolean = false

    /**
     * Crcdata Checksum in extended header
     */
    var crc32 = 0

    /**
     * Tag padding
     */
    var paddingSize = 0

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
        copyPrimitives(convertedTag)
        //Copy Frames
        copyFrames(convertedTag)
        log.debug("Created tag from a tag of a different version")
    }

    override fun supportedTag(): SupportedTag = SupportedTag.ID3v23Tag

    /**
     * @return textual tag identifier
     */
    override fun getIdentifier(): String {
        return "ID3v2.30"
    }

    /**
     * Retrieve the Release
     */
    override fun getRelease(): Int {
        return RELEASE
    }

    /**
     * Retrieve the Major Version
     */
    override fun getMajorVersion(): Int {
        return MAJOR_VERSION
    }

    /**
     * Retrieve the Revision
     */
    override fun getRevision(): Int {
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
        if ((frame.getIdentifier() == ID3v24FrameId.YEAR.id) &&
            (frame.frameBody is FrameBodyTDRC)
        ) {
            val tmpBody = frame.frameBody as FrameBodyTDRC
            //TODO will overwrite any existing TYER or TIME frame, do we ever want multiples of these
            tmpBody.findMatchingMaskAndExtractV3Values()
            var newFrame: ID3v23Frame
            if (tmpBody.year != "") {
                newFrame = ID3v23Frame(ID3v23FrameId.TYER.id)
                (newFrame.frameBody as FrameBodyTYER).setText(tmpBody.year)
                frames.add(newFrame)
            }
            if (tmpBody.date != "") {
                newFrame = ID3v23Frame(ID3v23FrameId.TDAT.id)
                (newFrame.frameBody as FrameBodyTDAT).setText(tmpBody.date)
                (newFrame.frameBody as FrameBodyTDAT).isMonthOnly = tmpBody.monthOnly
                frames.add(newFrame)
            }
            if (!tmpBody.time.equals("")) {
                newFrame = ID3v23Frame(ID3v23FrameId.TIME.id)
                (newFrame.frameBody as FrameBodyTIME).setText(tmpBody.time)
                (newFrame.frameBody as FrameBodyTIME).hoursOnly = tmpBody.hoursOnly
                frames.add(newFrame)
            }
        } else if ((frame.getIdentifier() == ID3v24FrameId.INVOLVED_PEOPLE.id) &&
            (frame.frameBody is FrameBodyTIPL)
        ) {
            val pairs = (frame.frameBody as FrameBodyTIPL).getPairing()?.mapping
            val ipls: AbstractID3v2Frame = ID3v23Frame(
                frame as ID3v24Frame,
                ID3v23FrameId.INVOLVED_PEOPLE.id
            )
            val iplsBody = FrameBodyIPLS(
                frame.frameBody?.getTextEncoding() ?: 0,
                pairs ?: error("No mapping")
            )
            ipls.frameBody = iplsBody
            frames.add(ipls)
        } else if ((frame.getIdentifier() == ID3v24FrameId.MUSICIAN_CREDITS.id) &&
            (frame.frameBody is FrameBodyTMCL)
        ) {
            val pairs = (frame.frameBody as FrameBodyTMCL).getPairing()?.mapping
            val ipls: AbstractID3v2Frame = ID3v23Frame(
                frame as ID3v24Frame,
                ID3v23FrameId.INVOLVED_PEOPLE.id
            )
            val iplsBody = FrameBodyIPLS(
                frame.frameBody?.getTextEncoding() ?: 0,
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
        if (isExtended) {
            size += TAG_EXT_HEADER_LENGTH
            if (isCrcDataFlag) {
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
    override fun write(file: File?, audioStartLocation: Long): Long {
        log.debug("Writing tag to file:")

        //Write Body Buffer
        var bodyByteBuffer: ByteArray = writeFramesToBuffer().toByteArray()
        log.debug(
            "bodybytebuffer:sizebeforeunsynchronisation:" +
                    bodyByteBuffer.size
        )

        // Unsynchronize if option enabled and unsync required
        isUnsynchronization =
            TagOptionSingleton.unsyncTags &&
                    ID3Unsynchronization.requiresUnsynchronization(bodyByteBuffer)
        if (isUnsynchronization) {
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
        isExtended = false
        isExperimental = false
        isCrcDataFlag = false

        // Create Header Buffer,allocate maximum possible size for the header
        val headerBuffer = ByteBuffer.allocate(
            TAG_HEADER_LENGTH + TAG_EXT_HEADER_LENGTH + TAG_EXT_HEADER_CRC_LENGTH
        )

        //TAGID
        headerBuffer.put(TAG_ID)

        //Major Version
        headerBuffer.put(getMajorVersion().toByte())

        //Minor Version
        headerBuffer.put(getRevision().toByte())

        //Flags
        var flagsByte: Int = 0
        if (isUnsynchronization) {
            flagsByte = flagsByte or MASK_V23_UNSYNCHRONIZATION
        }
        if (isExtended) {
            flagsByte = flagsByte or MASK_V23_EXTENDED_HEADER
        }
        if (isExperimental) {
            flagsByte = flagsByte or MASK_V23_EXPERIMENTAL
        }
        headerBuffer.put(flagsByte.toByte())

        //Additional Header Size,(for completeness we never actually write the extended header)
        var additionalHeaderSize = 0
        if (isExtended) {
            additionalHeaderSize += TAG_EXT_HEADER_LENGTH
            if (isCrcDataFlag) {
                additionalHeaderSize += TAG_EXT_HEADER_CRC_LENGTH
            }
        }

        //Size As Recorded in Header, don't include the main header length
        headerBuffer.put(
            ID3SyncSafeInteger.valueToBuffer(padding + size + additionalHeaderSize)
        )

        //Write Extended Header
        if (isExtended) {
            var extFlagsByte1: Int = 0
            val extFlagsByte2: Int = 0

            //Contains CRCData
            if (isCrcDataFlag) {
                headerBuffer.putInt(
                    TAG_EXT_HEADER_DATA_LENGTH + TAG_EXT_HEADER_CRC_LENGTH
                )
                extFlagsByte1 = extFlagsByte1 or MASK_V23_CRC_DATA_PRESENT
                headerBuffer.put(extFlagsByte1.toByte())
                headerBuffer.put(extFlagsByte2.toByte())
                headerBuffer.putInt(paddingSize)
                headerBuffer.putInt(crc32)
            } else {
                headerBuffer.putInt(TAG_EXT_HEADER_DATA_LENGTH)
                headerBuffer.put(extFlagsByte1.toByte())
                headerBuffer.put(extFlagsByte2.toByte())
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
    override fun read(buffer: ByteBuffer?): Boolean {
        if (buffer == null || !seek(buffer)) {
            return false
        }
        log.debug("Reading ID3v23 tag")

        readHeaderFlags(buffer)

        // Read the size, this is size of tag not including the tag header
        val size = ID3SyncSafeInteger.bufferToValue(buffer)
        log.debug(ErrorMessage.ID_TAG_SIZE.getMsg(size))

        //Extended Header
        if (isExtended) {
            readExtendedHeader(buffer, size)
        }

        //Slice Buffer, so position markers tally with size (i.e do not include tagHeader)
        var bufferWithoutHeader = buffer.slice()
        //We need to synchronize the buffer
        if (isUnsynchronization) {
            bufferWithoutHeader = ID3Unsynchronization.synchronize(
                bufferWithoutHeader
            )
        }

        readFrames(bufferWithoutHeader, size)
        log.debug(
            "Loaded Frames,there are:" +
                    frameMap.size
        )

        return true
    }

    /**
     * Read header flags
     *
     *
     * Log info messages for flags that have been set and log warnings when bits have been set for unknown flags
     *
     * @param buffer
     */
    private fun readHeaderFlags(buffer: ByteBuffer?) {
        if (buffer == null) {
            return
        }
        //Allowable Flags
        val flags = buffer.get().toInt()
        isUnsynchronization = (flags and MASK_V23_UNSYNCHRONIZATION) != 0
        isExtended = (flags and MASK_V23_EXTENDED_HEADER) != 0
        isExperimental = (flags and MASK_V23_EXPERIMENTAL) != 0

        //Not allowable/Unknown Flags
        if ((flags and FileConstants.BIT4) != 0) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT4
                )
            )
        }

        if ((flags and FileConstants.BIT3) != 0) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT3
                )
            )
        }

        if ((flags and FileConstants.BIT2) != 0) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT2
                )
            )
        }

        if ((flags and FileConstants.BIT1) != 0) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT1
                )
            )
        }

        if ((flags and FileConstants.BIT0) != 0) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT0
                )
            )
        }

        if (isUnsynchronization) {
            log.debug(
                ErrorMessage.ID3_TAG_UNSYNCHRONIZED.getMsg()
            )
        }

        if (isExtended) {
            log.debug(ErrorMessage.ID3_TAG_EXTENDED.getMsg())
        }

        if (isExperimental) {
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
            val extFlag = buffer.get().toInt()
            isCrcDataFlag = (extFlag and MASK_V23_CRC_DATA_PRESENT) != 0
            if (isCrcDataFlag) {
                log.warn(
                    ErrorMessage.ID3_TAG_CRC_FLAG_SET_INCORRECTLY.getMsg()
                )
            }
            //2nd Flag Int (not used)
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
            val extFlag = buffer.get().toInt()
            isCrcDataFlag = (extFlag and MASK_V23_CRC_DATA_PRESENT) != 0
            if (!isCrcDataFlag) {
                log.warn(
                    ErrorMessage.ID3_TAG_CRC_FLAG_SET_INCORRECTLY.getMsg()
                )
            }
            //2nd Flag Int (not used)
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
        frameMap.clear()
        encryptedFrameMap.clear()

        //Read the size from the Tag Header
        this.fileReadBytes = size
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
                log.debug("Looking for next frame at:$posBeforeRead")
                next = ID3v23Frame(byteBuffer)
                id = next.getIdentifier()
                log.debug("Found $id at frame at:$posBeforeRead")
                loadFrameIntoMap(id, next)
            } catch (_: PaddingException) { //Found Padding, no more frames
                log.debug("Found padding starting at:${byteBuffer.position()}")
                break
            } catch (ex: EmptyFrameException) { //Found Empty Frame, log it - empty frames should not exist
                log.warn("Empty Frame:${ex.message}")
                this.emptyFrameBytes += ID3v23Frame.FRAME_HEADER_SIZE
            } catch (ifie: InvalidFrameIdentifierException) {
                log.warn("Invalid Frame Identifier:${ifie.message}")
                this.invalidFrames++
                //Don't try and find any more frames
                break
            } //and we have reached padding //Problem trying to find frame, often just occurs because frameHeader includes padding
            catch (ife: InvalidFrameException) {
                log.warn("Invalid Frame:${ife.message}")
                this.invalidFrames++
                //Don't try and find any more frames
                break
            } //in case we can read the next frame //Failed reading frame but may just have invalid data but correct length so lets carry on
            catch (idete: InvalidDataTypeException) {
                log.warn("Corrupt Frame:${idete.message}")
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

    /**
     * Delete fields with this (frame) id
     *
     * @param id
     */
    override fun deleteField(id: String) {
        super.doDeleteTagField(FrameAndSubId(null, id, null))
    }

    /**
     * {@inheritDoc}
     */
    override fun createField(artwork: Artwork): TagField {
        val frame: AbstractID3v2Frame = createFrame(
            getFrameAndSubIdFromGenericKey(GenericFieldKey.COVER_ART).frameId
        )
        val body = frame.frameBody as FrameBodyAPIC
        if (!artwork.isLinked) {
            body.setObjectValue(DataTypes.OBJ_PICTURE_DATA, artwork.binaryData)
            body.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, artwork.pictureType)
            body.setObjectValue(DataTypes.OBJ_MIME_TYPE, artwork.mimeType)
            body.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
            return frame
        } else {
            body.setObjectValue(
                DataTypes.OBJ_PICTURE_DATA,
                artwork.imageUrl?.toByteArray(StandardCharsets.ISO_8859_1)
            )
            body.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, artwork.pictureType)
            body.setObjectValue(DataTypes.OBJ_MIME_TYPE, FrameBodyAPIC.IMAGE_IS_URL)
            body.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
            return frame
        }
    }

    /**
     * Create Artwork
     *
     * @param data
     * @param mimeType of the image
     * @return
     * @see PictureTypes
     */
    fun createArtworkField(data: ByteArray?, mimeType: String): TagField {
        val frame: AbstractID3v2Frame = createFrame(
            getFrameAndSubIdFromGenericKey(GenericFieldKey.COVER_ART).frameId
        )
        val body = frame.frameBody as FrameBodyAPIC

        body.setObjectValue(DataTypes.OBJ_PICTURE_DATA, data)
        body.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, PictureTypes.DEFAULT_ID)
        body.setObjectValue(DataTypes.OBJ_MIME_TYPE, mimeType)
        body.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
        return frame
    }

    override fun createFrame(id: String): ID3v23Frame {
        return ID3v23Frame(id)
    }

    /**
     * Overridden because GENRE can need converting of data to ID3v23 format and
     * YEAR key is specially processed by getFields() for ID3
     *
     * @param id
     * @return
     */
    override fun getAll(id: GenericFieldKey): List<String> {
        return if (id === GenericFieldKey.GENRE) {
            getFields(id).firstOrNull()?.let { f ->
                ((f as AbstractID3v2Frame).frameBody as FrameBodyTCON)
                    .getValues()
                    .mapNotNull { next -> FrameBodyTCON.convertID3v22GenreToGeneric(next) }
            }?:listOf()
        } else if (id === GenericFieldKey.YEAR) {
            getFields(id).mapNotNull { next ->
                (next as? TagTextField)?.getContent()
            }
        } else {
            super.getAll(id)
        }
    }

    override fun getFrameAndSubIdFromGenericKey(genericKey: GenericFieldKey): FrameAndSubId {
        val id3v23FieldKey = ID3v23FrameId.fromFieldKey(genericKey) ?: throw KeyNotFoundException(genericKey.name)
        return FrameAndSubId(
            genericKey,
            id3v23FieldKey.id,
            id3v23FieldKey.fieldKey?.subId
        )
    }

    /**
     * For representing the MP3File in an XML Format
     */
    override fun createStructure() {
        MP3File.tagFormatter?.openHeadingElement(
            TYPE_TAG,
            getIdentifier()?:""
        )

        super.createStructureHeader()

        //Header
        MP3File.tagFormatter?.openHeadingElement(TYPE_HEADER, "")
        MP3File.tagFormatter?.addElement(
            TYPE_UNSYNCHRONISATION,
            this.isUnsynchronization
        )
        MP3File.tagFormatter?.addElement(TYPE_EXTENDED, this.isExtended)
        MP3File.tagFormatter?.addElement(
            TYPE_EXPERIMENTAL,
            this.isExperimental
        )
        MP3File.tagFormatter?.addElement(TYPE_CRCDATA, this.crc32)
        MP3File.tagFormatter?.addElement(
            TYPE_PADDINGSIZE,
            this.paddingSize
        )
        MP3File.tagFormatter?.closeHeadingElement(TYPE_HEADER)
        //Body
        super.createStructureBody()
        MP3File.tagFormatter?.closeHeadingElement(TYPE_TAG)
    }
}