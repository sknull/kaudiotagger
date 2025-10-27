package de.visualdigits.kaudiotagger.model.frame.framebody.id3

import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractFrameBodyUrlLink
import java.nio.ByteBuffer

/**
 * Payment URL link frames.
 *
 * The 'Payment' frame is a URL pointing at a webpage that will handle the process of paying for this file.
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
class FrameBodyWPAY: AbstractFrameBodyUrlLink, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyWPAY datatype.
     */
    constructor()

    /**
     * Creates a new FrameBodyWPAY datatype.
     *
     * @param urlLink
     */
    constructor(urlLink: String) : super(urlLink)

    constructor(body: FrameBodyWPAY) : super(body)

    /**
     * Creates a new FrameBodyWPAY datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.URL_PAYMENT.id
    }
}
