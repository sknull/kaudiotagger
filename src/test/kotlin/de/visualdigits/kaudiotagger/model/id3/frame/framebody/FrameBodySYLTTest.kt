package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class FrameBodySYLTTest : AbstractTestCase() {

    @Test
    fun testWriteFrame() {
        val testFile =  copyAudioToTmp("testV1.mp3")
        var mp3File =  MP3File.read(testFile)

        val id3 = ID3v24Tag()

        // Create some data (lyric,teminator,size)
        val data = ByteArray(5)
        data[0] = 'A'.code.toByte()
        data[1] = 'B'.code.toByte()
        data[2] = '\u0000'.code.toByte()
        data[3] = 25
        data[4] = 45

        // Create USLT frame
        var frameBody =  FrameBodySYLT(
            TEXT_ENCODING_KEY,
            LANG_CODE,
            TIMESTAMP_FORMAT_KEY,
            CONTENT_TYPE_KEY,
            DESCRIPTION,
            data
        )

        var frame = ID3v24Frame(ID3v24FrameId.SYNC_LYRIC.id)
        frame.frameBody = frameBody
        id3.setFrame(frame)

        // Create TPE1 frame (just so we can see where SYLT framebody ends)
        val frameBody2 =  FrameBodyTPE1()
        frameBody2.setText("TESTINGFRAME")
        val frame2 = ID3v24Frame(ID3v24FrameId.ARTIST.id)
        frame2.frameBody = frameBody2
        id3.setFrame(frame2)

        mp3File.setTag(id3)
        mp3File.save()

        mp3File = MP3File.read(testFile)
        frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.SYNC_LYRIC.id) as ID3v24Frame
        frameBody = frame.frameBody as FrameBodySYLT
        Assertions.assertEquals(DESCRIPTION, frameBody.getDescription())
        Assertions.assertEquals(LANG_CODE, frameBody.getLanguage())
        Assertions.assertEquals(TEXT_ENCODING_KEY, frameBody.getTextEncoding().toInt())
        Assertions.assertEquals(TIMESTAMP_FORMAT_KEY, frameBody.getTimeStampFormat())
        Assertions.assertEquals(CONTENT_TYPE_KEY, frameBody.getContentType())
        Assertions.assertArrayEquals(data, frameBody.getLyrics())
    }

    companion object {
        private const val DESCRIPTION = "test"
        private const val LANG_CODE = "eng"
        private const val TEXT_ENCODING_KEY = 1
        private const val TIMESTAMP_FORMAT_KEY = 1
        private const val CONTENT_TYPE_KEY = 2
    }
}