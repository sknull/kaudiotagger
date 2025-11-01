package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.field.TagField
import de.visualdigits.kaudiotagger.model.common.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.common.tag.Tag
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.SupportedTag
import de.visualdigits.kaudiotagger.model.id3.types.GenreTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v1FieldKey
import de.visualdigits.kaudiotagger.model.images.Artwork
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.ID3Tags
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

open class ID3v1Tag: AbstractID3v1Tag, Tag {
    
    companion object {
        // For writing output
        const val TYPE_COMMENT: String = "comment"
        const val FIELD_COMMENT_LENGTH: Int = 30
        const val FIELD_COMMENT_POS: Int = 97
        const val BYTE_TO_UNSIGNED: Int = 0xff
        const val GENRE_UNDEFINED: Int = 0xff
        const val RELEASE: Int = 1
        const val MAJOR_VERSION: Int = 0
        const val REVISION: Int = 0

        var tagFieldToID3v1Field: Map<GenericFieldKey, ID3v1FieldKey> = mapOf(
            GenericFieldKey.ARTIST to ID3v1FieldKey.ARTIST,
            GenericFieldKey.ALBUM to ID3v1FieldKey.ALBUM,
            GenericFieldKey.TITLE to ID3v1FieldKey.TITLE,
            GenericFieldKey.TRACK to ID3v1FieldKey.TRACK,
            GenericFieldKey.YEAR to ID3v1FieldKey.YEAR,
            GenericFieldKey.GENRE to ID3v1FieldKey.GENRE,
            GenericFieldKey.COMMENT to ID3v1FieldKey.COMMENT
        )


        fun read(file: RandomAccessFile): ID3v1Tag? {
            val fc = file.getChannel()
            fc.position(file.length() - TAG_LENGTH)
            val byteBuffer = ByteBuffer.allocate(TAG_LENGTH)
            fc.read(byteBuffer)
            byteBuffer.flip()

            val tag = ID3v1Tag()

            return if (tag.read(byteBuffer)) tag else null
        }
    }

    private var album: String = ""
    private var artist: String = ""
    private var comment: String = ""
    private var title: String = ""
    private var year: String = ""
    private var genre: Int = -1

    var dataBuffer: ByteArray = byteArrayOf()

    /**
     * Creates a new ID3v1 datatype.
     */
    constructor()

    constructor(copyObject: ID3v1Tag): super(copyObject) {
        this.album = copyObject.album
        this.artist = copyObject.artist
        this.comment = copyObject.comment
        this.title = copyObject.title
        this.year = copyObject.year
        this.genre = copyObject.genre
    }

    constructor(mp3tag: AbstractTag) {
        val convertedTag: ID3v11Tag?
        if (mp3tag is ID3v1Tag) {
            throw UnsupportedOperationException(
                "Copy Constructor not called. Please type cast the argument"
            )
        }
        if (mp3tag is ID3v11Tag) {
            convertedTag = mp3tag
        } else {
            convertedTag = ID3v11Tag(mp3tag)
        }
        this.album = convertedTag.getAlbum()
        this.artist = convertedTag.getArtist()
        this.comment = convertedTag.getComment()
        this.title = convertedTag.getTitle()
        this.year = convertedTag.getYear()
        this.genre = convertedTag.getGenre()
    }

    override fun supportedTag(): SupportedTag = SupportedTag.ID3v1Tag

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
        dataBuffer = ByteArray(TAG_LENGTH)
        byteBuffer.position(0)
        byteBuffer[dataBuffer, 0, TAG_LENGTH]

        title = String(
            dataBuffer,
            FIELD_TITLE_POS,
            FIELD_TITLE_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim()
        var m = endofStringPattern.matcher(title)
        if (m.find()) {
            title = title.take(m.start())
        }

        artist = String(
            dataBuffer,
            FIELD_ARTIST_POS,
            FIELD_ARTIST_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim()
        m = endofStringPattern.matcher(artist)
        if (m.find()) {
            artist = artist.take(m.start())
        }

        year = String(
            dataBuffer,
            FIELD_YEAR_POS,
            FIELD_YEAR_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim()
        m = endofStringPattern.matcher(year)
        if (m.find()) {
            year = year.take(m.start())
        }

        comment = String(
            dataBuffer,
            ID3v11Tag.FIELD_COMMENT_POS,
            ID3v11Tag.FIELD_COMMENT_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim()
        m = endofStringPattern.matcher(comment)
        if (m.find()) {
            comment = comment.take(m.start())
        }

        genre = dataBuffer[FIELD_GENRE_POS].toInt()

        return true
    }

    /**
     * Does a tag of this version exist within the byteBuffer
     *
     * @return whether tag exists within the byteBuffer
     */
    override fun seek(byteBuffer: ByteBuffer): Boolean {
        val buffer = ByteArray(FIELD_TAGID_LENGTH)
        // read the TAG value
        byteBuffer[buffer, 0, FIELD_TAGID_LENGTH]
        return (buffer.contentEquals(TAG_ID))
    }

    /**
     * Write this tag to the file, replacing any tag previously existing
     *
     * @param file
     */
    override fun write(file: RandomAccessFile) {
        log.debug("Saving ID3v1 tag to file")
        val buffer = ByteArray(TAG_LENGTH)
        var i: Int
        var str: String?
        delete(file)
        file.seek(file.length())
        // Copy the TAGID into new buffer
        System.arraycopy(
            TAG_ID,
            FIELD_TAGID_POS,
            buffer,
            FIELD_TAGID_POS,
            TAG_ID.size
        )
        var offset = FIELD_TITLE_POS
        if (TagOptionSingleton.id3v1SaveTitle) {
            str = ID3Tags.truncate(title, FIELD_TITLE_LENGTH)
            i = 0
            while (i < str.length) {
                buffer[i + offset] = str[i].code.toByte()
                i++
            }
        }
        offset = FIELD_ARTIST_POS
        if (TagOptionSingleton.id3v1SaveArtist) {
            str = ID3Tags.truncate(artist, FIELD_ARTIST_LENGTH)
            i = 0
            while (i < str.length) {
                buffer[i + offset] = str[i].code.toByte()
                i++
            }
        }
        offset = FIELD_ALBUM_POS
        if (TagOptionSingleton.id3v1SaveAlbum) {
            str = ID3Tags.truncate(album, FIELD_ALBUM_LENGTH)
            i = 0
            while (i < str.length) {
                buffer[i + offset] = str[i].code.toByte()
                i++
            }
        }
        offset = FIELD_YEAR_POS
        if (TagOptionSingleton.id3v1SaveYear) {
            str = ID3Tags.truncate(year, FIELD_YEAR_LENGTH)
            i = 0
            while (i < str.length) {
                buffer[i + offset] = str[i].code.toByte()
                i++
            }
        }
        offset = FIELD_COMMENT_POS
        if (TagOptionSingleton.id3v1SaveComment) {
            str = ID3Tags.truncate(comment, FIELD_COMMENT_LENGTH)
            i = 0
            while (i < str.length) {
                buffer[i + offset] = str[i].code.toByte()
                i++
            }
        }
        offset = FIELD_GENRE_POS
        if (TagOptionSingleton.id3v1SaveGenre) {
            buffer[offset] = genre.toByte()
        }
        file.write(buffer)
        log.debug("Saved ID3v1 tag to file")
    }

    /**
     * Get Genre
     *
     * @return genre or empty string if not valid
     */
    fun getFirstGenre(): String {
        val genreId = (genre and BYTE_TO_UNSIGNED).toInt()
        val genreValue = GenreTypes.fromId(genreId)
        return if (genreValue == null) {
            ""
        } else {
            genreValue.friendlyName
        }
    }

    override fun createField(artwork: Artwork): TagField {
        throw java.lang.UnsupportedOperationException(
            ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg()
        )
    }

    /**
     * Create Tag Field using generic key
     */
    override fun createField(genericKey: GenericFieldKey, vararg values: String): TagField? {
        val value = values[0]
        val idv1FieldKey = tagFieldToID3v1Field[genericKey]
        return ID3v1TagField(idv1FieldKey?.name?:error("No id"), value)
    }

    override fun addField(tagField: TagField?) {
        // to be implemented
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
        val tagfield = createField(genericKey, *values)
        setField(tagfield)
    }

    override fun setField(field: TagField?) {
        val genericKey = GenericFieldKey.valueOf(field?.getIdentifier()?:error("No id"))
        when (genericKey) {
            GenericFieldKey.ARTIST -> setArtist(field.toString())
            GenericFieldKey.ALBUM -> setAlbum(field.toString())
            GenericFieldKey.TITLE -> setTitle(field.toString())
            GenericFieldKey.GENRE -> setGenreVal(field.toString())
            GenericFieldKey.YEAR -> setYear(field.toString())
            GenericFieldKey.COMMENT -> setComment(field.toString())
            else -> { log.warn("Unknown key '$genericKey'") }
        }
    }

    fun getAlbum(): String = album

    /**
     * @return album within list or empty if does not exist
     */
    open fun getAlbumTag(): List<TagField> {
        return if (album.isNotEmpty()) {
            val field = ID3v1TagField(
                ID3v1FieldKey.ALBUM.name,
                album
            )
            mutableListOf(field)
        } else {
            mutableListOf()
        }
    }

    /**
     * Set Album
     *
     * @param album
     */
    open fun setAlbum(album: String) {
        this.album = ID3Tags.truncate(album, FIELD_ALBUM_LENGTH)
    }

    fun getArtist(): String = artist

    /**
     * @return Artist within list or empty if does not exist
     */
    open fun getArtistTag(): List<TagField> {
        return if (artist.isNotEmpty()) {
            val field = ID3v1TagField(
                ID3v1FieldKey.ARTIST.name,
                artist
            )
            mutableListOf(field)
        } else {
            mutableListOf()
        }
    }

    /**
     * Set Artist
     *
     * @param artist
     */
    open fun setArtist(artist: String) {
        this.artist = ID3Tags.truncate(artist, FIELD_ARTIST_LENGTH)
    }

    /**
     * @return comment within list or empty if does not exist
     */
    open fun getCommentTag(): List<TagField> {
        return if (comment.isNotEmpty()) {
            val field = ID3v1TagField(
                ID3v1FieldKey.COMMENT.name,
                comment
            )
            mutableListOf(field)
        } else {
            mutableListOf()
        }
    }

    fun getComment(): String = comment

    /**
     * Set Comment
     *
     * @param comment
     */
    open fun setComment(comment: String) {
        this.comment = ID3Tags.truncate(comment, FIELD_COMMENT_LENGTH)
    }

    fun getGenre(): Int = genre

    /**
     * Get Genre field
     *
     *
     * Only a single genre is available in ID3v1
     *
     * @return
     */
    open fun getGenreTag(): List<TagField> {
        return getFirst(GenericFieldKey.GENRE)
            ?.let { f -> listOf(ID3v1TagField(ID3v1FieldKey.GENRE.name, f)) }
            ?:listOf()
    }

    fun setGenre(genre: Int) {
        this.genre = genre
    }

    /**
     * Sets the genreID,
     *
     *
     * ID3v1 only supports genres defined in a predefined list
     * so if unable to find value in list set 255, which seems to be the value
     * winamp uses for undefined.
     *
     * @param genreVal
     */
    open fun setGenreVal(genreVal: String) {
        val genreID = GenreTypes.fromName(genreVal)?.id
        if (genreID != null) {
            this.genre = genreID
        } else {
            this.genre = GENRE_UNDEFINED
        }
    }

    fun getTitle(): String = title

    /**
     * Get title field
     *
     *
     * Only a single title is available in ID3v1
     *
     * @return
     */
    open fun getTitleTag(): List<TagField> {
        return getFirst(GenericFieldKey.TITLE)
            ?.let { f -> listOf(ID3v1TagField(ID3v1FieldKey.TITLE.name, f)) }
            ?:listOf()
    }

    /**
     * Set Title
     *
     * @param title
     */
    open fun setTitle(title: String) {
        this.title = ID3Tags.truncate(title, FIELD_TITLE_LENGTH)
    }

    fun getYear(): String = year

    /**
     * Get year field
     *
     *
     * Only a single year is available in ID3v1
     *
     * @return
     */
    open fun getYearTag(): List<TagField> {
        return getFirst(GenericFieldKey.YEAR)
            ?.let { f -> listOf(ID3v1TagField(ID3v1FieldKey.YEAR.name, f)) }
            ?:listOf()
    }

    /**
     * Set year
     *
     * @param year
     */
    fun setYear(year: String?) {
        this.year = ID3Tags.truncate(year, FIELD_YEAR_LENGTH)
    }

    /**
     * Maps the generic key to the ogg key and return the list of values for this field as strings
     *
     * @param genericKey
     * @return
     */
    override fun getAll(genericKey: GenericFieldKey): List<String> {
        return getFirst(genericKey)?.let { f -> listOf(f) }?:listOf()
    }

    override fun getFirst(id: String): String? {
        return getFirst(GenericFieldKey.valueOf(id))
    }

    /**
     * Retrieve the first value that exists for this generic key
     *
     * @param genericKey
     * @return
     */
    override fun getFirst(genericKey: GenericFieldKey?): String? {
        return when (genericKey) {
            GenericFieldKey.ARTIST -> artist
            GenericFieldKey.ALBUM -> album
            GenericFieldKey.TITLE -> title
            GenericFieldKey.GENRE -> getFirstGenre()
            GenericFieldKey.YEAR -> year
            GenericFieldKey.COMMENT -> comment
            else -> ""
        }
    }

    override fun getFirstField(genericKey: GenericFieldKey): TagField? {
        return getFields(genericKey)[0]
    }

    override fun getFirstField(id: String?): TagField? {
        return id?.let { i -> getFirstField(GenericFieldKey.valueOf(i)) }
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

    override fun createCompilationField(value: Boolean): TagField? {
        throw java.lang.UnsupportedOperationException(
            ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg()
        )
    }

    override fun deleteField(key: String) {
        deleteField(GenericFieldKey.valueOf(key.uppercase()))
    }

    /**
     * Delete any instance of tag fields with this key
     *
     * @param genericKey
     */
    override fun deleteField(genericKey: GenericFieldKey) {
        when (genericKey) {
            GenericFieldKey.ARTIST -> setArtist("")
            GenericFieldKey.ALBUM -> setAlbum("")
            GenericFieldKey.TITLE -> setTitle("")
            GenericFieldKey.GENRE -> setGenreVal("")
            GenericFieldKey.YEAR -> setYear("")
            GenericFieldKey.COMMENT -> setComment("")
            else -> { log.warn("Unknown key '$genericKey'") }
        }
    }

    override fun hasCommonFields(): Boolean {
        return true
    }

    override fun hasField(id: String): Boolean {
        try {
            val key = GenericFieldKey.valueOf(id.uppercase())
            return hasField(key)
        } catch (_: IllegalArgumentException) {
            return false
        }
    }

    override fun hasField(genericKey: GenericFieldKey): Boolean {
        return getFirst(genericKey)?.isNotEmpty() == true
    }

    override fun isEmpty(): Boolean {
        return !(getFirst(GenericFieldKey.TITLE)?.isNotEmpty() == true
                || artist.isNotEmpty()
                || album.isNotEmpty()
                || getFirst(GenericFieldKey.GENRE)?.isNotEmpty() == true
                || getFirst(GenericFieldKey.YEAR)?.isNotEmpty() == true
                || comment.isNotEmpty()
                )
    }

    override fun getArtworkList(): List<Artwork> {
        return listOf()
    }

    /**
     * Returns a [list][List] of [TagField] objects whose &quot;[id][TagField.getId]&quot;
     * is the specified one.<br></br>
     *
     * @param genericKey The generic field key
     * @return A list of [TagField] objects with the given &quot;id&quot;.
     */
    override fun getFields(genericKey: GenericFieldKey?): List<TagField> {
        return when (genericKey) {
            GenericFieldKey.ARTIST -> getArtistTag()
            GenericFieldKey.ALBUM -> getAlbumTag()
            GenericFieldKey.TITLE -> getTitleTag()
            GenericFieldKey.GENRE -> getGenreTag()
            GenericFieldKey.YEAR -> getYearTag()
            GenericFieldKey.COMMENT -> getCommentTag()
            else -> listOf()
        }
    }

    /**
     * Create structured representation of this item.
     */
    open fun createStructure() {
        MP3File.tagFormatter?.openHeadingElement(
            TYPE_TAG,
            getIdentifier()?:""
        )
        // Header
        MP3File.tagFormatter?.addElement(TYPE_TITLE, this.title)
        MP3File.tagFormatter?.addElement(TYPE_ARTIST, this.artist)
        MP3File.tagFormatter?.addElement(TYPE_ALBUM, this.album)
        MP3File.tagFormatter?.addElement(TYPE_YEAR, this.year)
        MP3File.tagFormatter?.addElement(TYPE_COMMENT, this.comment)
        MP3File.tagFormatter?.addElement(TYPE_GENRE, this.genre.toInt())
        MP3File.tagFormatter?.closeHeadingElement(TYPE_TAG)
    }
}