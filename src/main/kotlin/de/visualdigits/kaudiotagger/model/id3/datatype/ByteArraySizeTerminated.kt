package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody

class ByteArraySizeTerminated: TextEncodedStringSizeTerminated  {

    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody
    ) : super(identifier, frameBody)

    constructor(copyObject: ByteArraySizeTerminated) : super(copyObject)

    override fun equals(obj: Any?): Boolean {
        return obj is ByteArraySizeTerminated && super.equals(obj)
    }

    /**
     * @param arr
     * @param offset
     */
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
            setValue(null)
            return
        }

        val len = arr.size - offset
        val value = ByteArray(len)
        System.arraycopy(arr, offset, value, 0, len)
        setValue(value)
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
    override fun getSize(): Int = getValue()?.let { v -> (v as? ByteArray)?.size}?:0

    /**
     * Write contents to a byte array
     *
     * @return a byte array that that contians the data that should be perisisted to file
     */
    override fun writeByteArray(): ByteArray? {
        log.debug("Writing byte array$identifier")
        return getValue() as? ByteArray
    }
}