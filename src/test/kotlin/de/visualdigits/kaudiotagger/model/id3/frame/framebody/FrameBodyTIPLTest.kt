package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions
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

        val v23InitialisedFrame: ID3v23Frame
            get() {
                val frame = ID3v23Frame(ID3v23FrameId.INVOLVED_PEOPLE.id)
                val fb = getInitialisedBody()
                frame.frameBody = fb
                return frame
            }

        val initialisedFrame: ID3v24Frame
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.INVOLVED_PEOPLE.id)
                val fb = getInitialisedBody()
                frame.frameBody = fb
                return frame
            }

        val initialisedFrameOdd: ID3v24Frame
            get() {
                val frame = ID3v24Frame(ID3v24FrameId.INVOLVED_PEOPLE.id)
                val fb = getInitialisedBodyOdd()
                frame.frameBody = fb
                return frame
            }
    }

    @Test
    fun testCreateID3v24Frame() {
        val frame = ID3v24Frame(ID3v24FrameId.INVOLVED_PEOPLE.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(ID3v24FrameId.INVOLVED_PEOPLE.id, frame.getIdentifier())
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        Assertions.assertFalse(
            ID3v24FrameId.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v24FrameId.isSupported(frame.getIdentifier())
        )
        assertEquals(INVOLVED_PEOPLE, fb.getText())
    }

    @Test
    fun testCreateID3v23Frame() {
        val frame = ID3v23Frame(ID3v23FrameId.INVOLVED_PEOPLE.id)
        val fb = getInitialisedBody()
        frame.frameBody = fb

        assertEquals(
            ID3v23FrameId.INVOLVED_PEOPLE.id,
            frame.getIdentifier()
        )
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        Assertions.assertFalse(
            ID3v23FrameId.isExtension(frame.getIdentifier())
        )
        Assertions.assertTrue(
            ID3v23FrameId.isSupported(frame.getIdentifier())
        )
        assertEquals(INVOLVED_PEOPLE, fb.getText())
    }

    @Test
    fun testSaveToFile() {
        val testFile =  copyAudioToTmp(
            "testV1.mp3",
            "test1016.mp3"
        )
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(initialisedFrame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.INVOLVED_PEOPLE.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTIPL
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    @Test
    fun testSaveToFileOdd() {
        val testFile =  copyAudioToTmp(
            "testV1.mp3",
            "test1016.mp3"
        )
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(initialisedFrameOdd)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.INVOLVED_PEOPLE.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTIPL
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    @Test
    fun testSaveEmptyFrameToFile() {
        val testFile =  copyAudioToTmp(
            "testV1.mp3",
            "test1004.mp3"
        )
        var mp3File =  MP3File.read(testFile)

        var frame = ID3v24Frame(ID3v24FrameId.INVOLVED_PEOPLE.id)
        frame.frameBody = FrameBodyTIPL()

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(frame)
        mp3File.setTag(tag)
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        frame = mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v24FrameId.INVOLVED_PEOPLE.id) as ID3v24Frame
        val body =  frame.frameBody as FrameBodyTIPL
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
    }

    @Test
    fun testConvertV24ToV23() {
        val testFile =  copyAudioToTmp(
            "testV1.mp3",
            "test1005.mp3"
        )
        var mp3File =  MP3File.read(testFile)

        // Create and Save
        val tag = ID3v24Tag()
        tag.setFrame(initialisedFrame)

        mp3File.setTag(tag)
        mp3File.save()

        // Reload and convert to v23 and save
        mp3File = MP3File.read(testFile)
        val v23Tag = ID3v23Tag(mp3File.getID3v2TagAsv24())
        mp3File.setTag(v23Tag)

        Assertions.assertTrue(v23Tag.hasField("IPLS"))
        mp3File.save()

        // Reload
        mp3File = MP3File.read(testFile)
        val frame =  mp3File
            .getID3v2Tag()
            ?.getFrame(ID3v23FrameId.INVOLVED_PEOPLE.id) as ID3v23Frame
        val body =  frame.frameBody as FrameBodyIPLS
        Assertions.assertEquals(TextEncoding.ISO_8859_1.id, body.getTextEncoding())
        assertEquals(INVOLVED_PEOPLE, body.getText())
        assertEquals("producer", body.getKeyAtIndex(0))
        assertEquals("eno,lanois", body.getValueAtIndex(0))
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
