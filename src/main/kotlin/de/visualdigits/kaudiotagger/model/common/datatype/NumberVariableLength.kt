package de.visualdigits.kaudiotagger.model.common.datatype

import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.util.ID3Tags


/**
 * Represents a number which may span a number of bytes when written to file depending what size is to be represented.
 *
 *
 * The bitorder in ID3v2 is most significant bit first (MSB). The byteorder in multibyte numbers is most significant
 * byte first (e.g. $12345678 would be encoded $12 34 56 78), also known as big endian and network byte order.
 *
 *
 * In ID3Specification would be denoted as $xx xx xx xx (xx ...) , this denotes at least four bytes but may be more.
 * Sometimes may be completely optional (zero bytes)
 */
class NumberVariableLength : AbstractDataType {

    /**
     * Return the  minimum  number of digits that can be used to express the number
     *
     * @return the minimum number of digits that can be used to express the number
     */
    var minimumLength: Int = MINIMUM_NO_OF_DIGITS

    /**
     * Creates a new ObjectNumberVariableLength datatype, set minimum length to zero
     * if this datatype is optional.
     *
     * @param identifier
     * @param frameBody
     * @param minimumSize
     */
    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody,
        minimumSize: Int
    ) : super(identifier, frameBody) {
        //Set minimum length, which can be zero if optional
        this.minimumLength = minimumSize
    }

    constructor(copy: NumberVariableLength) : super(copy) {
        this.minimumLength = copy.minimumLength
    }

    /**
     * @param obj
     * @return
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is NumberVariableLength) {
            return false
        }

        return this.minimumLength == obj.minimumLength && super.equals(obj)
    }

    /**
     * Read from Byte Array
     *
     * @param arr
     * @param offset
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    override fun readByteArray(arr: ByteArray, offset: Int) {
        //Coding error, should never happen

        //Coding error, should never happen as far as I can see
        require(offset >= 0) { "negativer offset into an array offset:$offset" }

        //If optional then set value to zero, this will mean that if this frame is written back to file it will be created
        //with this additional datatype wheras it didnt exist but I think this is probably an advantage the frame is
        //more likely to be parsed by other applications if it contains optional fields.
        //if not optional problem with this frame
        if (offset >= arr.size) {
            if (this.minimumLength == 0) {
                setValue(0L)
                return
            } else {
                throw InvalidDataTypeException(
                    "Offset to byte array is out of bounds: offset = $offset, array.length = ${arr.size}"
                )
            }
        }

        var lvalue: Long = 0

        //Read the bytes (starting from offset), the most significant byte of the number being constructed is read first,
        //we then shift the resulting long one byte over to make room for the next byte
        for (i in offset..<arr.size) {
            lvalue = lvalue shl 8
            lvalue += (arr[i].toInt() and 0xff).toLong()
        }

        setValue(lvalue)
    }

    /**
     * @return String representation of the number
     */
    override fun toString(): String {
        if (getValue() == null) {
            return ""
        } else {
            return getValue().toString()
        }
    }

    /**
     * Write to Byte Array
     *
     * @return the datatype converted to a byte array
     */
    override fun writeByteArray(): ByteArray {
        val size = getSize()
        val arr: ByteArray?

        if (size == 0) {
            arr = ByteArray(0)
        } else {
            var temp = ID3Tags.getWholeNumber(getValue())
            arr = ByteArray(size)

            //keeps shifting the number downwards and masking the last 8 bist to get the value for the next byte
            //to be written
            for (i in size - 1 downTo 0) {
                arr[i] = (temp and 0xFFL).toByte()
                temp = temp shr 8
            }
        }
        return arr
    }

    /**
     * Return the maximum number of digits that can be used to express the number
     *
     * @return the maximum number of digits that can be used to express the number
     */
    fun getMaximumLength(): Int {
        return MAXIMUM_NO_OF_DIGITS
    }

    /**
     * @return the number of bytes required to write this to a file
     */
    override fun getSize(): Int {
        if (getValue() == null) {
            return 0
        } else {
            var temp = ID3Tags.getWholeNumber(getValue())
            var size = 0

            (MINIMUM_NO_OF_DIGITS..getMaximumLength()).forEach { i ->
                val current = temp.toByte().toInt() and 0xFF
                if (current != 0) {
                    size = i
                }
                temp = temp shr getMaximumLength()
            }

            return if (this.minimumLength > size) this.minimumLength else size
        }
    }

    companion object {
        const val MINIMUM_NO_OF_DIGITS: Int = 1
        const val MAXIMUM_NO_OF_DIGITS: Int = 8
    }
}
