package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody

/**
 * Represents a bit flag within a byte
 */
class BooleanByte : AbstractDataType {

    var bitPosition: Int = -1

    /**
     * Creates a new ObjectBooleanByte datatype.
     *
     * @param identifier
     * @param frameBody
     * @param bitPosition
     */
    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody?,
        bitPosition: Int
    ) : super(identifier, frameBody) {
        if ((bitPosition < 0) || (bitPosition > 7)) {
            throw IndexOutOfBoundsException("Bit position needs to be from 0 - 7 : $bitPosition")
        }

        this.bitPosition = bitPosition
    }

    constructor(copyObject: BooleanByte): super(copyObject) {
        this.bitPosition = copyObject.bitPosition
    }

    /**
     * @return
     */
    override fun getSize(): Int {
        return 1
    }

    /**
     * @param obj
     * @return
     */
    override fun equals(obj: Any?): Boolean {
        if (obj !is BooleanByte) {
            return false
        }

        return this.bitPosition == obj.bitPosition && super.equals(obj)
    }

    /**
     * @param arr
     * @param offset
     */
    override fun readByteArray(arr: ByteArray, offset: Int) {
        if ((offset < 0) || (offset >= arr.size)) {
            throw IndexOutOfBoundsException(
                "Offset to byte array is out of bounds: offset = " +
                        offset +
                        ", array.length = " +
                        arr.size
            )
        }

        var newValue = arr[offset]

        newValue = (newValue.toInt() shr bitPosition).toByte()
        newValue = (newValue.toInt() and 0x1).toByte()
        setValue(newValue.toInt() == 1)
    }

    /**
     * @return
     */
    override fun toString(): String {
        return getValue().toString()
    }

    /**
     * @return
     */
    override fun writeByteArray(): ByteArray? {
        val retValue = ByteArray(1)
        if (getValue() != null) {
            retValue[0] = (if (getValue() as Boolean) 1 else 0).toByte()
            retValue[0] = (retValue[0].toInt() shl bitPosition).toByte()
        }

        return retValue
    }
}