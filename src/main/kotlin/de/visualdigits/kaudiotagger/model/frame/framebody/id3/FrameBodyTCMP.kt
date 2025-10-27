package de.visualdigits.kaudiotagger.model.frame.framebody.id3

import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractFrameBodyTextInfo
import java.nio.ByteBuffer

/**
 * Is part of a Compilation (iTunes frame)
 *
 *
 * determines whether or not track is part of compilation
 *
 * @author : Paul Taylor
 */
class FrameBodyTCMP: AbstractFrameBodyTextInfo, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyTCMP datatype, with compilation enabled
     *
     *
     * This is the preferred constructor to use because TCMP frames should not exist
     * unless they are set to true
     */
    constructor() {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        setObjectValue(DataTypes.OBJ_TEXT, IS_COMPILATION)
    }

    constructor(body: FrameBodyTCMP) : super(body)

    /**
     * Creates a new FrameBodyTCMP datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String) : super(textEncoding, text)

    /**
     * Creates a new FrameBodyTIT1 datatype.
     *
     * @param byteBuffer
     * @param frameSize
     * @throws InvalidTagException
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    val isCompilation: Boolean
        get() = this.getText().equals(IS_COMPILATION)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.IS_COMPILATION.id
    }

    companion object {
        //TODO does iTunes have to have null terminator?
        var IS_COMPILATION: String = "1\u0000"
    }
}
