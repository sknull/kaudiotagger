package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import java.nio.ByteBuffer

/**
 * Official audio source webpage URL link frames.
 *
 * The 'Official audio source webpage' frame is a URL pointing at the official webpage for the source of the audio
 * file, e.g. a movie.
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
class FrameBodyWOAS: AbstractFrameBodyUrlLink, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyWOAS datatype.
     */
    constructor()

    /**
     * Creates a new FrameBodyWOAS datatype.
     *
     * @param urlLink
     */
    constructor(urlLink: String) : super(urlLink)

    constructor(body: FrameBodyWOAS) : super(body)

    /**
     * Creates a new FrameBodyWOAS datatype.
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
        return ID3v24Frames.URL_SOURCE_WEB.id
    }
}
