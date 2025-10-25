package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.datatype.PartOfSet
import de.visualdigits.kaudiotagger.model.datatype.PartOfSetValue
import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import java.nio.ByteBuffer

abstract class AbstractFrameBodyNumberTotal: AbstractID3v2FrameBody {

    /**
     * Creates a new FrameBodyTRCK datatype.
     */
    constructor() {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        setObjectValue(DataTypes.OBJ_TEXT, PartOfSetValue())
    }

    constructor(body: AbstractFrameBodyNumberTotal): super(body)

    /**
     * Creates a new FrameBodyTRCK datatype, the value is parsed literally
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        setObjectValue(DataTypes.OBJ_TEXT, PartOfSetValue(text))
    }

    constructor(
        textEncoding: Byte,
        trackNo: Int,
        trackTotal: Int
    ) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        setObjectValue(
            DataTypes.OBJ_TEXT,
            PartOfSetValue(trackNo, trackTotal)
        )
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    constructor(
        identifier: String? = null,
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(identifier, byteBuffer, frameSize)

    override fun getUserFriendlyValue(): String? {
        val value = getObjectValue(
            DataTypes.OBJ_TEXT
        ) as PartOfSetValue
        return value.count.toString()
    }

    fun getText(): String {
        return getObjectValue(DataTypes.OBJ_TEXT).toString()
    }

    open fun setText(text: String) {
        setObjectValue(DataTypes.OBJ_TEXT, PartOfSetValue(text))
    }

    fun getNumber(): Int {
        val value: PartOfSetValue = getObjectValue(
            DataTypes.OBJ_TEXT
        ) as PartOfSetValue
        return value.count
    }

    fun setNumber(trackNo: Int) {
        (getObjectValue(DataTypes.OBJ_TEXT) as PartOfSetValue).count = trackNo
    }

    fun setNumber(trackNo: String) {
        (getObjectValue(DataTypes.OBJ_TEXT) as PartOfSetValue).count = trackNo.toInt()
    }

    fun getNumberAsText(): String? {
        return (getObjectValue(DataTypes.OBJ_TEXT) as PartOfSetValue
                ).getCountAsText()
    }

    fun getTotal(): Int {
        return (getObjectValue(DataTypes.OBJ_TEXT) as PartOfSetValue
                ).total
    }

    fun setTotal(trackTotal: Int) {
        (getObjectValue(DataTypes.OBJ_TEXT) as PartOfSetValue).total = trackTotal
    }

    fun setTotal(trackTotal: String) {
        (getObjectValue(DataTypes.OBJ_TEXT) as PartOfSetValue).total = trackTotal.toInt()
    }

    fun getTotalAsText(): String {
        return (getObjectValue(DataTypes.OBJ_TEXT) as PartOfSetValue
                ).getTotalAsText()
    }

    override fun setupObjectList() {
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_TEXT_ENCODING,
                this,
                TextEncoding.TEXT_ENCODING_FIELD_SIZE
            )
        )
        objectList.add(PartOfSet(DataTypes.OBJ_TEXT, this))
    }
}