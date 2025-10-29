package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3V24FrameId
import java.nio.ByteBuffer

/**
 * Subtitle/Description refinement Text information frame.
 *
 * The 'Subtitle/Description refinement' frame is used for information directly related to the contents title (e.g. "Op. 16" or "Performed live at Wembley").
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
class FrameBodyTIT3: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyTIT3 datatype.
     */
    constructor()

    constructor(body: FrameBodyTIT3) : super(body)

    /**
     * Creates a new FrameBodyTIT3 datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTIT3 datatype.
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
        return ID3V24FrameId.TITLE_REFINEMENT.id

    }
}
