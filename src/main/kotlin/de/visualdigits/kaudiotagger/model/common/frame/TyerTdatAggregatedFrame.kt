package de.visualdigits.kaudiotagger.model.common.frame

import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTDAT
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23Frames

class TyerTdatAggregatedFrame(): AggregatedFrame() {

    companion object {
        val ID_TYER_TDAT: String = ID3v23Frames.TYER.id + ID3v23Frames.TDAT.id
    }

    override fun getContent(): String {
        val sb = StringBuilder()
        val i = getFrames().iterator()
        val tyer = i.next()
        sb.append(tyer.getContent())
        val tdat = i.next()
        if (tdat.getContent()?.length == FrameBodyTDAT.DATA_SIZE) {
            sb.append("-")
            sb.append(
                tdat.getContent(),
                FrameBodyTDAT.MONTH_START,
                FrameBodyTDAT.MONTH_END
            )

            if ((tdat.frameBody as? FrameBodyTDAT)?.isMonthOnly == false) {
                sb.append("-")
                sb.append(
                    tdat.getContent(),
                    FrameBodyTDAT.DAY_START,
                    FrameBodyTDAT.DAY_END
                )
            }
        }
        return sb.toString()
    }
}