package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import java.nio.charset.StandardCharsets

class Lyrics3TimeStamp : AbstractDataType {
    /**
     * @return
     */
    /**
     *
     */
    var minute: Long = 0

    /**
     * @return
     */
    /**
     *
     */
    var second: Long = 0

    /**
     * Creates a new ObjectLyrics3TimeStamp datatype.
     *
     * @param identifier
     * @param frameBody
     */
    constructor(identifier: String?, frameBody: AbstractTagFrameBody) : super(identifier, frameBody)

    constructor(identifier: String) : super(identifier, null)

    constructor(copy: Lyrics3TimeStamp) : super(copy) {
        this.minute = copy.minute
        this.second = copy.second
    }

    /**
     * Todo this is wrong
     *
     * @param s
     */
    fun readString(s: String) {
    }

    /**
     * @return
     */
    override fun getSize(): Int = 7

    /**
     * Creates a new ObjectLyrics3TimeStamp datatype.
     *
     * @param timeStamp
     * @param timeStampFormat
     */
    fun setTimeStamp(timeStamp: Long, timeStampFormat: Byte) {
        /**
         * @todo convert both types of formats
         */
        var timeStamp = timeStamp
        timeStamp = timeStamp / 1000
        minute = timeStamp / 60
        second = timeStamp % 60
    }

    /**
     * @param obj
     * @return
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is Lyrics3TimeStamp) {
            return false
        }

        if (this.minute != obj.minute) {
            return false
        }

        return this.second == obj.second && super.equals(obj)
    }

    /**
     * @return
     */
    override fun toString(): String {
        return writeString()
    }

    /**
     * @return
     */
    fun writeString(): String {
        var str: String
        str = "["

        if (minute < 0) {
            str += "00"
        } else {
            if (minute < 10) {
                str += '0'
            }

            str += minute.toString()
        }

        str += ':'

        if (second < 0) {
            str += "00"
        } else {
            if (second < 10) {
                str += '0'
            }

            str += second.toString()
        }

        str += ']'

        return str
    }

    override fun readByteArray(arr: ByteArray, offset: Int) {
        readString(arr.toString(), offset)
    }

    /**
     * @param timeStamp
     * @param offset
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    fun readString(timeStamp: String, offset: Int) {
        var timeStamp = timeStamp
        if (timeStamp == null) {
            throw NullPointerException("Image is null")
        }

        if ((offset < 0) || (offset >= timeStamp.length)) {
            throw IndexOutOfBoundsException(
                "Offset to timeStamp is out of bounds: offset = " +
                        offset +
                        ", timeStamp.length()" +
                        timeStamp.length
            )
        }

        timeStamp = timeStamp.substring(offset)

        if (timeStamp.length == 7) {
            minute = timeStamp.substring(1, 3).toInt().toLong()
            second = timeStamp.substring(4, 6).toInt().toLong()
        } else {
            minute = 0
            second = 0
        }
    }

    override fun writeByteArray(): ByteArray {
        return writeString().toByteArray(StandardCharsets.ISO_8859_1)
    }
}
