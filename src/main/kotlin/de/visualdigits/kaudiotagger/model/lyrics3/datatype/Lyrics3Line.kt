package de.visualdigits.kaudiotagger.model.lyrics3.datatype

import de.visualdigits.kaudiotagger.model.id3.datatype.AbstractDataType
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.id3.datatype.ID3v2LyricLine
import java.nio.charset.StandardCharsets
import java.util.LinkedList

class Lyrics3Line : AbstractDataType {
    /**
     *
     */
    var timeStamp = LinkedList<Lyrics3TimeStamp>()

    /**
     * @return
     */
    /**
     *
     */
    var lyric: String? = ""

    /**
     * Creates a new ObjectLyrics3Line datatype.
     *
     * @param identifier
     * @param frameBody
     */
    constructor(identifier: String?, frameBody: AbstractTagFrameBody) : super(identifier, frameBody)

    constructor(copyObject: Lyrics3Line): super(copyObject) {
        this.lyric = copyObject.lyric
        var newTimeStamp: Lyrics3TimeStamp
        for (i in copyObject.timeStamp.indices) {
            newTimeStamp = Lyrics3TimeStamp(copyObject.timeStamp.get(i))
            this.timeStamp.add(newTimeStamp)
        }
    }

    /**
     * @return
     */
    override fun getSize(): Int = timeStamp.sumOf<Lyrics3TimeStamp> { aTimeStamp -> aTimeStamp.getSize() } + (lyric?.length ?: 0)

    /**
     * @return
     */
    fun getTimeStamp(): MutableIterator<Lyrics3TimeStamp> {
        return timeStamp.iterator()
    }

    /**
     * @param time
     */
    fun setTimeStamp(time: Lyrics3TimeStamp) {
        timeStamp.clear()
        timeStamp.add(time)
    }

    fun addLyric(newLyric: String) {
        this.lyric += newLyric
    }

    fun addLyric(line: ID3v2LyricLine) {
        this.lyric += line.text
    }

    /**
     * @param time
     */
    fun addTimeStamp(time: Lyrics3TimeStamp) {
        timeStamp.add(time)
    }

    /**
     * @return
     */
    fun hasTimeStamp(): Boolean {
        return !timeStamp.isEmpty()
    }

    /**
     * @return
     */
    override fun toString(): String {
        var str = ""
        for (aTimeStamp in timeStamp) {
            str += aTimeStamp.toString()
        }
        return "timeStamp = " + str + ", lyric = " + lyric + "\n"
    }

    override fun readByteArray(arr: ByteArray, offset: Int) {
        readString(arr.toString(), offset)
    }

    /**
     * @param lineString
     * @param offset
     */
    fun readString(lineString: String, offset: Int) {
        var offset = offset
        if (lineString == null) {
            throw NullPointerException("Image is null")
        }
        if ((offset < 0) || (offset >= lineString.length)) {
            throw IndexOutOfBoundsException(
                "Offset to line is out of bounds: offset = " +
                        offset +
                        ", line.length()" +
                        lineString.length
            )
        }
        var delim: Int
        var time: Lyrics3TimeStamp
        timeStamp = LinkedList<Lyrics3TimeStamp>()
        delim = lineString.indexOf("[", offset)
        while (delim >= 0) {
            offset = lineString.indexOf("]", delim) + 1
            time = Lyrics3TimeStamp("Time Stamp")
            time.readString(lineString.substring(delim, offset))
            timeStamp.add(time)
            delim = lineString.indexOf("[", offset)
        }
        lyric = lineString.substring(offset)
    }

    override fun writeByteArray(): ByteArray? {
        return writeString().toByteArray(StandardCharsets.ISO_8859_1)
    }

    /**
     * @return
     */
    fun writeString(): String {
        var str: String = ""
        var time: Lyrics3TimeStamp
        for (aTimeStamp in timeStamp) {
            time = aTimeStamp
            str += time.writeString()
        }
        return str + lyric
    }
}