package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import java.nio.ByteBuffer

class FrameBodyTMOO: AbstractFrameBodyTextInfo, ID3v24FrameBody {

    constructor()

    constructor(body: FrameBodyTMOO): super(body)

    /**
     * Creates a new FrameBodyTMOO datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text)

    constructor(body: FrameBodyTXXX) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding())
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        this.setObjectValue(DataTypes.OBJ_TEXT, body.getText())
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    constructor(
        identifier: String? = null,
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(identifier, byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.MOOD.id
    }
}