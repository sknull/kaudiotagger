package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTDAT
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import java.nio.ByteBuffer


/**
 * For use in ID3 for mapping YEAR field to TYER and TDAT Frames
 */
class TyerTdatAggregatedFrame : AggregatedFrame() {

    companion object {
        val ID_TYER_TDAT: String = ID3v23FrameId.TYER.id + ID3v23FrameId.TDAT.id
    }

    override fun getContent(): String {
        val sb = StringBuilder()
        val framesList = frames.toList()
        val tyer = framesList.firstOrNull()
        sb.append(tyer?.getContent()?:"")
        val tdat = framesList[1]
        if (tdat.getContent()?.length == FrameBodyTDAT.DATA_SIZE) {
            sb.append("-")
            sb.append(
                tdat.getContent(),
                FrameBodyTDAT.MONTH_START,
                FrameBodyTDAT.MONTH_END
            )

            if (!(tdat.frameBody as FrameBodyTDAT).isMonthOnly) {
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

    override fun getFrameIdSize(): Int {
        return ID_TYER_TDAT.length
    }

    override fun getFrameSizeSize(): Int {
        TODO("Not yet implemented")
    }

    override fun getFrameHeaderSize(): Int {
        TODO("Not yet implemented")
    }

    override fun read(byteBuffer: ByteBuffer?): Boolean {
        TODO("Not yet implemented")
    }
}
