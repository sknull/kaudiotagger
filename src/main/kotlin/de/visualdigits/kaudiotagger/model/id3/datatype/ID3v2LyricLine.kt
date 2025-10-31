package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import java.nio.charset.StandardCharsets

class ID3v2LyricLine : AbstractDataType {

    var text: String = ""

    var timeStamp: Long = 0

    constructor(identifier: String?, frameBody: AbstractTagFrameBody) : super(identifier, frameBody)

    constructor(copyObject: ID3v2LyricLine): super(copyObject) {
        this.text = copyObject.text
        this.timeStamp = copyObject.timeStamp
    }

    /**
     * @param byteArray
     * @param offset
     */
    override fun readByteArray(byteArray: ByteArray, offset: Int) {

        if ((offset < 0) || (offset >= byteArray.size)) {
            throw IndexOutOfBoundsException(
                "Offset to byte array is out of bounds: offset = " +
                        offset +
                        ", array.length = " +
                        byteArray.size
            )
        }

        // offset += ();
        text = String(
            byteArray,
            offset,
            byteArray.size - offset - 4,
            StandardCharsets.ISO_8859_1
        )

        // text = text.substring(0, text.length() - 5);
        timeStamp = 0

        for (i in byteArray.size - 4..<byteArray.size) {
            timeStamp = timeStamp shl 8
            timeStamp += byteArray[i].toLong()
        }
    }

    override fun toString(): String {
        return "$timeStamp $text"
    }

    override fun writeByteArray(): ByteArray {
        val arr = ByteArray(getSize())
        (0 until text.length).forEach { i ->
            arr[i] = text[i].code.toByte()
        }

        var i = text.length
        arr[i++] = 0
        arr[i++] = ((timeStamp and 0xFF000000L) shr 24).toByte()
        arr[i++] = ((timeStamp and 0x00FF0000L) shr 16).toByte()
        arr[i++] = ((timeStamp and 0x0000FF00L) shr 8).toByte()
        arr[i] = (timeStamp and 0x000000FFL).toByte()

        return arr
    }

    override fun getSize(): Int = text.length + 1 + 4
}