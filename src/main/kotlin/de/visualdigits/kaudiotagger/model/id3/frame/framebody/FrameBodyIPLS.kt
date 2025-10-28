package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.datatype.ValuePairs
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23Frames
import java.nio.ByteBuffer

class FrameBodyIPLS: AbstractFrameBodyPairs, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyIPLS datatype.
     */
    constructor()

    /**
     * Creates a new FrameBodyIPLS data type.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text)

    constructor(body: FrameBodyIPLS) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding())
        setObjectValue(DataTypes.OBJ_TEXT, body.getPairing())
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * Convert from V4 to V3 Frame
     *
     * @param body
     */
    constructor(body: FrameBodyTIPL) {
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
    override fun getIdentifier(): String {
        return ID3v23Frames.INVOLVED_PEOPLE.id
    }
}