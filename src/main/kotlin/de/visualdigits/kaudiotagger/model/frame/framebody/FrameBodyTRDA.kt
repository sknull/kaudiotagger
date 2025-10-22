package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.kframe.ID3v23KFrame
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

    /**
     * Creates a new FrameBodyTRDA datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws java.io.IOException
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer, frameSize: Int): super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v23KFrame.TRDA.id
    }
}