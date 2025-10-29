package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3V24FrameId
import java.nio.ByteBuffer

class FrameBodyTSRC: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyTSRC datatype.
     */
    constructor()

    constructor(body: FrameBodyTSRC) : super(body)

    /**
     * Creates a new FrameBodyTSRC datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTSRC datatype.
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
        return ID3V24FrameId.ISRC.id

    }
}
