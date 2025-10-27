package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.common.types.Languages
import java.nio.ByteBuffer

/**
 * Language(s) Text information frame.
 *
 * The 'Language(s)' frame should contain the languages of the text or lyrics spoken or sung in the audio. The language is represented with three characters according to ISO-639-2. If more than one language is used in the text their language codes should follow according to their usage.
 *
 *
 * For more details, please refer to the ID3 specifications:
 *
 *  * [ID3 v2.3.0 Spec](http://www.id3.org/id3v2.3.0.txt)
 *
 *
 *
 * TODO:Although rare TLAN can actually return multiple language codes, at the moment they are all returned as a single
 * string via getText(), any additional parsing has to be done externally.
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyTLAN: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyTLAN datatype.
     */
    constructor() : super()

    constructor(body: FrameBodyTLAN) : super(body)

    /**
     * Creates a new FrameBodyTLAN datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTLAN datatype.
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
        return ID3v24Frames.LANGUAGE.id
    }

    /**
     * @return true if text value is valid language code
     */
    fun isValid(): Boolean {
        return Languages.fromDescription(getFirstTextValue()) != null
    }
}
