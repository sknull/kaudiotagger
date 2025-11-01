package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.exceptions.EmptyFrameException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidTagException
import de.visualdigits.kaudiotagger.model.common.field.TagField
import de.visualdigits.kaudiotagger.model.common.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.StandardIPLSKey
import de.visualdigits.kaudiotagger.model.common.types.SupportedTag
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.frame.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v22Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyAPIC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyCOMM
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyIPLS
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTALB
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTCON
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTDRC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIPL
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIT2
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTMCL
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTPE1
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTRCK
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyUnsupported
import de.visualdigits.kaudiotagger.model.id3.types.GenreTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.model.id3.types.MusicianCredits
import de.visualdigits.kaudiotagger.model.id3.types.PictureTypes
import de.visualdigits.kaudiotagger.model.images.Artwork
import de.visualdigits.kaudiotagger.model.lyrics3.tag.AbstractLyrics3
import de.visualdigits.kaudiotagger.model.lyrics3.tag.Lyrics3v2
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.FileConstants
import de.visualdigits.kaudiotagger.util.ID3SyncSafeInteger
import java.io.File
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class ID3v24Tag : AbstractID3v2Tag {

    companion object {

        /**
         * ID3v2.4 Header bit mask
         */
        const val MASK_V24_UNSYNCHRONIZATION: Int = FileConstants.BIT7
        /**
         * ID3v2.4 Header bit mask
         */
        const val MASK_V24_EXTENDED_HEADER: Int = FileConstants.BIT6
        /**
         * ID3v2.4 Header bit mask
         */
        const val MASK_V24_EXPERIMENTAL: Int = FileConstants.BIT5
        /**
         * ID3v2.4 Header bit mask
         */
        const val MASK_V24_FOOTER_PRESENT: Int = FileConstants.BIT4
        /**
         * ID3v2.4 Extended header bit mask
         */
        const val MASK_V24_TAG_UPDATE: Int = FileConstants.BIT6
        /**
         * ID3v2.4 Extended header bit mask
         */
        const val MASK_V24_CRC_DATA_PRESENT: Int = FileConstants.BIT5
        /**
         * ID3v2.4 Extended header bit mask
         */
        const val MASK_V24_TAG_RESTRICTIONS: Int = FileConstants.BIT4
        /**
         * ID3v2.4 Extended header bit mask
         */
        const val MASK_V24_TAG_SIZE_RESTRICTIONS: Int = FileConstants.BIT7 or FileConstants.BIT6
        /**
         * ID3v2.4 Extended header bit mask
         */
        const val MASK_V24_TEXT_ENCODING_RESTRICTIONS: Int = FileConstants.BIT5
        /**
         * ID3v2.4 Extended header bit mask
         */
        const val MASK_V24_TEXT_FIELD_SIZE_RESTRICTIONS: Int = FileConstants.BIT4 or FileConstants.BIT3
        /**
         * ID3v2.4 Extended header bit mask
         */
        const val MASK_V24_IMAGE_ENCODING: Int = FileConstants.BIT2
        /**
         * ID3v2.4 Extended header bit mask
         */
        const val MASK_V24_IMAGE_SIZE_RESTRICTIONS: Int = FileConstants.BIT2 or FileConstants.BIT1
        /**
         * ID3v2.4 Header Footer bit mask
         */
        const val MASK_V24_TAG_ALTER_PRESERVATION: Int = FileConstants.BIT6
        /**
         * ID3v2.4 Header Footer bit mask
         */
        const val MASK_V24_FILE_ALTER_PRESERVATION: Int = FileConstants.BIT5
        /**
         * ID3v2.4 Header Footer bit mask
         */
        const val MASK_V24_READ_ONLY: Int = FileConstants.BIT4
        /**
         * ID3v2.4 Header Footer bit mask
         */
        const val MASK_V24_GROUPING_IDENTITY: Int = FileConstants.BIT6
        /**
         * ID3v2.4 Header Footer bit mask
         */
        const val MASK_V24_COMPRESSION: Int = FileConstants.BIT4
        /**
         * ID3v2.4 Header Footer bit mask
         */
        const val MASK_V24_ENCRYPTION: Int = FileConstants.BIT3
        /**
         * ID3v2.4 Header Footer bit mask
         */
        const val MASK_V24_FRAME_UNSYNCHRONIZATION: Int = FileConstants.BIT2
        /**
         * ID3v2.4 Header Footer bit mask
         */
        const val MASK_V24_DATA_LENGTH_INDICATOR: Int = FileConstants.BIT1
        
        const val RELEASE: Int = 2
        const val MAJOR_VERSION: Int = 4
        const val REVISION: Int = 0
        const val TYPE_FOOTER: String = "footer"
        const val TYPE_IMAGEENCODINGRESTRICTION: String = "imageEncodingRestriction"
        const val TYPE_IMAGESIZERESTRICTION: String = "imageSizeRestriction"
        const val TYPE_TAGRESTRICTION: String = "tagRestriction"
        const val TYPE_TAGSIZERESTRICTION: String = "tagSizeRestriction"
        const val TYPE_TEXTENCODINGRESTRICTION: String = "textEncodingRestriction"
        const val TYPE_TEXTFIELDSIZERESTRICTION: String = "textFieldSizeRestriction"
        const val TYPE_UPDATETAG: String = "updateTag"
        const val TYPE_CRCDATA: String = "crcdata"
        const val TYPE_EXPERIMENTAL: String = "experimental"

        /**
         * ID3v2.4 Header Footer are the same as the header flags. WHY move the
         * flags from thier position in 2.3
         */
        const val TYPE_EXTENDED: String = "extended"
        const val TYPE_PADDINGSIZE: String = "paddingsize"
        const val TYPE_UNSYNCHRONISATION: String = "unsyncronisation"
        
        var TAG_EXT_HEADER_LENGTH: Int = 6
        var TAG_EXT_HEADER_UPDATE_LENGTH: Int = 1
        var TAG_EXT_HEADER_CRC_LENGTH: Int = 6
        var TAG_EXT_HEADER_RESTRICTION_LENGTH: Int = 2
        var TAG_EXT_HEADER_CRC_DATA_LENGTH: Int = 5
        var TAG_EXT_HEADER_RESTRICTION_DATA_LENGTH: Int = 1
        var TAG_EXT_NUMBER_BYTES_DATA_LENGTH: Int = 1


        fun read(byteBuffer: ByteBuffer?): ID3v24Tag? {
            val tag = ID3v24Tag()

            return if (tag.read(byteBuffer)) tag else null
        }
    }

    override fun supportedTag(): SupportedTag = SupportedTag.ID3v24Tag

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
     * CRC Checksum
     */
    var crcData: Int = 0

    /**
     * Contains a footer
     */
    var isFooter: Boolean = false

    /**
     * Tag is an update
     */
    var isUpdateTag: Boolean = false

    /**
     * Tag has restrictions
     */
    var isTagRestriction: Boolean = false

    /**
     * If Set Image encoding restrictions
     *
     *
     * 0   No restrictions
     * 1   Images are encoded only with PNG PNG or JPEG JFIF.
     */
    var imageEncodingRestriction: Int = 0

    /**
     * If set Image size restrictions
     *
     *
     * 00  No restrictions
     * 01  All images are 256x256 pixels or smaller.
     * 10  All images are 64x64 pixels or smaller.
     * 11  All images are exactly 64x64 pixels, unless required
     * otherwise.
     */
    var imageSizeRestriction: Int = 0

    /**
     * If set then Tag Size Restrictions
     *
     *
     * 00   No more than 128 frames and 1 MB total tag size.
     * 01   No more than 64 frames and 128 KB total tag size.
     * 10   No more than 32 frames and 40 KB total tag size.
     * 11   No more than 32 frames and 4 KB total tag size.
     */
    var tagSizeRestriction: Int = 0

    /**
     * If set Text encoding restrictions
     *
     *
     * 0    No restrictions
     * 1    Strings are only encoded with ISO-8859-1 ISO-8859-1 or
     * UTF-8 UTF-8.
     */
    var textEncodingRestriction: Int = 0

    /**
     * Tag padding
     */
    var paddingSize: Int = 0

    /**
     * If set Text fields size restrictions
     *
     *
     * 00   No restrictions
     * 01   No string is longer than 1024 characters.
     * 10   No string is longer than 128 characters.
     * 11   No string is longer than 30 characters.
     *
     *
     * Note that nothing is said about how many bytes is used to
     * represent those characters, since it is encoding dependent. If a
     * text frame consists of more than one string, the sum of the
     * strungs is restricted as stated.
     */
    var textFieldSizeRestriction: Int = 0

    /**
     * Creates a new empty ID3v2_4 datatype.
     */
    constructor()

    /**
     * Copy Constructor, creates a new ID3v2_4 Tag based on another ID3v2_4 Tag
     *
     * @param copyObject
     */
    constructor(copyObject: ID3v24Tag) {
        log.debug("Creating tag from another tag of same type")
        copyPrimitives(copyObject)
        copyFrames(copyObject)
    }

    /**
     * Creates a new ID3v2_4 datatype based on another (non 2.4) tag
     *
     * @param mp3tag
     */
    constructor(mp3tag: AbstractTag?): this() {
        log.debug("Creating tag from a tag of a different version")
        if (mp3tag != null) {
            // Should use simpler copy constructor
            when (mp3tag) {
                is ID3v24Tag -> {
                    throw UnsupportedOperationException(
                        "Copy Constructor not called. Please type cast the argument"
                    )
                }

                is AbstractID3v2Tag -> {
                    copyPrimitives(mp3tag)
                    copyFrames(mp3tag)
                }

                is ID3v1Tag -> {
                    // convert id3v1 tags.
                    var newFrame: ID3v24Frame?
                    var newBody: AbstractID3v2FrameBody?
                    if (mp3tag.getTitle().isNotEmpty()) {
                        newBody = FrameBodyTIT2(0, mp3tag.getTitle())
                        newFrame = ID3v24Frame(ID3v24FrameId.TITLE.id)
                        newFrame.frameBody = newBody
                        frameMap[newFrame.getIdentifier() ?: error("No identifier")] = newFrame
                    }
                    if (mp3tag.getArtist().isNotEmpty()) {
                        newBody = FrameBodyTPE1(0, mp3tag.getArtist())
                        newFrame = ID3v24Frame(ID3v24FrameId.ARTIST.id)
                        newFrame.frameBody = newBody
                        frameMap[newFrame.getIdentifier() ?: error("No identifier")] = newFrame
                    }
                    if (mp3tag.getAlbum().isNotEmpty()) {
                        newBody = FrameBodyTALB(0, mp3tag.getAlbum())
                        newFrame = ID3v24Frame(ID3v24FrameId.ALBUM.id)
                        newFrame.frameBody = newBody
                        frameMap[newFrame.getIdentifier() ?: error("No identifier")] = newFrame
                    }
                    if (mp3tag.getYear().isNotEmpty()) {
                        newBody = FrameBodyTDRC(0, mp3tag.getYear())
                        newFrame = ID3v24Frame(ID3v24FrameId.YEAR.id)
                        newFrame.frameBody = newBody
                        frameMap[newFrame.getIdentifier() ?: error("No identifier")] = newFrame
                    }
                    if (mp3tag.getComment().isNotEmpty()) {
                        newBody = FrameBodyCOMM(0, "ENG", "", mp3tag.getComment())
                        newFrame = ID3v24Frame(ID3v24FrameId.COMMENT.id)
                        newFrame.frameBody = newBody
                        frameMap[newFrame.getIdentifier() ?: error("No identifier")] = newFrame
                    }
                    if (((mp3tag.getGenre() and ID3v1Tag.BYTE_TO_UNSIGNED) >= 0) &&
                        ((mp3tag.getGenre() and ID3v1Tag.BYTE_TO_UNSIGNED) !=
                                ID3v1Tag.BYTE_TO_UNSIGNED)
                    ) {
                        val genreId: Int = (mp3tag.getGenre() and ID3v1Tag.BYTE_TO_UNSIGNED)
                        val genre = "($genreId) ${GenreTypes.fromId(genreId)}"

                        newBody = FrameBodyTCON(0, genre)
                        newFrame = ID3v24Frame(ID3v24FrameId.GENRE.id)
                        newFrame.frameBody = newBody
                        frameMap[newFrame.getIdentifier() ?: error("No identifier")] = newFrame
                    }
                    if (mp3tag is ID3v11Tag) {
                        mp3tag.track?.let {
                            if (it > 0) {
                                newBody = FrameBodyTRCK(0, mp3tag.track.toString())
                                newFrame = ID3v24Frame(ID3v24FrameId.TRACK.id)
                                newFrame.frameBody = newBody
                                frameMap[newFrame.getIdentifier() ?: error("No identifier")] = newFrame
                            }
                        }
                    }
                }

                is AbstractLyrics3 -> {
                    // Put the conversion stuff in the individual frame code.
                    Lyrics3v2(mp3tag).fieldMap.values.forEach { field ->
                        try {
                            val newFrame = ID3v24Frame(field)
                            frameMap[newFrame.getIdentifier() ?: error("No identifier")] = newFrame
                        } catch (_: InvalidTagException) {
                            log.warn(
                                "Unable to convert Lyrics3 to v24 Frame:Frame Identifier"
                            )
                        }
                    }
                }
            }
        }
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

    /**
     * Copy primitives applicable to v2.4, this is used when cloning a v2.4 datatype
     * and other objects such as v2.3 so need to check instanceof
     */
    override fun copyPrimitives(copyObject: AbstractID3v2Tag) {
        log.debug("Copying primitives")
        super.copyPrimitives(copyObject)

        if (copyObject is ID3v24Tag) {
            this.isFooter = copyObject.isFooter
            this.isTagRestriction = copyObject.isTagRestriction
            this.isUpdateTag = copyObject.isUpdateTag
            this.imageEncodingRestriction = copyObject.imageEncodingRestriction
            this.imageSizeRestriction = copyObject.imageSizeRestriction
            this.tagSizeRestriction = copyObject.tagSizeRestriction
            this.textEncodingRestriction = copyObject.textEncodingRestriction
            this.textFieldSizeRestriction = copyObject.textFieldSizeRestriction
        }
    }

    /**
     * @return identifier
     */
    override fun getIdentifier(): String {
        return "ID3v2.40"
    }

    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null || !seek(byteBuffer)) {
            return false
        }
        log.debug("Reading ID3v24 tag")
        frameMap.clear()
        encryptedFrameMap.clear()

        readHeaderFlags(byteBuffer)

        // Read the size, this is size of tag apart from tag header
        val size = ID3SyncSafeInteger.bufferToValue(byteBuffer)
        log.debug(
            "Reading tag from file size set in header is" +
                    size
        )

        if (isExtended) {
            readExtendedHeader(byteBuffer)
        }

        // Note if there was an extended header the size value has padding taken
        // off so we dont search it.
        readFrames(byteBuffer, size)

        return true
    }

    /**
     * Read frames from tag
     *
     * @param byteBuffer
     * @param size
     */
    fun readFrames(byteBuffer: ByteBuffer, size: Int) {
        log.debug("Start of frame body at${byteBuffer.position()}")
        // Now start looking for frames
        frameMap.clear()
        encryptedFrameMap.clear()

        // Read the size from the Tag Header
        this.fileReadBytes = size
        // Read the frames until got to upto the size as specified in header
        log.debug("Start of frame body at:${byteBuffer.position()},frames data size is:$size")
        while (byteBuffer.position() <= size) {
            try {
                // Read Frame
                log.debug("looking for next frame at:${byteBuffer.position()}")
                val newFram = ID3v24Frame(byteBuffer)
                val identifier = newFram.getIdentifier()
                loadFrameIntoMap(identifier, newFram)
            } catch (ex: EmptyFrameException) { // Found Empty Frame
                log.warn("Empty Frame:${ex.message}")
                this.emptyFrameBytes += TAG_HEADER_LENGTH
            } catch (ife: InvalidFrameException) { // Problem trying to find frame
                log.warn("Invalid Frame:${ife.message}")
                // Don't try and find any more frames
                break
            } // in case we can read the next frame // Failed reading frame but may just have invalid data but correct length so lets carry on
            catch (idete: InvalidDataTypeException) {
                log.warn("Corrupt Frame:${idete.message}")
                continue
            }
        }
    }

    /**
     * Read the optional extended header
     *
     * @param byteBuffer
     */
    private fun readExtendedHeader(byteBuffer: ByteBuffer) {
        var buffer: ByteArray

        // int is 4 bytes.
        val extendedHeaderSize = byteBuffer.getInt()

        // the extended header must be at least 6 bytes
        if (extendedHeaderSize <= TAG_EXT_HEADER_LENGTH) {
            throw InvalidTagException(
                ErrorMessage.ID3_EXTENDED_HEADER_SIZE_TOO_SMALL.getMsg(
                    extendedHeaderSize
                )
            )
        }

        // Number of bytes
        byteBuffer.get()

        // Read the extended flag bytes
        val extFlag = byteBuffer.get().toInt()
        isUpdateTag = (extFlag and MASK_V24_TAG_UPDATE) != 0
        isCrcDataFlag = (extFlag and MASK_V24_CRC_DATA_PRESENT) != 0
        isTagRestriction = (extFlag and MASK_V24_TAG_RESTRICTIONS) != 0

        // read the length byte if the flag is set
        // this tag should always be zero but just in case
        // read this information.
        if (isUpdateTag) {
            byteBuffer.get()
        }

        // CRC-32
        if (isCrcDataFlag) {
            // the CRC has a variable length
            byteBuffer.get()
            buffer = ByteArray(TAG_EXT_HEADER_CRC_DATA_LENGTH)
            byteBuffer[buffer, 0, TAG_EXT_HEADER_CRC_DATA_LENGTH]
            crcData = 0
            for (i in 0..<TAG_EXT_HEADER_CRC_DATA_LENGTH) {
                crcData = crcData shl 8
                crcData += buffer[i]
            }
        }

        // Tag Restriction
        if (isTagRestriction) {
            byteBuffer.get()
            buffer = ByteArray(1)
            byteBuffer[buffer, 0, 1]
            tagSizeRestriction = ((buffer[0].toInt() and
                    MASK_V24_TAG_SIZE_RESTRICTIONS) shr
                    6)
            textEncodingRestriction = ((buffer[0].toInt() and
                    MASK_V24_TEXT_ENCODING_RESTRICTIONS) shr
                    5)
            textFieldSizeRestriction = ((buffer[0].toInt() and
                    MASK_V24_TEXT_FIELD_SIZE_RESTRICTIONS) shr
                    3)
            imageEncodingRestriction = ((buffer[0].toInt() and
                    MASK_V24_IMAGE_ENCODING) shr
                    2)
            imageSizeRestriction = (buffer[0].toInt() and
                    MASK_V24_IMAGE_SIZE_RESTRICTIONS)
        }
    }

    /**
     * Read header flags
     *
     *
     * Log info messages for falgs that have been set and log warnings when bits have been set for unknown flags
     *
     * @param byteBuffer
     */
    private fun readHeaderFlags(byteBuffer: ByteBuffer) {
        // Flags
        val flags = byteBuffer.get().toInt()
        isUnsynchronization = (flags and MASK_V24_UNSYNCHRONIZATION) != 0
        isExtended = (flags and MASK_V24_EXTENDED_HEADER) != 0
        isExperimental = (flags and MASK_V24_EXPERIMENTAL) != 0
        isFooter = (flags and MASK_V24_FOOTER_PRESENT) != 0

        // Not allowable/Unknown Flags
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

        if (isFooter) {
            log.warn(ErrorMessage.ID3_TAG_FOOTER.getMsg())
        }
    }

    /**
     * @return comparator used to order frames in preferred order for writing to file
     * so that most important frames are written first.
     */
    override fun getPreferredFrameOrderComparator(): Comparator<String> {
        return ID3v24PreferredFrameOrderComparator
    }

    /**
     * Copy the frame
     *
     *
     * If the frame is already an ID3v24 frame we can add as is, if not we need to convert
     * to id3v24 frame(s)
     *
     * @param newFrame
     */
    override fun addFrame(newFrame: AbstractID3v2Frame) {
        if (newFrame is ID3v24Frame) {
            copyFrameIntoMap(newFrame)
        } else {
            convertFrame(newFrame).forEach { next ->
                copyFrameIntoMap(next)
            }
        }
    }

    /**
     * Convert frame into ID3v24 frame(s)
     *
     * @param frame
     * @return
     */
    override fun convertFrame(frame: AbstractID3v2Frame): MutableList<AbstractID3v2Frame> {
        var frame = frame
        val frames: MutableList<AbstractID3v2Frame> = mutableListOf()
        if (frame is ID3v22Frame && frame.getIdentifier() == ID3v22FrameId.IPLS.id) {
            frame = ID3v23Frame(frame)
        }

        // This frame may need splitting and converting into two frames depending on its content
        if (frame is ID3v23Frame &&
            frame.getIdentifier() == ID3v23FrameId.INVOLVED_PEOPLE.id
        ) {
            val pairs = (frame.frameBody as? FrameBodyIPLS)?.getPairing()?.mapping?:error("No mapping")
            val pairsTipl: MutableList<Pair<String, String>> = mutableListOf()
            val pairsTmcl: MutableList<Pair<String, String>> = mutableListOf()

            for (next in pairs) {
                if (StandardIPLSKey.isKey(next.first)) {
                    pairsTipl.add(next)
                } else if (MusicianCredits.isKey(next.first)) {
                    pairsTmcl.add(next)
                } else {
                    pairsTipl.add(next)
                }
            }
            val tipl = ID3v24Frame(
                frame,
                ID3v24FrameId.INVOLVED_PEOPLE.id
            )
            val tiplBody = FrameBodyTIPL(
                frame.frameBody?.getTextEncoding()?:0,
                pairsTipl
            )
            tipl.frameBody = tiplBody
            frames.add(tipl)

            val tmcl: AbstractID3v2Frame = ID3v24Frame(
                frame,
                ID3v24FrameId.MUSICIAN_CREDITS.id
            )
            val tmclBody = FrameBodyTMCL(
                frame.frameBody?.getTextEncoding()?:0,
                pairsTmcl
            )
            tmcl.frameBody = tmclBody
            frames.add(tmcl)
        } else {
            frames.add(ID3v24Frame(frame))
        }
        return frames
    }

    /**
     * Two different frames both converted to TDRCFrames, now if this is the case one of them
     * may have actually have been created as a FrameUnsupportedBody because TDRC is only
     * supported in ID3v24, but is often created in v23 tags as well together with the valid TYER
     * frame OR it might be that we have two v23 frames that map to TDRC such as TYER,TIME or TDAT
     *
     * @param newFrame
     * @param existingFrame
     */
    override fun processDuplicateFrame(
        existingFrame: AbstractID3v2Frame,
        newFrame: AbstractID3v2Frame
    ) {
        // We dont add this new frame we just add the contents to existing frame
        //
        if (newFrame.frameBody is FrameBodyTDRC) {
            val newBody = newFrame.frameBody as FrameBodyTDRC
            when (existingFrame.frameBody) {
                is FrameBodyTDRC -> {
                    val body = existingFrame.frameBody as FrameBodyTDRC
                    //#304:Check for NullPointer, just ignore this frame

                    if (newBody.originalID == null) {
                        return
                    }
                    // Just add the data to the frame
                    when (newBody.originalID) {
                        ID3v23FrameId.TYER.id -> {
                            body.year = newBody.year
                        }
                        ID3v23FrameId.TDAT.id -> {
                            body.date = newBody.date
                            body.monthOnly = newBody.monthOnly
                        }
                        ID3v23FrameId.TIME.id -> {
                            body.time = newBody.time
                            body.hoursOnly = newBody.hoursOnly
                        }
                    }
                    body.setObjectValue(DataTypes.OBJ_TEXT, body.getFormattedText())
                }

                is FrameBodyUnsupported -> {
                    newFrame.getIdentifier()?.also { id -> frameMap[id] = newFrame }
                }

                else -> {
                    // we just lose this frame, we have already got one with the correct id.
                    log.warn("Found duplicate TDRC frame in invalid situation, discarding:${newFrame.getIdentifier()}")
                }
            }
        } else {
            super.processDuplicateFrame(existingFrame, newFrame)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun write(file: File?, audioStartByte: Long): Long {
        log.debug("Writing tag to file")

        // Write Body Buffer
        val bodyByteBuffer = writeFramesToBuffer().toByteArray()

        // Calculate Tag Size including Padding
        val sizeIncPadding = calculateTagSize(
            bodyByteBuffer.size + TAG_HEADER_LENGTH,
            audioStartByte.toInt()
        )

        // Calculate padding bytes required
        val padding = sizeIncPadding - (bodyByteBuffer.size + TAG_HEADER_LENGTH)

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
     *
     * @param padding is the size of the padding
     * @param size    is the size of the body data
     * @return ByteBuffer
     */
    private fun writeHeaderToBuffer(padding: Int, size: Int): ByteBuffer {
        // This would only be set if every frame in tag has been unsynchronized, I only unsychronize frames
        // that need it, in any case I have been advised not to set it even then.
        isUnsynchronization = false

        // Flags,currently we never calculate the CRC
        // and if we dont calculate them cant keep orig values. Tags are not
        // experimental and we never create extended header to keep things simple.
        isExtended = false
        isExperimental = false
        isFooter = false

        // Create Header Buffer,allocate maximum possible size for the header
        val headerBuffer = ByteBuffer.allocate(TAG_HEADER_LENGTH)
        // TAGID
        headerBuffer.put(TAG_ID)

        // Major Version
        headerBuffer.put(getMajorVersion().toByte())

        // Minor Version
        headerBuffer.put(getRevision().toByte())

        // Flags
        var flagsByte = 0
        if (isUnsynchronization) {
            flagsByte = flagsByte or MASK_V24_UNSYNCHRONIZATION
        }
        if (isExtended) {
            flagsByte = flagsByte or MASK_V24_EXTENDED_HEADER
        }
        if (isExperimental) {
            flagsByte = flagsByte or MASK_V24_EXPERIMENTAL
        }
        if (isFooter) {
            flagsByte = flagsByte or MASK_V24_FOOTER_PRESENT
        }
        headerBuffer.put(flagsByte.toByte())

        // Size As Recorded in Header, don't include the main header length
        // Additional Header Size,(for completeness we never actually write the extended header, or footer)
        var additionalHeaderSize = 0
        if (isExtended) {
            additionalHeaderSize += TAG_EXT_HEADER_LENGTH
            if (isUpdateTag) {
                additionalHeaderSize += TAG_EXT_HEADER_UPDATE_LENGTH
            }
            if (isCrcDataFlag) {
                additionalHeaderSize += TAG_EXT_HEADER_CRC_LENGTH
            }
            if (isTagRestriction) {
                additionalHeaderSize += TAG_EXT_HEADER_RESTRICTION_LENGTH
            }
        }

        // Size As Recorded in Header, don't include the main header length
        headerBuffer.put(
            ID3SyncSafeInteger.valueToBuffer(padding + size + additionalHeaderSize)
        )

        // Write Extended Header
        var extHeaderBuffer: ByteBuffer? = null
        if (isExtended) {
            // Write Extended Header Size
            var extendedSize = TAG_EXT_HEADER_LENGTH
            if (isUpdateTag) {
                extendedSize += TAG_EXT_HEADER_UPDATE_LENGTH
            }
            if (isCrcDataFlag) {
                extendedSize += TAG_EXT_HEADER_CRC_LENGTH
            }
            if (isTagRestriction) {
                extendedSize += TAG_EXT_HEADER_RESTRICTION_LENGTH
            }
            extHeaderBuffer = ByteBuffer.allocate(extendedSize)
            extHeaderBuffer.putInt(extendedSize)
            // Write Number of flags Int
            extHeaderBuffer.put(TAG_EXT_NUMBER_BYTES_DATA_LENGTH.toByte())
            // Write Extended Flags
            var extFlag = 0
            if (isUpdateTag) {
                extFlag = extFlag or MASK_V24_TAG_UPDATE
            }
            if (isCrcDataFlag) {
                extFlag = extFlag or MASK_V24_CRC_DATA_PRESENT
            }
            if (isTagRestriction) {
                extFlag = extFlag or MASK_V24_TAG_RESTRICTIONS
            }
            extHeaderBuffer.put(extFlag.toByte())
            // Write Update Data
            if (isUpdateTag) {
                extHeaderBuffer.put(0)
            }
            // Write CRC Data
            if (isCrcDataFlag) {
                extHeaderBuffer.put(TAG_EXT_HEADER_CRC_DATA_LENGTH.toByte())
                extHeaderBuffer.put(0)
                extHeaderBuffer.putInt(crcData)
            }
            // Write Tag Restriction
            if (isTagRestriction) {
                extHeaderBuffer.put(TAG_EXT_HEADER_RESTRICTION_DATA_LENGTH.toByte())
                extHeaderBuffer.put(0)
            }
        }

        if (extHeaderBuffer != null) {
            extHeaderBuffer.flip()
            headerBuffer.put(extHeaderBuffer)
        }

        headerBuffer.flip()

        return headerBuffer
    }

    /**
     * Delete fields with this (frame) id
     *
     * @param key
     */
    override fun deleteField(key: String) {
        super.doDeleteTagField(FrameAndSubId(null, key, null))
    }

    override fun createField(artwork: Artwork): TagField {
        val frame = createFrame(getFrameAndSubIdFromGenericKey(GenericFieldKey.COVER_ART)?.frameId)
        val body: FrameBodyAPIC = frame.frameBody as FrameBodyAPIC
        return if (!artwork.isLinked) {
            body.setObjectValue(DataTypes.OBJ_PICTURE_DATA, artwork.binaryData)
            body.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, artwork.pictureType)
            body.setObjectValue(DataTypes.OBJ_MIME_TYPE, artwork.mimeType)
            body.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
            frame
        }
        else {
            body.setObjectValue(
                DataTypes.OBJ_PICTURE_DATA,
                artwork.imageUrl?.toByteArray(StandardCharsets.ISO_8859_1)
            )
            body.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, artwork.pictureType)
            body.setObjectValue(DataTypes.OBJ_MIME_TYPE, FrameBodyAPIC.IMAGE_IS_URL)
            body.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
            frame
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
    fun createArtworkField(data: ByteArray, mimeType: String): TagField {
        val frame = createFrame(getFrameAndSubIdFromGenericKey(GenericFieldKey.COVER_ART)?.frameId)
        val body = frame.frameBody as FrameBodyAPIC
        body.setObjectValue(DataTypes.OBJ_PICTURE_DATA, data)
        body.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, PictureTypes.DEFAULT_ID)
        body.setObjectValue(DataTypes.OBJ_MIME_TYPE, mimeType)
        body.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
        return frame
    }

    /**
     * Create a new frame with the specified frameid
     *
     * @param id
     * @return
     */
    override fun createFrame(id: String?): ID3v24Frame {
        return ID3v24Frame(id)
    }

    /**
     * Maps the generic key to the id3 key and return the list of values for this field as strings
     *
     * @param genericKey
     * @return
     */
    override fun getAll(genericKey: GenericFieldKey): List<String> {
        return if (genericKey == GenericFieldKey.GENRE) {
            getFields(genericKey).firstOrNull()?.let { f ->
                ((f as AbstractID3v2Frame).frameBody as FrameBodyTCON)
                    .getValues()
                    .mapNotNull { next -> FrameBodyTCON.convertID3v22GenreToGeneric(next) }
            }?:listOf()
        } else {
            super.getAll(genericKey)
        }
    }

    override fun getFrameAndSubIdFromGenericKey(genericKey: GenericFieldKey?): FrameAndSubId? {
        when (genericKey) {
            null -> {
                throw IllegalArgumentException(
                    ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg()
                )
            }
            else -> {
                return ID3v24FrameId.fromFieldKey(genericKey)?.let { id3v24FieldKey ->
                    FrameAndSubId(
                        genericKey,
                        id3v24FieldKey.id,
                        id3v24FieldKey.fieldKey?.subId
                    )
                }
            }
        }
    }

    /**
     * Display the tag in an XMLFormat
     */
    override fun createStructure() {
        MP3File.tagFormatter?.openHeadingElement(
            TYPE_TAG,
            getIdentifier()
        )

        super.createStructureHeader()

        // Header
        MP3File.tagFormatter?.openHeadingElement(TYPE_HEADER, "")
        MP3File.tagFormatter?.addElement(
            TYPE_UNSYNCHRONISATION,
            this.isUnsynchronization
        )
        MP3File.tagFormatter?.addElement(TYPE_CRCDATA, this.crcData)
        MP3File.tagFormatter?.addElement(
            TYPE_EXPERIMENTAL,
            this.isExperimental
        )
        MP3File.tagFormatter?.addElement(TYPE_EXTENDED, this.isExtended)
        MP3File.tagFormatter?.addElement(
            TYPE_PADDINGSIZE,
            this.paddingSize
        )
        MP3File.tagFormatter?.addElement(TYPE_FOOTER, this.isFooter)
        MP3File.tagFormatter?.addElement(
            TYPE_IMAGEENCODINGRESTRICTION,
            this.paddingSize
        )
        MP3File.tagFormatter?.addElement(
            TYPE_IMAGESIZERESTRICTION,
            this.imageSizeRestriction
        )
        MP3File.tagFormatter?.addElement(
            TYPE_TAGRESTRICTION,
            this.isTagRestriction
        )
        MP3File.tagFormatter?.addElement(
            TYPE_TAGSIZERESTRICTION,
            this.tagSizeRestriction
        )
        MP3File.tagFormatter?.addElement(
            TYPE_TEXTFIELDSIZERESTRICTION,
            this.textFieldSizeRestriction
        )
        MP3File.tagFormatter?.addElement(
            TYPE_TEXTENCODINGRESTRICTION,
            this.textEncodingRestriction
        )
        MP3File.tagFormatter?.addElement(TYPE_UPDATETAG, this.isUpdateTag)
        MP3File.tagFormatter?.closeHeadingElement(TYPE_HEADER)

        // Body
        super.createStructureBody()

        MP3File.tagFormatter?.closeHeadingElement(TYPE_TAG)
    }

    override fun isMultipleAllowed(identifier: String?): Boolean = ID3v24FrameId.isMultipleAllowed(identifier)
}