package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import java.nio.ByteBuffer

/**
 * Publisher Text information frame.
 *
 * The 'Publisher' frame simply contains the name of the label or publisher.
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
class FrameBodyTPUB: AbstractFrameBodyTextInfo, ID3v23FrameBody, ID3v24FrameBody {

    /**
     * Creates a new FrameBodyTPUB datatype.
     */
    constructor()

    constructor(body: FrameBodyTPUB) : super(body)

    /**
     * Creates a new FrameBodyTPUB datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String?) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTPUB datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws java.io.IOException
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.PUBLISHER.id
    }
}
