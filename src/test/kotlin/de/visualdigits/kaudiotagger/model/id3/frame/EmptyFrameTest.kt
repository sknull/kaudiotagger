package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyWOAF
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyWORS
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v22Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EmptyFrameTest : AbstractTestCase() {

    @Test
    fun testWriteID3v23TagWithEmptyFrameFirst() {
        val testFile = copyAudioToTmp("testV1Cbr128ID3v2.mp3")

        var mp3File: MP3File? = null
        mp3File = MP3File.read(testFile)
        assertTrue(mp3File.hasID3v2Tag())
        assertEquals(8, mp3File.getID3v2Tag()?.getFieldCount())
        val emptyFrame = ID3v23Frame(
            ID3v23FrameId.URL_FILE_WEB.id
        )
        (emptyFrame.frameBody as FrameBodyWOAF).setUrlLink("")
        val nonemptyFrame = ID3v23Frame(
            ID3v23FrameId.URL_OFFICIAL_RADIO.id
        )
        (nonemptyFrame.frameBody as FrameBodyWORS).setUrlLink("something")

        mp3File.getID3v2Tag()?.setFrame(emptyFrame)
        mp3File.getID3v2Tag()?.setFrame(nonemptyFrame)
        mp3File.save()
        mp3File = MP3File.read(testFile)
        assertTrue(mp3File.hasID3v2Tag())

        //Empty frame is disregarded, but doesnt prevent retrievel of valid WORS frame, so count of frame increased by
        //one
        assertEquals(9, mp3File.getID3v2Tag()?.getFieldCount())
        assertEquals(0, mp3File.getID3v2Tag()?.getFields("WOAF")?.size)
        assertEquals(1, mp3File.getID3v2Tag()?.getFields("WORS")?.size)
    }

    @Test
    fun testWriteID3v24TagWithEmptyFrameFirst() {
        val testFile = copyAudioToTmp("testV1Cbr128ID3v2.mp3")

        var mp3File: MP3File? = null
        mp3File = MP3File.read(testFile)

        //Convert to v24
        mp3File.setTag(ID3v24Tag(mp3File.getID3v2Tag()))
        mp3File.save()

        assertTrue(mp3File.hasID3v2Tag())
        assertEquals(8, mp3File.getID3v2Tag()?.getFieldCount())

        val emptyFrame = ID3v24Frame(
            ID3v24FrameId.URL_FILE_WEB.id
        )
        (emptyFrame.frameBody as FrameBodyWOAF).setUrlLink("")
        val nonemptyFrame = ID3v24Frame(
            ID3v24FrameId.URL_OFFICIAL_RADIO.id
        )
        (nonemptyFrame.frameBody as FrameBodyWORS).setUrlLink("something")

        mp3File.getID3v2Tag()?.setFrame(emptyFrame)
        mp3File.getID3v2Tag()?.setFrame(nonemptyFrame)
        mp3File.save()
        mp3File = MP3File.read(testFile)
        assertTrue(mp3File.hasID3v2Tag())

        //Empty frame is disregarded, but doesnt prevent retrievel of valid WORS frame, so count of frame increased by
        //one
        assertEquals(9, mp3File.getID3v2Tag()?.getFieldCount())
        assertEquals(0, mp3File.getID3v2Tag()?.getFields("WOAF")?.size)
        assertEquals(1, mp3File.getID3v2Tag()?.getFields("WORS")?.size)
    }

    @Test
    fun testWriteID3v22TagWithEmptyFrameFirst() {
        val testFile = copyAudioToTmp("testV1Cbr128ID3v2.mp3")

        var mp3File: MP3File? = null
        mp3File = MP3File.read(testFile)

        //Convert to v24
        mp3File.setTag(ID3v22Tag(mp3File.getID3v2Tag()))
        mp3File.save()

        assertTrue(mp3File.hasID3v2Tag())
        assertEquals(8, mp3File.getID3v2Tag()?.getFieldCount())

        val emptyFrame: ID3v22Frame = ID3v22Frame(
            ID3v22FrameId.URL_FILE_WEB.id
        )
        (emptyFrame.frameBody as FrameBodyWOAF).setUrlLink("")
        val nonemptyFrame: ID3v22Frame = ID3v22Frame(
            ID3v22FrameId.URL_OFFICIAL_RADIO.id
        )
        (nonemptyFrame.frameBody as FrameBodyWORS).setUrlLink("something")

        mp3File.getID3v2Tag()?.setFrame(emptyFrame)
        mp3File.getID3v2Tag()?.setFrame(nonemptyFrame)
        mp3File.save()
        mp3File = MP3File.read(testFile)
        assertTrue(mp3File.hasID3v2Tag())

        //Empty frame is disregarded, but doesnt prevent retrievel of valid WORS frame, so count of frame increased by
        //one
        assertEquals(9, mp3File.getID3v2Tag()?.getFieldCount())
        assertEquals(0, mp3File.getID3v2Tag()?.getFields("WAF")?.size)
        assertEquals(1, mp3File.getID3v2Tag()?.getFields("WRS")?.size)
    }
}
