package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import org.junit.jupiter.api.Test

class FrameBodyTLANTest : AbstractTestCase() {

    @Test
    fun testWriteFileContainingTLANFrame() {
        val testFile =  prependAudioToTmp(
            "Issue116.id3",
            "testV1.mp3"
        )

        val mp3File =  MP3File.read(testFile)
        mp3File.save()
    }
}
