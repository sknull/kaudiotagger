package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.util.ID3Tags

open class NumberFixedLength: AbstractDataType {

    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody? = null,
        value: Any? = null
    ): super(identifier, frameBody, value)

    /**
     * Creates a new ObjectNumberFixedLength datatype.
     *
     * @param identifier
     * @param frameBody
     * @param size       the number of significant places that the number is held to
     */
    constructor(
            identifier: String?,
            frameBody: AbstractTagFrameBody? = null,
            size: Int
    ): this(identifier, frameBody) {
        require(size >= 0) { "Length is less than zero: $size" }
        setSize(size)
    }

    constructor(copy: NumberFixedLength): super(copy) {
        setSize(copy.getSize())
    }

    /**
     * Read the number from the byte array
     *
     * @param byteArray
     * @param offset
     */
    override fun readByteArray(byteArray: ByteArray, offset: Int) {
        if ((offset < 0) || (offset >= byteArray.size)) {
            throw InvalidDataTypeException(
                "Offset to byte array is out of bounds: offset = $offset, array.length = ${byteArray.size}"
            )
        }

        if (offset + getSize() > byteArray.size) {
            throw InvalidDataTypeException(
                "Offset plus size to byte array is out of bounds: offset = $offset, size = ${getSize()}() + arr.length ${byteArray.size}"
            )
        }

        var lvalue: Long = 0
        for (i in offset..<(offset + getSize())) {
            lvalue = lvalue shl 8
            lvalue += (byteArray[i].toInt() and 0xff).toLong()
        }
        setValue(lvalue)
        log.debug("Read NumberFixedlength:${getValue()}")
    }

    /**
     * Write data to byte array
     *
     * @return the datatype converted to a byte array
     */
    override fun writeByteArray(): ByteArray? {
        val arr = ByteArray(getSize())
        if (getValue() != null) {
            // Convert value to long
            val temp = ID3Tags.getWholeNumber(getValue())
            (getSize() - 1 downTo 0 ).forEach { i ->
                arr[i] =  (temp and 0xFF).toByte()
                temp shr 8
            }
        }
        return arr
    }
}