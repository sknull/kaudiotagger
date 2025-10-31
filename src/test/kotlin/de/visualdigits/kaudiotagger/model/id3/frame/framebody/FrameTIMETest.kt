package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameTIMETest : AbstractTestCase() {

    @Test
    fun testID3Specific() {
        var e: Exception? = null
        try {
            val tag = ID3v23Tag()
            val frame =  ID3v23Frame("TIME")
            frame.frameBody = FrameBodyTIME(TextEncoding.ISO_8859_1.id, "1110")
            tag.addFrame(frame)
            assertEquals("1110", tag.getFirst("TIME"))

            val v24tag =  ID3v24Tag(tag)
            assertEquals(1, v24tag.getFieldCount())
            assertNotNull(v24tag.getFirst("TDRC"))
            assertEquals("T11:10", v24tag.getFirst("TDRC"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }

    @Test
    fun testConvertingPartialTime() {
        var e: Exception? = null
        try {
            var tag =  ID3v24Tag()
            val frame =  ID3v24Frame("TDRC")
            val frameBody = FrameBodyTDRC(
                TextEncoding.ISO_8859_1.id,
                "2006-06-30T07"
            )
            frame.frameBody = frameBody
            tag.addFrame(frame)
            assertEquals("2006-06-30T07", tag.getFirst("TDRC"))
            assertEquals("2006", frameBody.year)
            assertEquals("3006", frameBody.date)

            val v23tag =  ID3v23Tag(tag)
            assertEquals(3, v23tag.getFieldCount())
            assertNotNull(v23tag.getFirst("TIME"))
            //"00" is created because cant just store hours in this field in v23
            assertEquals("0700", v23tag.getFirst("TIME"))
            assertEquals("2006", v23tag.getFirst("TYER"))
            assertEquals("3006", v23tag.getFirst("TDAT"))

            tag = ID3v24Tag(v23tag)
            // But because is MonthOnly flag set dd gets lost when convert back to v24
            assertEquals("2006-06-30T07", tag.getFirst("TDRC"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }
}
