package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyTPOSTest : AbstractTestCase() {
    
    @Test
    fun testCreateFrameBodyStringConstructor() {
        TagOptionSingleton.padNumbers = false
        var fb: FrameBodyTPOS? = null
        fb = FrameBodyTPOS(TextEncoding.ISO_8859_1.id, "1/11")

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(1, fb.getDiscNo())
        assertEquals(11, fb.getDiscTotal())

        assertEquals("1/11", fb.getText())
    }

    @Test
    fun testCreateFrameBodyIntegerConstructor() {
        TagOptionSingleton.padNumbers = false

        var fb: FrameBodyTPOS? = null
        fb = FrameBodyTPOS(TextEncoding.ISO_8859_1.id, 1, 11)

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(1, fb.getDiscNo())
        assertEquals(11, fb.getDiscTotal())

        assertEquals("1/11", fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        TagOptionSingleton.padNumbers = false
        var fb: FrameBodyTPOS? = null
        fb = FrameBodyTPOS()
        fb.setDiscNo(1)
        fb.setDiscTotal(11)

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals("1/11", fb.getText())
        assertEquals(1, fb.getDiscNo())
        assertEquals(11, fb.getDiscTotal())
    }

    @Test
    fun testCreateFrameBodyDiscOnly() {
        TagOptionSingleton.padNumbers = false
        var fb: FrameBodyTPOS? = null
        fb = FrameBodyTPOS()
        fb.setDiscNo(1)

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals("1", fb.getText())
        assertEquals(1, fb.getDiscNo())
        assertNull(fb.getDiscTotal())
    }

    @Test
    fun testCreateFrameBodyTotalOnly() {
        val fb = FrameBodyTPOS()
        fb.setDiscTotal(11)

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals("0/11", fb.getText())
        assertNull(fb.getDiscNo())
        assertEquals(11, fb.getDiscTotal())
    }

    @Test
    fun testCreateFrameBodyWithPadding() {
        TagOptionSingleton.padNumbers = true
        var fb: FrameBodyTPOS? = null
        fb = FrameBodyTPOS(TextEncoding.ISO_8859_1.id, 1, 11)

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(1, fb.getDiscNo())
        assertEquals(11, fb.getDiscTotal())

        assertEquals("01/11", fb.getText())
    }

    @Test
    fun testCreateFrameBodyWithPaddingTwo() {
        TagOptionSingleton.padNumbers = true
        var fb: FrameBodyTPOS? = null
        fb = FrameBodyTPOS(TextEncoding.ISO_8859_1.id, 3, 7)

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(3, fb.getDiscNo())
        assertEquals(7, fb.getDiscTotal())

        assertEquals("03/07", fb.getText())
    }

    // specify the value as a string with no padding. getText should still return with padding
    @Test
    fun testCreateFrameBodyWithPaddedRawTextCount() {
        TagOptionSingleton.padNumbers = false
        val fb = createFrameBodyAndAssertNumericValuesAndRawValueRetained(
            "01/11",
            1,
            11
        )
        assertEquals("01", fb.getDiscNoAsText())
        assertEquals("11", fb.getDiscTotalAsText())
    }

    private fun createFrameBodyAndAssertNumericValuesAndRawValueRetained(
        rawText: String?,
        expectedCount: Int,
        expectedTotal: Int
    ): FrameBodyTPOS {
        var fb: FrameBodyTPOS? = null
        fb = FrameBodyTPOS(TextEncoding.ISO_8859_1.id, rawText!!)

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(expectedCount, fb.getDiscNo())
        assertEquals(expectedTotal, fb.getDiscTotal())
        assertEquals(rawText, fb.getText())
        return fb
    }

    @Test
    fun testCreateFrameBodyWithUnpaddedRawTextCount() {
        TagOptionSingleton.padNumbers = false
        val fb = createFrameBodyAndAssertNumericValuesAndRawValueRetained(
            "1/11",
            1,
            11
        )
        assertEquals("1", fb.getDiscNoAsText())
        assertEquals("11", fb.getDiscTotalAsText())
    }

    @Test
    fun testCreateFrameBodyWithPaddedRawTextTotal() {
        TagOptionSingleton.padNumbers = false
        val fb = createFrameBodyAndAssertNumericValuesAndRawValueRetained(
            "1/03",
            1,
            3
        )
        assertEquals("1", fb.getDiscNoAsText())
        assertEquals("03", fb.getDiscTotalAsText())
    }

    @Test
    fun testCreateFrameBodyWithPaddedRawTextTotal2() {
        TagOptionSingleton.padNumbers = false
        val fb = createFrameBodyAndAssertNumericValuesAndRawValueRetained(
            "01/03",
            1,
            3
        )
        assertEquals("01", fb.getDiscNoAsText())
        assertEquals("03", fb.getDiscTotalAsText())
    }

    @Test
    fun testCreateFrameBodyWithUnpaddedRawTextTotal() {
        TagOptionSingleton.padNumbers = false
        val fb = createFrameBodyAndAssertNumericValuesAndRawValueRetained(
            "1/3",
            1,
            3
        )
        assertEquals("1", fb.getDiscNoAsText())
        assertEquals("3", fb.getDiscTotalAsText())
    }

    @Test
    fun testCreateFrameBodyWithPaddedRawTextCountIsPadded() {
        TagOptionSingleton.padNumbers = true
        var fb: FrameBodyTPOS? = null
        fb = FrameBodyTPOS(TextEncoding.ISO_8859_1.id, "01/11")

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(1, fb.getDiscNo())
        assertEquals(11, fb.getDiscTotal())
        assertEquals("01/11", fb.getText())
        assertEquals("01", fb.getDiscNoAsText())
        assertEquals("11", fb.getDiscTotalAsText())
    }

    @Test
    fun testCreateFrameBodyWithUnpaddedRawTextCountIsPadded() {
        TagOptionSingleton.padNumbers = true
        var fb: FrameBodyTPOS? = null
        fb = FrameBodyTPOS(TextEncoding.ISO_8859_1.id, "1/11")

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(1, fb.getDiscNo())
        assertEquals(11, fb.getDiscTotal())
        assertEquals("01/11", fb.getText())
        assertEquals("01", fb.getDiscNoAsText())
        assertEquals("11", fb.getDiscTotalAsText())
    }

    @Test
    fun testCreateFrameBodyWithPaddedRawTextTotalIsPadded() {
        TagOptionSingleton.padNumbers = true
        var fb: FrameBodyTPOS? = null
        fb = FrameBodyTPOS(TextEncoding.ISO_8859_1.id, "1/03")

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(1, fb.getDiscNo())
        assertEquals(3, fb.getDiscTotal())
        assertEquals("01/03", fb.getText())
        assertEquals("01", fb.getDiscNoAsText())
        assertEquals("03", fb.getDiscTotalAsText())
    }

    @Test
    fun testCreateFrameBodyWithPaddedRawTextTotal2IsPadded() {
        TagOptionSingleton.padNumbers = true
        createFrameBodyAndAssertNumericValuesAndRawValueRetained("01/03", 1, 3)
    }

    @Test
    fun testCreateFrameBodyWithUnpaddedRawTextTotalIsPadded() {
        TagOptionSingleton.padNumbers = true
        var fb: FrameBodyTPOS? = null
        fb = FrameBodyTPOS(TextEncoding.ISO_8859_1.id, "1/3")

        assertEquals(ID3v24FrameId.SET.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(1, fb.getDiscNo())
        assertEquals(3, fb.getDiscTotal())
        assertEquals("01/03", fb.getText())
        assertEquals("01", fb.getDiscNoAsText())
        assertEquals("03", fb.getDiscTotalAsText())
    }

    companion object {

        fun getInitialisedBody(): FrameBodyTPOS {
            val fb = FrameBodyTPOS()
            fb.setDiscNo(1)
            fb.setDiscTotal(11)
            return fb
        }
    }
}
