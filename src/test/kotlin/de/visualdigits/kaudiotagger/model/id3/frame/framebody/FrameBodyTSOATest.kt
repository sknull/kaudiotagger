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

class FrameBodyTSOATest : AbstractTestCase() {

    companion object {
        const val ALBUM_SORT: String = "albumsort"

        fun getInitialisedBody(): FrameBodyTSOA {
            val fb = FrameBodyTSOA()
            fb.setText(ALBUM_SORT)
            return fb
        }

        val initialisedFrame: ID3v24Frame
            get() {
                val frame =  ID3v24Frame(ID3v24FrameId.ALBUM_SORT_ORDER.id)
                val fb = getInitialisedBody()
                frame.frameBody = fb
                return frame
            }

        val v23InitialisedFrame: ID3v23Frame
            get() {
                val frame =  ID3v23Frame(ID3v23FrameId.ALBUM_SORT_ORDER_MUSICBRAINZ.id)
                val fb =  FrameBodyXSOATest.getInitialisedBody()
                frame.frameBody = fb
                return frame
            }
    }

    @Test
    fun testCreateID3v24Frame() {
        val frame = ID3v24Frame(ID3v24FrameId.ALBUM_SORT_ORDER.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(ID3v24FrameId.ALBUM_SORT_ORDER.id, frame.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertFalse(
            ID3v24FrameId.isExtension(frame.getIdentifier())
        )
        assertTrue(
            ID3v24FrameId.isSupported(frame.getIdentifier())
        )
        assertEquals(ALBUM_SORT, fb.getText())
    }

    @Test
    fun testCreateID3v23ITunesFrame() {
        val frame = ID3v23Frame(ID3v23FrameId.ALBUM_SORT_ORDER_ITUNES.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(
            ID3v23FrameId.ALBUM_SORT_ORDER_ITUNES.id,
            frame.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertTrue(
            ID3v23FrameId.isExtension(frame.getIdentifier())
        )
        assertFalse(
            ID3v23FrameId.isSupported(frame.getIdentifier())
        )
        assertEquals(ALBUM_SORT, fb.getText())
    }

    @Test
    fun testCreateID3v23MusicBrainzFrame() {
        val frame = ID3v23Frame(ID3v23FrameId.ALBUM_SORT_ORDER_MUSICBRAINZ.id)
        val fb = FrameBodyXSOATest.getInitialisedBody()
        frame.frameBody = fb

        assertEquals(
            ID3v23FrameId.ALBUM_SORT_ORDER_MUSICBRAINZ.id,
            frame.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertTrue(
            ID3v23FrameId.isExtension(frame.getIdentifier())
        )
        assertFalse(
            ID3v23FrameId.isSupported(frame.getIdentifier())
        )
        assertEquals(ALBUM_SORT, fb.getText())
    }

    @Test
    fun testCreateID3v22Frame() {
        val frame = ID3v22Frame(ID3v22FrameId.ALBUM_SORT_ORDER_ITUNES.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(
            ID3v22FrameId.ALBUM_SORT_ORDER_ITUNES.id,
            frame.getIdentifier()
        )
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertTrue(
            ID3v22FrameId.isExtension(frame.getIdentifier())
        )
        assertFalse(
            ID3v22FrameId.isSupported(frame.getIdentifier())
        )
        assertEquals(ALBUM_SORT, fb.getText())
    }

    @Test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v24Tag()
        tag.setFrame(initialisedFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.ALBUM_SORT_ORDER.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTSOA
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    @Test
    fun testSaveEmptyFrameToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        var frame =  ID3v24Frame(ID3v24FrameId.ALBUM_SORT_ORDER.id)
        frame.frameBody = FrameBodyTSOA()

        // Create and Save
        val tag =  ID3v24Tag()
        tag.setFrame(frame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.ALBUM_SORT_ORDER.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTSOA
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    @Test
    fun testConvertV24ToV23() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v24Tag()
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
            ?.getFrame(ID3v23FrameId.ALBUM_SORT_ORDER_ITUNES.id) as ID3v23Frame
        val body =  frame.frameBody as FrameBodyTSOA
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(ALBUM_SORT, body.getText())
    }

    @Test
    fun testConvertV24ToV22() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v24Tag()
        tag.setFrame(initialisedFrame)

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
            ?.getFrame(ID3v22FrameId.ALBUM_SORT_ORDER_ITUNES.id) as ID3v22Frame
        val body =  frame.frameBody as FrameBodyTSOA
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(ALBUM_SORT, body.getText())
    }

    @Test
    fun testConvertV23ITunesToV22() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v23Tag()
        tag.setFrame(initialisedFrame)

        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert from v23 to v22 and save
        mp3File = MP3File.read(testFile)
        mp3File.getID3v2Tag()?.also { tag -> mp3File.setTag(ID3v22Tag(tag)) }
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v22FrameId.ALBUM_SORT_ORDER_ITUNES.id) as ID3v22Frame
        val body =  frame.frameBody as FrameBodyTSOA
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(ALBUM_SORT, body.getText())
    }

    @Test
    fun testConvertV23MusicBrainzToV22() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v23Tag()
        tag.setFrame(v23InitialisedFrame)

        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert from v23 to v22 and save
        mp3File = MP3File.read(testFile)
        mp3File.getID3v2Tag()?.also { tag -> mp3File.setTag(ID3v22Tag(tag)) }
        mp3File.save()

        // Reload will be converted to same TST version for v22
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v22FrameId.ALBUM_SORT_ORDER_ITUNES.id) as ID3v22Frame
        val body =  frame.frameBody as FrameBodyTSOA
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(ALBUM_SORT, body.getText())
    }

    @Test
    fun testConvertV22ToV24() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag =  ID3v22Tag()

        //..Notes (uses v22Frame but frame body will be the v23/24 version)
        val id3v22frame =  ID3v22Frame(ID3v22FrameId.ALBUM_SORT_ORDER_ITUNES.id)
        (id3v22frame.frameBody as FrameBodyTSOA).setText(
            ALBUM_SORT
        )
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
            ?.getFrame(ID3v24FrameId.ALBUM_SORT_ORDER.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTSOA
        assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(ALBUM_SORT, body.getText())
    }

    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTSOA? = null
        fb = FrameBodyTSOA(
            TextEncoding.ISO_8859_1.id,
            ALBUM_SORT
        )

        assertEquals(ID3v24FrameId.ALBUM_SORT_ORDER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ALBUM_SORT, fb.getText())
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTSOA? = null
        fb = FrameBodyTSOA()
        fb.setText(ALBUM_SORT)

        assertEquals(ID3v24FrameId.ALBUM_SORT_ORDER.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(ALBUM_SORT, fb.getText())
    }
}
