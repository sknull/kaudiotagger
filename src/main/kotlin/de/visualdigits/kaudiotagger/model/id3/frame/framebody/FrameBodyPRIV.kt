package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.types.ID3V24FrameId
import java.nio.ByteBuffer

/**
 * Private frame.
 *
 *
 *
 *
 * This frame is used to contain information from a software producer
 * that its program uses and does not fit into the other frames. The
 * frame consists of an 'Owner identifier' string and the binary data.
 * The 'Owner identifier' is a null-terminated string with a URL
 * containing an email address, or a link to a location where an email
 * address can be found, that belongs to the organisation responsible
 * for the frame. Questions regarding the frame should be sent to the
 * indicated email address. The tag may contain more than one "PRIV"
 * frame but only with different contents. It is recommended to keep the
 * number of "PRIV" frames as low as possible.
 *
 *
 * Header for 'Private frame'
 * Owner identifier
 * The private data
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
class FrameBodyPRIV: AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyPRIV datatype.
     */
    constructor() {
        this.setObjectValue(DataTypes.OBJ_OWNER, "")
        this.setObjectValue(DataTypes.OBJ_DATA, ByteArray(0))
    }

    constructor(body: FrameBodyPRIV) : super(body)

    /**
     * Creates a new FrameBodyPRIV datatype.
     *
     * @param owner
     * @param data
     */
    constructor(owner: String?, data: ByteArray?) {
        this.setObjectValue(DataTypes.OBJ_OWNER, owner)
        this.setObjectValue(DataTypes.OBJ_DATA, data)
    }

    /**
     * Creates a new FrameBodyPRIV datatype.
     *
     * @param byteBuffer
     * @param frameSize
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * @return
     */
    fun getData(): ByteArray? {
        return getObjectValue(DataTypes.OBJ_DATA) as ByteArray?
    }

    /**
     * @param data
     */
    fun setData(data: ByteArray?) {
        setObjectValue(DataTypes.OBJ_DATA, data)
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3V24FrameId.PRIVATE.id
    }

    /**
     * @return
     */
    fun getOwner(): String? {
        return getObjectValue(DataTypes.OBJ_OWNER) as String?
    }

    /**
     * @param owner
     */
    fun setOwner(owner: String?) {
        setObjectValue(DataTypes.OBJ_OWNER, owner)
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(StringNullTerminated(DataTypes.OBJ_OWNER, this))
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }
}
