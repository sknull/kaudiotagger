package de.visualdigits.kaudiotagger.model.common.types

import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.id3.datatype.AbstractDataType
import de.visualdigits.kaudiotagger.util.ID3Tags

/**
 * Represents a [FrameBodySYTC] tempo code.
 *
 *
 * The tempo is in BPM described with one or two bytes. If the
 * first byte has the value $FF, one more byte follows, which is added
 * to the first giving a range from 2 - 510 BPM, since $00 and $01 is
 * reserved. $00 is used to describe a beat-free time period, which is
 * not the same as a music-free time period. $01 is used to indicate one
 * single beat-stroke followed by a beat-free period.
 *
 * @author [Hendrik Schreiber](mailto:hs@tagtraum.com)
 * @version $Id:$
 */
class TempoCode : AbstractDataType {

    constructor(copyObject: TempoCode): super(copyObject)

    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody?
    ) : super(identifier, frameBody, 0)

    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody?,
        value: Any?
    ) : super(identifier, frameBody, value)

    override fun readByteArray(byteArray: ByteArray, offset: Int) {
        require(offset >= 0) { "negative offset into an array offset:$offset" }
        if (offset >= byteArray.size) {
            throw InvalidDataTypeException("Offset to byte array is out of bounds: offset = $offset, array.length = ${byteArray.size}")
        }

        var lvalue: Long = 0
        lvalue += (byteArray[offset].toInt() and 0xff).toLong()
        if (lvalue == 0xFFL) {
            lvalue += (byteArray[offset + 1].toInt() and 0xff).toLong()
        }
        setValue(lvalue)
    }

    override fun writeByteArray(): ByteArray {
        val size = getSize()
        val arr = ByteArray(size)
        var temp = ID3Tags.getWholeNumber(getValue())
        var offset = 0
        if (temp >= 0xFF) {
            arr[offset] = 0xFF.toByte()
            offset++
            temp -= 0xFF
        }
        arr[offset] = (temp and 0xFFL).toByte()

        return arr
    }

    override fun getSize(): Int {
        return if (getValue() == null) {
            0
        } else {
            if (ID3Tags.getWholeNumber(getValue()) < 0xFF)
                MINIMUM_NO_OF_DIGITS
            else
                MAXIMUM_NO_OF_DIGITS
        }
    }

    override fun toString(): String {
        return if (getValue() == null) "" else getValue().toString()
    }

    companion object {
        private const val MINIMUM_NO_OF_DIGITS = 1
        private const val MAXIMUM_NO_OF_DIGITS = 2
    }
}
