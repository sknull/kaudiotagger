package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import java.nio.ByteBuffer

class FrameBodyTDAT : AbstractFrameBodyTextInfo, ID3v23FrameBody {

    companion object {

        const val DATA_SIZE: Int = 4
        const val DAY_START: Int = 0
        const val DAY_END: Int = 2
        const val MONTH_START: Int = 2
        const val MONTH_END: Int = 4
    }

    var isMonthOnly = false

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v23FrameId.TDAT.id
    }

    constructor()

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * Creates a new FrameBodyTDAT datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String?): super(textEncoding, text)
}