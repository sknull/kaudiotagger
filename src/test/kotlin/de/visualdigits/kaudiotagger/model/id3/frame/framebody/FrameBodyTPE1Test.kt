package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyTPE1Test : AbstractTestCase() {

    companion object {
        const val TPE1_TEST_STRING: String = "beck"

        const val TPE1_UNICODE_REQUIRED_TEST_STRING: String = "\u01ff\u01ffbeck"

        fun getInitialisedBody(): FrameBodyTPE1 {
            // Text Encoding doesnt matter until written to file
            val fb: FrameBodyTPE1 = FrameBodyTPE1(
                TextEncoding.ISO_8859_1.id,
                TPE1_TEST_STRING
            )
            return fb
        }

        fun getUnicodeRequiredInitialisedBody(): FrameBodyTPE1 {
            // Text Encoding doesnt matter until written to file
            val fb: FrameBodyTPE1 = FrameBodyTPE1(
                TextEncoding.ISO_8859_1.id,
                TPE1_UNICODE_REQUIRED_TEST_STRING
            )
            return fb
        }
    }

    @Test
    fun testGeneric() {
        var e: Exception? = null
        try {
            val tag = ID3v23Tag()
            tag.addField(GenericFieldKey.ARTIST, "testartist")
            assertEquals("testartist", tag.getFirst(GenericFieldKey.ARTIST))
            assertEquals("testartist", tag.getFirst("TPE1"))
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
            val frame =  ID3v23Frame("TPE1")
            frame.frameBody = FrameBodyTPE1(TextEncoding.ISO_8859_1.id, "testartist")
            tag.addFrame(frame)
            assertEquals("testartist", tag.getFirst(GenericFieldKey.ARTIST))
            assertEquals("testartist", tag.getFirst("TPE1"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }

    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTPE1? = null
        fb = FrameBodyTPE1(
            TextEncoding.UTF_16.id,
            TPE1_UNICODE_REQUIRED_TEST_STRING
        )

        assertEquals(ID3v24FrameId.ARTIST.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.UTF_16.id, fb.getTextEncoding())
        assertEquals(
            TPE1_UNICODE_REQUIRED_TEST_STRING,
            fb.getText()
        )
    }
}
