package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyPOPMTest : AbstractTestCase() {

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

    companion object {
        const val POPM_EMAIL: String = "paul@jaudiotagger.dev.net"
        const val POPM_RATING: Long = 167
        const val POPM_COUNTER: Long = 1000

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
}
