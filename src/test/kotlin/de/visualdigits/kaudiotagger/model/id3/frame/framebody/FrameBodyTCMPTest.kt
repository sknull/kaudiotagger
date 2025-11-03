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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.fail
import java.io.File

class FrameBodyTCMPTest : AbstractTestCase() {

    companion object {
        const val COMPILATION_TRUE: String = "1"

        fun getInitialisedBody(): FrameBodyTCMP {
            val fb = FrameBodyTCMP()
            return fb
        }

        val initialisedFrame: ID3v24Frame
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.IS_COMPILATION.id)
                val fb = getInitialisedBody()
                frame.frameBody = fb
                return frame
            }
    }

    @Test
    fun testCreateID3v24Frame() {
        val frame = ID3v24Frame(ID3v24FrameId.IS_COMPILATION.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(ID3v24FrameId.IS_COMPILATION.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertTrue(
            ID3v24FrameId.isExtension(frame.getIdentifier())
        )
        Assertions.assertFalse(
            ID3v24FrameId.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testCreateID3v23Frame() {
        val frame = ID3v23Frame(ID3v23FrameId.IS_COMPILATION.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(
            ID3v23FrameId.IS_COMPILATION.id,
            frame.getIdentifier()
        )
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertTrue(
            ID3v23FrameId.isExtension(frame.getIdentifier())
        )
        Assertions.assertFalse(
            ID3v23FrameId.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testCreateID3v22Frame() {
        val frame = ID3v22Frame(ID3v22FrameId.IS_COMPILATION.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(
            ID3v22FrameId.IS_COMPILATION.id,
            frame.getIdentifier()
        )
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertTrue(
            ID3v22FrameId.isExtension(frame.getIdentifier())
        )
        Assertions.assertFalse(
            ID3v22FrameId.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp(
            "testV1.mp3",
            "test1000.mp3"
        )
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
        var mp3File =  MP3File.read(testFile)

        var frame = ID3v24Frame(ID3v24FrameId.IS_COMPILATION.id)
        frame.frameBody = FrameBodyTCMP()

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(frame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
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
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(initialisedFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert to v23 and save
        mp3File = MP3File.read(testFile)
        mp3File.setTag(ID3v23Tag(mp3File.getID3v2TagAsv24()))
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
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
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag = ID3v22Tag()
        val id3v22frame = ID3v22Frame(
            ID3v22FrameId.IS_COMPILATION.id
        )
        tag.setFrame(id3v22frame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert to v23 and save
        mp3File = MP3File.read(testFile)
        val iD3v24Tag = ID3v24Tag(mp3File.getID3v2Tag())
        mp3File.setTag(iD3v24Tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.IS_COMPILATION.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTCMP
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTCMP? = null
        fb = FrameBodyTCMP()

        assertEquals(ID3v24FrameId.IS_COMPILATION.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertTrue(fb.isCompilation())
        assertEquals(FrameBodyTCMP.IS_COMPILATION, fb.getText())
        assertEquals(COMPILATION_TRUE, fb.getFirstTextValue())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTCMP? = null
        fb = FrameBodyTCMP()

        assertEquals(ID3v24FrameId.IS_COMPILATION.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertTrue(fb.isCompilation())
        assertEquals(FrameBodyTCMP.IS_COMPILATION, fb.getText())
        assertEquals(COMPILATION_TRUE, fb.getFirstTextValue())
    }
}
