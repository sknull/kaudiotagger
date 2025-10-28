package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import java.nio.ByteBuffer

/**
 * Official artist/performer webpage URL link frames.
 *
 * The 'Official artist/performer webpage' frame is a URL pointing at the artists official webpage.
 * There may be more than one "WOAR" frame in a tag if the audio contains more than one performer, but not with
 * the same content.
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
class FrameBodyWOAR: AbstractFrameBodyUrlLink, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyWOAR datatype.
     */
    constructor()

    /**
     * Creates a new FrameBodyWOAR datatype.
     *
     * @param urlLink
     */
    constructor(urlLink: String) : super(urlLink)

    constructor(body: FrameBodyWOAR) : super(body)

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
        return ID3v24Frames.URL_ARTIST_WEB.id
    }
}
