package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.model.id3.types.Languages
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.nio.file.Files

class FrameBodyUSLTTest : AbstractTestCase() {

    @Test
    fun testReadULST() {
        val testFile =  copyAudioToTmp("test23.mp3")

        val mp3File =  MP3File.read(testFile)
        val v24frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.UNSYNC_LYRICS.id) as ID3v24Frame

        // Old method
        val lyricsBody =  v24frame.frameBody as FrameBodyUSLT
        assertEquals(589, lyricsBody.getFirstTextValue()?.length)
        assertEquals("", lyricsBody.getDescription())
        assertEquals("   ", lyricsBody.getLanguage())

        // New Method should be same length
        val file = MP3File.read(testFile)
        assertEquals(589, file.getTag()?.getFirst(GenericFieldKey.LYRICS)?.length)
    }

    @Test
    fun testWriteULSTID3v24() {
        val testFile =  copyAudioToTmp("test23.mp3")

        var mp3File =  MP3File.read(testFile)
        var v24frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.UNSYNC_LYRICS.id) as ID3v24Frame

        // Get lyrics frame and modify
        var lyricsBody =  v24frame.frameBody as FrameBodyUSLT
        assertEquals(589, lyricsBody.getFirstTextValue()?.length)
        assertEquals(1, lyricsBody.getTextEncoding())
        lyricsBody.setLanguage(Languages.DEFAULT_ID)
        lyricsBody.setDescription("description")
        lyricsBody.setLyric("lyric1")
        mp3File.save()

        // Check normal values
        mp3File = MP3File.read(testFile)
        v24frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.UNSYNC_LYRICS.id) as ID3v24Frame
        lyricsBody = v24frame.frameBody as FrameBodyUSLT
        assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage())
        assertEquals("description", lyricsBody.getDescription())
        assertEquals("lyric1", lyricsBody.getLyric())
        assertEquals(1, lyricsBody.getTextEncoding())

        // Now force to UTF-16
        lyricsBody.setLyric("lyric\u111F")
        mp3File.save()
        mp3File = MP3File.read(testFile)
        v24frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.UNSYNC_LYRICS.id) as ID3v24Frame
        lyricsBody = v24frame.frameBody as FrameBodyUSLT
        assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage())
        assertEquals("description", lyricsBody.getDescription())
        assertEquals("lyric\u111F", lyricsBody.getLyric())
        assertEquals(1, lyricsBody.getTextEncoding())

        // Now check UTf16 with empty description
        lyricsBody.setDescription("")
        mp3File.save()
        mp3File = MP3File.read(testFile)
        v24frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.UNSYNC_LYRICS.id) as ID3v24Frame
        lyricsBody = v24frame.frameBody as FrameBodyUSLT
        mp3File.save()
        assertEquals("", lyricsBody.getDescription())
        assertEquals(1, lyricsBody.getTextEncoding())
    }

    @Test
    fun testWriteULSTID3v23() {
        val testFile =  copyAudioToTmp("testV1.mp3")

        var mp3File =  MP3File.read(testFile)
        val tag =  ID3v23Tag()
        mp3File.setTag(tag)

        // Create lyrics frame and modify
        var lyricsBody =  FrameBodyUSLT()
        lyricsBody.setLanguage(Languages.DEFAULT_ID)
        lyricsBody.setDescription("description")
        lyricsBody.setLyric("lyric1")
        var v23frame =  ID3v23Frame(ID3v23FrameId.UNSYNC_LYRICS.id)
        v23frame.frameBody = lyricsBody
        tag.setFrame(v23frame)
        mp3File.save()

        // Check normal values
        mp3File = MP3File.read(testFile)
        v23frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.UNSYNC_LYRICS.id) as ID3v23Frame
        lyricsBody = v23frame.frameBody as FrameBodyUSLT
        assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage())
        assertEquals("description", lyricsBody.getDescription())
        assertEquals("lyric1", lyricsBody.getLyric())
        assertEquals(0, lyricsBody.getTextEncoding())

        // Change to another ISO8859value
        lyricsBody.setLyric("lyric")
        mp3File.save()
        mp3File = MP3File.read(testFile)
        v23frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.UNSYNC_LYRICS.id) as ID3v23Frame
        lyricsBody = v23frame.frameBody as FrameBodyUSLT
        assertEquals("lyric", lyricsBody.getLyric())
        assertEquals(0, lyricsBody.getTextEncoding())

        // Now force to UTF-16
        lyricsBody.setLyric("lyric\u111F")
        mp3File.save()
        mp3File = MP3File.read(testFile)
        v23frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.UNSYNC_LYRICS.id) as ID3v23Frame
        lyricsBody = v23frame.frameBody as FrameBodyUSLT
        assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage())
        assertEquals("description", lyricsBody.getDescription())
        assertEquals("lyric\u111F", lyricsBody.getLyric())
        assertEquals(1, lyricsBody.getTextEncoding())

        // Now check UTf16 with empty description
        lyricsBody.setDescription("")
        mp3File.save()
        mp3File = MP3File.read(testFile)
        v23frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.UNSYNC_LYRICS.id) as ID3v23Frame
        lyricsBody = v23frame.frameBody as FrameBodyUSLT
        mp3File.save()
        assertEquals("", lyricsBody.getDescription())
        assertEquals(1, lyricsBody.getTextEncoding())
    }

    @Test
    fun testWriteULSTID3v23Test2() {
        val testFile =  copyAudioToTmp("testV1.mp3")

        var mp3File =  MP3File.read(testFile)
        val tag =  ID3v23Tag()
        mp3File.setTag(tag)

        // Create lyrics frame and modify
        var lyricsBody =  FrameBodyUSLT()
        lyricsBody.setLanguage(Languages.DEFAULT_ID)
        lyricsBody.setDescription("")
        lyricsBody.setLyric("lyric1\u111f")
        var v23frame =  ID3v23Frame(ID3v23FrameId.UNSYNC_LYRICS.id)
        v23frame.frameBody = lyricsBody
        tag.setFrame(v23frame)
        mp3File.save()

        // Check normal values
        mp3File = MP3File.read(testFile)
        v23frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.UNSYNC_LYRICS.id) as ID3v23Frame
        lyricsBody = v23frame.frameBody as FrameBodyUSLT
        assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage())
        assertEquals("", lyricsBody.getDescription())
        assertEquals("lyric1\u111f", lyricsBody.getLyric())
        assertEquals(1, lyricsBody.getTextEncoding())
    }

    /**
     * V23 tag created as v24
     *
     * @throws Exception
     */
    @Test
    fun testWriteULSTID3v23Test3() {
        val testFile =  copyAudioToTmp("testV1.mp3")

        var mp3File =  MP3File.read(testFile)
        val tag =  ID3v23Tag()
        mp3File.setTag(tag)

        // Create lyrics frame and modify
        var lyricsBody =  FrameBodyUSLT()
        lyricsBody.setLanguage(Languages.DEFAULT_ID)
        lyricsBody.setDescription("")
        lyricsBody.setLyric("lyric1\u111f")
        val v24frame =  ID3v24Frame(ID3v24FrameId.UNSYNC_LYRICS.id)
        v24frame.frameBody = lyricsBody
        tag.setFrame(v24frame)
        mp3File.save()

        // Check normal values
        mp3File = MP3File.read(testFile)
        val v23frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.UNSYNC_LYRICS.id) as ID3v23Frame
        lyricsBody = v23frame.frameBody as FrameBodyUSLT
        assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage())
        assertEquals("", lyricsBody.getDescription())
        assertEquals("lyric1\u111f", lyricsBody.getLyric())
        assertEquals(1, lyricsBody.getTextEncoding())
    }

    /**
     * V23 tag created as v24
     *
     * @throws Exception
     */
    @Test
    fun testWriteULSTID3v23Test4() {
        val testFile =  copyAudioToTmp("testV1.mp3")

        var mp3File =  MP3File.read(testFile)
        val tag =  ID3v23Tag()
        mp3File.setTag(tag)

        // Create lyrics frame and modify
        var lyricsBody =  FrameBodyUSLT()
        lyricsBody.setLanguage(Languages.DEFAULT_ID)
        lyricsBody.setDescription("")
        lyricsBody.setLyric("lyric1")
        val v24frame =  ID3v24Frame(ID3v24FrameId.UNSYNC_LYRICS.id)
        v24frame.frameBody = lyricsBody
        tag.setFrame(v24frame)
        mp3File.save()

        // Check normal values
        mp3File = MP3File.read(testFile)
        var v23frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.UNSYNC_LYRICS.id) as ID3v23Frame
        lyricsBody = v23frame.frameBody as FrameBodyUSLT
        assertEquals(Languages.DEFAULT_ID, lyricsBody.getLanguage())
        assertEquals("", lyricsBody.getDescription())
        assertEquals("lyric1", lyricsBody.getLyric())
        assertEquals(0, lyricsBody.getTextEncoding())

        // Change Encoding
        lyricsBody.setTextEncoding(1.toByte())
        mp3File.save()

        mp3File = MP3File.read(testFile)
        v23frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.UNSYNC_LYRICS.id) as ID3v23Frame
        assertEquals(1, lyricsBody.getTextEncoding())
    }

    @Test
    fun testWriteUnicodeBody() {
        val fb = FrameBodyUSLT(
            textEncoding = TextEncoding.UTF_16.id,
            language = "eng",
            description = "",
            text = UTF16_REQUIRED
        )
        val baos = ByteArrayOutputStream()
        fb.write(baos)
        val fos = FileOutputStream(Files.createTempFile("kaudiotagger", "TEST.TXT").toFile())
        fos.write(baos.toByteArray())
        val frameBody: ByteArray = baos.toByteArray()
        val correctBits = makeByteArray(
            intArrayOf(
                0x01,
                'e'.code,
                'n'.code,
                'g'.code,
                0xff,
                0xfe,
                0x00,
                0x00,
                0xff,
                0xfe,
                0x26,
                0x20,
            )
        )
        assertArrayEquals(correctBits, frameBody)
    }

    private fun cmp(a: ByteArray, b: ByteArray): String? {
        if (a.size != b.size) {
            return ("length of byte arrays differ (${a.size}!=${b.size})")
        }
        for (i in a.indices) {
            if (a[i] != b[i]) {
                return ("byte arrays differ at offset $i (${a[i]}!=${b[i]})")
            }
        }
        return null
    }

    private fun makeByteArray(ints: IntArray): ByteArray {
        val bs = ByteArray(ints.size)
        for (i in ints.indices) {
            bs[i] = ints[i].toByte()
        }
        return bs
    }

    companion object {
        const val UTF16_REQUIRED: String = "\u2026"
    }
}
