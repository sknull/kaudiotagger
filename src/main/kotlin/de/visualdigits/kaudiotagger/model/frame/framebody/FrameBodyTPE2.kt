package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import java.nio.ByteBuffer

/**
 * Band/Orchestra/Accompaniment Text information frame.
 *
 * The 'Band/Orchestra/Accompaniment' frame is used for additional information about the performers in the recording.
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
class FrameBodyTPE2: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {
    
    /**
     * Creates a new FrameBodyTPE2 datatype.
     */
    constructor()

    constructor(body: FrameBodyTPE2) : super(body)

    /**
     * Creates a new FrameBodyTPE2 datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String?) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTPE2 datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.ACCOMPANIMENT.id
    }
}
