package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FrameBodyTXXXTest : AbstractTestCase() {

    companion object {
        val TXXX_TEST_DESC: String = FrameBodyTXXX.BARCODE

        const val TXXX_TEST_STRING: String = "0123456789"

        fun getInitialisedBody(): FrameBodyTXXX {
            // Text Encoding doesnt matter until written to file
            val fb = FrameBodyTXXX(
                TextEncoding.ISO_8859_1.id,
                TXXX_TEST_STRING,
                TXXX_TEST_DESC
            )
            return fb
        }

        fun getV22InitialisedFrame(): ID3v22Frame {
            val frame = ID3v22Frame(ID3v22FrameId.USER_DEFINED_INFO.id)
            val fb = getInitialisedBody()
            frame.frameBody = fb
            return frame
        }

        fun getV23InitialisedFrame(): ID3v23Frame {
            val frame = ID3v23Frame(ID3v23FrameId.USER_DEFINED_INFO.id)
            val fb = getInitialisedBody()
            frame.frameBody = fb
            return frame
        }

        fun getV24InitialisedFrame(): ID3v24Frame {
            val frame = ID3v24Frame(ID3v24FrameId.USER_DEFINED_INFO.id)
            val fb = getInitialisedBody()
            frame.frameBody = fb
            return frame
        }
    }

    @Test
    fun testCreateID3v24Frame() {
        val frame = getV24InitialisedFrame()

        assertEquals(
            ID3v24FrameId.USER_DEFINED_INFO.id,
            frame.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, frame.frameBody?.getTextEncoding())
        assertFalse(
            ID3v24FrameId.isExtension(frame.getIdentifier())
        )
        assertTrue(
            ID3v24FrameId.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testCreateID3v23Frame() {

        val frame = getV23InitialisedFrame()

        assertEquals(
            ID3v23FrameId.USER_DEFINED_INFO.id,
            frame.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, frame.frameBody?.getTextEncoding())
        assertFalse(
            ID3v23FrameId.isExtension(frame.getIdentifier())
        )
        assertTrue(
            ID3v23FrameId.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testCreateID3v22Frame() {

        val frame = getV22InitialisedFrame()

        assertEquals(
            ID3v22FrameId.USER_DEFINED_INFO.id,
            frame.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, frame.frameBody?.getTextEncoding())
        assertFalse(
            ID3v22FrameId.isExtension(frame.getIdentifier())
        )
        assertTrue(
            ID3v22FrameId.isSupported(frame.getIdentifier())
        )
    }

    @Test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v24Tag()
        tag.setFrame(getV24InitialisedFrame())
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.USER_DEFINED_INFO.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTXXX
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    @Test
    fun testConvertV24ToV23() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v24Tag()
        tag.setFrame(getV24InitialisedFrame())

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
            ?.getFrame(ID3v23FrameId.USER_DEFINED_INFO.id) as ID3v23Frame
        val body =  frame.frameBody as FrameBodyTXXX
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(TXXX_TEST_DESC, body.getText())
    }

    @Test
    fun testConvertV24ToV22() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v24Tag()
        tag.setFrame(getV24InitialisedFrame())

        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert to v22 and save
        mp3File = MP3File.read(testFile)
        mp3File.setTag(ID3v22Tag(mp3File.getID3v2TagAsv24()))
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v22FrameId.USER_DEFINED_INFO.id) as ID3v22Frame
        val body =  frame.frameBody as FrameBodyTXXX
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(TXXX_TEST_DESC, body.getText())
    }

    @Test
    fun testConvertV23ToV22() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v23Tag()
        tag.setFrame(getV23InitialisedFrame())

        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert from v23 to v22 and save
        mp3File = MP3File.read(testFile)
        mp3File.setTag(ID3v22Tag(mp3File.getID3v2Tag()))
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v22FrameId.USER_DEFINED_INFO.id) as ID3v22Frame
        val body =  frame.frameBody as FrameBodyTXXX
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(TXXX_TEST_DESC, body.getText())
    }

    @Test
    fun testConvertV22ToV24() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v22Tag()

        //..Notes (uses v22Frame but frame body will be the v23/24 version)
        val id3v22frame =  ID3v22Frame(ID3v22FrameId.USER_DEFINED_INFO.id)
        (id3v22frame.frameBody as FrameBodyTXXX).setText(TXXX_TEST_STRING)
        tag.setFrame(id3v22frame)

        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert from v22 to v24 and save
        mp3File = MP3File.read(testFile)
        mp3File.setTag(ID3v24Tag(mp3File.getID3v2Tag()))
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.USER_DEFINED_INFO.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTXXX
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(TXXX_TEST_STRING, body.getText())
    }

    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTXXX? = null
        fb = FrameBodyTXXX(
            TextEncoding.ISO_8859_1.id,
            TXXX_TEST_STRING,
            TXXX_TEST_DESC
        )

        assertEquals(ID3v24FrameId.USER_DEFINED_INFO.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(TXXX_TEST_STRING, fb.getDescription())
        assertEquals(TXXX_TEST_DESC, fb.getFirstTextValue())
    }
}
