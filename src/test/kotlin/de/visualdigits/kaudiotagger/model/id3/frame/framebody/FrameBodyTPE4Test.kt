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

class FrameBodyTPE4Test : AbstractTestCase() {

    @Test
    fun testGenericv22() {
        var e: Exception? = null
        try {
            val tag =  ID3v22Tag()
            tag.addField(GenericFieldKey.REMIXER, "testREMIXER")
            assertEquals("testREMIXER", tag.getFirst(GenericFieldKey.REMIXER))
            assertEquals("testREMIXER", tag.getFirst("TP4"))
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
            val frame =  ID3v22Frame("TP4")
            frame.frameBody = FrameBodyTPE3(TextEncoding.ISO_8859_1.id, "testRemixer")
            tag.addFrame(frame)
            assertEquals("testRemixer", tag.getFirst(GenericFieldKey.REMIXER))
            assertEquals("testRemixer", tag.getFirst("TP4"))
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
            tag.addField(GenericFieldKey.REMIXER, "testRemixer")
            assertEquals("testRemixer", tag.getFirst(GenericFieldKey.REMIXER))
            assertEquals("testRemixer", tag.getFirst("TPE4"))
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
            val frame =  ID3v23Frame("TPE4")
            frame.frameBody = FrameBodyTPE3(TextEncoding.ISO_8859_1.id, "testRemixer")
            tag.addFrame(frame)
            assertEquals("testRemixer", tag.getFirst(GenericFieldKey.REMIXER))
            assertEquals("testRemixer", tag.getFirst("TPE4"))
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
            tag.addField(GenericFieldKey.REMIXER, "testRemixer")
            assertEquals("testRemixer", tag.getFirst(GenericFieldKey.REMIXER))
            assertEquals("testRemixer", tag.getFirst("TPE4"))
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
            val frame =  ID3v24Frame("TPE4")
            frame.frameBody = FrameBodyTPE3(TextEncoding.ISO_8859_1.id, "testRemixer")
            tag.addFrame(frame)
            assertEquals("testRemixer", tag.getFirst(GenericFieldKey.REMIXER))
            assertEquals("testRemixer", tag.getFirst("TPE4"))
        } catch (ex: java.lang.Exception) {
            e = ex
            ex.printStackTrace()
        }
        assertNull(e)
    }
}
