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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyPOPMTest : AbstractTestCase() {

    companion object {
        const val POPM_EMAIL: String = "paul@jaudiotagger.dev.net"
        const val POPM_RATING: Long = 167
        const val POPM_COUNTER: Long = 1000

        private const val ISSUE_72_TEST_EMAIL = "Windows Media Player 9 Series"
        private const val ISSUE_72_TEST_RATING = 255
        private const val ISSUE_72_TEST_COUNTER = 0

        fun getInitialisedFrame(): ID3v24Frame {
            val frame: ID3v24Frame = ID3v24Frame(ID3v24FrameId.POPULARIMETER.id)
            val fb: FrameBodyPOPM = getInitialisedBody()
            frame.frameBody = fb
            return frame
        }

        fun getInitialisedBody(): FrameBodyPOPM {
            val fb = FrameBodyPOPM(POPM_EMAIL, POPM_RATING, POPM_COUNTER)
            return fb
        }
    }

    @Test
    fun testCreateID3v24Frame() {
        val frame = ID3v24Frame(ID3v24FrameId.POPULARIMETER.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(ID3v24FrameId.POPULARIMETER.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(
            POPM_EMAIL,
            (frame.frameBody as FrameBodyPOPM).getEmailToUser()
        )
        Assertions.assertEquals(
            POPM_RATING,
            (frame.frameBody as FrameBodyPOPM).getRating()
        )
        Assertions.assertEquals(
            POPM_COUNTER,
            (frame.frameBody as FrameBodyPOPM).getCounter()
        )
        Assertions.assertFalse(
            ID3v24FrameId.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v24FrameId.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testCreateID3v23Frame() {
        val frame = ID3v23Frame(ID3v23FrameId.POPULARIMETER.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(ID3v23FrameId.POPULARIMETER.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(
            POPM_EMAIL,
            (frame.frameBody as FrameBodyPOPM).getEmailToUser()
        )
        Assertions.assertEquals(
            POPM_RATING,
            (frame.frameBody as FrameBodyPOPM).getRating()
        )
        Assertions.assertEquals(
            POPM_COUNTER,
            (frame.frameBody as FrameBodyPOPM).getCounter()
        )
    }

    @Test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(getInitialisedFrame())
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.POPULARIMETER.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyPOPM
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(POPM_EMAIL, body.getEmailToUser())
        Assertions.assertEquals(POPM_RATING, body.getRating())
        Assertions.assertEquals(POPM_COUNTER, body.getCounter())
    }

    @Test
    fun testSaveEmptyFrameToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        var frame = ID3v24Frame(ID3v24FrameId.POPULARIMETER.id)
        frame.frameBody = FrameBodyPOPM()

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(frame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.POPULARIMETER.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyPOPM
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals("", body.getEmailToUser())
        Assertions.assertEquals(0, body.getRating())
        Assertions.assertEquals(0, body.getCounter())
    }

    @Test
    fun testReadFileContainingPOMFrameWithoutCounter() {
        val testFile =  prependAudioToTmp(
            "Issue72.id3",
            "testV1.mp3"
        )

        val mp3File =  MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.POPULARIMETER.id) as ID3v23Frame
        val body =  frame.frameBody as FrameBodyPOPM
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(ISSUE_72_TEST_EMAIL, body.getEmailToUser())
        Assertions.assertEquals(ISSUE_72_TEST_RATING, body.getRating().toInt())
        Assertions.assertEquals(ISSUE_72_TEST_COUNTER, body.getCounter().toInt())
    }

    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyPOPM? = null
        fb = FrameBodyPOPM(POPM_EMAIL, POPM_RATING, POPM_COUNTER)

        assertEquals(ID3v24FrameId.POPULARIMETER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(POPM_EMAIL, fb.getEmailToUser())
        assertEquals(POPM_RATING, fb.getRating())
        assertEquals(POPM_COUNTER, fb.getCounter())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyPOPM? = null
        fb = FrameBodyPOPM()
        fb.setEmailToUser(POPM_EMAIL)
        fb.setRating(POPM_RATING)
        fb.setCounter(POPM_COUNTER)

        assertEquals(ID3v24FrameId.POPULARIMETER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(POPM_EMAIL, fb.getEmailToUser())
        assertEquals(POPM_RATING, fb.getRating())
        assertEquals(POPM_COUNTER, fb.getCounter())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructorWithoutCounter() {
        var fb: FrameBodyPOPM? = null
        fb = FrameBodyPOPM()
        fb.setEmailToUser(POPM_EMAIL)
        fb.setRating(POPM_RATING)

        assertEquals(ID3v24FrameId.POPULARIMETER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(POPM_EMAIL, fb.getEmailToUser())
        assertEquals(POPM_RATING, fb.getRating())
        assertEquals(0, fb.getCounter())
    }
}
