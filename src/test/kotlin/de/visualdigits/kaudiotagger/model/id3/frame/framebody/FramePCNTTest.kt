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

class FramePCNTTest : AbstractTestCase() {
    @Test
    fun testCreateID3v24Frame() {
        val frame = ID3v24Frame(ID3v24FrameId.PLAY_COUNTER.id)
        val fb = FrameBodyPCNTTest.getInitialisedBody()
        frame.frameBody = fb

        Assertions.assertEquals(ID3v24FrameId.PLAY_COUNTER.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        Assertions.assertEquals(
            FrameBodyPCNTTest.PCNT_COUNTER,
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
        val fb = FrameBodyPCNTTest.getInitialisedBody()
        frame.frameBody = fb

        Assertions.assertEquals(ID3v23FrameId.PLAY_COUNTER.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        Assertions.assertEquals(
            FrameBodyPCNTTest.PCNT_COUNTER,
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
        Assertions.assertEquals(FrameBodyPCNTTest.PCNT_COUNTER, body.getCounter())
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

    companion object {
        val initialisedFrame: ID3v24Frame
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.PLAY_COUNTER.id)
                val fb =  FrameBodyPCNTTest.getInitialisedBody()
                frame.frameBody = fb
                return frame
            }
    }
}