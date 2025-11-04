package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf

class DuplicateFrameTest : AbstractTestCase() {
    
    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    fun testReadingFileWithCorruptFirstFrame() {
        val testFile = copyAudioToTmp("test78.mp3")

        var f = MP3File.read(testFile)
        var tag = f.getTag()
        assertInstanceOf(ID3v23Tag::class.java, f.getTag())
        var id3v23tag = tag as ID3v23Tag
        //Frame contains two TYER frames
        assertEquals(21, id3v23tag.duplicateBytes)
        assertEquals("*TYER*", "*" + id3v23tag.duplicateFrameId + "*")
        f.commit()
        f = MP3File.read(testFile)
        tag = f.getTag()
        id3v23tag = tag as ID3v23Tag
        //After save the duplicate frame has been discarded
        assertEquals(0, id3v23tag.duplicateBytes)
        assertEquals("", id3v23tag.duplicateBytes)
    }
}
