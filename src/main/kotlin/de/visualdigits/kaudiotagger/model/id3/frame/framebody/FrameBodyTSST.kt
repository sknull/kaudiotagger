package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame
import java.nio.ByteBuffer

class FrameBodyTSST: AbstractFrameBodyTextInfo, ID3v23FrameBody, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyTSST datatype.
     */
    constructor()

    constructor(body: FrameBodyTSST) : super(body)

    /**
     * Creates a new FrameBodyTSST datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTSST datatype.
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
        return ID3V24Frame.SET_SUBTITLE.id
    }
}
