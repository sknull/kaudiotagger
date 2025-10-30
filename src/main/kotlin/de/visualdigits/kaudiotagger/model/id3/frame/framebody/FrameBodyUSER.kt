package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.AbstractString
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.id3.datatype.StringHashMap
import de.visualdigits.kaudiotagger.model.id3.datatype.StringSizeTerminated
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.model.id3.types.Languages
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * Terms of use frame.
 *
 *
 *
 *
 * This frame contains a brief description of the terms of use and
 * ownership of the file. More detailed information concerning the legal
 * terms might be available through the "WCOP" frame. Newlines are
 * allowed in the text. There may only be one "USER" frame in a tag.
 *
 * <table border=0 width="70%">
 * <tr><td colspan=2>&lt;Header for 'Terms of use frame', ID: "USER"&gt;</td></tr>
 * <tr><td>Text encoding  </td><td>$xx</td></tr>
 * <tr><td>Language       </td><td>$xx xx xx</td></tr>
 * <tr><td>The actual text</td><td>&lt;text string according to encoding&gt;</td></tr>
</table> *
 *
 *
 * For more details, please refer to the ID3 specifications:
 *
 *  * [ID3 v2.3.0 Spec](http://www.id3.org/id3v2.3.0.txt)
 *
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyUSER: AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyUSER datatype.
     */
    constructor()

    constructor(body: FrameBodyUSER) : super(body)

    /**
     * Creates a new FrameBodyUSER datatype.
     *
     * @param textEncoding
     * @param language
     * @param text
     */
    constructor(textEncoding: Byte, language: String?, text: String?) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        setObjectValue(DataTypes.OBJ_LANGUAGE, language)
        setObjectValue(DataTypes.OBJ_TEXT, text)
    }

    /**
     * Create a new FrameBodyUser by reading from byte buffer
     *
     * @param byteBuffer
     * @param frameSize
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24FrameId.TERMS_OF_USE.id
    }

    /**
     * @return lanaguage
     */
    fun getLanguage(): String? {
        return getObjectValue(DataTypes.OBJ_LANGUAGE) as? String
    }

    /**
     * @param language
     */
    fun setOwner(language: String?) {
        setObjectValue(DataTypes.OBJ_LANGUAGE, language)
    }

    /**
     * If the text cannot be encoded using current encoder, change the encoder
     *
     * @param tagBuffer
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        if (!(getObject(DataTypes.OBJ_TEXT) as AbstractString).canBeEncoded()) {
            this.setTextEncoding(TextEncoding.UTF_16.id)
        }
        super.write(tagBuffer!!)
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
        objectList.add(StringSizeTerminated(DataTypes.OBJ_TEXT, this))
    }
}
