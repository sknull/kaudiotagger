package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.common.field.TagField
import de.visualdigits.kaudiotagger.model.common.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.SupportedTag
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyCOMM
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTALB
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTCON
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTDRC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIT2
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTPE1
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTRCK
import de.visualdigits.kaudiotagger.model.id3.types.GenreTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v1FieldKey
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.model.images.Artwork
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.ID3Tags
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class ID3v11Tag: ID3v1Tag {
    
    companion object {
        // For writing output
        const val TYPE_TRACK: String = "track"
        const val TRACK_MAX_VALUE: Int = 255
        const val TRACK_MIN_VALUE: Int = 1
        const val FIELD_COMMENT_LENGTH: Int = 28
        const val FIELD_COMMENT_POS: Int = 97
        const val FIELD_TRACK_INDICATOR_LENGTH: Int = 1
        const val FIELD_TRACK_INDICATOR_POS: Int = 125
        const val FIELD_TRACK_LENGTH: Int = 1
        const val FIELD_TRACK_POS: Int = 126
        const val RELEASE: Int = 1
        const val MAJOR_VERSION: Int = 1
        const val REVISION: Int = 0


        fun read(file: RandomAccessFile): ID3v11Tag? {
            val fc = file.getChannel()
            fc.position(file.length() - TAG_LENGTH)
            val byteBuffer = ByteBuffer.allocate(TAG_LENGTH)
            fc.read(byteBuffer)
            byteBuffer.flip()

            val tag = ID3v11Tag()

            return if (tag.read(byteBuffer)) tag else null
        }
    }

    /**
     * Track is held as a single byte in v1.1
     */
    var track: Int? = null

    /**
     * Creates a new ID3v11 datatype.
     */
    constructor()

    constructor(copyObject: ID3v11Tag): super(copyObject) {
        this.track = copyObject.track
    }

    /**
     * Creates a new ID3v11 datatype from a non v11 tag
     *
     * @param mp3tag
     */
    constructor(mp3tag: AbstractTag?) {
        if (mp3tag is ID3v1Tag) {
            if (mp3tag is ID3v11Tag) {
                throw UnsupportedOperationException(
                    "Copy Constructor not called. Please type cast the argument"
                )
            }
            // id3v1_1 objects are also id3v1 objects
            setTitle(mp3tag.getTitle())
            setArtist(mp3tag.getArtist())
            setAlbum(mp3tag.getAlbum())
            setComment(mp3tag.getComment())
            setYear(mp3tag.getYear())
            setGenre(mp3tag.getGenre().id)
        } else {
            // first change the tag to ID3v2_4 tag if not one already
            val id3tag: ID3v24Tag = mp3tag as? ID3v24Tag ?: ID3v24Tag(mp3tag)
            var frame: ID3v24Frame
            var text: String?
            if (id3tag.hasField(ID3v24FrameId.TITLE.id)) {
                frame = id3tag.getFrame(ID3v24FrameId.TITLE.id) as ID3v24Frame
                text = (frame.frameBody as FrameBodyTIT2).getText()
                setTitle(ID3Tags.truncate(text, FIELD_TITLE_LENGTH))
            }
            if (id3tag.hasField(ID3v24FrameId.ARTIST.id)) {
                frame = id3tag.getFrame(ID3v24FrameId.ARTIST.id) as ID3v24Frame
                text = (frame.frameBody as FrameBodyTPE1).getText()
                setArtist(ID3Tags.truncate(text, FIELD_ARTIST_LENGTH))
            }
            if (id3tag.hasField(ID3v24FrameId.ALBUM.id)) {
                frame = id3tag.getFrame(ID3v24FrameId.ALBUM.id) as ID3v24Frame
                text = (frame.frameBody as FrameBodyTALB).getText()
                setAlbum(ID3Tags.truncate(text, FIELD_ALBUM_LENGTH))
            }
            if (id3tag.hasField(ID3v24FrameId.YEAR.id)) {
                frame = id3tag.getFrame(ID3v24FrameId.YEAR.id) as ID3v24Frame
                text = (frame.frameBody as FrameBodyTDRC).getText()
                setYear(ID3Tags.truncate(text, FIELD_YEAR_LENGTH))
            }

            if (id3tag.hasField(ID3v24FrameId.COMMENT.id)) {
                text = ""
                id3tag.getFrameOfType(
                    ID3v24FrameId.COMMENT.id
                ).forEach { frame ->
                    text += (((frame as ID3v24Frame).frameBody as FrameBodyCOMM).getText() + " ")
                }
                setComment(ID3Tags.truncate(text, FIELD_COMMENT_LENGTH))
            }
            if (id3tag.hasField(ID3v24FrameId.GENRE.id)) {
                frame = id3tag.getFrame(ID3v24FrameId.GENRE.id) as ID3v24Frame
                text = (frame.frameBody as FrameBodyTCON).getText()
                setGenre(ID3Tags.findNumber(text?:"0")?.toInt()?:GenreTypes.UNKNOWN.id)
            }
            if (id3tag.hasField(ID3v24FrameId.TRACK.id)) {
                frame = id3tag.getFrame(ID3v24FrameId.TRACK.id) as ID3v24Frame
                this.track = (frame.frameBody as FrameBodyTRCK).getTrackNo()
            }
        }
    }

    override fun supportedTag(): SupportedTag = SupportedTag.ID3v11Tag

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
     * Read in a tag from the ByteBuffer
     *
     * @param byteBuffer from where to read in a tag
     */
    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null || !seek(byteBuffer)) {
            return false
        }
        log.debug("Reading v1.1 tag")

        // Do single file read of data to cut down on file reads
        val dataBuffer = ByteArray(TAG_LENGTH)
        byteBuffer.position(0)
        byteBuffer[dataBuffer, 0, TAG_LENGTH]
        setTitle(String(
            dataBuffer,
            FIELD_TITLE_POS,
            FIELD_TITLE_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim { it <= ' ' })
        var m = endofStringPattern.matcher(getTitle()?:"")
        if (m.find()) {
            setTitle(getTitle()?.substring(0, m.start()))
        }
        setArtist(String(
            dataBuffer,
            FIELD_ARTIST_POS,
            FIELD_ARTIST_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim { it <= ' ' })
        m = endofStringPattern.matcher(getArtist()?:"")
        if (m.find()) {
            setArtist(getArtist()?.substring(0, m.start()))
        }
        setAlbum(String(
            dataBuffer,
            FIELD_ALBUM_POS,
            FIELD_ALBUM_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim { it <= ' ' })
        m = endofStringPattern.matcher(getAlbum()?:"")
        if (m.find()) {
            setAlbum(getAlbum()?.substring(0, m.start()))
        }
        setYear(String(
            dataBuffer,
            FIELD_YEAR_POS,
            FIELD_YEAR_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim { it <= ' ' })
        m = endofStringPattern.matcher(getYear()?:"")
        if (m.find()) {
            setYear(getYear()?.substring(0, m.start()))
        }
        setComment(String(
            dataBuffer,
            FIELD_COMMENT_POS,
            FIELD_COMMENT_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim { it <= ' ' })
        m = endofStringPattern.matcher(getComment()?:"")
        if (m.find()) {
            setComment(getComment()?.substring(0, m.start()))
        }
        track = dataBuffer[FIELD_TRACK_POS].toInt()
        setGenre(dataBuffer[FIELD_GENRE_POS].toInt())
        
        return true
    }

    /**
     * Find identifier within byteBuffer to indicate that a v11 tag exists within the buffer
     *
     * @param byteBuffer
     * @return true if find header for v11 tag is within buffer
     */
    override fun seek(byteBuffer: ByteBuffer): Boolean {
        if(!super.seek(byteBuffer)) {
            return false
        }
        // Check for the empty byte before the TRACK
        byteBuffer.position(FIELD_TRACK_INDICATOR_POS)
        if (byteBuffer.get() != END_OF_FIELD) {
            return false
        }
        // Now check for TRACK if the next byte is also null byte then not v1.1
        // tag, however this means cannot have v1_1 tag with track setField to zero/undefined
        // because on next read will be v1 tag.
        return byteBuffer.get() != END_OF_FIELD
    }

    /**
     * Write this representation of tag to the file indicated
     *
     * @param file that this tag should be written to
     */
    override fun write(file: RandomAccessFile) {
        log.debug("Saving ID3v11 tag to file")
        var buffer = ByteArray(TAG_LENGTH)
        var i: Int
        var str: String?
        delete(file)
        file.seek(file.length())
        System.arraycopy(
            TAG_ID,
            FIELD_TAGID_POS,
            buffer,
            FIELD_TAGID_POS,
            TAG_ID.size
        )
        var offset = FIELD_TITLE_POS
        if (TagOptionSingleton.id3v1SaveTitle) {
            str = ID3Tags.truncate(getTitle(), FIELD_TITLE_LENGTH)
            i = 0
            while (i < (str?.length?:0)) {
                buffer[i + offset] = str?.get(i)?.code?.toByte()?:0.toByte()
                i++
            }
        }
        offset = FIELD_ARTIST_POS
        if (TagOptionSingleton.id3v1SaveArtist) {
            str = ID3Tags.truncate(getArtist(), FIELD_ARTIST_LENGTH)
            i = 0
            while (i < (str?.length?:0)) {
                buffer[i + offset] = str?.get(i)?.code?.toByte()?:0.toByte()
                i++
            }
        }
        offset = FIELD_ALBUM_POS
        if (TagOptionSingleton.id3v1SaveAlbum) {
            str = ID3Tags.truncate(getAlbum(), FIELD_ALBUM_LENGTH)
            i = 0
            while (i < (str?.length?:0)) {
                buffer[i + offset] = str?.get(i)?.code?.toByte()?:0.toByte()
                i++
            }
        }
        offset = FIELD_YEAR_POS
        if (TagOptionSingleton.id3v1SaveYear) {
            str = ID3Tags.truncate(getYear(), FIELD_YEAR_LENGTH)
            i = 0
            while (i < (str?.length?:0)) {
                buffer[i + offset] = str?.get(i)?.code?.toByte()?:0.toByte()
                i++
            }
        }
        offset = FIELD_COMMENT_POS
        if (TagOptionSingleton.id3v1SaveComment) {
            str = ID3Tags.truncate(getComment(), FIELD_COMMENT_LENGTH)
            i = 0
            while (i < (str?.length?:0)) {
                buffer[i + offset] = str?.get(i)?.code?.toByte()?:0.toByte()
                i++
            }
        }
        offset = FIELD_TRACK_POS
        buffer[offset] = track?.toByte()?:0.toByte() // skip one byte extra blank for 1.1 definition
        offset = FIELD_GENRE_POS
        if (TagOptionSingleton.id3v1SaveGenre) {
            buffer[offset] = getGenre().id.toByte()
        }
        file.write(buffer)

        log.debug("Saved ID3v11 tag to file")
    }

    override fun addField(artwork: Artwork) {
        throw java.lang.UnsupportedOperationException(
            ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg()
        )
    }

    override fun addField(genericKey: GenericFieldKey, vararg values: String) {
        setField(genericKey, *values)
    }

    override fun setField(genericKey: GenericFieldKey, vararg values: String) {
        when (genericKey) {
            GenericFieldKey.TRACK -> setTrackValue(values.firstOrNull())
            else -> super.setField(createField(genericKey, *values))
        }
    }

    override fun isEmpty(): Boolean {
        return super.isEmpty() && track == null
    }

    override fun getFields(): List<ID3v1TagField> {
        return (super.getFields() + getTrackTag()).sortedBy { t -> t.getIdentifier() }
    }

    /**
     * Delete any instance of tag fields with this key
     *
     * @param genericKey
     */
    override fun deleteField(genericKey: GenericFieldKey) {
        when (genericKey) {
            GenericFieldKey.TRACK -> setTrackValue(null)
            else -> super.deleteField(genericKey)
        }
    }

    /**
     * @return Artist within list or empty if does not exist
     */
    fun getTrackTag(): List<ID3v1TagField> {
        return if (track != null) {
            listOf(
                ID3v1TagField(
                    ID3v1FieldKey.TRACK.name,
                    track.toString()
                )
            )
        } else {
            listOf()
        }
    }

    override fun getFields(genericKey: GenericFieldKey?): List<TagField> {
        return when (genericKey) {
            GenericFieldKey.TRACK -> getTrackTag()
            else -> super.getFields(genericKey)
        }
    }

    override fun setField(field: TagField?) {
        val genericKey = GenericFieldKey.valueOf(field?.getIdentifier()?:error("No id"))
        when (genericKey) {
            GenericFieldKey.TRACK -> setTrackValue(getValue(field))
            else -> super.setField(field)
        }
    }

    /**
     * Retrieve the first value that exists for this generic key
     *
     * @param genericKey
     * @return
     */
    override fun getFirst(genericKey: GenericFieldKey?): String? {
        return when (genericKey) {
            GenericFieldKey.TRACK -> getValue(GenericFieldKey.TRACK, 0)
            else -> super.getFirst(genericKey)
        }
    }

    fun setTrackValue(trackValue: String?) {
        this.track = trackValue?.toIntOrNull()
    }

    /**
     * Return the track number as a String.
     *
     * @return track
     */
    fun getFirstTrack(): String {
        return ((track?:0) and BYTE_TO_UNSIGNED).toString()
    }

    /**
     * Delete all instance of artwork Field
     *
     */
    override fun deleteArtworkField() {
        throw java.lang.UnsupportedOperationException(
            ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg()
        )
    }

    override fun createCompilationField(value: Boolean): TagField {
        throw java.lang.UnsupportedOperationException(
            ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg()
        )
    }
}