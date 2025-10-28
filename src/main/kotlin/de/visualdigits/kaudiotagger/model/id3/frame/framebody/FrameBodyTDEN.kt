package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame
import java.nio.ByteBuffer

class FrameBodyTDEN: AbstractFrameBodyTextInfo, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyTDEN datatype.
     */
    constructor()

    constructor(body: FrameBodyTDEN) : super(body)

    /**
     * Creates a new FrameBodyTDEN datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTDEN datatype.
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
        return ID3V24Frame.ENCODING_TIME.id
    }
}
