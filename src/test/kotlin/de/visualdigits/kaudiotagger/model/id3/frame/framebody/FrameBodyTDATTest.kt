package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v2Version
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Disabled
import org.junit.jupiter.api.Test

class FrameBodyTDATTest : AbstractTestCase() {

    @Test
    fun testID3Specific() {
        val tag =  ID3v23Tag()
        val frame =  ID3v23Frame("TDAT")
        frame.frameBody = FrameBodyTDAT(TextEncoding.ISO_8859_1.id, "3006")
        tag.addFrame(frame)
        assertEquals("3006", tag.getFirst("TDAT"))

        val v24tag =  ID3v24Tag(tag)
        assertEquals(1, v24tag.getFieldCount())
        assertNotNull(v24tag.getFirst("TDAT"))
        assertEquals("-06-30", v24tag.getFirst("TDRC"))
    }

    @Test
    fun testID3SpecificWithYearAndTime() {
        var tag =  ID3v23Tag()
        val frame =  ID3v23Frame("TDAT")
        frame.frameBody = FrameBodyTDAT(TextEncoding.ISO_8859_1.id, "3006")
        tag.addFrame(frame)
        assertEquals("3006", tag.getFirst("TDAT"))

        val frameYear =  ID3v23Frame("TYER")
        frameYear.frameBody = FrameBodyTYER(TextEncoding.ISO_8859_1.id, "1980")
        tag.addFrame(frameYear)
        assertEquals("1980", tag.getFirst("TYER"))

        val frameTime =  ID3v23Frame("TIME")
        frameTime.frameBody = FrameBodyTIME(TextEncoding.ISO_8859_1.id, "1200")
        tag.addFrame(frameTime)
        assertEquals("1200", tag.getFirst("TIME"))
        assertEquals(3, tag.getFieldCount())

        // Create v24tag from v23, all these time frames shouod be merged into one
        var v24tag =  ID3v24Tag(tag)
        assertEquals(1, v24tag.getFieldCount())
        assertNotNull(v24tag.getFirst("TDAT"))
        assertNotNull(v24tag.getFirst("TIME"))
        assertNotNull(v24tag.getFirst("TYER"))
        assertEquals("1980-06-30T12:00", v24tag.getFirst("TDRC"))

        // Now create v23tag from v24, the tdrc frame should be split up and the values of the individual
        // values should match the v23 format not simply break up the v24 string
        tag = ID3v23Tag(v24tag)
        assertEquals(3, tag.getFieldCount())
        assertEquals("3006", tag.getFirst("TDAT"))
        assertEquals("1980", tag.getFirst("TYER"))
        assertEquals("1200", tag.getFirst("TIME"))

        // Do it again to check it works second time around
        v24tag = ID3v24Tag(tag)
        assertEquals(1, v24tag.getFieldCount())
        assertNotNull(v24tag.getFirst("TDAT"))
        assertNotNull(v24tag.getFirst("TIME"))
        assertNotNull(v24tag.getFirst("TYER"))
        assertEquals("1980-06-30T12:00", v24tag.getFirst("TDRC"))
    }

    @Test
    fun testConvertingPartialDate() {
        var tag =  ID3v24Tag()
        val frame =  ID3v24Frame("TDRC")
        frame.frameBody = FrameBodyTDRC(TextEncoding.ISO_8859_1.id, "2006-06")
        tag.addFrame(frame)
        assertEquals("2006-06", tag.getFirst("TDRC"))

        val v23tag =  ID3v23Tag(tag)
        assertEquals(3, v23tag.getFieldCount())
        assertNotNull(v23tag.getFirst("TYER"))
        assertEquals("2006", v23tag.getFirst("TYER"))
        assertNotNull(v23tag.getFirst("TDAT"))
        //"01" is created because cant just store month in this field in v23
        assertEquals("0106", v23tag.getFirst("TDAT"))

        tag = ID3v24Tag(v23tag)
        // But because is MonthOnly flag set dd gets lost when convert back to v24
        assertEquals("2006-06", tag.getFirst("TDRC"))
    }

    @Test
    @Disabled("currently not working") // todo fix test
    fun testReadingID3AsV24Generic() {
        val testFile =  copyAudioToTmp(
            "testV1.mp3",
            "id3asv24.mp3"
        )
        TagOptionSingleton.id3v2Version = ID3v2Version.ID3_V23
        var af = MP3File.read(testFile)
        af.getTagAndConvertOrCreateAndSetDefault()
        af.getTag()?.setField(GenericFieldKey.ARTIST, "fred")
        af.getTag()?.setField(GenericFieldKey.YEAR, "2003-06-23")
        af.commit()
        assertEquals(af.getTag()?.getFirst(GenericFieldKey.YEAR), "2003-06-23")
        af = MP3File.read(testFile)
        assertEquals(af.getTag()?.getFirst(GenericFieldKey.ARTIST), "fred")
        assertEquals(af.getTag()?.getAll(GenericFieldKey.ARTIST)?.get(0), "fred")
        assertEquals(af.getTag()?.getFirst(GenericFieldKey.YEAR), "2003-06-23")
        assertEquals(af.getTag()?.getAll(GenericFieldKey.YEAR)?.get(0), "2003-06-23")
        val iD3v2TagAsv24 = af.getID3v2TagAsv24()
        assertEquals(
            "2003-06-23",
            iD3v2TagAsv24.getFirst(GenericFieldKey.YEAR)
        )
        assertEquals(
            "2003-06-23",
            iD3v2TagAsv24.getAll(GenericFieldKey.YEAR).get(0)
        )
    }
}
