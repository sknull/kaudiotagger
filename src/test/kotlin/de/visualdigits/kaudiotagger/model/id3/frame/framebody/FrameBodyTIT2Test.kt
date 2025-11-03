package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyTIT2Test : AbstractTestCase() {

    @Test
    fun testGeneric() {
        var e: Exception? = null
        try {
            val tag = ID3v23Tag()
            tag.addField(GenericFieldKey.TITLE, "testtitle")
            assertEquals("testtitle", tag.getFirst(GenericFieldKey.TITLE))
            assertEquals("testtitle", tag.getFirst("TIT2"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }

    @Test
    fun testID3Specific() {
        var e: Exception? = null
        try {
            val tag =  ID3v23Tag()
            val frame =  ID3v23Frame("TIT2")
            frame.frameBody = FrameBodyTPE1(TextEncoding.ISO_8859_1.id, "testtitle")
            tag.addFrame(frame)
            assertEquals("testtitle", tag.getFirst(GenericFieldKey.TITLE))
            assertEquals("testtitle", tag.getFirst("TIT2"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }
}
