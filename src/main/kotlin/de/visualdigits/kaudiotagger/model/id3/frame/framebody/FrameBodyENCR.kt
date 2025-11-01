package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberFixedLength
import de.visualdigits.kaudiotagger.model.id3.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import java.nio.ByteBuffer

/**
 * Encryption method registration frame.
 *
 *
 *
 *
 * To identify with which method a frame has been encrypted the
 * encryption method must be registered in the tag with this frame. The
 * 'Owner identifier' is a null-terminated string with a URL
 * containing an email address, or a link to a location where an email
 * address can be found, that belongs to the organisation responsible
 * for this specific encryption method. Questions regarding the
 * encryption method should be sent to the indicated email address. The
 * 'Method symbol' contains a value that is associated with this method
 * throughout the whole tag. Values below $80 are reserved. The 'Method
 * symbol' may optionally be followed by encryption specific data. There
 * may be several "ENCR" frames in a tag but only one containing the
 * same symbol and only one containing the same owner identifier. The
 * method must be used somewhere in the tag. See section 3.3.1, flag j
 * for more information.
 *
 * <table border=0 width="70%">
 * <tr><td colspan=2>&lt;Header for 'Encryption method registration', ID: "ENCR"&gt;</td></tr>
 * <tr><td>Owner identifier</td><td width="80%">&lt;text string&gt; $00</td></tr>
 * <tr><td>Method symbol   </td><td>$xx                           </td></tr>
 * <tr><td>Encryption data </td><td>&lt;binary data&gt;           </td></tr>
</table> *
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
class FrameBodyENCR

    : AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyENCR datatype.
     */
    constructor() {
        this.setObjectValue(DataTypes.OBJ_OWNER, "")
        this.setObjectValue(DataTypes.OBJ_METHOD_SYMBOL, 0.toByte())
        this.setObjectValue(DataTypes.OBJ_ENCRYPTION_INFO, ByteArray(0))
    }

    constructor(body: FrameBodyENCR) : super(body)

    /**
     * Creates a new FrameBodyENCR datatype.
     *
     * @param owner
     * @param methodSymbol
     * @param data
     */
    constructor(owner: String?, methodSymbol: Byte, data: ByteArray?) {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner)
        this.setObjectValue(DataTypes.OBJ_METHOD_SYMBOL, methodSymbol)
        this.setObjectValue(DataTypes.OBJ_ENCRYPTION_INFO, data)
    }

    /**
     * Creates a new FrameBodyENCR datatype.
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
        return ID3v24FrameId.ENCRYPTION.id
    }

    fun getOwner(): String? {
        return getObjectValue(DataTypes.OBJ_OWNER) as? String
    }

    fun setOwner(owner: String?) {
        setObjectValue(DataTypes.OBJ_OWNER, owner)
    }

    override fun setupObjectList() {
        objectList.add(StringNullTerminated(DataTypes.OBJ_OWNER, this))
        objectList.add(NumberFixedLength(DataTypes.OBJ_METHOD_SYMBOL, this, 1))
        objectList.add(
            ByteArraySizeTerminated(DataTypes.OBJ_ENCRYPTION_INFO, this)
        )
    }
}
