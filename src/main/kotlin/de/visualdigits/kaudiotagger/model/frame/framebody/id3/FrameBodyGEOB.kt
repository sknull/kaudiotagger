package de.visualdigits.kaudiotagger.model.frame.framebody.id3

import de.visualdigits.kaudiotagger.model.datatype.AbstractString
import de.visualdigits.kaudiotagger.model.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.datatype.TextEncodedStringNullTerminated
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * General encapsulated object frame.
 *
 *
 *
 *
 * In this frame any type of file can be encapsulated. After the header,
 * 'Frame size' and 'Encoding' follows 'MIME type' represented as
 * as a terminated string encoded with ISO-8859-1. The
 * filename is case sensitive and is encoded as 'Encoding'. Then follows
 * a content description as terminated string, encoded as 'Encoding'.
 * The last thing in the frame is the actual object. The first two
 * strings may be omitted, leaving only their terminations. There may be more than one "GEOB"
 * frame in each tag, but only one with the same content descriptor.
 *
 * <table border=0 width="70%">
 * <tr><td colspan=2> &lt;Header for 'General encapsulated object', ID: "GEOB"&gt;</td></tr>
 * <tr><td>Text encoding       </td><td>$xx                     </td></tr>
 * <tr><td>MIME type           </td><td>&lt;text string&gt; $00 </td></tr>
 * <tr><td>Filename            </td><td>&lt;text string according to encoding&gt; $00 (00)</td></tr>
 * <tr><td>Content description </td><td><text string according to encoding> $00 (00)</text></td></tr>
 * <tr><td>Encapsulated object </td><td>&lt;binary data&gt;     </td></tr>
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
class FrameBodyGEOB: AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyGEOB datatype.
     */
    constructor() {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        this.setObjectValue(DataTypes.OBJ_MIME_TYPE, "")
        this.setObjectValue(DataTypes.OBJ_FILENAME, "")
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
        this.setObjectValue(DataTypes.OBJ_DATA, ByteArray(0))
    }

    constructor(body: FrameBodyGEOB) : super(body)

    /**
     * Creates a new FrameBodyGEOB datatype.
     *
     * @param textEncoding
     * @param mimeType
     * @param filename
     * @param description
     * @param `object`
     */
    constructor(
        textEncoding: Byte,
        mimeType: String?,
        filename: String?,
        description: String?,
        byteArray: ByteArray?
    ) {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        this.setObjectValue(DataTypes.OBJ_MIME_TYPE, mimeType)
        this.setObjectValue(DataTypes.OBJ_FILENAME, filename)
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
        this.setObjectValue(DataTypes.OBJ_DATA, byteArray)
    }

    /**
     * Creates a new FrameBodyGEOB datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    var description: String?
        /**
         * @return the description field
         */
        get() = getObjectValue(DataTypes.OBJ_DESCRIPTION) as String?
        /**
         * @param description
         */
        set(description) {
            setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
        }

    /**
     * @return
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.GENERAL_ENCAPS_OBJECT.id
    }

    /**
     * If the filename or description cannot be encoded using current encoder, change the encoder
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        if (!(getObject(DataTypes.OBJ_FILENAME) as AbstractString).canBeEncoded()) {
            this.setTextEncoding(TextEncoding.UTF_16.id)
        }
        if (!(getObject(DataTypes.OBJ_DESCRIPTION) as AbstractString).canBeEncoded()
        ) {
            this.setTextEncoding(TextEncoding.UTF_16.id)
        }
        super.write(tagBuffer)
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
        objectList.add(StringNullTerminated(DataTypes.OBJ_MIME_TYPE, this))
        objectList.add(
            TextEncodedStringNullTerminated(DataTypes.OBJ_FILENAME, this)
        )
        objectList.add(
            TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this)
        )
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }
}
