package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3V24FrameId
import java.nio.ByteBuffer

/**
 * Lyricist/Text writer Text information frame.
 *
 * The 'Lyricist(s)/Text writer(s)' frame is intended for the writer(s) of the text or lyrics in the recording. They are seperated with the "/" character.
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
class FrameBodyTEXT: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyTEXT datatype.
     */
    constructor()

    constructor(body: FrameBodyTEXT) : super(body)

    /**
     * Creates a new FrameBodyTEXT datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTEXT datatype.
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
        return ID3V24FrameId.LYRICIST.id
    }
}
