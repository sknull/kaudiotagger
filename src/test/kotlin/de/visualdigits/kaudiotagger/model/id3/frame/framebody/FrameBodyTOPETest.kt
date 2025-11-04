package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test

class FrameBodyTOPETest : AbstractTestCase() {

    @Test
    fun testSavingV24ToV23() {
        val testFile =  prependAudioToTmp(
            "Issue122.id3",
            "testV1.mp3"
        )
        var mp3File =  MP3File.read(testFile)
        val v24Tag =  mp3File.getID3v2Tag() as ID3v24Tag

        // Save as V23
        var v23Tag = ID3v23Tag(v24Tag)
        mp3File.setTag(v23Tag)
        mp3File.save()

        mp3File = MP3File.read(testFile)
        v23Tag = mp3File.getID3v2Tag() as ID3v23Tag
        val v23frame =  v23Tag?.getFrame(
            ID3v23FrameId.ORIGARTIST.id
        ) as ID3v23Frame
        assertInstanceOf(FrameBodyTOPE::class.java, v23frame.frameBody)
    }
}
