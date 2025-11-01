package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.datatype.AbstractString
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.id3.datatype.StringHashMap
import de.visualdigits.kaudiotagger.model.id3.datatype.TextEncodedStringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.TextEncodedStringSizeTerminated
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.model.id3.types.Languages
import de.visualdigits.kaudiotagger.model.lyrics3.datatype.Lyrics3Line
import de.visualdigits.kaudiotagger.util.ID3TextEncodingConversion
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * Unsychronised lyrics3/text transcription frame.
 *
 *
 *
 *
 * This frame contains the lyrics3 of the song or a text transcription of other vocal activities. The head includes an
 * encoding descriptor and a content descriptor. The body consists of the actual text. The 'Content descriptor' is a
 * terminated string. If no descriptor is entered, 'Content descriptor' is $00 (00) only. Newline characters are
 * allowed in the text. There may be more than one 'Unsynchronised lyrics3/text transcription' frame in each tag, but
 * only one with the same language and content descriptor.
 *
 *
 * <table border=0 width="70%">
 * <tr><td colspan=2>&lt;Header for 'Unsynchronised lyrics3/text transcription', ID: "USLT"&gt;</td></tr>
 * <tr><td>Text encoding     </td><td width="80%">$xx</td></tr>
 * <tr><td>Language          </td><td>$xx xx xx</td></tr>
 * <tr><td>Content descriptor</td><td>&lt;text string according to encoding&gt; $00 (00)</td></tr>
 * <tr><td>Lyrics/text       </td><td>&lt;full text string according to encoding&gt;</td></tr>
</table> *
 *
 *
 * You can retrieve the first value without the null terminator using [.getFirstTextValue]
 *
 *
 * For more details, please refer to the ID3 specifications:
 *
 *  * [ID3 v2.3.0 Spec](http:// www.id3.org/id3v2.3.0.txt)
 *
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyUSLT: AbstractID3v2FrameBody, ID3v23FrameBody, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyUSLT dataType.
     */
    constructor() {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1.id)
        setObjectValue(DataTypes.OBJ_LANGUAGE, "")
        setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
        setObjectValue(DataTypes.OBJ_LYRICS, "")
    }

    /**
     * Copy constructor
     *
     * @param body
     */
    constructor(body: FrameBodyUSLT) : super(body)

    /**
     * Creates a new FrameBodyUSLT datatype.
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
        setObjectValue(DataTypes.OBJ_LYRICS, text)
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    override fun getUserFriendlyValue(): String? {
        return getFirstTextValue()
    }

    /**
     * Get first value
     *
     * @return value at index 0
     */
    fun getFirstTextValue(): String? {
        val text = getObject(DataTypes.OBJ_LYRICS) as TextEncodedStringSizeTerminated
        return text.getValueAtIndex(0)
    }

    /**
     * Get a description field
     *
     * @return description
     */
    fun getDescription(): String? {
        return getObjectValue(DataTypes.OBJ_DESCRIPTION) as? String
    }

    /**
     * Set a description field
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
        return ID3v24FrameId.UNSYNC_LYRICS.id
    }

    /**
     * Get the language field
     *
     * @return language
     */
    fun getLanguage(): String? {
        return getObjectValue(DataTypes.OBJ_LANGUAGE) as? String
    }

    /**
     * Set the language field
     *
     * @param language
     */
    fun setLanguage(language: String?) {
        setObjectValue(DataTypes.OBJ_LANGUAGE, language)
    }

    /**
     * Add additional lyric to the lyric field
     *
     * @param text
     */
    fun addLyric(text: String?) {
        this.setLyric(this.getLyric() + text)
    }

    /**
     * Get the lyric field
     *
     * @return lyrics
     */
    fun getLyric(): String? {
        return getObjectValue(DataTypes.OBJ_LYRICS) as? String
    }

    /**
     * Set the lyric field
     *
     * @param lyric
     */
    fun setLyric(lyric: String?) {
        setObjectValue(DataTypes.OBJ_LYRICS, lyric)
    }

    fun addLyric(line: Lyrics3Line) {
        this.setLyric(this.getLyric() + line.writeString())
    }

    override fun write(tagBuffer: ByteArrayOutputStream) {
        // Ensure valid for type
        this.setTextEncoding(ID3TextEncodingConversion.getTextEncoding(header, getTextEncoding()))

        // Ensure valid for data
        if (!(getObject(DataTypes.OBJ_DESCRIPTION) as AbstractString).canBeEncoded()
        ) {
            this.setTextEncoding(ID3TextEncodingConversion.getUnicodeTextEncoding(header))
        }
        if (!(getObject(DataTypes.OBJ_LYRICS) as AbstractString).canBeEncoded()) {
            this.setTextEncoding(ID3TextEncodingConversion.getUnicodeTextEncoding(header))
        }
        super.write(tagBuffer)
    }

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
            TextEncodedStringSizeTerminated(DataTypes.OBJ_LYRICS, this)
        )
    }
}
