package de.visualdigits.kaudiotagger.model.tag.id3

import de.visualdigits.kaudiotagger.model.exceptions.TagException
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyCOMM
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTALB
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTCON
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTDRC
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTIT2
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTPE1
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTRCK
import de.visualdigits.kaudiotagger.model.frame.id3.ID3v24Frame
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
import de.visualdigits.kaudiotagger.model.tag.AbstractTag
import de.visualdigits.kaudiotagger.util.ID3Tags
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel

class ID3v11Tag: ID3v1Tag {
    
    companion object {
        //For writing output
        const val TYPE_TRACK: String = "track"
        const val TRACK_UNDEFINED: Int = 0
        const val TRACK_MAX_VALUE: Int = 255
        const val TRACK_MIN_VALUE: Int = 1
        const val FIELD_COMMENT_LENGTH: Int = 28
        const val FIELD_COMMENT_POS: Int = 97
        const val FIELD_TRACK_INDICATOR_LENGTH: Int = 1
        const val FIELD_TRACK_INDICATOR_POS: Int = 125
        const val FIELD_TRACK_LENGTH: Int = 1
        const val FIELD_TRACK_POS: Int = 126
        const val RELEASE: Byte = 1
        const val MAJOR_VERSION: Byte = 1
        const val REVISION: Byte = 0
    }

    /**
     * Track is held as a single byte in v1.1
     */
    var track: Byte = TRACK_UNDEFINED.toByte()

    /**
     * Creates a new ID3v11 datatype.
     */
    constructor() {
    }

    constructor(copyObject: ID3v11Tag): super(copyObject) {
        this.track = copyObject.track
    }

    /**
     * Creates a new ID3v11 datatype from a non v11 tag
     *
     * @param mp3tag
     * @throws UnsupportedOperationException
     */
    constructor(mp3tag: AbstractTag) {
        if (mp3tag != null) {
            if (mp3tag is ID3v1Tag) {
                if (mp3tag is ID3v11Tag) {
                    throw UnsupportedOperationException(
                        "Copy Constructor not called. Please type cast the argument"
                    )
                }
                // id3v1_1 objects are also id3v1 objects
                this.title = mp3tag.title
                this.artist = mp3tag.artist
                this.album = mp3tag.album
                this.comment = mp3tag.comment
                this.year = mp3tag.year
                this.genre = mp3tag.genre
            } else {
                val id3tag: ID3v24Tag
                // first change the tag to ID3v2_4 tag if not one already
                if (mp3tag !is ID3v24Tag) {
                    id3tag = ID3v24Tag(mp3tag)
                } else {
                    id3tag = mp3tag
                }
                var frame: ID3v24Frame
                var text: String
                if (id3tag.hasFrame(ID3v24KFrame.TITLE.id)) {
                    frame = id3tag.getFrame(ID3v24KFrame.TITLE.id) as ID3v24Frame
                    text = (frame.frameBody as FrameBodyTIT2).getText()
                    this.title = ID3Tags.truncate(text, FIELD_TITLE_LENGTH)
                }
                if (id3tag.hasFrame(ID3v24KFrame.ARTIST.id)) {
                    frame = id3tag.getFrame(ID3v24KFrame.ARTIST.id) as ID3v24Frame
                    text = (frame.frameBody as FrameBodyTPE1).getText()
                    this.artist = ID3Tags.truncate(text, FIELD_ARTIST_LENGTH)
                }
                if (id3tag.hasFrame(ID3v24KFrame.ALBUM.id)) {
                    frame = id3tag.getFrame(ID3v24KFrame.ALBUM.id) as ID3v24Frame
                    text = (frame.frameBody as FrameBodyTALB).getText()
                    this.album = ID3Tags.truncate(text, FIELD_ALBUM_LENGTH)
                }
                if (id3tag.hasFrame(ID3v24KFrame.YEAR.id)) {
                    frame = id3tag.getFrame(ID3v24KFrame.YEAR.id) as ID3v24Frame
                    text = (frame.frameBody as FrameBodyTDRC).getText()
                    this.year = ID3Tags.truncate(text, FIELD_YEAR_LENGTH)
                }

                if (id3tag.hasFrame(ID3v24KFrame.COMMENT.id)) {
                    text = ""
                    id3tag.getFrameOfType(
                        ID3v24KFrame.COMMENT.id
                    ).forEach { frame ->
                        text += (((frame as ID3v24Frame).frameBody as FrameBodyCOMM).getText() + " ")
                    }
                    this.comment = ID3Tags.truncate(text, FIELD_COMMENT_LENGTH)
                }
                if (id3tag.hasFrame(ID3v24KFrame.GENRE.id)) {
                    frame = id3tag.getFrame(ID3v24KFrame.GENRE.id) as ID3v24Frame
                    text = (frame.frameBody as FrameBodyTCON).getText()
                    try {
                        this.genre = ID3Tags.findNumber(text).toByte()
                    } catch (ex: TagException) {
                        log.warn(
                            "Unable to convert TCON frame to format suitable for v11 tag",
                            ex
                        )
                        this.genre = GENRE_UNDEFINED as Byte
                    }
                }
                if (id3tag.hasFrame(ID3v24KFrame.TRACK.id)) {
                    frame = id3tag.getFrame(ID3v24KFrame.TRACK.id) as ID3v24Frame
                    this.track =
                        (frame.frameBody as FrameBodyTRCK).getTrackNo().toByte()
                }
            }
        }
    }

    /**
     * Creates a new ID3v11 datatype.
     *
     * @param file
     * @throws TagNotFoundException
     * @throws IOException
     */
    constructor(file: RandomAccessFile) {
        val fc: FileChannel
        val byteBuffer = ByteBuffer.allocate(TAG_LENGTH)

        fc = file.getChannel()
        fc.position(file.length() - TAG_LENGTH)

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
        super.read(byteBuffer)
        track = dataBuffer[FIELD_TRACK_POS]
    }

    /**
     * Find identifier within byteBuffer to indicate that a v11 tag exists within the buffer
     *
     * @param byteBuffer
     * @return true if find header for v11 tag within buffer
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
        //Now check for TRACK if the next byte is also null byte then not v1.1
        //tag, however this means cannot have v1_1 tag with track setField to zero/undefined
        //because on next read will be v1 tag.
        return byteBuffer.get() != END_OF_FIELD
    }

    /**
     * Write this representation of tag to the file indicated
     *
     * @param file that this tag should be written to
     * @throws IOException thrown if there were problems writing to the file
     */
    override fun write(file: RandomAccessFile) {
        log.debug("Saving ID3v11 tag to file")
        val buffer = ByteArray(TAG_LENGTH)
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
            str = ID3Tags.truncate(title, FIELD_TITLE_LENGTH)
            i = 0
            while (i < str.length) {
                buffer[i + offset] = str.get(i).code.toByte()
                i++
            }
        }
        offset = FIELD_ARTIST_POS
        if (TagOptionSingleton.id3v1SaveArtist) {
            str = ID3Tags.truncate(artist, FIELD_ARTIST_LENGTH)
            i = 0
            while (i < str.length) {
                buffer[i + offset] = str.get(i).code.toByte()
                i++
            }
        }
        offset = FIELD_ALBUM_POS
        if (TagOptionSingleton.id3v1SaveAlbum) {
            str = ID3Tags.truncate(album, FIELD_ALBUM_LENGTH)
            i = 0
            while (i < str.length) {
                buffer[i + offset] = str.get(i).code.toByte()
                i++
            }
        }
        offset = FIELD_YEAR_POS
        if (TagOptionSingleton.id3v1SaveYear) {
            str = ID3Tags.truncate(year, FIELD_YEAR_LENGTH)
            i = 0
            while (i < str.length) {
                buffer[i + offset] = str.get(i).code.toByte()
                i++
            }
        }
        offset = FIELD_COMMENT_POS
        if (TagOptionSingleton.id3v1SaveComment) {
            str = ID3Tags.truncate(comment, FIELD_COMMENT_LENGTH)
            i = 0
            while (i < str.length) {
                buffer[i + offset] = str.get(i).code.toByte()
                i++
            }
        }
        offset = FIELD_TRACK_POS
        buffer[offset] = track // skip one byte extra blank for 1.1 definition
        offset = FIELD_GENRE_POS
        if (TagOptionSingleton.id3v1SaveGenre) {
            buffer[offset] = genre
        }
        file.write(buffer)

        log.debug("Saved ID3v11 tag to file")
    }
}