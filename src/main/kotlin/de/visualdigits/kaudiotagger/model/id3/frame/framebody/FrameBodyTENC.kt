package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import java.nio.ByteBuffer

/**
 * Encoded by Text information frame.
 *
 * The 'Encoded by' frame contains the name of the person or organisation that encoded the audio file.
 * This field may contain a copyright message, if the audio file also is copyrighted by the encoder.
 *
 *
 * For more details, please refer to the ID3 specifications:
 *
 *  * [ID3 v2.3.0 Spec](http://www.id3.org/id3v2.3.0.txt)
 *
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyTENC: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyTENC dataType.
     */
    constructor()

    constructor(body: FrameBodyTENC) : super(body)

    /**
     * Creates a new FrameBodyTENC dataType.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTENC dataType.
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
        return ID3v24Frames.ENCODEDBY.id
    }
}
