package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.ValuePairs
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
import java.nio.ByteBuffer

class FrameBodyTMCL: AbstractFrameBodyPairs, ID3v24FrameBody {

    /**
     * Creates a new FrameBodyTIPL datatype.
     */
    constructor()

    /**
     * Creates a new FrameBodyTIPL data type.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text)

    /**
     * Creates a new FrameBodyTIPL data type.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer, frameSize: Int): super(byteBuffer, frameSize)

    /**
     * Convert from V3 to V4 Frame
     *
     * @param body
     */
    constructor(body: FrameBodyIPLS) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding())
        setObjectValue(DataTypes.OBJ_TEXT, body.getPairing())
    }

    /**
     * Construct from a set of pairs
     *
     * @param textEncoding
     * @param pairs
     */
    constructor(textEncoding: Byte, pairs: MutableList<Pair<String, String>>) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        val values = ValuePairs()
        for (next in pairs) {
            values.add(next)
        }
        setObjectValue(DataTypes.OBJ_TEXT, values)
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String? {
        return ID3v24KFrame.MUSICIAN_CREDITS.id
    }
}
