package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyTDRCTest : AbstractTestCase() {

    companion object {
        const val TEST_YEAR: String = "2002"
    }

    @Test
    fun testReadFileContainingTDRCAndTYERFrames() {
        val testFile =  prependAudioToTmp(
            "Issue73.id3",
            "testV1.mp3"
        )

        val mp3File =  MP3File.read(testFile)
        mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.TYER.id) as? ID3v23Frame
        mp3File
            .getID3v2TagAsv24()
            ?.getFrame(ID3v23FrameId.TYER.id) as? ID3v24Frame
    }

    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTDRC? = null
        fb = FrameBodyTDRC()
        fb.date = TEST_YEAR

        assertEquals(ID3v24FrameId.YEAR.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(TEST_YEAR, fb.date)
    }
}
