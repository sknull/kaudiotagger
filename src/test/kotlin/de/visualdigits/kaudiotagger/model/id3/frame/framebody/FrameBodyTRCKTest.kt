package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyTRCKTest : AbstractTestCase() {
    
    @Test
    fun testCreateFrameBodyStringConstructor() {
        TagOptionSingleton.padNumbers = false

        var fb: FrameBodyTRCK? = null
        fb = FrameBodyTRCK(TextEncoding.ISO_8859_1.id, "1/11")

        assertEquals(ID3v24FrameId.TRACK.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(1, fb.getTrackNo())
        assertEquals(11, fb.getTrackTotal())

        assertEquals("1/11", fb.getText())
    }

    @Test
    fun testCreateFrameBodyIntegerConstructor() {
        TagOptionSingleton.padNumbers = false

        var fb: FrameBodyTRCK? = null
        fb = FrameBodyTRCK(TextEncoding.ISO_8859_1.id, 1, 11)

        assertEquals(ID3v24FrameId.TRACK.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(1, fb.getTrackNo())
        assertEquals(11, fb.getTrackTotal())

        assertEquals("1/11", fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTRCK? = null
        fb = FrameBodyTRCK()

        assertEquals(ID3v24FrameId.TRACK.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals("", fb.getText())
        assertNull(fb.getTrackNo())
        assertNull(fb.getTrackTotal())
    }

    @Test
    fun testCreateFrameBodyTrackOnly() {
        TagOptionSingleton.padNumbers = false

        var fb: FrameBodyTRCK? = null
        fb = FrameBodyTRCK()
        fb.setTrackNo(1)

        assertEquals(ID3v24FrameId.TRACK.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals("1", fb.getText())
        assertEquals(1, fb.getTrackNo())
        assertNull(fb.getTrackTotal())
    }

    @Test
    fun testCreateFrameBodyTotalOnly() {
        var fb: FrameBodyTRCK? = null
        fb = FrameBodyTRCK()
        fb.setTrackTotal(11)

        assertEquals(ID3v24FrameId.TRACK.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals("0/11", fb.getText())
        assertNull(fb.getTrackNo())
        assertEquals(11, fb.getTrackTotal())
    }

    @Test
    fun testCreateFrameBodyWithPadding() {
        TagOptionSingleton.padNumbers = true
        var fb: FrameBodyTRCK? = null
        fb = FrameBodyTRCK(TextEncoding.ISO_8859_1.id, 1, 11)

        assertEquals(ID3v24FrameId.TRACK.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(1, fb.getTrackNo())
        assertEquals(11, fb.getTrackTotal())

        assertEquals("01/11", fb.getText())
    }

    @Test
    fun testCreateFrameBodyWithPaddingTwo() {
        TagOptionSingleton.padNumbers = true
        var fb: FrameBodyTRCK? = null
        fb = FrameBodyTRCK(TextEncoding.ISO_8859_1.id, 3, 7)

        assertEquals(ID3v24FrameId.TRACK.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(3, fb.getTrackNo())
        assertEquals(7, fb.getTrackTotal())

        assertEquals("03/07", fb.getText())
    }

    // specify the value as a string with no padding. getText should still return with padding
    @Test
    fun testCreateFrameBodyWithPaddedRawTextCount() {
        createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("01/11", 1, 11)
    }

    private fun createFrameBodyAndAssertNumericValuesAndRawPaddingRetained(
        rawText: String?,
        expectedCount: Int,
        expectedTotal: Int
    ) {
        var fb: FrameBodyTRCK? = null
        fb = FrameBodyTRCK(TextEncoding.ISO_8859_1.id, rawText!!)

        assertEquals(ID3v24FrameId.TRACK.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(expectedCount, fb.getTrackNo())
        assertEquals(expectedTotal, fb.getTrackTotal())
        assertEquals(rawText, fb.getText())
    }

    @Test
    fun testCreateFrameBodyWithUnpaddedRawTextCount() {
        createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("1/11", 1, 11)
    }

    @Test
    fun testCreateFrameBodyWithPaddedRawTextTotal() {
        createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("1/03", 1, 3)
    }

    @Test
    fun testCreateFrameBodyWithUnpaddedRawTextTotal() {
        createFrameBodyAndAssertNumericValuesAndRawPaddingRetained("1/3", 1, 3)
    }

    companion object {

        fun getInitialisedBody(): FrameBodyTRCK {
            TagOptionSingleton.padNumbers = false
            val fb = FrameBodyTRCK()
            fb.setTrackNo(1)
            fb.setTrackTotal(11)
            return fb
        }
    }
}
