package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.kframe.ID3v23KFrame

class FrameBodyTDAT() : AbstractFrameBodyTextInfo(), ID3v23FrameBody {

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
    override fun getIdentifier(): String? {
        return ID3v23KFrame.TDAT.id
    }
}