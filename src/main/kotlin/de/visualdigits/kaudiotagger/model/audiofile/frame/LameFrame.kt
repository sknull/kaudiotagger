package de.visualdigits.kaudiotagger.model.audiofile.frame

import de.visualdigits.kaudiotagger.util.Utils
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

class LameFrame(
    lameHeader: ByteBuffer
) {

    companion object {
        const val LAME_HEADER_BUFFER_SIZE: Int = 36
        const val ENCODER_SIZE: Int = 9 // Includes LAME ID
        const val LAME_ID_SIZE: Int = 4
        const val LAME_ID: String = "LAME"

        /**
         * Parse frame
         *
         * @param bb
         * @return frame or null if not exists
         */
        fun parseLameFrame(bb: ByteBuffer): LameFrame? {
            val lameHeader = bb.slice()
            val id = Utils.getString(
                lameHeader,
                0,
                LAME_ID_SIZE,
                StandardCharsets.ISO_8859_1
            )
            lameHeader.rewind()
            if (id == LAME_ID) {
                val lameFrame = LameFrame(lameHeader)

                return lameFrame
            }
            return null
        }
    }

    val encoder: String = Utils.getString(
        lameHeader,
        0,
        ENCODER_SIZE,
        StandardCharsets.ISO_8859_1
    )
}