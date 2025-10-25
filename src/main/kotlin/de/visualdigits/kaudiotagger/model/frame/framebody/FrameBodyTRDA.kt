package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v23Frames
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
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

    constructor(
        identifier: String? = null,
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(identifier, byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v23Frames.TRDA.id
    }
}