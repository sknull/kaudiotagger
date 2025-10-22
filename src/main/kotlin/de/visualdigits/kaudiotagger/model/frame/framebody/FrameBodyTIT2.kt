package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
import java.nio.ByteBuffer

class FrameBodyTIT2: AbstractFrameBodyTextInfo, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyTIT2 datatype.
     */
    constructor() {
    }

    constructor(body: FrameBodyTIT2): super(body)

    /**
     * Creates a new FrameBodyTIT2 datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text)

    /**
     * Creates a new FrameBodyTIT2 datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer, frameSize: Int): super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24KFrame.TITLE.id
    }
}