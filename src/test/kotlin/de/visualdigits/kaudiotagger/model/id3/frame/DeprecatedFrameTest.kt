package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyDeprecated
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIME
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTYER
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test

class DeprecatedFrameTest : AbstractTestCase() {
    
    @Test
    fun testv24TagWithDeprecatedFrameShouldCreateAsDeprecated() {
        val testFile = prependAudioToTmp(
            "Issue88.id3",
            "testV1.mp3"
        )

        val mp3File = MP3File.read(testFile)

        val v24frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.TYER.id) as ID3v24Frame?
        assertNotNull(v24frame)
        assertInstanceOf(FrameBodyDeprecated::class.java, v24frame?.frameBody)
    }

    @Test
    fun testConvertTagWithDeprecatedFrameToTagWhereFrameShouldNoLongerBeDeprecated() {
        val testFile = prependAudioToTmp(
            "Issue88.id3",
            "testV1.mp3"
        )

        var mp3File = MP3File.read(testFile)

        var v23Tag = ID3v23Tag(mp3File.getID3v2Tag()!!)
        var v23frame = (v23Tag.getFrame(
            ID3v23FrameId.TYER.id
        ) as MultiID3v2Frame).frames[0] as ID3v23Frame
        assertInstanceOf(FrameBodyTYER::class.java, v23frame.frameBody)
        v23frame = (v23Tag.getFrame(
            ID3v23FrameId.TYER.id
        ) as MultiID3v2Frame).frames[1] as ID3v23Frame
        assertInstanceOf(FrameBodyTYER::class.java, v23frame.frameBody)

        mp3File.setTag(v23Tag)
        mp3File.save()

        mp3File = MP3File.read(testFile)
        v23Tag = mp3File.getID3v2Tag() as ID3v23Tag
        v23frame = v23Tag.getFrame(ID3v23FrameId.TYER.id) as ID3v23Frame
        assertInstanceOf(FrameBodyTYER::class.java, v23frame.frameBody)
    }

    @Test
    fun testSavingV24DeprecatedTIMETagToV23() {
        val testFile = prependAudioToTmp(
            "Issue122-1.id3",
            "testV1.mp3"
        )
        var mp3File = MP3File.read(testFile)
        val v24Tag = mp3File.getID3v2Tag() as ID3v24Tag
        val v24frame = v24Tag.getFrame(
            ID3v23FrameId.TIME.id
        ) as ID3v24Frame?
        assertNotNull(v24frame)
        assertInstanceOf(FrameBodyDeprecated::class.java, v24frame?.frameBody)

        //Save as V23
        var v23Tag = ID3v23Tag(v24Tag)
        mp3File.setTag(v23Tag)
        mp3File.save()

        mp3File = MP3File.read(testFile)
        v23Tag = mp3File.getID3v2Tag() as ID3v23Tag
        val v23frame = v23Tag.getFrame(
            ID3v23FrameId.TIME.id
        ) as ID3v23Frame
        assertInstanceOf(FrameBodyTIME::class.java, v23frame.frameBody)
    }

    @Test
    fun testSavingV24DeprecatedEmptyTDATTagToV23() {
        val testFile = prependAudioToTmp(
            "Issue122-2.id3",
            "testV1.mp3"
        )
        var mp3File = MP3File.read(testFile)
        val v24Tag = mp3File.getID3v2Tag() as ID3v24Tag
        val v24frame = v24Tag.getFrame(
            ID3v23FrameId.TDAT.id
        ) as ID3v24Frame?
        assertNotNull(v24frame)
        assertInstanceOf(FrameBodyDeprecated::class.java, v24frame?.frameBody)

        //Save as V23
        var v23Tag = ID3v23Tag(v24Tag)
        mp3File.setTag(v23Tag)
        mp3File.save()

        mp3File = MP3File.read(testFile)
        v23Tag = mp3File.getID3v2Tag() as ID3v23Tag
        val v23frame: Any? = v23Tag.getFrame(ID3v23FrameId.TYER.id)
        assertNotNull(v23frame)
        assertInstanceOf(FrameBodyTYER::class.java, (v23frame as AbstractID3v2Frame).frameBody)
    }
}
