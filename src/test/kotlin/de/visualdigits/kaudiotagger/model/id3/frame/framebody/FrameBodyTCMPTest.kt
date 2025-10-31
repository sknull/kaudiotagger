package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FrameBodyTCMPTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTCMP? = null
        fb = FrameBodyTCMP()

        assertEquals(ID3v24FrameId.IS_COMPILATION.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertTrue(fb.isCompilation())
        assertEquals(FrameBodyTCMP.IS_COMPILATION, fb.getText())
        assertEquals(COMPILATION_TRUE, fb.getFirstTextValue())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTCMP? = null
        fb = FrameBodyTCMP()

        assertEquals(ID3v24FrameId.IS_COMPILATION.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertTrue(fb.isCompilation())
        assertEquals(FrameBodyTCMP.IS_COMPILATION, fb.getText())
        assertEquals(COMPILATION_TRUE, fb.getFirstTextValue())
    }

    companion object {
        const val COMPILATION_TRUE: String = "1"

        fun getInitialisedBody(): FrameBodyTCMP {
            val fb = FrameBodyTCMP()
            return fb
        }
    }
}
