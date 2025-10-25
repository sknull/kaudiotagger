package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody


class BooleanString : AbstractDataType {
    /**
     * Creates a new ObjectBooleanString datatype.
     *
     * @param identifier
     * @param frameBody
     */
    constructor(identifier: String, frameBody: AbstractTagFrameBody) : super(identifier, frameBody)

    constructor(copyObject: BooleanString) : super(copyObject)

    /**
     * @return
     */
    override fun getSize(): Int {
        return 1
    }

    override fun equals(obj: Any?): Boolean {
        return obj is BooleanString && super.equals(obj)
    }

    /**
     * @param offset
     * @throws NullPointerException
     * @throws IndexOutOfBoundsException
     */
    override fun readByteArray(arr: ByteArray, offset: Int) {
        val b = arr[offset]
        setValue(b != '0'.code.toByte())
    }

    /**
     * @return
     */
    override fun toString(): String {
        return getValue()?.toString()?:""
    }

    /**
     * @return
     */
    override fun writeByteArray(): ByteArray {
        val booleanValue = ByteArray(1)
        if (getValue() == null) {
            booleanValue[0] = '0'.code.toByte()
        } else {
            if (getValue() as Boolean) {
                booleanValue[0] = '0'.code.toByte()
            } else {
                booleanValue[0] = '1'.code.toByte()
            }
        }
        return booleanValue
    }
}
