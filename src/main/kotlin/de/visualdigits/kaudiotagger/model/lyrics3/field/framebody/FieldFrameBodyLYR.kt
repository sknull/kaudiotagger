package de.visualdigits.kaudiotagger.model.lyrics3.field.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.ID3v2LyricLine
import de.visualdigits.kaudiotagger.model.lyrics3.datatype.Lyrics3Line
import de.visualdigits.kaudiotagger.model.lyrics3.datatype.Lyrics3TimeStamp
import de.visualdigits.kaudiotagger.model.lyrics3.types.Lyrics3v2Fields
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidTagException
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodySYLT
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyUSLT
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.nio.ByteBuffer

class FieldFrameBodyLYR: AbstractLyrics3v2FieldFrameBody {

    var lines = mutableListOf<Lyrics3Line>()

    /**
     * Creates a FieldBodyLYR datatype.
     */
    constructor()

    constructor(copyObject: FieldFrameBodyLYR): super(copyObject) {
        var old: Lyrics3Line?
        copyObject.lines.indices.forEach { i ->
            old = copyObject.lines.get(i)
            this.lines.add(Lyrics3Line(old))
        }
    }

    /**
     * Creates a FieldBodyLYR datatype.
     *
     * @param sync
     */
    constructor(sync: FrameBodySYLT) {
        addLyric(sync)
    }

    /**
     * Creates a FieldBodyLYR datatype.
     *
     * @param line
     */
    constructor(line: String) {
        readString(line)
    }

    /**
     * Creates a FieldBodyLYR datatype.
     *
     * @param unsync
     */
    constructor(unsync: FrameBodyUSLT) {
        addLyric(unsync)
    }

    /**
     * Creates a new FieldBodyLYR datatype.
     *
     * @param byteBuffer
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    /**
     * @param sync
     */
    fun addLyric(sync: FrameBodySYLT) {
        // SYLT frames are made of individual lines
        var newLine: Lyrics3Line?
        var currentLine: ID3v2LyricLine?
        var timeStamp: Lyrics3TimeStamp?
        val lineMap: MutableMap<String?, Lyrics3Line> = mutableMapOf()

        sync.objectList.forEach { cl ->
            // createField copy to use in tag
            currentLine = ID3v2LyricLine(cl as ID3v2LyricLine)
            timeStamp = Lyrics3TimeStamp("Time Stamp", this)
            timeStamp.setTimeStamp(
                currentLine.timeStamp,
                sync.getTimeStampFormat() as Byte
            )

            if (lineMap.containsKey(currentLine.text)) {
                newLine = lineMap.get(currentLine.text)
                newLine?.addTimeStamp(timeStamp)
            } else {
                newLine = Lyrics3Line("Lyric Line", this)
                newLine.lyric = currentLine.text?:""
                newLine.setTimeStamp(timeStamp)
                lineMap[currentLine.text] = newLine
                lines.add(newLine)
            }
        }
    }

    /**
     * @param unsync
     */
    fun addLyric(unsync: FrameBodyUSLT) {
        // USLT frames are just long text string;
        val line = Lyrics3Line("Lyric Line", this)
        line.lyric = unsync.getLyric()
        lines.add(line)
    }

    /**
     *
     */
    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null) {
            return false
        }
        val lineString: String

        var buffer = ByteArray(5)

        // read the 5 character size
        byteBuffer.get(buffer, 0, 5)

        val size = Integer.parseInt(String(buffer, 0, 5))

        if (
                (size == 0) &&
                        (!TagOptionSingleton.lyrics3KeepEmptyFieldIfRead)
        ) {
            throw InvalidTagException("Lyircs3v2 Field has size of zero.")
        }

        buffer = ByteArray(size)

        // read the SIZE length description
        byteBuffer.get(buffer)
        lineString = String(buffer)
        readString(lineString)

        return false
    }

    /**
     * @param lineString
     */
    private fun readString(lineString: String) {
        // now readString each line and put in the vector;
        var token: String?
        var offset = 0
        var delim: Int = lineString.indexOf(Lyrics3v2Fields.CRLF)
        lines = mutableListOf()

        var line: Lyrics3Line?

        while (delim >= 0) {
            token = lineString.substring(offset, delim)
            line = Lyrics3Line("Lyric Line", this)
            line.lyric = token
            lines.add(line)
            offset = delim + Lyrics3v2Fields.CRLF.length
            delim = lineString.indexOf(Lyrics3v2Fields.CRLF, offset)
        }

        if (offset < lineString.length) {
            token = lineString.substring(offset)
            line = Lyrics3Line("Lyric Line", this)
            line.lyric = token
            lines.add(line)
        }
    }

    /**
     * @return
     */
    private fun writeString(): String {
        var line: Lyrics3Line
        var str = ""

        for (line1 in lines) {
            line = line1 as Lyrics3Line
            str += (line.writeString() + Lyrics3v2Fields.CRLF)
        }

        return str

        //return str.substring(0,str.length()-2); // cut off the last CRLF pair
    }

    /**
     * @return
     */
    fun getLyric(): String {
        return writeString()
    }

    /**
     * @param str
     */
    fun setLyric(str: String) {
        readString(str)
    }

    /**
     * @param obj
     * @return
     */
    override fun isSubsetOf(obj: Any?): Boolean {
        if (obj !is FieldFrameBodyLYR) {
            return false
        }

        val superset = obj.lines
        for (line in lines) {
            if (!superset.contains(line)) {
                return false
            }
        }

        return super.isSubsetOf(obj)
    }

    /**
     * @return
     */
    fun hasTimeStamp(): Boolean {
        var present = false

        for (line in lines) {
            if ((line as Lyrics3Line).hasTimeStamp()) {
                present = true
            }
        }

        return present
    }

    /**
     * @return
     */
    override fun getIdentifier(): String {
        return "LYR"
    }

    /**
     * @return
     */
    override fun getSize(): Int {
        var size = 0
        var line: Lyrics3Line

        for (line1 in lines) {
            line = line1 as Lyrics3Line
            size += (line.getSize() + 2)
        }

        return size

        //return size - 2; // cut off the last crlf pair
    }

    /**
     * TODO
     */
    override fun setupObjectList() {
    }
}