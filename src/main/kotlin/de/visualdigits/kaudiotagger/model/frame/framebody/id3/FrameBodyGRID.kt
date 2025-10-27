package de.visualdigits.kaudiotagger.model.frame.framebody.id3

import de.visualdigits.kaudiotagger.model.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.NumberFixedLength
import de.visualdigits.kaudiotagger.model.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import java.nio.ByteBuffer

/**
 * Group identification registration frame.
 *
 *
 *
 *
 * This frame enables grouping of otherwise unrelated frames. This can
 * be used when some frames are to be signed. To identify which frames
 * belongs to a set of frames a group identifier must be registered in
 * the tag with this frame. The 'Owner identifier' is a null-terminated
 * string with a URL containing an email address, or a link to a
 * location where an email address can be found, that belongs to the
 * organisation responsible for this grouping. Questions regarding the
 * grouping should be sent to the indicated email address. The 'Group
 * symbol' contains a value that associates the frame with this group
 * throughout the whole tag. Values below $80 are reserved. The 'Group
 * symbol' may optionally be followed by some group specific data, e.g.
 * a digital signature. There may be several "GRID" frames in a tag but
 * only one containing the same symbol and only one containing the same
 * owner identifier. The group symbol must be used somewhere in the tag.
 * See section 3.3.1, flag j for more information.
 *
 * <table border=0 width="70%">
 * <tr><td colspan=2>&lt;Header for 'Group ID registration', ID: "GRID"&gt;</td></tr>
 * <tr><td>Owner identifier     </td><td>&lt;text string&gt; $00</td></tr>
 * <tr><td>Group symbol         </td><td width="80%">$xx        </td></tr>
 * <tr><td>Group dependent data </td><td>&lt;binary data&gt;    </td></tr>
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
class FrameBodyGRID

    : AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyGRID datatype.
     */
    constructor()

    constructor(body: FrameBodyGRID) : super(body)

    /**
     * Creates a new FrameBodyGRID datatype.
     *
     * @param owner
     * @param groupSymbol
     * @param data
     */
    constructor(owner: String?, groupSymbol: Byte, data: ByteArray?) {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner)
        this.setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, groupSymbol)
        this.setObjectValue(DataTypes.OBJ_GROUP_DATA, data)
    }

    /**
     * Creates a new FrameBodyGRID datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    var groupSymbol: Byte
        /**
         * @return
         */
        get() {
            if (getObjectValue(DataTypes.OBJ_GROUP_SYMBOL) != null) {
                return (getObjectValue(DataTypes.OBJ_GROUP_SYMBOL) as Long).toByte()
            } else {
                return 0.toByte()
            }
        }
        /**
         * @param textEncoding
         */
        set(textEncoding) {
            setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, textEncoding)
        }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.GROUP_ID_REG.id
    }

    var owner: String?
        /**
         * @return
         */
        get() = getObjectValue(DataTypes.OBJ_OWNER) as String?
        /**
         * @param owner
         */
        set(owner) {
            setObjectValue(DataTypes.OBJ_OWNER, owner)
        }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringNullTerminated(DataTypes.OBJ_OWNER, this))
        objectList.add(NumberFixedLength(DataTypes.OBJ_GROUP_SYMBOL, this, 1))
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_GROUP_DATA, this))
    }
}
