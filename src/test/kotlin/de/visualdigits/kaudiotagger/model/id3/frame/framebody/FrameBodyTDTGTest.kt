package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyTDTGTest : AbstractTestCase() {

    @Test
    fun testID3Specific() {
        var e: Exception? = null
        try {
            val tag =  ID3v24Tag()
            val frame =  ID3v24Frame("TDTG")
            frame.frameBody = FrameBodyTDTG(TextEncoding.ISO_8859_1.id, "1998-11-03 11:10")
            tag.addFrame(frame)
            assertEquals("1998-11-03 11:10", tag.getFirst("TDTG"))

            val v23tag =  ID3v23Tag(tag)
            assertEquals(1, v23tag.getFieldCount())
            assertNotNull(v23tag.getFirst("TDTG"))
            assertInstanceOf(
                FrameBodyUnsupported::class.java,
                ((v23tag?.getFrame("TDTG") as AbstractID3v2Frame).frameBody)
            )
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }
}
