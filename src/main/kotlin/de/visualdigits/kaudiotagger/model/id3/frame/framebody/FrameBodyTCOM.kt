package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import java.nio.ByteBuffer


/**
 * Composer Text information frame.
 *
 * The 'Composer(s)' frame is intended for the name of the composer(s).
 * They are separated with the "/" character in ID3v23.
 * Null separated as is the norm in ID3v24
 *
 *
 *
 * For more details, please refer to the ID3 specifications:
 *
 *  * [ID3 v2.3.0 Spec](http:// www.id3.org/id3v2.3.0.txt)
 *
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyTCOM: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyTCOM datatype.
     */
    constructor()

    constructor(body: FrameBodyTCOM) : super(body)

    /**
     * Creates a new FrameBodyTCOM datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String?) : super(textEncoding, text)

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24FrameId.COMPOSER.id
    }
}
