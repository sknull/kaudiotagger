package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v22Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v22Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyTPUBTest : AbstractTestCase() {

    @Test
    fun testGenericv22() {
        var e: Exception? = null
        try {
            val tag =  ID3v22Tag()
            tag.addField(GenericFieldKey.RECORD_LABEL, "testrecordlabel")
            assertEquals("testrecordlabel", tag.getFirst(GenericFieldKey.RECORD_LABEL))
            assertEquals("testrecordlabel", tag.getFirst("TPB"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }

    @Test
    fun testID3Specificv22() {
        var e: Exception? = null
        try {
            val tag =  ID3v22Tag()
            val frame =  ID3v22Frame("TPB")
            frame.frameBody = FrameBodyTPE3(TextEncoding.ISO_8859_1.id, "testrecordlabel")
            tag.addFrame(frame)
            assertEquals("testrecordlabel", tag.getFirst(GenericFieldKey.RECORD_LABEL))
            assertEquals("testrecordlabel", tag.getFirst("TPB"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }

    @Test
    fun testGenericv23() {
        var e: Exception? = null
        try {
            val tag =  ID3v23Tag()
            tag.addField(GenericFieldKey.RECORD_LABEL, "testrecordlabel")
            assertEquals("testrecordlabel", tag.getFirst(GenericFieldKey.RECORD_LABEL))
            assertEquals("testrecordlabel", tag.getFirst("TPUB"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }

    @Test
    fun testID3Specificv23() {
        var e: Exception? = null
        try {
            val tag =  ID3v23Tag()
            val frame =  ID3v23Frame("TPUB")
            frame.frameBody = FrameBodyTPE3(TextEncoding.ISO_8859_1.id, "testrecordlabel")
            tag.addFrame(frame)
            assertEquals("testrecordlabel", tag.getFirst(GenericFieldKey.RECORD_LABEL))
            assertEquals("testrecordlabel", tag.getFirst("TPUB"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }

    @Test
    fun testGenericv24() {
        var e: Exception? = null
        try {
            val tag =  ID3v24Tag()
            tag.addField(GenericFieldKey.RECORD_LABEL, "testrecordlabel")
            assertEquals("testrecordlabel", tag.getFirst(GenericFieldKey.RECORD_LABEL))
            assertEquals("testrecordlabel", tag.getFirst("TPUB"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }

    @Test
    fun testID3Specificv24() {
        var e: Exception? = null
        try {
            val tag =  ID3v24Tag()
            val frame =  ID3v24Frame("TPUB")
            frame.frameBody = FrameBodyTPE3(TextEncoding.ISO_8859_1.id, "testrecordlabel")
            tag.addFrame(frame)
            assertEquals("testrecordlabel", tag.getFirst(GenericFieldKey.RECORD_LABEL))
            assertEquals("testrecordlabel", tag.getFirst("TPUB"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }
}
