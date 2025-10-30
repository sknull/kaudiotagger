package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameTDLYTest : AbstractTestCase() {

    @Test
    fun testID3Specific() {
        var e: Exception? = null
        try {
            val tag =  ID3v24Tag()
            val frame =  ID3v24Frame("TDLY")
            frame.frameBody = FrameBodyTDLY(TextEncoding.ISO_8859_1.id, "11:10")
            tag.addFrame(frame)
            assertEquals("11:10", tag.getFirst("TDLY"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }
}
