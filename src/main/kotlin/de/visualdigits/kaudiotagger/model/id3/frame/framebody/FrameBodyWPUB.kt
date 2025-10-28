package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import java.nio.ByteBuffer

/**
 * Publishers official webpage URL link frames.
 *
 * The 'Publishers official webpage' frame is a URL pointing at the official wepage for the publisher.
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
class FrameBodyWPUB: AbstractFrameBodyUrlLink, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyWPUB datatype.
     */
    constructor()

    /**
     * Creates a new FrameBodyWPUB datatype.
     *
     * @param urlLink
     */
    constructor(urlLink: String) : super(urlLink)

    constructor(body: FrameBodyWPUB) : super(body)

    /**
     * Creates a new FrameBodyWPUB datatype.
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
        return ID3v24Frames.URL_PUBLISHERS.id
    }
}
