package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.util.ID3Tags

open class NumberFixedLength(
    identifier: String,
    frameBody: AbstractTagFrameBody? = null,
    value: Any? = null
): AbstractDataType(
    identifier,
    frameBody,
    value
) {
    /**
     * Creates a new ObjectNumberFixedLength datatype.
     *
     * @param identifier
     * @param frameBody
     * @param size       the number of significant places that the number is held to
     * @throws IllegalArgumentException
     */
    constructor(
            identifier: String,
            frameBody: AbstractTagFrameBody? = null,
            size: Int
    ): this(identifier, frameBody) {
        if (size < 0) {
            throw IllegalArgumentException("Length is less than zero: $size")
        }
        this.size = size
    }

    constructor(copy: NumberFixedLength): this(copy.identifier) {
        this.size = copy.size
    }

    /**
     * Return size
     *
     * @return the size of this number
     */
    override fun getSize(): Int {
        return size
    }

    /**
     * Read the number from the byte array
     *
     * @param arr
     * @param offset
     */
    override fun readByteArray(arr: ByteArray, offset: Int) {
        if ((offset < 0) || (offset >= arr.size)) {
            throw InvalidDataTypeException(
                "Offset to byte array is out of bounds: offset = $offset, array.length = ${arr.size}"
            )
        }

        if (offset + size > arr.size) {
            throw InvalidDataTypeException(
                "Offset plus size to byte array is out of bounds: offset = $offset, size = $size + arr.length ${arr.size}"
            )
        }

        var lvalue: Long = 0
        for (i in offset..<(offset + size)) {
            lvalue = lvalue shl 8
            lvalue += (arr[i].toInt() and 0xff).toLong()
        }
        value = lvalue
        log.debug("Read NumberFixedlength:$value")
    }

    /**
     * Write data to byte array
     *
     * @return the datatype converted to a byte array
     */
    override fun writeByteArray(): ByteArray {
        val arr = ByteArray(size)
        if (value != null) {
            //Convert value to long
            val temp = ID3Tags.getWholeNumber(value!!)
            (size - 1 downTo 0 ).forEach { i ->
                arr[i] =  (temp and 0xFF).toByte()
                temp shr 8
            }
        }
        return arr;
    }
}