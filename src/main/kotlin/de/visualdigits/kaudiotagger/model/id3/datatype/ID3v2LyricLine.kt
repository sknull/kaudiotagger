package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import java.nio.charset.StandardCharsets

class ID3v2LyricLine : AbstractDataType {

    /**
     *
     */
    var text: String = ""

    /**
     * @return
     */
    /**
     *
     */
    var timeStamp: Long = 0

    constructor(identifier: String?, frameBody: AbstractTagFrameBody) : super(identifier, frameBody)

    constructor(copy: ID3v2LyricLine) : super(copy) {
        this.text = copy.text
        this.timeStamp = copy.timeStamp
    }

    /**
     * @param obj
     * @return
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is ID3v2LyricLine) {
            return false
        }

        if (this.text != obj.text) {
            return false
        }

        return this.timeStamp == obj.timeStamp && super.equals(obj)
    }

    /**
     * @param arr
     * @param offset
     */
    override fun readByteArray(arr: ByteArray, offset: Int) {
        if (arr == null) {
            throw NullPointerException("Byte array is null")
        }

        if ((offset < 0) || (offset >= arr.size)) {
            throw IndexOutOfBoundsException(
                "Offset to byte array is out of bounds: offset = " +
                        offset +
                        ", array.length = " +
                        arr.size
            )
        }

        //offset += ();
        text = String(
            arr,
            offset,
            arr.size - offset - 4,
            StandardCharsets.ISO_8859_1
        )

        //text = text.substring(0, text.length() - 5);
        timeStamp = 0

        for (i in arr.size - 4..<arr.size) {
            timeStamp = timeStamp shl 8
            timeStamp += arr[i].toLong()
        }
    }

    /**
     * @return
     */
    override fun toString(): String {
        return timeStamp.toString() + " " + text
    }

    /**
     * @return
     */
    override fun writeByteArray(): ByteArray {
        var i: Int
        val arr = ByteArray(getSize())

        i = 0
        while (i < text.length) {
            arr[i] = text.get(i).code.toByte()
            i++
        }

        arr[i++] = 0
        arr[i++] = ((timeStamp and 0xFF000000L) shr 24).toByte()
        arr[i++] = ((timeStamp and 0x00FF0000L) shr 16).toByte()
        arr[i++] = ((timeStamp and 0x0000FF00L) shr 8).toByte()
        arr[i++] = (timeStamp and 0x000000FFL).toByte()

        return arr
    }

    /**
     * @return
     */
    override fun getSize(): Int = text.length + 1 + 4
}