package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody


class BooleanString : AbstractDataType {

    /**
     * Creates a new ObjectBooleanString datatype.
     *
     * @param identifier
     * @param frameBody
     */
    constructor(identifier: String?, frameBody: AbstractTagFrameBody) : super(identifier, frameBody)

    constructor(copyObject: BooleanString) : super(copyObject)

    override fun getSize(): Int {
        return 1
    }

    override fun readByteArray(byteArray: ByteArray, offset: Int) {
        val b = byteArray[offset]
        setValue(b != '0'.code.toByte())
    }

    override fun toString(): String {
        return getValue()?.toString()?:""
    }

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
