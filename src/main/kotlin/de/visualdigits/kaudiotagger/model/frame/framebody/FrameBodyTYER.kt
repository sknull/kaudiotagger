package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.kframe.ID3v23KFrame
import java.nio.ByteBuffer

class FrameBodyTYER: AbstractFrameBodyTextInfo, ID3v23FrameBody {

    constructor()

    constructor(body: FrameBodyTYER): super(body)

    /**
     * When converting v4 TDRC frame to v3 TYER
     *
     * @param body
     */
    constructor(body: FrameBodyTDRC) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        setObjectValue(DataTypes.OBJ_TEXT, body.getText())
    }

    /**
     * Creates a new FrameBodyTYER datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text)

    /**
     * Creates a new FrameBodyTYER datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer, frameSize: Int): super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v23KFrame.TYER.id
    }
}