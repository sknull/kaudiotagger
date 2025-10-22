package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
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

    /**
     * Creates a new FrameBodyTMOO datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws java.io.IOException
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer, frameSize: Int): super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24KFrame.MOOD.id
    }
}