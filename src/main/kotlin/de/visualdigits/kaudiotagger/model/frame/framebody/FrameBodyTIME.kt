package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.kframe.ID3v23KFrame
import java.nio.ByteBuffer

class FrameBodyTIME: AbstractFrameBodyTextInfo, ID3v23FrameBody {

    var hoursOnly = false

    constructor(body: FrameBodyTIME): super(body)

    /**
     * Creates a new FrameBodyTIME datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text)

    /**
     * Creates a new FrameBodyTIME datatype.
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
        return ID3v23KFrame.TIME.id
    }
}