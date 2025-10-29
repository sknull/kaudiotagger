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
import de.visualdigits.kaudiotagger.model.common.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.SupportedTag
import de.visualdigits.kaudiotagger.model.id3.frame.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v22Frame
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractFrameBodyTextInfo
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyAPIC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyPIC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTCON
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTDRC
import de.visualdigits.kaudiotagger.model.id3.types.ID3V22FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3V24FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ImageFormats
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

class ID3v22Tag : AbstractID3v2Tag {

    companion object {

        /**
         * Bit mask to indicate tag is Unsychronization
         */
        val MASK_V22_UNSYNCHRONIZATION: Int = FileConstants.BIT7
        /**
         * Bit mask to indicate tag is compressed, although compression is not
         * actually defined in v22 so just ignored
         */
        val MASK_V22_COMPRESSION: Int = FileConstants.BIT6

        const val RELEASE: Int = 2
        const val MAJOR_VERSION: Int = 2
        const val REVISION: Int = 0
        const val TYPE_COMPRESSION: String = "compression"
        const val TYPE_UNSYNCHRONISATION: String = "unsyncronisation"


        fun read(byteBuffer: ByteBuffer?): ID3v22Tag? {
            val tag = ID3v22Tag()

            return if (tag.read(byteBuffer)) tag else null
        }
    }

    /**
     * Creates a new empty ID3v2_2 tag.
     */
    constructor()

    /**
     * Copy Constructor, creates a new ID3v2_2 Tag based on another ID3v2_2 Tag
     *
     * @param copyObject
     */
    constructor(copyObject: ID3v22Tag) : super(copyObject) {
        //This doesnt do anything.
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
        //Default Superclass constructor does nothing
        if (mp3tag != null) {
            val convertedTag: ID3v24Tag?
            //Should use the copy constructor instead
            if ((mp3tag !is ID3v23Tag) && (mp3tag is ID3v22Tag)) {
                throw UnsupportedOperationException(
                    "Copy Constructor not called. Please type cast the argument"
                )
            } else if (mp3tag is ID3v24Tag) {
                convertedTag = mp3tag
            } else {
                convertedTag = ID3v24Tag(mp3tag)
            }
            //Set the primitive types specific to v2_2.
            copyPrimitives(convertedTag)
            //Set v2.2 Frames
            copyFrames(convertedTag)
            log.debug("Created tag from a tag of a different version")
        }
    }

    override fun supportedTag(): SupportedTag = SupportedTag.ID3v22Tag

    /**
     * Copy primitives applicable to v2.2
     */
    override fun copyPrimitives(copyObj: AbstractID3v2Tag) {
        log.debug("Copying primitives")
        super.copyPrimitives(copyObj)

        //Set the primitive types specific to v2_2.
        if (copyObj is ID3v22Tag) {
            this.isCompression = copyObj.isCompression
            this.isUnsynchronization = copyObj.isUnsynchronization
        } else if (copyObj is ID3v23Tag) {
            this.isCompression = copyObj.isCompression
            this.isUnsynchronization = copyObj.isUnsynchronization
        } else if (copyObj is ID3v24Tag) {
            this.isCompression = false
            this.isUnsynchronization = copyObj.isUnsynchronization
        }
    }

    /**
     * The tag is compressed, although no compression scheme is defined in ID3v22
     */
    var isCompression: Boolean = false

    /**
     * If set all frames in the tag uses unsynchronisation
     */
    var isUnsynchronization: Boolean = false

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

    /**
     * @return an indentifier of the tag type
     */
    override fun getIdentifier(): String {
        return "ID3v2_2.20"
    }

    /**
     * Return frame size based upon the sizes of the frames rather than the size
     * including padding recorded in the tag header
     *
     * @return size
     */
    override fun getSize(): Int {
        return TAG_HEADER_LENGTH + super.getSize()
    }

    /**
     * {@inheritDoc}
     */
    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null || !seek(byteBuffer)) {
            return false
        }
        log.debug("Reading tag from file")

        //Read the flags
        readHeaderFlags(byteBuffer)

        // Read the size
        val size: Int = ID3SyncSafeInteger.bufferToValue(byteBuffer)

        //Slice Buffer, so position markers tally with size (i.e do not include tagheader)
        var bufferWithoutHeader = byteBuffer.slice()

        //We need to synchronize the buffer
        if (this.isUnsynchronization) {
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
        val major = byteBuffer.get().toInt()
        if (major != getMajorVersion()) {
            return false
        }
        //Minor Version
        val minor = byteBuffer.get().toInt()
        return minor == getRevision()
    }

    /**
     * Read tag Header Flags
     *
     * @param byteBuffer
     */
    private fun readHeaderFlags(byteBuffer: ByteBuffer) {
        //Flags
        val flags = byteBuffer.get().toInt()
        isUnsynchronization = (flags and MASK_V22_UNSYNCHRONIZATION) != 0
        isCompression = (flags and MASK_V22_COMPRESSION) != 0

        if (isUnsynchronization) {
            log.debug(
                ErrorMessage.ID3_TAG_UNSYNCHRONIZED.getMsg()
            )
        }

        if (isCompression) {
            log.debug(
                ErrorMessage.ID3_TAG_COMPRESSED.getMsg()
            )
        }

        //Not allowable/Unknown Flags
        if ((flags and FileConstants.BIT5) != 0) {
            log.warn(
                ErrorMessage.ID3_INVALID_OR_UNKNOWN_FLAG_SET.getMsg(
                    FileConstants.BIT5
                )
            )
        }
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
        var next: ID3v22Frame?
        frameMap.clear()
        encryptedFrameMap.clear()

        //Read the size from the Tag Header
        this.fileReadBytes = size
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
        if ((frame.getIdentifier() == ID3V24FrameId.YEAR.id) && (tmpBody is FrameBodyTDRC)) {
            var newFrame: ID3v22Frame
            if (tmpBody.year.isNotEmpty()) {
                //Create Year frame (v2.2 id,but uses v2.3 body)
                newFrame = ID3v22Frame(ID3V22FrameId.TYER.id)
                (newFrame.frameBody as AbstractFrameBodyTextInfo).setText(tmpBody.year)
                frames.add(newFrame)
            }
            if (tmpBody.time.isNotEmpty()) {
                //Create Time frame (v2.2 id,but uses v2.3 body)
                newFrame = ID3v22Frame(ID3V22FrameId.TIME.id)
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
    override fun write(file: File?, audioStartByte: Long): Long {
        log.debug("Writing tag to file:")

        // Write Body Buffer
        var bodyByteBuffer = writeFramesToBuffer().toByteArray()

        // Unsynchronize if option enabled and unsync required
        isUnsynchronization =
            TagOptionSingleton.unsyncTags &&
                    ID3Unsynchronization.requiresUnsynchronization(bodyByteBuffer)
        if (isUnsynchronization) {
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
            file,
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
        isCompression = false

        //Create Header Buffer
        val headerBuffer = ByteBuffer.allocate(TAG_HEADER_LENGTH)

        //TAGID
        headerBuffer.put(TAG_ID)
        //Major Version
        headerBuffer.put(getMajorVersion().toByte())
        //Minor Version
        headerBuffer.put(getRevision().toByte())

        //Flags
        var flags = 0
        if (isUnsynchronization) {
            flags = flags or MASK_V22_UNSYNCHRONIZATION
        }
        if (isCompression) {
            flags = flags or MASK_V22_COMPRESSION
        }

        headerBuffer.put(flags.toByte())

        //Size As Recorded in Header, don't include the main header length
        headerBuffer.put(ID3SyncSafeInteger.valueToBuffer(padding + size))
        headerBuffer.flip()

        return headerBuffer
    }
    /**
     * Maps the generic key to the id3 key and return the list of values for this field as strings
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
        } else {
            super.getAll(id)
        }
    }

    override fun getFrameAndSubIdFromGenericKey(genericKey: GenericFieldKey): FrameAndSubId {
        val id3v22FieldKey = ID3V22FrameId.fromFieldKey(genericKey) ?: throw KeyNotFoundException(genericKey.name)
        return FrameAndSubId(
            genericKey,
            id3v22FieldKey.id,
            id3v22FieldKey.fieldKey?.subId
        )
    }

    /**
     * Create Frame
     *
     * @param id frameid
     * @return
     */
    override fun createFrame(id: String): ID3v22Frame {
        return ID3v22Frame(id)
    }

    override fun createField(genericKey: GenericFieldKey, vararg values: String): TagField {
        val value: String = values[0]
        if (genericKey == GenericFieldKey.GENRE) {
            val formatKey = getFrameAndSubIdFromGenericKey(genericKey)
            val frame: AbstractID3v2Frame = createFrame(formatKey.frameId)
            val framebody = frame.frameBody as FrameBodyTCON
            framebody.setV23Format()
            framebody.setText(FrameBodyTCON.convertGenericToID3v22Genre(value))
            return frame
        } else {
            return super.createField(genericKey, *values)
        }
    }


    /**
     * {@inheritDoc}
     */
    override fun createField(artwork: Artwork): TagField {
        val frame: AbstractID3v2Frame = createFrame(
            getFrameAndSubIdFromGenericKey(GenericFieldKey.COVER_ART).frameId
        )
        val body = frame.frameBody as FrameBodyPIC
        if (!artwork.isLinked) {
            body.setObjectValue(DataTypes.OBJ_PICTURE_DATA, artwork.binaryData)
            body.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, artwork.pictureType)
            body.setObjectValue(
                DataTypes.OBJ_IMAGE_FORMAT,
                ImageFormats.fromMimeType(artwork.mimeType)
            )
            body.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
            return frame
        } else {
            body.setObjectValue(
                DataTypes.OBJ_PICTURE_DATA,
                artwork.imageUrl?.toByteArray(StandardCharsets.ISO_8859_1)
            )
            body.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, artwork.pictureType)
            body.setObjectValue(
                DataTypes.OBJ_IMAGE_FORMAT,
                FrameBodyAPIC.IMAGE_IS_URL
            )
            body.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
            return frame
        }
    }

    fun createArtworkField(data: ByteArray?, mimeType: String?): TagField {
        val frame: AbstractID3v2Frame = createFrame(
            getFrameAndSubIdFromGenericKey(GenericFieldKey.COVER_ART).frameId
        )
        val body = frame.frameBody as FrameBodyPIC
        body.setObjectValue(DataTypes.OBJ_PICTURE_DATA, data)
        body.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, PictureTypes.DEFAULT_ID)
        body.setObjectValue(
            DataTypes.OBJ_IMAGE_FORMAT,
            ImageFormats.fromMimeType(mimeType)
        )
        body.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
        return frame
    }

    /**
     * Delete fields with this (frame) id
     *
     * @param id
     */
    override fun deleteField(id: String) {
        super.doDeleteTagField(FrameAndSubId(null, id, null))
    }

    override fun createStructure() {
        MP3File.tagFormatter?.openHeadingElement(
            TYPE_TAG,
            getIdentifier()?:""
        )

        super.createStructureHeader()

        //Header
        MP3File.tagFormatter?.openHeadingElement(TYPE_HEADER, "")
        MP3File.tagFormatter?.addElement(
            TYPE_COMPRESSION,
            this.isCompression
        )
        MP3File.tagFormatter?.addElement(
            TYPE_UNSYNCHRONISATION,
            this.isUnsynchronization
        )
        MP3File.tagFormatter?.closeHeadingElement(TYPE_HEADER)
        //Body
        super.createStructureBody()

        MP3File.tagFormatter?.closeHeadingElement(TYPE_TAG)
    }
}