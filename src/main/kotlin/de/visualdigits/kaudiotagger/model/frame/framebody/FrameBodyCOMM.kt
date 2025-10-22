package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.AbstractString
import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.datatype.StringHashMap
import de.visualdigits.kaudiotagger.model.datatype.TextEncodedStringNullTerminated
import de.visualdigits.kaudiotagger.model.datatype.TextEncodedStringSizeTerminated
import de.visualdigits.kaudiotagger.model.datatype.types.Languages
import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.ID3TextEncodingConversion
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class FrameBodyCOMM: AbstractID3v2FrameBody, ID3v23FrameBody, ID3v24FrameBody {
    
    companion object {

        //Most players only read comment with description of blank
        const val DEFAULT: String = ""

        //used by iTunes for volume normalization, although uses the COMMENT field not usually displayed as a comment
        const val ITUNES_NORMALIZATION: String = "iTunNORM"
        const val MM_CUSTOM1: String = "Songs-DB_Custom1"
        const val MM_CUSTOM2: String = "Songs-DB_Custom2"
        const val MM_CUSTOM3: String = "Songs-DB_Custom3"
        const val MM_CUSTOM4: String = "Songs-DB_Custom4"
        const val MM_CUSTOM5: String = "Songs-DB_Custom5"
        const val MM_OCCASION: String = "Songs-DB_Occasion"
        const val MM_QUALITY: String = "Songs-DB_Preference"
        const val MM_TEMPO: String = "Songs-DB_Tempo"
        //Various descriptions used by MediaMonkey, (note Media Monkey uses non-standard language field XXX)
        const val MM_PREFIX: String = "Songs-DB"
    }

    /**
     * Creates a new FrameBodyCOMM datatype.
     */
    constructor() {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        setObjectValue(DataTypes.OBJ_LANGUAGE, Languages.DEFAULT_ID)
        setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
        setObjectValue(DataTypes.OBJ_TEXT, "")
    }

    constructor(body: FrameBodyCOMM): super(body)

    /**
     * Creates a new FrameBodyCOMM datatype.
     *
     * @param textEncoding
     * @param language
     * @param description
     * @param text
     */
    constructor(
        textEncoding: Byte,
        language: String,
        description: String,
        text: String
    ) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        setObjectValue(DataTypes.OBJ_LANGUAGE, language)
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
        setObjectValue(DataTypes.OBJ_TEXT, text)
    }

    /**
     * Construct a Comment frame body from the buffer
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    constructor(byteBuffer: ByteBuffer, frameSize: Int): super(byteBuffer, frameSize)

    fun isMediaMonkeyFrame(): Boolean {
        val desc = getDescription()
        if (desc != null && desc.length != 0) {
            return desc.startsWith(MM_PREFIX)
        }
        return false
    }

    /**
     * Get the description field, which describes the type of comment
     *
     * @return description field
     */
    fun getDescription(): String? {
        return getObjectValue(DataTypes.OBJ_DESCRIPTION) as String?
    }

    /**
     * Set the description field, which describes the type of comment
     *
     * @param description
     */
    fun setDescription(description: String) {
        if (description == null) {
            throw IllegalArgumentException(
                ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg()
            )
        }
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
    }

    fun isItunesFrame(): Boolean {
        val desc = getDescription()
        if (desc != null && desc.length != 0) {
            return desc == ITUNES_NORMALIZATION
        }
        return false
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24KFrame.COMMENT.id
    }

    /**
     * Get the language the comment is written in
     *
     * @return the language
     */
    fun getLanguage(): String? {
        return getObjectValue(DataTypes.OBJ_LANGUAGE) as String?
    }

    /**
     * Sets the language the comment is written in
     *
     * @param language
     */
    fun setLanguage(language: String?) {
        //TODO not sure if this might break existing code
        /*if(language==null)
        {
             throw new IllegalArgumentException(ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg());
        } */
        setObjectValue(DataTypes.OBJ_LANGUAGE, language)
    }

    override fun getUserFriendlyValue(): String? {
        return getText()
    }

    /**
     * Returns the the text field which holds the comment, adjusted to ensure does not return trailing null
     * which is due to a iTunes bug.
     *
     * @return the text field
     */
    fun getText(): String? {
        val text =
            getObject(DataTypes.OBJ_TEXT) as TextEncodedStringSizeTerminated
        return text.getValueAtIndex(0)
    }

    /**
     * @param text
     */
    fun setText(text: String) {
        if (text == null) {
            throw IllegalArgumentException(
                ErrorMessage.GENERAL_INVALID_NULL_ARGUMENT.getMsg()
            )
        }
        setObjectValue(DataTypes.OBJ_TEXT, text)
    }

    /**
     *
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
            StringHashMap(
                DataTypes.OBJ_LANGUAGE,
                this,
                Languages.LANGUAGE_FIELD_SIZE
            )
        )
        objectList.add(
            TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this)
        )
        objectList.add(
            TextEncodedStringSizeTerminated(DataTypes.OBJ_TEXT, this)
        )
    }

    /**
     * Because COMM have a text encoding we need to check the text String does
     * not contain characters that cannot be encoded in current encoding before
     * we write data. If there are we change the encoding.
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        //Ensure valid for type
        setTextEncoding(
            ID3TextEncodingConversion.getTextEncoding(header, getTextEncoding())
        )

        //Ensure valid for data
        if (!(getObject(DataTypes.OBJ_TEXT) as AbstractString).canBeEncoded()) {
            this.setTextEncoding(
                ID3TextEncodingConversion.getUnicodeTextEncoding(header)
            )
        }
        if (!(getObject(DataTypes.OBJ_DESCRIPTION) as AbstractString).canBeEncoded()
        ) {
            this.setTextEncoding(
                ID3TextEncodingConversion.getUnicodeTextEncoding(header)
            )
        }
        super.write(tagBuffer!!)
    }

    /**
     * Retrieve the complete text String but without any trailing nulls
     *
     *
     * If multiple values are held these will be returned, needless trailing nulls will not be returned
     *
     * @return the text string
     */
    fun getTextWithoutTrailingNulls(): String {
        val text =
            getObject(DataTypes.OBJ_TEXT) as TextEncodedStringSizeTerminated
        return text.getValueWithoutTrailingNull()
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
}