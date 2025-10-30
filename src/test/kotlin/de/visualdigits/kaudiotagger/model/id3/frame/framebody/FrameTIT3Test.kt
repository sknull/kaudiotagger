package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameTIT3Test : AbstractTestCase() {

    @Test
    fun testID3Specific() {
        val tag = ID3v23Tag()
        val frame =  ID3v23Frame("TIT3")
        frame.frameBody = FrameBodyTPE1(TextEncoding.ISO_8859_1.id, "testsubtitle")
        tag.addFrame(frame)
        assertEquals("testsubtitle", tag.getFirst("TIT3"))
    }
}
