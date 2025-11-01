package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyTDRCTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTDRC? = null
        fb = FrameBodyTDRC()
        fb.date = TEST_YEAR

        assertEquals(ID3v24FrameId.YEAR.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(TEST_YEAR, fb.date)
    }

    companion object {
        const val TEST_YEAR: String = "2002"
    }
}
