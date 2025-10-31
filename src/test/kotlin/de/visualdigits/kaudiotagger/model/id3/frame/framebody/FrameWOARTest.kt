package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

class FrameWOARTest : AbstractTestCase() {

    @Test
    fun testCreateID3v24Frame() {
        val frame = initialisedFrame
        Assertions.assertInstanceOf(FrameBodyWOAR::class.java, frame.frameBody)
        Assertions.assertEquals(ID3v24FrameId.URL_ARTIST_WEB.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, frame.frameBody?.getTextEncoding())
        Assertions.assertFalse(
            ID3v24FrameId.Companion.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v24FrameId.Companion.isSupported(frame.getIdentifier())
        )
        Assertions.assertEquals(
            NORMAL_LINK,
            (frame.frameBody as FrameBodyWOAR).getUrlLink()
        )
    }

    @Test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
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
            ?.getFrame(ID3v24FrameId.URL_ARTIST_WEB.id) as ID3v24Frame
        Assertions.assertInstanceOf(FrameBodyWOAR::class.java, frame.frameBody)
        Assertions.assertEquals(ID3v24FrameId.URL_ARTIST_WEB.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, frame.frameBody?.getTextEncoding())
        Assertions.assertFalse(
            ID3v24FrameId.Companion.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v24FrameId.Companion.isSupported(frame.getIdentifier())
        )
        Assertions.assertEquals(
            NORMAL_LINK,
            (frame.frameBody as FrameBodyWOAR).getUrlLink()
        )
    }

    @Test
    fun testCreateID3v24UnicodeFrame() {
        val frame = initialisedUnicodeFrame
        Assertions.assertInstanceOf(FrameBodyWOAR::class.java, frame.frameBody)
        Assertions.assertEquals(ID3v24FrameId.URL_ARTIST_WEB.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, frame.frameBody?.getTextEncoding())
        Assertions.assertFalse(
            ID3v24FrameId.Companion.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v24FrameId.Companion.isSupported(frame.getIdentifier())
        )
        Assertions.assertEquals(
            UNICODE_ENCODED,
            (frame.frameBody as FrameBodyWOAR).getUrlLink()
        )
    }

    // This fails beccause cant save Unicode to WOAR fields
    @Test
    fun testSaveUnicodeToFile() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.Companion.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(initialisedUnicodeFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.Companion.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.URL_ARTIST_WEB.id) as ID3v24Frame
        Assertions.assertInstanceOf(FrameBodyWOAR::class.java, frame.frameBody)
        Assertions.assertEquals(ID3v24FrameId.URL_ARTIST_WEB.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, frame.frameBody?.getTextEncoding())
        Assertions.assertFalse(
            ID3v24FrameId.Companion.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v24FrameId.Companion.isSupported(frame.getIdentifier())
        )
        Assertions.assertEquals(
            UNICODE_ENCODED,
            (frame.frameBody as FrameBodyWOAR).getUrlLink()
        )
    }

    // This fails beccause cant save Unicode to WOAR fields
    @Test
    fun testSaveUnicodeToFile2() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.Companion.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(rawUnicodeFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.Companion.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.URL_ARTIST_WEB.id) as ID3v24Frame
        Assertions.assertInstanceOf(FrameBodyWOAR::class.java, frame.frameBody)
        Assertions.assertEquals(ID3v24FrameId.URL_ARTIST_WEB.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, frame.frameBody?.getTextEncoding())
        Assertions.assertFalse(
            ID3v24FrameId.Companion.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v24FrameId.Companion.isSupported(frame.getIdentifier())
        )
        Assertions.assertEquals(
            UNICODE_ENCODED,
            (frame.frameBody as FrameBodyWOAR).getUrlLink()
        )
    }

    companion object {
        const val NORMAL_LINK =  "http:www.btinternet.com/~birdpoo/kots.htm"

        // Note cant put Japanese chars directly into code because the source code is not a UTF8 file
        const val UNICODE_LINK_START =  "http:// ja.wikipedia.org/wiki/"
        const val UNICODE_LINK_END =  "\u5742\u672c\u4e5d"
        const val UNICODE_ENCODED =  "http:// ja.wikipedia.org/wiki/%E5%9D%82%E6%9C%AC%E4%B9%9D"
        const val UNICODE_LINK =  "http:// ja.wikipedia.org/wiki/\u5742\u672c\u4e5d"

        val initialisedFrame: ID3v24Frame
            // http:// ja.wikipedia.org/wiki/%E5%9D%82%E6%9C%AC%E4%B9%9D
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.URL_ARTIST_WEB.id)
                val fb =  FrameBodyWOAR()
                fb.setUrlLink(NORMAL_LINK)
                frame.frameBody = fb
                return frame
            }

        val initialisedUnicodeFrame: ID3v24Frame
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.URL_ARTIST_WEB.id)
                val fb =  FrameBodyWOAR()
                fb.setUrlLink(
                    UNICODE_LINK_START +
                            URLEncoder.encode(
                                UNICODE_LINK_END,
                                StandardCharsets.UTF_8
                            )
                )

                // fb.setUrlLink(URLEncoder.encode(UNICODE_LINK_START+UNICODE_LINK_END,"utf8"));
                frame.frameBody = fb
                return frame
            }

        val rawUnicodeFrame: ID3v24Frame
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.URL_ARTIST_WEB.id)
                val fb =  FrameBodyWOAR()
                fb.setUrlLink(UNICODE_LINK)

                frame.frameBody = fb
                return frame
            }
    }
}