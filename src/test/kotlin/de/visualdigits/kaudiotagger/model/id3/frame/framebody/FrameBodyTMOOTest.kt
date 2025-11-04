package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyTMOOTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTMOO? = null
        fb = FrameBodyTMOO(TextEncoding.ISO_8859_1.id, MOOD)

        assertEquals(ID3v24FrameId.MOOD.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(MOOD, fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTMOO? = null
        fb = FrameBodyTMOO()
        fb.setText(MOOD)

        assertEquals(ID3v24FrameId.MOOD.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(MOOD, fb.getText())
    }

    companion object {
        const val MOOD: String = "mellow"

        val initialisedBody: FrameBodyTMOO
            get() {
                val fb = FrameBodyTMOO()
                fb.setText(MOOD)
                return fb
            }
    }
}
