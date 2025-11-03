package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyTRCKTest : AbstractTestCase() {

    companion object {

        fun getInitialisedBody(): FrameBodyTRCK {
            TagOptionSingleton.padNumbers = false
            val fb = FrameBodyTRCK()
            fb.setTrackNo(1)
            fb.setTrackTotal(11)
            return fb
        }

        val initialisedFrame: ID3v24Frame
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.TRACK.id)
                val fb = getInitialisedBody()
                frame.frameBody = fb
                return frame
            }
    }

    @Test
    fun testCreateID3v24Frame() {
        val frame = ID3v24Frame(ID3v24FrameId.TRACK.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(ID3v24FrameId.TRACK.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals("1/11", (frame.frameBody as FrameBodyTRCK).getText())
        Assertions.assertFalse(
            ID3v24FrameId.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v24FrameId.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testCreateID3v23Frame() {
        val frame = ID3v23Frame(ID3v23FrameId.TRACK.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(ID3v23FrameId.TRACK.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals("1/11", (frame.frameBody as FrameBodyTRCK).getText())
    }

    @Test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(initialisedFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.TRACK.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTRCK
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals("1/11", (frame.frameBody as FrameBodyTRCK).getText())
    }

    @Test
    fun testSaveEmptyFrameToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        var frame = ID3v24Frame(ID3v24FrameId.TRACK.id)
        frame.frameBody = FrameBodyTRCK()

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(frame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.TRACK.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTRCK
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals("", (frame.frameBody as FrameBodyTRCK).getText())
    }

    @Test
    fun testMergingMultipleTrackFrames() {
        val tag = ID3v24Tag()
        val field1 = tag.createField(GenericFieldKey.TRACK, "1")
        tag.setField(field1)
        val field2 = tag.createField(GenericFieldKey.TRACK_TOTAL, "10")
        tag.setField(field2)
        assertEquals("1", tag.getFirst(GenericFieldKey.TRACK))
        assertEquals("10", tag.getFirst(GenericFieldKey.TRACK_TOTAL))
        Assertions.assertInstanceOf(AbstractID3v2Frame::class.java, tag.getFrame("TRCK"))
    }

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
}
