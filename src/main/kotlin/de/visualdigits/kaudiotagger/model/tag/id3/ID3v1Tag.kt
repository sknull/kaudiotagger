package de.visualdigits.kaudiotagger.model.tag.id3

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.datatype.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.datatype.types.GenreTypes
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v1FieldKey
import de.visualdigits.kaudiotagger.model.exceptions.KeyNotFoundException
import de.visualdigits.kaudiotagger.model.field.TagField
import de.visualdigits.kaudiotagger.model.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.tag.Tag
import de.visualdigits.kaudiotagger.model.tag.images.Artwork
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.ID3Tags
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets

open class ID3v1Tag: AbstractID3v1Tag, Tag {
    
    companion object {
        //For writing output
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

    /**
     * Creates a new ID3v1 datatype.
     *
     * @param file
     * @throws TagNotFoundException
     * @throws IOException
     */
    constructor(file: RandomAccessFile) {
        val fc: FileChannel = file.getChannel()
        fc.position(file.length() - TAG_LENGTH)
        val byteBuffer = ByteBuffer.allocate(TAG_LENGTH)
        fc.read(byteBuffer)
        byteBuffer.flip()
        read(byteBuffer)
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
     * Read in a tag from the ByteBuffer
     *
     * @param byteBuffer from where to read in a tag
     */
    override fun read(byteBuffer: ByteBuffer?) {
        if (byteBuffer == null) {
            return
        }
        if (!seek(byteBuffer)) {
            error("ID3v1 tag not found");
        }
        log.debug("Reading v1.1 tag");

        //Do single file read of data to cut down on file reads
        dataBuffer = ByteArray(TAG_LENGTH)
        byteBuffer.position(0);
        byteBuffer.get(dataBuffer, 0, TAG_LENGTH);

        title = String(
            dataBuffer,
            FIELD_TITLE_POS,
            FIELD_TITLE_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim();
        var m = endofStringPattern.matcher(title);
        if (m.find()) {
            title = title.take(m.start());
        }

        artist = String(
            dataBuffer,
            FIELD_ARTIST_POS,
            FIELD_ARTIST_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim();
        m = endofStringPattern.matcher(artist);
        if (m.find()) {
            artist = artist.take(m.start());
        }

        var album = String(
            dataBuffer,
            FIELD_ALBUM_POS,
            FIELD_ALBUM_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim();
        m = endofStringPattern.matcher(album);
        if (m.find()) {
            album = album.take(m.start());
        }

        year = String(
            dataBuffer,
            FIELD_YEAR_POS,
            FIELD_YEAR_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim();
        m = endofStringPattern.matcher(year);
        if (m.find()) {
            year = year.take(m.start());
        }

        comment = String(
            dataBuffer,
            ID3v11Tag.Companion.FIELD_COMMENT_POS,
            ID3v11Tag.Companion.FIELD_COMMENT_LENGTH,
            StandardCharsets.ISO_8859_1
        ).trim();
        m = endofStringPattern.matcher(comment);
        if (m.find()) {
            comment = comment.take(m.start());
        }

        genre = dataBuffer[FIELD_GENRE_POS].toInt()
    }

    /**
     * Does a tag of this version exist within the byteBuffer
     *
     * @return whether tag exists within the byteBuffer
     */
    override fun seek(byteBuffer: ByteBuffer): Boolean {
        val buffer = ByteArray(FIELD_TAGID_LENGTH);
        // read the TAG value
        byteBuffer.get(buffer, 0, FIELD_TAGID_LENGTH);
        return (buffer.contentEquals(TAG_ID));
    }

    /**
     * Write this tag to the file, replacing any tag previously existing
     *
     * @param file
     * @throws IOException
     */
    override fun write(file: RandomAccessFile) {
        log.debug("Saving ID3v1 tag to file")
        val buffer = ByteArray(TAG_LENGTH)
        var i: Int
        var str: String?
        delete(file)
        file.seek(file.length())
        //Copy the TAGID into new buffer
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
        if (genreValue == null) {
            return ""
        } else {
            return genreValue.friendlyName
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
    override fun createField(genericKey: GenericFieldKey, vararg values: String): TagField {
        val value = values[0]
        val idv1FieldKey = tagFieldToID3v1Field.get(genericKey)
        return ID3v1TagField(idv1FieldKey?.name?:error("No id"), value)
    }

    fun setField(artwork: Artwork) {
        throw java.lang.UnsupportedOperationException(
            ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg()
        )
    }

    override fun addField(field: TagField) {
        //TODO
    }

    override fun addField(artwork: Artwork) {
        throw java.lang.UnsupportedOperationException(
            ErrorMessage.GENERIC_NOT_SUPPORTED.getMsg()
        )
    }

    override fun addField(genericKey: GenericFieldKey, vararg value: String) {
        setField(genericKey, *value)
    }

    open fun setField(genericKey: GenericFieldKey, vararg value: String) {
        val tagfield = createField(genericKey, *value)
        setField(tagfield)
    }

    open fun setField(field: TagField) {
        val genericKey: GenericFieldKey = GenericFieldKey.valueOf(field.getIdentifier()?:error("No id"))
        when (genericKey) {
            GenericFieldKey.ARTIST -> setArtist(field.toString())
            GenericFieldKey.ALBUM -> setAlbum(field.toString())
            GenericFieldKey.TITLE -> setTitle(field.toString())
            GenericFieldKey.GENRE -> setGenreVal(field.toString())
            GenericFieldKey.YEAR -> setYear(field.toString())
            GenericFieldKey.COMMENT -> setComment(field.toString())
            else -> {}
        }
    }

    fun getAlbum(): String = album

    /**
     * @return album within list or empty if does not exist
     */
    open fun getAlbumTag(): List<TagField> {
        if (album.isNotEmpty()) {
            val field = ID3v1TagField(
                ID3v1FieldKey.ALBUM.name,
                album
            )
            return mutableListOf(field)
        } else {
            return mutableListOf()
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
        if (artist.isNotEmpty()) {
            val field = ID3v1TagField(
                ID3v1FieldKey.ARTIST.name,
                artist
            )
            return mutableListOf(field)
        } else {
            return mutableListOf()
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
        if (comment.isNotEmpty()) {
            val field = ID3v1TagField(
                ID3v1FieldKey.COMMENT.name,
                comment
            )
            return mutableListOf(field)
        } else {
            return mutableListOf()
        }
    }

    fun getComment(): String = comment

    /**
     * Set Comment
     *
     * @param comment
     * @throws IllegalArgumentException if comment null
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
        if (getFirst(GenericFieldKey.GENRE).isNotEmpty()) {
            val field = ID3v1TagField(
                ID3v1FieldKey.GENRE.name,
                getFirst(GenericFieldKey.GENRE)
            )
            return mutableListOf(field)
        } else {
            return mutableListOf()
        }
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
        if (getFirst(GenericFieldKey.TITLE).isNotEmpty()) {
            val field = ID3v1TagField(
                ID3v1FieldKey.TITLE.name,
                getFirst(GenericFieldKey.TITLE)
            )
            return mutableListOf(field)
        } else {
            return mutableListOf()
        }
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
        if (getFirst(GenericFieldKey.YEAR).length > 0) {
            val field = ID3v1TagField(
                ID3v1FieldKey.YEAR.name,
                getFirst(GenericFieldKey.YEAR)
            )
            return mutableListOf(field)
        } else {
            return mutableListOf()
        }
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
     * Retrieve the first value that exists for this generic key
     *
     * @param genericKey
     * @return
     */
    open fun getFirst(genericKey: GenericFieldKey): String {
        when (genericKey) {
            GenericFieldKey.ARTIST -> return artist
            GenericFieldKey.ALBUM -> return album
            GenericFieldKey.TITLE -> return title
            GenericFieldKey.GENRE -> return getFirstGenre()
            GenericFieldKey.YEAR -> return year
            GenericFieldKey.COMMENT -> return comment
            else -> return ""
        }
    }

    /**
     * Delete all instance of artwork Field
     *
     * @throws KeyNotFoundException
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

    override fun deleteField(id: String) {
        val key = GenericFieldKey.valueOf(id.uppercase())
        deleteField(key)
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
            else -> {}
        }
    }

    override fun hasCommonFields(): Boolean {
        //TODO
        return true
    }

    override fun hasField(id: String): Boolean {
        try {
            val key = GenericFieldKey.valueOf(id.uppercase());
            return hasField(key);
        } catch (_: IllegalArgumentException) {
            return false;
        }
    }

    override fun hasField(genericKey: GenericFieldKey): Boolean {
        return getFirst(genericKey).isNotEmpty()
    }

    override fun isEmpty(): Boolean {
        return !(getFirst(GenericFieldKey.TITLE).isNotEmpty()
                || artist.isNotEmpty()
                || album.isNotEmpty()
                || getFirst(GenericFieldKey.GENRE).isNotEmpty()
                || getFirst(GenericFieldKey.YEAR).isNotEmpty()
                || comment.isNotEmpty()
                )
    }

    override fun getArtworkList(): List<Artwork> {
        return listOf()
    }

    /**
     * Create structured representation of this item.
     */
    open fun createStructure() {
        MP3File.tagFormatter?.openHeadingElement(
            TYPE_TAG,
            getIdentifier()?:""
        )
        //Header
        MP3File.tagFormatter?.addElement(TYPE_TITLE, this.title)
        MP3File.tagFormatter?.addElement(TYPE_ARTIST, this.artist)
        MP3File.tagFormatter?.addElement(TYPE_ALBUM, this.album)
        MP3File.tagFormatter?.addElement(TYPE_YEAR, this.year)
        MP3File.tagFormatter?.addElement(TYPE_COMMENT, this.comment)
        MP3File.tagFormatter?.addElement(TYPE_GENRE, this.genre.toInt())
        MP3File.tagFormatter?.closeHeadingElement(TYPE_TAG)
    }
}