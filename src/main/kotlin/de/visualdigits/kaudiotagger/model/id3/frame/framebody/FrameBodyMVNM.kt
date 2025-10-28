package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import java.nio.ByteBuffer

/**
 * Apple defined Movement frame works the same way as regular Text Frames
 *
 *
 * This is not an official standard frame, but Apple makes its own rules !
 */
class FrameBodyMVNM: AbstractFrameBodyTextInfo, ID3v23FrameBody, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyMVNM datatype.
     */
    constructor() : super()

    constructor(body: FrameBodyMVNM) : super(body)

    /**
     * Creates a new FrameBodyTRCK datatype, the value is parsed literally
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTRCK datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws java.io.IOException
     * @throws org.jaudiotagger.tag.InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.MOVEMENT.id
    }
}
