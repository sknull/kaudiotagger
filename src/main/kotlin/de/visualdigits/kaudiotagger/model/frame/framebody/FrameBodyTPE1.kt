package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
import java.nio.ByteBuffer

class FrameBodyTPE1: AbstractFrameBodyTextInfo, ID3v23FrameBody, ID3v24FrameBody {

    /**
     * Creates a new FrameBodyTPE1 datatype.
     */
    constructor() {
    }

    constructor(body: FrameBodyTPE1): super(body)

    /**
     * Creates a new FrameBodyTPE1 datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text)

    /**
     * Creates a new FrameBodyTPE1 datatype.
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
        return ID3v24KFrame.ARTIST.id
    }
}