package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3V23FrameId
import java.nio.ByteBuffer

class FrameBodyTRDA: AbstractFrameBodyTextInfo, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyTRDA datatype.
     */
    constructor()

    constructor(body: FrameBodyTRDA): super(body)

    /**
     * Creates a new FrameBodyTRDA datatype.
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
        return ID3V23FrameId.TRDA.id
    }
}