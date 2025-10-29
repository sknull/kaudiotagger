package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3V24FrameId
import java.nio.ByteBuffer

/**
 * Playlist delay Text information frame.
 *
 * The 'Playlist delay' defines the numbers of milliseconds of silence between every song in a playlist. The player should use the "ETC" frame, if present, to skip initial silence and silence at the end of the audio to match the 'Playlist delay' time. The time is represented as a numeric string.
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
class FrameBodyTDLY: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyTDLY datatype.
     */
    constructor()

    constructor(body: FrameBodyTDLY) : super(body)

    /**
     * Creates a new FrameBodyTDLY datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTDLY datatype.
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
        return ID3V24FrameId.PLAYLIST_DELAY.id
    }
}
