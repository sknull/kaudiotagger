package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23Frames
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import java.nio.ByteBuffer

class FrameBodyTYER: AbstractFrameBodyTextInfo, ID3v23FrameBody {

    constructor()

    constructor(body: FrameBodyTYER): super(body)

    /**
     * When converting v4 TDRC frame to v3 TYER
     *
     * @param body
     */
    constructor(body: FrameBodyTDRC) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        setObjectValue(DataTypes.OBJ_TEXT, body.getText())
    }

    /**
     * Creates a new FrameBodyTYER datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text)

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v23Frames.TYER.id
    }
}