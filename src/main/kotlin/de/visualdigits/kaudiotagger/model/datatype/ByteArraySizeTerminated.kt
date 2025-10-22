package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody

class ByteArraySizeTerminated(
    identifier: String,
    frameBody: AbstractTagFrameBody? = null,
    value: Any? = null
): TextEncodedStringSizeTerminated(
    identifier,
    frameBody,
    value
)  {

    override fun equals(obj: Any?): Boolean {
        return obj is ByteArraySizeTerminated && super.equals(obj)
    }

    /**
     * @param arr
     * @param offset
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    @Throws(InvalidDataTypeException::class)
    override fun readByteArray(arr: ByteArray, offset: Int) {
        if (arr == null) {
            throw NullPointerException("Byte array is null")
        }

        if (offset < 0) {
            throw IndexOutOfBoundsException(
                "Offset to byte array is out of bounds: offset = " +
                        offset +
                        ", array.length = " +
                        arr.size
            )
        }

        //Empty Byte Array
        if (offset >= arr.size) {
            value = null
            return
        }

        val len = arr.size - offset
        value = ByteArray(len)
        System.arraycopy(arr, offset, value, 0, len)
    }

    /**
     * Because this is usually binary data and could be very long we just return
     * the number of bytes held
     *
     * @return the number of bytes
     */
    override fun toString(): String {
        return getSize().toString() + " bytes"
    }

    /**
     * Return the size in byte of this datatype
     *
     * @return the size in bytes
     */
    override fun getSize(): Int {
        var len = 0

        if (value != null) {
            len = (value as ByteArray).size
        }

        return len
    }

    /**
     * Write contents to a byte array
     *
     * @return a byte array that that contians the data that should be perisisted to file
     */
    override fun writeByteArray(): ByteArray {
        log.debug("Writing byte array" + identifier)
        return value as ByteArray
    }
}