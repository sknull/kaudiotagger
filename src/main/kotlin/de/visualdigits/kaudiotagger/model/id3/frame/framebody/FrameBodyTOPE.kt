package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import java.nio.ByteBuffer

/**
 * Original artist(s)/performer(s) Text information frame.
 *
 * The 'Original artist(s)/performer(s)' frame is intended for the performer(s) of the original recording, if for
 * example the music in the file should be a cover of a previously released song. The performers are separated with
 * the "/" character.
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
class FrameBodyTOPE: AbstractFrameBodyTextInfo, ID3v23FrameBody, ID3v24FrameBody {

    /**
     * Creates a new FrameBodyTOPE datatype.
     */
    constructor()

    constructor(body: FrameBodyTOPE) : super(body)

    /**
     * Creates a new FrameBodyTOPE datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTOPE datatype.
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
        return ID3v24Frames.ORIGARTIST.id
    }
}
