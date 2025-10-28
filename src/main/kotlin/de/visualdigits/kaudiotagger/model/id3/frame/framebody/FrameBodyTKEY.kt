package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.id3.types.MusicalKey
import java.nio.ByteBuffer

/**
 * Initial key Text information frame.
 *
 * The 'Initial key' frame contains the musical key in which the sound starts. It is represented as a string with
 * a maximum length of three characters. The ground keys are represented with "A","B","C","D","E", "F" and "G" and halfkeys represented
 * with "b" and "#". Minor is represented as "m". Example "Cbm". Off key is represented with an "o" only.
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
class FrameBodyTKEY: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyTKEY datatype.
     */
    constructor()

    constructor(body: FrameBodyTKEY) : super(body)

    /**
     * Creates a new FrameBodyTKEY datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTKEY datatype.
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
        return ID3v24Frames.INITIAL_KEY.id
    }

    /**
     * @return true if text value is valid musical key notation
     */
    fun isValid(): Boolean {
        return MusicalKey.isValid(getFirstTextValue())
    }
}
