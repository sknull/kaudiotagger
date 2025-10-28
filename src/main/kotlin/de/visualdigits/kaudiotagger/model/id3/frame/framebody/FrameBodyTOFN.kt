package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import java.nio.ByteBuffer

/**
 * Original filename Text information frame.
 *
 * The 'Original filename' frame contains the preferred filename for the file, since some media doesn't allow the
 * desired length of the filename. The filename is case sensitive and includes its suffix.
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
class FrameBodyTOFN: AbstractFrameBodyTextInfo, ID3v23FrameBody, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyTOFN datatype.
     */
    constructor()

    constructor(body: FrameBodyTOFN) : super(body)

    /**
     * Creates a new FrameBodyTOFN datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTOFN datatype.
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
        return ID3v24Frames.ORIG_FILENAME.id
    }
}
