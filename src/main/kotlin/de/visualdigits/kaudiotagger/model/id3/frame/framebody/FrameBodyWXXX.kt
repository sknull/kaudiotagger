package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.AbstractString
import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.common.datatype.StringSizeTerminated
import de.visualdigits.kaudiotagger.model.common.datatype.TextEncodedStringNullTerminated
import de.visualdigits.kaudiotagger.model.common.datatype.TextEncodedStringSizeTerminated
import de.visualdigits.kaudiotagger.model.common.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * Represents a user defined url
 */
class FrameBodyWXXX: AbstractFrameBodyUrlLink, ID3v24FrameBody, ID3v23FrameBody {

    companion object {
        const val URL_DISCOGS_RELEASE_SITE: String = "DISCOGS_RELEASE"
        const val URL_WIKIPEDIA_RELEASE_SITE: String = "WIKIPEDIA_RELEASE"
        const val URL_OFFICIAL_RELEASE_SITE: String = "OFFICIAL_RELEASE"
        const val URL_DISCOGS_ARTIST_SITE: String = "DISCOGS_ARTIST"
        const val URL_WIKIPEDIA_ARTIST_SITE: String = "WIKIPEDIA_ARTIST"
        const val URL_LYRICS_SITE: String = "LYRICS_SITE"
    }

    /**
     * Creates a new FrameBodyWXXX datatype.
     */
    constructor() {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
        this.setObjectValue(DataTypes.OBJ_URLLINK, "")
    }

    constructor(body: FrameBodyWXXX) : super(body)

    /**
     * Creates a new FrameBodyWXXX datatype.
     *
     * @param textEncoding
     * @param description
     * @param urlLink
     */
    constructor(textEncoding: Byte, description: String, urlLink: String) {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
        this.setObjectValue(DataTypes.OBJ_URLLINK, urlLink)
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * @return a description of the hyperlink
     */
    fun getDescription(): String? {
        return getObjectValue(DataTypes.OBJ_DESCRIPTION) as String?
    }

    /**
     * Set a description of the hyperlink
     *
     * @param description
     */
    fun setDescription(description: String?) {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.USER_DEFINED_URL.id
    }

    /**
     * If the description cannot be encoded using the current encoding change the encoder
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        if (!(getObject(DataTypes.OBJ_DESCRIPTION) as AbstractString).canBeEncoded()
        ) {
            this.setTextEncoding(TextEncoding.UTF_16.id)
        }
        super.write(tagBuffer)
    }

    /**
     * This is different ot other URL Links
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
            TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this)
        )
        objectList.add(StringSizeTerminated(DataTypes.OBJ_URLLINK, this))
    }

    /**
     * Retrieve the complete text String but without any trailing nulls
     *
     *
     * If multiple values are held these will be returned, needless trailing nulls will not be returned
     *
     * @return the text string
     */
    fun getUrlLinkWithoutTrailingNulls(): String {
        val text =
            getObject(DataTypes.OBJ_URLLINK) as TextEncodedStringSizeTerminated
        return text.getValueWithoutTrailingNull()
    }

    /**
     * Get first value
     *
     * @return value at index 0
     */
    fun getFirstUrlLink(): String? {
        val text =
            getObject(DataTypes.OBJ_URLLINK) as TextEncodedStringSizeTerminated
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
    fun getUrlLinkAtIndex(index: Int): String? {
        val text =
            getObject(DataTypes.OBJ_URLLINK) as TextEncodedStringSizeTerminated
        return text.getValueAtIndex(index)
    }

    fun getUrlLinks(): MutableList<String> {
        val text =
            getObject(DataTypes.OBJ_URLLINK) as TextEncodedStringSizeTerminated
        return text.getValues()
    }

    /**
     * Add additional value to value
     *
     * @param value at index
     */
    fun addUrlLink(value: String) {
        val text = getObject(DataTypes.OBJ_URLLINK) as TextEncodedStringSizeTerminated
        text.addValue(value)
    }
}
