package de.visualdigits.kaudiotagger.model.lyrics3.tag

import de.visualdigits.kaudiotagger.model.common.exceptions.TagException
import de.visualdigits.kaudiotagger.model.common.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v1Tag
import de.visualdigits.kaudiotagger.model.lyrics3.frame.framebody.FieldFrameBodyLYR
import de.visualdigits.kaudiotagger.util.ID3Tags
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer

class Lyrics3v1: AbstractLyrics3 {

    var lyric = ""

    /**
     * Creates a Lyrics3v1 datatype.
     */
    constructor()

    constructor(copyObject: Lyrics3v1): super(copyObject) {
        this.lyric = copyObject.lyric
    }

    constructor(mp3Tag: AbstractTag) {
        val lyricTag: Lyrics3v2

        if (mp3Tag is Lyrics3v1) {
            throw UnsupportedOperationException(
                "Copy Constructor not called. Please type cast the argument"
            )
        } else if (mp3Tag is Lyrics3v2) {
            lyricTag = mp3Tag
        } else {
            lyricTag = Lyrics3v2(mp3Tag)
        }

        val lyricField = lyricTag.getField("LYR")?.frameBody as? FieldFrameBodyLYR
        this.lyric = lyricField?.getLyric()?:""
    }

    /**
     * Creates a Lyrics3v1 datatype.
     *
     * @param byteBuffer
     */
    constructor(byteBuffer: ByteBuffer) {
        try {
            this.read(byteBuffer)
        } catch (e: TagException) {
            log.error("Something went wrong", e)
        }
    }

    /**
     * @param byteBuffer
     */
    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null || !seek(byteBuffer)) {
            return false
        }

        val buffer = ByteArray(5100 + 9 + 11)
        byteBuffer.get(buffer)
        val lyricBuffer: String = String(buffer)

        lyric = lyricBuffer.substringBefore("LYRICSEND")

        return true
    }

    /**
     * TODO implement
     *
     * @param byteBuffer
     * @return
     */
    override fun seek(byteBuffer: ByteBuffer): Boolean {
        return false
    }

    /**
     * @param file
     * @return
     */
    fun seek(file: RandomAccessFile): Boolean {
        val buffer = ByteArray(5100 + 9 + 11)
        var lyricsEnd: String
        val lyricsStart: String
        var offset: Long

        // check right before the ID3 1.0 tag for the lyrics3 tag
        file.seek(file.length() - 128 - 9)
        file.read(buffer, 0, 9)
        lyricsEnd = String(buffer, 0, 9)

        if (lyricsEnd.equals("LYRICSEND")) {
            offset = file.filePointer
        } else {
            // check the end of the file for a lyrics3 tag incase an ID3
            // tag wasn't placed after it.
            file.seek(file.length() - 9)
            file.read(buffer, 0, 9)
            lyricsEnd = String(buffer, 0, 9)

            if (lyricsEnd.equals("LYRICSEND")) {
                offset = file.filePointer
            } else {
                return false
            }
        }

        // the tag can at most only be 5100 bytes
        offset -= (5100 + 9 + 11)
        file.seek(offset)
        file.read(buffer)
        lyricsStart = String(buffer)

        // search for the tag
        val i = lyricsStart.indexOf("LYRICSBEGIN")
        if (i == -1) {
            return false
        }

        file.seek(offset + i + 11)

        return true
    }

    /**
     * @param obj
     * @return
     */
    override fun isSubsetOf(obj: Any?): Boolean {
        return ((obj is Lyrics3v1) &&
                (obj.lyric.contains(this.lyric))
                )
    }

    /**
     * @return
     */
    override fun getIdentifier(): String {
        return "Lyrics3v1.00"
    }

    /**
     * @return
     */
    override fun getSize(): Int {
        return "LYRICSBEGIN".length + lyric.length + "LYRICSEND".length
    }

    /**
     * @param file
     */
    override fun write(file: RandomAccessFile) {
        var str: String
        var offset: Int
        val buffer: ByteArray?
        val id3v1tag: ID3v1Tag? = null

        delete(file)
        file.seek(file.length())

        buffer = ByteArray(lyric.length + 11 + 9)

        str = "LYRICSBEGIN"

        for (i in 0..<str.length) {
            buffer[i] = str.get(i).code.toByte()
        }

        offset = str.length

        str = ID3Tags.truncate(lyric, 5100)

        for (i in 0..<str.length) {
            buffer[i + offset] = str.get(i).code.toByte()
        }

        offset += str.length

        str = "LYRICSEND"

        for (i in 0..<str.length) {
            buffer[i + offset] = str.get(i).code.toByte()
        }

        offset += str.length

        file.write(buffer, 0, offset)

        if (id3v1tag != null) {
            id3v1tag.write(file)
        }
    }
}