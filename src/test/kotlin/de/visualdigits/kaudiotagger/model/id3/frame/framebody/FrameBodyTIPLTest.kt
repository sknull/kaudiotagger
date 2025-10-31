package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyTIPLTest : AbstractTestCase() {

    companion object {

        const val INVOLVED_PEOPLE: String = "producer\u0000eno,lanois"
        const val INVOLVED_PEOPLE_ODD: String = "producer\u0000eno,lanois\u0000engineer"

        fun getInitialisedBodyOdd(): FrameBodyTIPL {
            val fb = FrameBodyTIPL()
            fb.setText(INVOLVED_PEOPLE_ODD)
            return fb
        }

        fun getInitialisedBody(): FrameBodyTIPL {
            val fb = FrameBodyTIPL()
            fb.setText(INVOLVED_PEOPLE)
            return fb
        }
    }

    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyTIPL? = null
        fb = FrameBodyTIPL()
        fb.setText(INVOLVED_PEOPLE)

        assertEquals(ID3v24FrameId.INVOLVED_PEOPLE.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(INVOLVED_PEOPLE, fb.getText())
        // assertEquals(2,fb.getNumberOfValues());
        // assertEquals("producer",fb.getNumberOfPairs());
        assertEquals("producer", fb.getKeyAtIndex(0))
        assertEquals("eno,lanois", fb.getValueAtIndex(0))
    }

    @Test
    fun testCreateFrameBodyodd() {
        var fb: FrameBodyTIPL? = null
        fb = FrameBodyTIPL()
        fb.setText(INVOLVED_PEOPLE_ODD)

        assertEquals(ID3v24FrameId.INVOLVED_PEOPLE.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(INVOLVED_PEOPLE, fb.getText())
        // assertEquals(2,fb.getNumberOfValues());
        // assertEquals("producer",fb.getNumberOfPairs());
        assertEquals("producer", fb.getKeyAtIndex(0))
        assertEquals("eno,lanois", fb.getValueAtIndex(0))
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyTIPL? = null
        fb = FrameBodyTIPL()
        fb.setText(INVOLVED_PEOPLE)

        assertEquals(ID3v24FrameId.INVOLVED_PEOPLE.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(INVOLVED_PEOPLE, fb.getText())
    }

    @Test
    fun testCreateFromIPLS() {
        val fbv3 = FrameBodyIPLSTest.getInitialisedBody()
        var fb: FrameBodyTIPL? = null
        fb = FrameBodyTIPL(fbv3)

        assertEquals(ID3v24FrameId.INVOLVED_PEOPLE.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(
            "*" + fb.getText() + "*",
            "*" + FrameBodyIPLSTest.INVOLVED_PEOPLE + "*"
        )
        assertEquals(2, fb.getNumberOfPairs())
        assertEquals("producer", fb.getKeyAtIndex(0))
        assertEquals("eno,lanois", fb.getValueAtIndex(0))
    }

// todo
//    /**
//     * Uses TMCL frame
//     *
//     * @throws Exception
//     */
//    @Test
//    fun testMultiArrangerIDv24() {
//        val testFile = copyAudioToTmp("testV1.mp3", "testWriteArrangerv24.mp3")
//        var f = MP3File.read(testFile)
//        assertNull(f.tags)
//
//        f.setTag(ID3v24Tag())
//        f.getTag().setField(GenericFieldKey.ARRANGER, "Arranger1")
//        f.getTag().addField(GenericFieldKey.ARRANGER, "Arranger2")
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals("Arranger1", f.getTag().getFirst(GenericFieldKey.ARRANGER))
//        assertEquals("Arranger1", f.getTag().getValue(GenericFieldKey.ARRANGER, 0))
//        assertEquals("Arranger2", f.getTag().getValue(GenericFieldKey.ARRANGER, 1))
//
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(2, f.getTag().getFields(GenericFieldKey.ARRANGER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
}
