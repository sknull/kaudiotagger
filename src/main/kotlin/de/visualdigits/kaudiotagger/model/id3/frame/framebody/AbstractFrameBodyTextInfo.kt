package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.id3.datatype.TextEncodedStringSizeTerminated
import de.visualdigits.kaudiotagger.util.ID3TextEncodingConversion
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

abstract class AbstractFrameBodyTextInfo: AbstractID3v2FrameBody {

    /**
     * Creates a new FrameBodyTextInformation datatype. The super.super
     * Constructor sets up the Object list for the frame.
     */
    constructor() {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1.id)
        setObjectValue(DataTypes.OBJ_TEXT, "")
    }

    /**
     * Copy Constructor
     *
     * @param body AbstractFrameBodyTextInformation
     */
    constructor(copyObject: AbstractFrameBodyTextInfo): super(copyObject)

    /**
     * Creates a new FrameBodyTextInformation data type. This is used when user
     * wants to create a new frame based on data in a user interface.
     *
     * @param textEncoding Specifies what encoding should be used to write
     * text to file.
     * @param text         Specifies the text String.
     */
    constructor(textEncoding: Byte, text: String?) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        setObjectValue(DataTypes.OBJ_TEXT, text)
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    override fun getUserFriendlyValue(): String? {
        return getTextWithoutTrailingNulls()
    }

    /**
     * Retrieve the complete text String but without any trailing nulls
     *
     *
     * If multiple values are held these will be returned, needless trailing nulls will not be returned
     *
     * @return the text string
     */
    fun getTextWithoutTrailingNulls(): String? {
        val text = getObject(DataTypes.OBJ_TEXT) as? TextEncodedStringSizeTerminated
        return text?.getValueWithoutTrailingNull()
    }

    /**
     * Retrieve the complete text String as it is held internally.
     *
     *
     * If multiple values are held these will be returned, needless trailing nulls will also be returned
     *
     * @return the text string
     */
    fun getText(): String? {
        return (getObjectValue(DataTypes.OBJ_TEXT) as? String)
    }

    /**
     * Set the Full Text String.
     *
     * <p>If this String contains null terminator characters these are parsed as value
     * separators, allowing you to hold multiple strings within one text frame. This functionality is only
     * officially support in ID3v24.
     *
     * @param text to set
     */
    fun setText(text: String?) {
        setObjectValue(DataTypes.OBJ_TEXT, text)
    }

    /**
     * Get first value
     *
     * @return value at index 0
     */
    fun getFirstTextValue(): String? {
        val text =
            getObject(DataTypes.OBJ_TEXT) as TextEncodedStringSizeTerminated
        return text.getValueAtIndex(0)
    }

    /**
     * Get text value at index
     *
     *
     * When a multiple values are stored within a single text frame this method allows access to any of the
     * individual values.
     *
     * @param index
     * @return value at index
     */
    fun getValueAtIndex(index: Int): String? {
        val text =
            getObject(DataTypes.OBJ_TEXT) as TextEncodedStringSizeTerminated
        return text.getValueAtIndex(index)
    }

    fun getValues(): MutableList<String> {
        val text =
            getObject(DataTypes.OBJ_TEXT) as TextEncodedStringSizeTerminated
        return text.getValues()
    }

    /**
     * Add additional value to value
     *
     * @param value at index
     */
    fun addTextValue(value: String) {
        val text =
            getObject(DataTypes.OBJ_TEXT) as TextEncodedStringSizeTerminated
        text.addValue(value)
    }

    /**
     * @return number of text values, usually one
     */
    fun getNumberOfValues(): Int {
        val text =
            getObject(DataTypes.OBJ_TEXT) as TextEncodedStringSizeTerminated
        return text.getNumberOfValues()
    }

    /**
     * Because Text frames have a text encoding we need to check the text
     * String does not contain characters that cannot be encoded in
     * current encoding before we write data. If there are change the text
     * encoding.
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        // Ensure valid for type
        setTextEncoding(
            ID3TextEncodingConversion.getTextEncoding(header, getTextEncoding())
        )

        // Ensure valid for data
        val terminated = getObject(DataTypes.OBJ_TEXT) as? TextEncodedStringSizeTerminated
        val bool = terminated?.canBeEncoded() == false
        if (bool) {
            this.setTextEncoding(ID3TextEncodingConversion.getUnicodeTextEncoding(header))
        }
        super.write(tagBuffer)
    }

    /**
     * Setup the Object List. All text frames contain a text encoding
     * and then a text string.
     *
     *
     * TODO:would like to make final but cannot because overridden by FrameBodyTXXX
     */
    override fun setupObjectList() {
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_TEXT_ENCODING,
                this,
                TextEncoding.TEXT_ENCODING_FIELD_SIZE
            )
        )
        objectList.add(
            TextEncodedStringSizeTerminated(
                DataTypes.OBJ_TEXT,
                this
            )
        )
    }
}