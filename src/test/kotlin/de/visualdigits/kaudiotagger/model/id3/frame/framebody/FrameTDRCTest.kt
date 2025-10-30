package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import org.junit.jupiter.api.Test

class FrameTDRCTest : AbstractTestCase() {

    @Test
    fun testReadFileContainingTDRCAndTYERFrames() {
        val testFile =  prependAudioToTmp(
            "Issue73.id3",
            "testV1.mp3"
        )

        val mp3File =  MP3File.Companion.read(testFile)
        val v23frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.TYER.id) as? ID3v23Frame
        val v24frame =  mp3File
            .getID3v2TagAsv24()
            ?.getFrame(ID3v23FrameId.TYER.id) as? ID3v24Frame
    }
}