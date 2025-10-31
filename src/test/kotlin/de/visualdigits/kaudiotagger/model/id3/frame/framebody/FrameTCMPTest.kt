package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v22Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v22Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.io.File

class FrameTCMPTest : AbstractTestCase() {

    @Test
    fun testCreateID3v24Frame() {
        val frame = ID3v24Frame(ID3v24FrameId.IS_COMPILATION.id)
        val fb = FrameBodyTCMPTest.getInitialisedBody()
        frame.frameBody = fb

        Assertions.assertEquals(ID3v24FrameId.IS_COMPILATION.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        Assertions.assertTrue(
            ID3v24FrameId.Companion.isExtension(frame.getIdentifier())
        )
        Assertions.assertFalse(
            ID3v24FrameId.Companion.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testCreateID3v23Frame() {
        val frame = ID3v23Frame(ID3v23FrameId.IS_COMPILATION.id)
        val fb = FrameBodyTCMPTest.getInitialisedBody()
        frame.frameBody = fb

        Assertions.assertEquals(
            ID3v23FrameId.IS_COMPILATION.id,
            frame.getIdentifier()
        )
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        Assertions.assertTrue(
            ID3v23FrameId.Companion.isExtension(frame.getIdentifier())
        )
        Assertions.assertFalse(
            ID3v23FrameId.Companion.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testCreateID3v22Frame() {
        val frame = ID3v22Frame(ID3v22FrameId.IS_COMPILATION.id)
        val fb = FrameBodyTCMPTest.getInitialisedBody()
        frame.frameBody = fb

        Assertions.assertEquals(
            ID3v22FrameId.IS_COMPILATION.id,
            frame.getIdentifier()
        )
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        Assertions.assertTrue(
            ID3v22FrameId.Companion.isExtension(frame.getIdentifier())
        )
        Assertions.assertFalse(
            ID3v22FrameId.Companion.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp(
            "testV1.mp3",
            "test1000.mp3"
        )
        var mp3File =  MP3File.Companion.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(initialisedFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.Companion.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.IS_COMPILATION.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTCMP
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    @Test
    fun testSaveEmptyFrameToFile() {
        val testFile =  copyAudioToTmp(
            "testV1.mp3",
            "test1001.mp3"
        )
        var mp3File =  MP3File.Companion.read(testFile)

        var frame = ID3v24Frame(ID3v24FrameId.IS_COMPILATION.id)
        frame.frameBody = FrameBodyTCMP()

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(frame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.Companion.read(testFile)
        frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.IS_COMPILATION.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTCMP
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    @Test
    fun testConvertV24ToV23() {
        val testFile =  copyAudioToTmp(
            "testV1.mp3",
            "test1002.mp3"
        )
        var mp3File =  MP3File.Companion.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(initialisedFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert to v23 and save
        mp3File = MP3File.Companion.read(testFile)
        mp3File.setTag(ID3v23Tag(mp3File.getID3v2TagAsv24()))
        mp3File.save()

        // Reload
        mp3File = MP3File.Companion.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.IS_COMPILATION.id) as ID3v23Frame
        val body =  frame.frameBody as FrameBodyTCMP
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    @Test
    fun testFiles() {
        val testFile = copyAudioToTmp(
            "testV1.mp3",
            "test1003.mp3"
        )
        if(!testFile.renameTo(File(testFile.parentFile, "foobar.tst"))) {
            fail("could not rename")
        }
    }

    @Test
    fun testConvertV22ToV24() {
        val testFile =  copyAudioToTmp(
            "testV1.mp3",
            "test1003.mp3"
        )
        var mp3File =  MP3File.Companion.read(testFile)

        // Create and Save
        val tag = ID3v22Tag()
        val id3v22frame = ID3v22Frame(
            ID3v22FrameId.IS_COMPILATION.id
        )
        tag.setFrame(id3v22frame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert to v23 and save
        mp3File = MP3File.Companion.read(testFile)
        val iD3v24Tag = ID3v24Tag(mp3File.getID3v2Tag())
        mp3File.setTag(iD3v24Tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.Companion.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.IS_COMPILATION.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTCMP
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    companion object {
        val initialisedFrame: ID3v24Frame
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.IS_COMPILATION.id)
                val fb =  FrameBodyTCMPTest.getInitialisedBody()
                frame.frameBody = fb
                return frame
            }
    }
}