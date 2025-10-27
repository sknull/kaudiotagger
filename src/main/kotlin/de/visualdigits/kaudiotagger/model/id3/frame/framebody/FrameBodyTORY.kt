package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.types.ID3v23Frames
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import java.nio.ByteBuffer

/**
 * Original release year Text information frame.
 *
 * The 'Original release year' frame is intended for the year when the original recording, if for example the music
 * in the file should be a cover of a previously released song, was released. The field is formatted as in the "TYER"
 * frame.
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
class FrameBodyTORY: AbstractFrameBodyTextInfo, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyTORY datatype.
     */
    constructor()

    constructor(body: FrameBodyTORY) : super(body)

    /**
     * Creates a new FrameBodyTORY datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * When converting v4 TDOR to v3 TORY frame
     *
     * @param body
     */
    constructor(body: FrameBodyTDOR) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        var year = body.getText()
        if (body.getText().length > NUMBER_OF_DIGITS_IN_YEAR) {
            year = body.getText().substring(0, NUMBER_OF_DIGITS_IN_YEAR)
        }
        setObjectValue(DataTypes.OBJ_TEXT, year)
    }

    /**
     * Creates a new FrameBodyTORY datatype.
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
        return ID3v23Frames.TORY.id
    }

    companion object {
        private const val NUMBER_OF_DIGITS_IN_YEAR = 4
    }
}
