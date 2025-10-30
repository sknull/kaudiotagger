package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.id3.frame.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf

class FrameTPOSTest : AbstractTestCase() {

    @Test
    fun testMergingMultipleFrames() {
        val tag = ID3v24Tag()
        tag.setField(tag.createField(GenericFieldKey.DISC_NO, "1"))
        tag.setField(tag.createField(GenericFieldKey.DISC_TOTAL, "10"))
        Assertions.assertEquals("1", tag.getFirst(GenericFieldKey.DISC_NO))
        Assertions.assertEquals("10", tag.getFirst(GenericFieldKey.DISC_TOTAL))
        Assertions.assertInstanceOf(AbstractID3v2Frame::class.java, tag?.getFrame("TPOS"))
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    fun testDiscNo() {
        val orig =  fileResource("testdata", "test82.mp3")
        val af = MP3File.Companion.read(orig)
        val newTags = af.getTag()
        val i =  newTags?.getFields()
        while (i?.hasNext() == true) {
            println(i.next()?.getIdentifier())
        }
    }
}