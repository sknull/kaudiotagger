package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
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

class FrameBodyPCNTTest : AbstractTestCase() {

    companion object {
        const val PCNT_COUNTER: Long = 1000

        fun getInitialisedBody(): FrameBodyPCNT {
            val fb = FrameBodyPCNT(PCNT_COUNTER)
            return fb
        }

        val initialisedFrame: ID3v24Frame
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.PLAY_COUNTER.id)
                val fb = getInitialisedBody()
                frame.frameBody = fb
                return frame
            }
    }

    @Test
    fun testCreateID3v24Frame() {
        val frame = ID3v24Frame(ID3v24FrameId.PLAY_COUNTER.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(ID3v24FrameId.PLAY_COUNTER.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        Assertions.assertEquals(
            PCNT_COUNTER,
            (frame.frameBody as FrameBodyPCNT).getCounter()
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
        val frame = ID3v23Frame(ID3v23FrameId.PLAY_COUNTER.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(ID3v23FrameId.PLAY_COUNTER.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        Assertions.assertEquals(
            PCNT_COUNTER,
            (frame.frameBody as FrameBodyPCNT).getCounter()
        )
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
            ?.getFrame(ID3v24FrameId.PLAY_COUNTER.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyPCNT
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        Assertions.assertEquals(PCNT_COUNTER, body.getCounter())
    }

    @Test
    fun testSaveEmptyFrameToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        var frame = ID3v24Frame(ID3v24FrameId.PLAY_COUNTER.id)
        frame.frameBody = FrameBodyPCNT()

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(frame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.PLAY_COUNTER.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyPCNT
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        Assertions.assertEquals(0, body.getCounter())
    }

    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyPCNT? = null
        fb = FrameBodyPCNT(PCNT_COUNTER)

        assertEquals(ID3v24FrameId.PLAY_COUNTER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(PCNT_COUNTER, fb.getCounter())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyPCNT? = null
        fb = FrameBodyPCNT()
        fb.setCounter(PCNT_COUNTER)

        assertEquals(ID3v24FrameId.PLAY_COUNTER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(PCNT_COUNTER, fb.getCounter())
    }
}
