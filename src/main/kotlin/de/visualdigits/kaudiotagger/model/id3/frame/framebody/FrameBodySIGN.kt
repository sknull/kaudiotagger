package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberFixedLength
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import java.nio.ByteBuffer

class FrameBodySIGN: AbstractID3v2FrameBody, ID3v24FrameBody {
    
    /**
     * Creates a new FrameBodySIGN datatype.
     */
    constructor()

    constructor(body: FrameBodySIGN) : super(body)

    /**
     * Creates a new FrameBodySIGN datatype.
     *
     * @param groupSymbol
     * @param signature
     */
    constructor(groupSymbol: Byte, signature: ByteArray?) {
        this.setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, groupSymbol)
        this.setObjectValue(DataTypes.OBJ_SIGNATURE, signature)
    }

    /**
     * Creates a new FrameBodySIGN datatype.
     *
     * @param byteBuffer
     * @param frameSize
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    fun getGroupSymbol(): Byte {
        return if (getObjectValue(DataTypes.OBJ_GROUP_SYMBOL) != null) {
            (getObjectValue(DataTypes.OBJ_GROUP_SYMBOL) as? Byte)?:0.toByte()
        } else {
            0.toByte()
        }
    }

    /**
     * @param groupSymbol
     */
    fun setGroupSymbol(groupSymbol: Byte) {
        setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, groupSymbol)
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24FrameId.SIGNATURE.id
    }

    fun getSignature(): ByteArray? {
        return getObjectValue(DataTypes.OBJ_SIGNATURE) as? ByteArray
    }

    /**
     * @param signature
     */
    fun setSignature(signature: ByteArray?) {
        setObjectValue(DataTypes.OBJ_SIGNATURE, signature)
    }

    override fun setupObjectList() {
        objectList.add(NumberFixedLength(DataTypes.OBJ_GROUP_SYMBOL, this, 1))
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_SIGNATURE, this))
    }
}
