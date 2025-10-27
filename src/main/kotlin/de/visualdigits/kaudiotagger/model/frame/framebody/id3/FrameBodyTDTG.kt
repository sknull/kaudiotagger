package de.visualdigits.kaudiotagger.model.frame.framebody.id3

import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractFrameBodyTextInfo
import java.nio.ByteBuffer

/**
 *
 * The 'Tagging time' frame contains a timestamp describing then the
 * audio was tagged. Timestamp format is described in the ID3v2
 * structure document
 */
class FrameBodyTDTG: AbstractFrameBodyTextInfo, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyTDTG datatype.
     */
    constructor()

    constructor(body: FrameBodyTDTG) : super(body)

    /**
     * Creates a new FrameBodyTDTG datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTDTG datatype.
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
        return ID3v24Frames.TAGGING_TIME.id
    }
}
