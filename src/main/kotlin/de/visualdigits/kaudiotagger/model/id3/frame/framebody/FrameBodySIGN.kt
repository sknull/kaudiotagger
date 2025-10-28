package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.datatype.NumberFixedLength
import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame
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
     * @throws InvalidTagException if unable to create framebody from buffer
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    var groupSymbol: Byte
        /**
         * @return
         */
        get() {
            if (getObjectValue(DataTypes.OBJ_GROUP_SYMBOL) != null) {
                return (getObjectValue(DataTypes.OBJ_GROUP_SYMBOL) as kotlin.Byte?)!!
            } else {
                return 0.toByte()
            }
        }
        /**
         * @param groupSymbol
         */
        set(groupSymbol) {
            setObjectValue(DataTypes.OBJ_GROUP_SYMBOL, groupSymbol)
        }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3V24Frame.SIGNATURE.id
    }

    var signature: ByteArray?
        /**
         * @return
         */
        get() = getObjectValue(DataTypes.OBJ_SIGNATURE) as ByteArray?
        /**
         * @param signature
         */
        set(signature) {
            setObjectValue(DataTypes.OBJ_SIGNATURE, signature)
        }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(NumberFixedLength(DataTypes.OBJ_GROUP_SYMBOL, this, 1))
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_SIGNATURE, this))
    }
}
