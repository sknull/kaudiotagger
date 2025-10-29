package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.FieldKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File

class FrameBodyTMCLTest : AbstractTestCase() {

// todo
//    /**
//     * Uses TMCL frame
//     *
//     * @throws Exception
//     */
//    @Test
//    fun testWritePerformersIDv24() {
//        val testFile = copyAudioToTmp("testV1.mp3", "testWritePerformersv24.mp3")
//        var f = MP3File.read(testFile)
//        assertTrue(f.tags.isEmpty())
//
//        f.setTag(ID3v24Tag())
//        f.getTag().setField(GenericFieldKey.PERFORMER, "violinist", "Nigel Kennedy")
//        f.getTag().addField(GenericFieldKey.PERFORMER, "harpist", "Gloria Divosky")
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getFirst(GenericFieldKey.PERFORMER)
//        )
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 1)
//        )
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
//
//    @Test
//    fun testWritePerformersAndDeleteIDv24() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWritePerformersAndDeletev24.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v24Tag())
//        f.getTag().setField(GenericFieldKey.PERFORMER, "violinist", "Nigel Kennedy")
//        f.getTag().addField(GenericFieldKey.PERFORMER, "harpist", "Gloria Divosky")
//        assertEquals(1, f.getTag().getFieldCount())
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//
//        f.getTag().deleteField(GenericFieldKey.PERFORMER)
//        assertEquals(0, f.getTag().getFieldCount())
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(0, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(0, f.getTag().getFieldCount())
//        assertEquals(0, f.getTag().getFieldCount())
//    }
//
//    @Test
//    fun testWritePerformersIDv23() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWritePerformersv23.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v23Tag())
//        f.getTag().setField(GenericFieldKey.PERFORMER, "violinist", "Nigel Kennedy")
//        f.getTag().addField(GenericFieldKey.PERFORMER, "harpist", "Gloria Divosky")
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getFirst(GenericFieldKey.PERFORMER)
//        )
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 1)
//        )
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
//
//    @Test
//    fun testWritePerformersIDv22() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWritePerformersv22.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v22Tag())
//        f.getTag().setField(GenericFieldKey.PERFORMER, "violinist", "Nigel Kennedy")
//        f.getTag().addField(GenericFieldKey.PERFORMER, "harpist", "Gloria Divosky")
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getFirst(GenericFieldKey.PERFORMER)
//        )
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 1)
//        )
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
//
//    /**
//     * Uses TMCL frame
//     *
//     * @throws Exception
//     */
//    @Test
//    fun testWritePerformersIDv24v2() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWritePerformersv24.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v24Tag())
//        f.getTag().setField(GenericFieldKey.PERFORMER, "violinist\u0000Nigel Kennedy")
//        f.getTag().addField(GenericFieldKey.PERFORMER, "harpist\u0000Gloria Divosky")
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getFirst(GenericFieldKey.PERFORMER)
//        )
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 1)
//        )
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
//
//    /**
//     * Uses TMCL frame
//     *
//     * @throws Exception
//     */
//    @Test
//    fun testWritePerformersIDv24v3() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWritePerformersv24.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v24Tag())
//        f
//            .getTag()
//            .setField(
//                GenericFieldKey.PERFORMER,
//                PerformerHelper.formatForId3("Nigel Kennedy", "violinist")
//            )
//        f
//            .getTag()
//            .setField(
//                GenericFieldKey.PERFORMER,
//                PerformerHelper.formatForId3("Gloria Divosky", "harpist")
//            )
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getFirst(GenericFieldKey.PERFORMER)
//        )
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 1)
//        )
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
//
//    @Test
//    fun testWritePerformersIDv23v2() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWritePerformersv23.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v23Tag())
//        f.getTag().setField(GenericFieldKey.PERFORMER, "violinist\u0000Nigel Kennedy")
//        f.getTag().addField(GenericFieldKey.PERFORMER, "harpist\u0000Gloria Divosky")
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getFirst(GenericFieldKey.PERFORMER)
//        )
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 1)
//        )
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
//
//    @Test
//    fun testWritePerformersIDv23v3() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWritePerformersv23.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v23Tag())
//        f
//            .getTag()
//            .setField(
//                GenericFieldKey.PERFORMER,
//                PerformerHelper.formatForId3("Nigel Kennedy", "violinist")
//            )
//        f
//            .getTag()
//            .setField(
//                GenericFieldKey.PERFORMER,
//                PerformerHelper.formatForId3("Gloria Divosky", "harpist")
//            )
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getFirst(GenericFieldKey.PERFORMER)
//        )
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 1)
//        )
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
//
//    @Test
//    fun testWritePerformersIDv22v2() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWritePerformersv22.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v22Tag())
//        f.getTag().setField(GenericFieldKey.PERFORMER, "violinist\u0000Nigel Kennedy")
//        f.getTag().addField(GenericFieldKey.PERFORMER, "harpist\u0000Gloria Divosky")
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getFirst(GenericFieldKey.PERFORMER)
//        )
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 1)
//        )
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
//
//    @Test
//    fun testWritePerformersIDv22v3() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWritePerformersv22.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v22Tag())
//        f
//            .getTag()
//            .setField(
//                GenericFieldKey.PERFORMER,
//                PerformerHelper.formatForId3("Nigel Kennedy", "violinist")
//            )
//        f
//            .getTag()
//            .setField(
//                GenericFieldKey.PERFORMER,
//                PerformerHelper.formatForId3("Gloria Divosky", "harpist")
//            )
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getFirst(GenericFieldKey.PERFORMER)
//        )
//        assertEquals(
//            "violinist\u0000Nigel Kennedy",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 1)
//        )
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
//
//    @Test
//    fun testWriteMultiplePeopleIDv24() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWriteMultiplePeoplev24.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v24Tag())
//        f.getTag().setField(GenericFieldKey.PRODUCER, "steve lilllywhite")
//        f.getTag().addField(GenericFieldKey.PERFORMER, "harpist", "Gloria Divosky")
//        assertEquals(2, f.getTag().getFieldCount())
//        assertEquals("steve lilllywhite", f.getTag().getFirst(GenericFieldKey.PRODUCER))
//        assertEquals(
//            "steve lilllywhite",
//            f.getTag().getValue(GenericFieldKey.PRODUCER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(2, f.getTag().getFieldCount())
//        assertEquals(2, f.getTag().getFieldCount())
//
//        f.getTag().deleteField(GenericFieldKey.PERFORMER)
//        assertEquals("steve lilllywhite", f.getTag().getFirst(GenericFieldKey.PRODUCER))
//        assertEquals(
//            "steve lilllywhite",
//            f.getTag().getValue(GenericFieldKey.PRODUCER, 0)
//        )
//
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(0, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
//
//    @Test
//    fun testWriteMultiplePeopleIDv23() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWriteMultiplePeoplev23.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v23Tag())
//        f.getTag().setField(GenericFieldKey.PRODUCER, "steve lilllywhite")
//        f.getTag().addField(GenericFieldKey.PERFORMER, "harpist", "Gloria Divosky")
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals("steve lilllywhite", f.getTag().getFirst(GenericFieldKey.PRODUCER))
//        assertEquals(
//            "steve lilllywhite",
//            f.getTag().getValue(GenericFieldKey.PRODUCER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//
//        f.getTag().deleteField(GenericFieldKey.PERFORMER)
//        assertEquals("steve lilllywhite", f.getTag().getFirst(GenericFieldKey.PRODUCER))
//        assertEquals(
//            "steve lilllywhite",
//            f.getTag().getValue(GenericFieldKey.PRODUCER, 0)
//        )
//
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
//
//    @Test
//    fun testWriteMultiplePeopleIDv22() {
//        val testFile: File? = copyAudioToTmp(
//            "testV1.mp3",
//            "testWriteMultiplePeoplev22.mp3"
//        )
//        var f: AudioFile = AudioFileIO.read(testFile)
//        assertNull(f.getTag())
//
//        f.setTag(ID3v22Tag())
//        f.getTag().setField(GenericFieldKey.PRODUCER, "steve lilllywhite")
//        f.getTag().addField(GenericFieldKey.PERFORMER, "harpist", "Gloria Divosky")
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals("steve lilllywhite", f.getTag().getFirst(GenericFieldKey.PRODUCER))
//        assertEquals(
//            "steve lilllywhite",
//            f.getTag().getValue(GenericFieldKey.PRODUCER, 0)
//        )
//        assertEquals(
//            "harpist\u0000Gloria Divosky",
//            f.getTag().getValue(GenericFieldKey.PERFORMER, 0)
//        )
//
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//
//        f.getTag().deleteField(GenericFieldKey.PERFORMER)
//        assertEquals("steve lilllywhite", f.getTag().getFirst(GenericFieldKey.PRODUCER))
//        assertEquals(
//            "steve lilllywhite",
//            f.getTag().getValue(GenericFieldKey.PRODUCER, 0)
//        )
//
//        f.commit()
//        f = AudioFileIO.read(testFile)
//        assertEquals(1, f.getTag().getFields(GenericFieldKey.PERFORMER).size())
//        assertEquals(1, f.getTag().getFieldCount())
//        assertEquals(1, f.getTag().getFieldCount())
//    }
}
