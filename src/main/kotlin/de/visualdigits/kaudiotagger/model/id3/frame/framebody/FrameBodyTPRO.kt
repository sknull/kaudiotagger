package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.types.ID3v24Frames
import java.nio.ByteBuffer

class FrameBodyTPRO: AbstractFrameBodyTextInfo, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyTPRO datatype.
     */
    constructor()

    constructor(body: FrameBodyTPRO) : super(body)

    /**
     * Creates a new FrameBodyTPRO datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTPRO datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws java.io.IOException
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.PRODUCED_NOTICE.id
    }
}
