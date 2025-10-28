package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import java.nio.ByteBuffer

/**
 *
 * The 'Original release time' frame contains a timestamp describing
 * when the original recording of the audio was released. Timestamp
 * format is described in the ID3v2 structure document.
 */
class FrameBodyTDOR: AbstractFrameBodyTextInfo, ID3v24FrameBody {
    /**
     * Creates a new FrameBodyTDOR datatype.
     */
    constructor()

    constructor(body: FrameBodyTDOR) : super(body)

    /**
     * When converting v3 TDAT to v4 TDRC frame
     *
     * @param body
     */
    constructor(body: FrameBodyTORY) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        setObjectValue(DataTypes.OBJ_TEXT, body.getText())
    }

    /**
     * Creates a new FrameBodyTDOR datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTDOR datatype.
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
        return ID3v24Frames.ORIGINAL_RELEASE_TIME.id
    }
}
