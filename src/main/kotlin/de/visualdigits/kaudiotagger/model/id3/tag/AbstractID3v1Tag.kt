package de.visualdigits.kaudiotagger.model.id3.tag

import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.util.regex.Pattern

abstract class AbstractID3v1Tag: AbstractID3Tag {
    
    companion object {
        // Tag ID as held in file
        const val TAG: String = "TAG"

        // If field is less than maximum field length this is how it is terminated
        val END_OF_FIELD: Byte = 0
        val TAG_ID: ByteArray = byteArrayOf('T'.code.toByte(), 'A'.code.toByte(), 'G'.code.toByte())

        // Fields Lengths common to v1 and v1.1 tags
        const val TAG_LENGTH: Int = 128
        const val TAG_DATA_LENGTH: Int = 125
        const val FIELD_TAGID_LENGTH: Int = 3
        const val FIELD_TITLE_LENGTH: Int = 30
        const val FIELD_ARTIST_LENGTH: Int = 30
        const val FIELD_ALBUM_LENGTH: Int = 30
        const val FIELD_YEAR_LENGTH: Int = 4
        const val FIELD_GENRE_LENGTH: Int = 1

        // Field Positions, starting from zero so fits in with Java Terminology
        const val FIELD_TAGID_POS: Int = 0
        const val FIELD_TITLE_POS: Int = 3
        const val FIELD_ARTIST_POS: Int = 33
        const val FIELD_ALBUM_POS: Int = 63
        const val FIELD_YEAR_POS: Int = 93
        const val FIELD_GENRE_POS: Int = 127

        // For writing output
        const val TYPE_TITLE: String = "title"
        const val TYPE_ARTIST: String = "artist"
        const val TYPE_ALBUM: String = "album"
        const val TYPE_YEAR: String = "year"
        const val TYPE_GENRE: String = "genre"

        // Used to detect end of field in String constructed from Data
        var endofStringPattern: Pattern = Pattern.compile("\\x00")
    }

    constructor()

    constructor(copyObject: AbstractID3v1Tag): super(copyObject)

    /**
     * Return the size of this tag, the size is fixed for tags of this type
     *
     * @return size of this tag in bytes
     */
    override fun getSize(): Int {
        return TAG_LENGTH
    }

    /**
     * Delete tag from file
     * Looks for tag and if found lops it off the file.
     *
     * @param file to delete the tag from
     */
    override fun delete(file: RandomAccessFile) {
        // Read into Byte Buffer
        log.debug("Deleting ID3v1 from file if exists")
        val fc: FileChannel = file.getChannel()
        if (file.length() < TAG_LENGTH) {
            throw IOException(
                "File not not appear large enough to contain a tag"
            )
        }
        fc.position(file.length() - TAG_LENGTH)
        val byteBuffer = ByteBuffer.allocate(TAG_LENGTH)
        fc.read(byteBuffer)
        byteBuffer.rewind()
        if (seekForV1OrV11Tag(byteBuffer)) {
            try {
                log.debug("Deleted ID3v1 tag")
                file.setLength(file.length() - TAG_LENGTH)
            } catch (ex: IOException) {
                log.error("Unable to delete existing ID3v1 Tag:" + ex.message)
            }
        } else {
            log.debug("Unable to find ID3v1 tag to deleteField")
        }
    }

    /**
     * Does a v1tag or a v11tag exist
     *
     * @return whether tag exists within the byteBuffer
     */
    fun seekForV1OrV11Tag(byteBuffer: ByteBuffer): Boolean {
        val buffer = ByteArray(FIELD_TAGID_LENGTH)
        // read the TAG value
        byteBuffer[buffer, 0, FIELD_TAGID_LENGTH]
        return (buffer.contentEquals(TAG_ID))
    }
}