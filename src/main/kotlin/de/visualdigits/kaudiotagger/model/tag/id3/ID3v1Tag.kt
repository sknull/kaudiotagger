package de.visualdigits.kaudiotagger.model.tag.id3

import de.visualdigits.kaudiotagger.model.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.tag.Tag
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
        const val BYTE_TO_UNSIGNED: Byte = 0xff.toByte()
        const val GENRE_UNDEFINED: Int = 0xff
        const val RELEASE: Byte = 1
        const val MAJOR_VERSION: Byte = 0
        const val REVISION: Byte = 0
    }

    var album: String = ""

    var artist: String = ""

    var comment: String = ""

    var title: String? = null

    var year: String = ""

    var genre: Byte = -1

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
        if (mp3tag != null) {
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
            this.album = convertedTag.album
            this.artist = convertedTag.artist
            this.comment = convertedTag.comment
            this.title = convertedTag.title
            this.year = convertedTag.year
            this.genre = convertedTag.genre
        }
    }

    /**
     * Creates a new ID3v1 datatype.
     *
     * @param file
     * @throws TagNotFoundException
     * @throws IOException
     */
    constructor(file: RandomAccessFile) {
        val fc: FileChannel
        val byteBuffer: ByteBuffer?

        fc = file.getChannel()
        fc.position(file.length() - TAG_LENGTH)
        byteBuffer = ByteBuffer.allocate(TAG_LENGTH)
        fc.read(byteBuffer)
        byteBuffer.flip()
        read(byteBuffer)
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

    /**
     * Read in a tag from the ByteBuffer
     *
     * @param byteBuffer from where to read in a tag
     */
    override fun read(byteBuffer: ByteBuffer) {
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
            title = title?.take(m.start());
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

        genre = dataBuffer[FIELD_GENRE_POS];
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
            buffer[offset] = genre
        }
        file.write(buffer)
        log.debug("Saved ID3v1 tag to file")
    }
}