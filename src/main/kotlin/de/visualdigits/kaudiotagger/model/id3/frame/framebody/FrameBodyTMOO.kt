package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import java.nio.ByteBuffer

class FrameBodyTMOO: AbstractFrameBodyTextInfo, ID3v24FrameBody {

    constructor()

    constructor(copyObject: FrameBodyTMOO): super(copyObject)

    /**
     * Creates a new FrameBodyTMOO datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text)

    constructor(body: FrameBodyTXXX) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding())
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1.id)
        this.setObjectValue(DataTypes.OBJ_TEXT, body.getText())
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24FrameId.MOOD.id
    }
}