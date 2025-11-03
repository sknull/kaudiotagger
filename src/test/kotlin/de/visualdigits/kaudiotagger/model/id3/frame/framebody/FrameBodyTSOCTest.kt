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
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class FrameBodyTSOCTest : AbstractTestCase() {

    companion object {
        const val COMPOSER_SORT: String = "composersort"

        fun getInitialisedBody(): FrameBodyTSOC {
            val fb = FrameBodyTSOC()
            fb.setText(COMPOSER_SORT)
            return fb
        }

        val v24InitialisedFrame: ID3v24Frame
            get() {
                val frame =  ID3v24Frame(ID3v24FrameId.COMPOSER_SORT_ORDER_ITUNES.id)
                val fb = getInitialisedBody()
                frame.frameBody = fb
                return frame
            }

        val v23InitialisedFrame: ID3v23Frame
            get() {
                val frame =  ID3v23Frame(ID3v23FrameId.COMPOSER_SORT_ORDER_ITUNES.id)
                val fb = getInitialisedBody()
                frame.frameBody = fb
                return frame
            }

        val v22InitialisedFrame: ID3v22Frame
            get() {
                val frame =  ID3v22Frame(ID3v22FrameId.COMPOSER_SORT_ORDER_ITUNES.id)
                val fb = getInitialisedBody()
                frame.frameBody = fb
                return frame
            }
    }

    @Test
    fun testCreateID3v24Frame() {
        val frame = v24InitialisedFrame

        assertEquals(
            ID3v24FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            frame.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, frame.frameBody?.getTextEncoding())
        assertTrue(
            ID3v24FrameId.isExtension(frame.getIdentifier())
        )
        assertFalse(
            ID3v24FrameId.isSupported(frame.getIdentifier())
        )
        assertEquals(
            COMPOSER_SORT,
            (frame.frameBody as FrameBodyTSOC).getText()
        )
    }

    @Test
    fun testCreateID3v23Frame() {
        val frame = v23InitialisedFrame

        assertEquals(
            ID3v23FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            frame.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, frame.frameBody?.getTextEncoding())
        assertTrue(
            ID3v23FrameId.isExtension(frame.getIdentifier())
        )
        assertFalse(
            ID3v23FrameId.isSupported(frame.getIdentifier())
        )
        assertEquals(
            COMPOSER_SORT,
            (frame.frameBody as FrameBodyTSOC).getText()
        )
    }

    @Test
    fun testCreateID3v22Frame() {
        val frame = v22InitialisedFrame

        assertEquals(
            ID3v22FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            frame.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, frame.frameBody?.getTextEncoding())
        assertTrue(
            ID3v22FrameId.isExtension(frame.getIdentifier())
        )
        assertFalse(
            ID3v22FrameId.isSupported(frame.getIdentifier())
        )
        assertEquals(
            COMPOSER_SORT,
            (frame.frameBody as FrameBodyTSOC).getText()
        )
    }

    @Test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v24Tag()
        tag.setFrame(v24InitialisedFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.COMPOSER_SORT_ORDER_ITUNES.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTSOC
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    @Test
    fun testConvertV24ToV23() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v24Tag()
        tag.setFrame(v24InitialisedFrame)

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
            ?.getFrame(ID3v23FrameId.COMPOSER_SORT_ORDER_ITUNES.id) as ID3v23Frame
        val body =  frame.frameBody as FrameBodyTSOC
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(COMPOSER_SORT, body.getText())
    }

    @Test
    fun testConvertV24ToV22() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v24Tag()
        tag.setFrame(v24InitialisedFrame)

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
            ?.getFrame(ID3v22FrameId.COMPOSER_SORT_ORDER_ITUNES.id) as ID3v22Frame
        val body =  frame.frameBody as FrameBodyTSOC
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(COMPOSER_SORT, body.getText())
    }

    @Test
    fun testConvertV23ToV22() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v23Tag()
        tag.setFrame(v23InitialisedFrame)

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
            ?.getFrame(ID3v22FrameId.COMPOSER_SORT_ORDER_ITUNES.id) as ID3v22Frame
        val body =  frame.frameBody as FrameBodyTSOC
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(COMPOSER_SORT, body.getText())
    }

    @Test
    fun testConvertV22ToV24() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v22Tag()

        //..Notes (uses v22Frame but frame body will be the v23/24 version)
        val id3v22frame =  ID3v22Frame(ID3v22FrameId.COMPOSER_SORT_ORDER_ITUNES.id)
        (id3v22frame.frameBody as FrameBodyTSOC).setText(COMPOSER_SORT)
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
            ?.getFrame(ID3v24FrameId.COMPOSER_SORT_ORDER_ITUNES.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTSOC
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(COMPOSER_SORT, body.getText())
    }

    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTSOC? = null
        fb = FrameBodyTSOC(
            TextEncoding.ISO_8859_1.id,
            COMPOSER_SORT
        )

        assertEquals(
            ID3v24FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            fb!!.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(COMPOSER_SORT, fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTSOC? = null
        fb = FrameBodyTSOC()
        fb.setText(COMPOSER_SORT)

        assertEquals(
            ID3v24FrameId.COMPOSER_SORT_ORDER_ITUNES.id,
            fb!!.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(COMPOSER_SORT, fb.getText())
    }
}
