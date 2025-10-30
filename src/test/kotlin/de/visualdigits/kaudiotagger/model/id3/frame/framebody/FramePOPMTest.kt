package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FramePOPMTest : AbstractTestCase() {
    @Test
    fun testCreateID3v24Frame() {
        val frame = ID3v24Frame(ID3v24FrameId.POPULARIMETER.id)
        val fb = FrameBodyPOPMTest.getInitialisedBody()
        frame.frameBody = fb

        Assertions.assertEquals(ID3v24FrameId.POPULARIMETER.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        Assertions.assertEquals(
            FrameBodyPOPMTest.POPM_EMAIL,
            (frame.frameBody as FrameBodyPOPM).getEmailToUser()
        )
        Assertions.assertEquals(
            FrameBodyPOPMTest.POPM_RATING,
            (frame.frameBody as FrameBodyPOPM).getRating()
        )
        Assertions.assertEquals(
            FrameBodyPOPMTest.POPM_COUNTER,
            (frame.frameBody as FrameBodyPOPM).getCounter()
        )
        Assertions.assertFalse(
            ID3v24FrameId.Companion.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v24FrameId.Companion.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testCreateID3v23Frame() {
        val frame = ID3v23Frame(ID3v23FrameId.POPULARIMETER.id)
        val fb = FrameBodyPOPMTest.getInitialisedBody()
        frame.frameBody = fb

        Assertions.assertEquals(ID3v23FrameId.POPULARIMETER.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        Assertions.assertEquals(
            FrameBodyPOPMTest.POPM_EMAIL,
            (frame.frameBody as FrameBodyPOPM).getEmailToUser()
        )
        Assertions.assertEquals(
            FrameBodyPOPMTest.POPM_RATING,
            (frame.frameBody as FrameBodyPOPM).getRating()
        )
        Assertions.assertEquals(
            FrameBodyPOPMTest.POPM_COUNTER,
            (frame.frameBody as FrameBodyPOPM).getCounter()
        )
    }

    @Test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.Companion.read(testFile)

        //Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(initialisedFrame)
        mp3File.setTag(tag)
        mp3File.save()

        //Reload
        mp3File = MP3File.Companion.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.POPULARIMETER.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyPOPM
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        Assertions.assertEquals(FrameBodyPOPMTest.POPM_EMAIL, body.getEmailToUser())
        Assertions.assertEquals(FrameBodyPOPMTest.POPM_RATING, body.getRating())
        Assertions.assertEquals(FrameBodyPOPMTest.POPM_COUNTER, body.getCounter())
    }

    @Test
    fun testSaveEmptyFrameToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.Companion.read(testFile)

        var frame = ID3v24Frame(ID3v24FrameId.POPULARIMETER.id)
        frame.frameBody = FrameBodyPOPM()

        //Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(frame)
        mp3File.setTag(tag)
        mp3File.save()

        //Reload
        mp3File = MP3File.Companion.read(testFile)
        frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.POPULARIMETER.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyPOPM
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        Assertions.assertEquals("", body.getEmailToUser())
        Assertions.assertEquals(0, body.getRating())
        Assertions.assertEquals(0, body.getCounter())
    }

    @Test
    fun testReadFileContainingPOMFrameWithoutCounter() {
        val testFile =  prependAudioToTmp(
            "Issue72.id3",
            "testV1.mp3"
        )

        val mp3File =  MP3File.Companion.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.POPULARIMETER.id) as ID3v23Frame
        val body =  frame.frameBody as FrameBodyPOPM
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        Assertions.assertEquals(ISSUE_72_TEST_EMAIL, body.getEmailToUser())
        Assertions.assertEquals(ISSUE_72_TEST_RATING, body.getRating().toInt())
        Assertions.assertEquals(ISSUE_72_TEST_COUNTER, body.getCounter().toInt())
    }

    companion object {
        private const val ISSUE_72_TEST_EMAIL = "Windows Media Player 9 Series"
        private const val ISSUE_72_TEST_RATING = 255
        private const val ISSUE_72_TEST_COUNTER = 0

        val initialisedFrame: ID3v24Frame
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.POPULARIMETER.id)
                val fb =  FrameBodyPOPMTest.getInitialisedBody()
                frame.frameBody = fb
                return frame
            }
    }
}