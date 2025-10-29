package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3V24FrameId
import java.nio.ByteBuffer

/**
 * Internet radio station owner Text information frame.
 *
 * The 'Internet radio station owner' frame contains the name of the owner of the internet radio station from which the audio is streamed.
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
class FrameBodyTRSO: AbstractFrameBodyTextInfo, ID3v23FrameBody, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyTRSO datatype.
     */
    constructor()

    constructor(body: FrameBodyTRSO) : super(body)

    /**
     * Creates a new FrameBodyTRSO datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTRSO datatype.
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
        return ID3V24FrameId.RADIO_OWNER.id
    }
}
