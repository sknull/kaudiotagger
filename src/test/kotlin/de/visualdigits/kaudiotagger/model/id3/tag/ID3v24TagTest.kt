/*
 * Jaudiotagger Copyright (C)2004,2005
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser
 * General Public  License as published by the Free Software Foundation; either version 2.1 of the License,
 * or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even
 * the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this library; if not,
 * you can getFields a copy from http://www.opensource.org/licenses/lgpl-license.php or write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 */
package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.field.TagField
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractFrameBodyTextInfo
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyCOMM
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTALB
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTCON
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTDRC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIT2
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTPE1
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTRCK
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.lang.String
import kotlin.Exception

class ID3v24TagTest : AbstractTestCase() {
 
    @Test
    fun testCreateTag() {
        val tag = ID3v24Tag()
        assertNotNull(tag)
    }

    @Test
    fun testCreateID3v24FromID3v11AndSave() {
        val testFile = copyAudioToTmp("testV1.mp3")

        var mp3File: MP3File? = null

        mp3File = MP3File.read(testFile)

        val v1Tag = ID3v11TagTest.initialisedTag

        assertFalse(mp3File!!.hasID3v1Tag())
        assertFalse(mp3File!!.hasID3v2Tag())
        mp3File!!.setTag(v1Tag)
        mp3File!!.setTag(mp3File!!.getID3v1Tag())
        assertTrue(mp3File!!.hasID3v1Tag())
        assertFalse(mp3File!!.hasID3v2Tag())
        mp3File!!.save()
        assertTrue(mp3File!!.hasID3v1Tag())
        assertFalse(mp3File!!.hasID3v2Tag())

        //Reload
        mp3File = MP3File.read(testFile)
        assertTrue(mp3File!!.hasID3v1Tag())
        assertFalse(mp3File!!.hasID3v2Tag())
    }

    @Test
    fun testCreateIDv24Tag() {
        val v2Tag = ID3v24Tag()
        assertEquals(2.toByte().toInt(), v2Tag.getRelease())
        assertEquals(4.toByte().toInt(), v2Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v2Tag.getRevision())
    }

    @Test
    fun testCreateID3v24FromID3v11() {
        val v1Tag = ID3v11TagTest.initialisedTag

        val v2Tag = ID3v24Tag(v1Tag)
        assertNotNull(v2Tag)
        assertEquals(
            ID3v11TagTest.ARTIST,
            ((v2Tag.getFrame(
                ID3v24FrameId.ARTIST.id
            ) as ID3v24Frame).frameBody as FrameBodyTPE1).getText()
        )
        assertEquals(
            ID3v11TagTest.ALBUM,
            ((v2Tag.getFrame(
                ID3v24FrameId.ALBUM.id
            ) as ID3v24Frame).frameBody as FrameBodyTALB).getText()
        )
        assertEquals(
            ID3v11TagTest.COMMENT,
            ((v2Tag.getFrame(
                ID3v24FrameId.COMMENT.id
            ) as ID3v24Frame).frameBody as FrameBodyCOMM).getText()
        )
        assertEquals(
            ID3v11TagTest.TITLE,
            ((v2Tag.getFrame(
                ID3v24FrameId.TITLE.id
            ) as ID3v24Frame).frameBody as FrameBodyTIT2).getText()
        )
        assertEquals(
            ID3v11TagTest.TRACK_VALUE,
            String.valueOf(
                ((v2Tag.getFrame(
                    ID3v24FrameId.TRACK.id
                ) as ID3v24Frame).frameBody as FrameBodyTRCK).getTrackNo()
            )
        )
        assertTrue(
            ((v2Tag.getFrame(
                ID3v24FrameId.GENRE.id
            ) as ID3v24Frame).frameBody as FrameBodyTCON).getText()!!.endsWith(ID3v11TagTest.GENRE_VAL)
        )
        assertEquals(
            ID3v11TagTest.YEAR,
            ((v2Tag.getFrame(
                ID3v24FrameId.YEAR.id
            ) as ID3v24Frame).frameBody as FrameBodyTDRC).getText()
        )
        assertEquals(2.toByte().toInt(), v2Tag.getRelease())
        assertEquals(4.toByte().toInt(), v2Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v2Tag.getRevision())

        //Newer methods
        assertEquals(
            ID3v11TagTest.ARTIST,
            v2Tag.getFirst(ID3v24FrameId.ARTIST.id)
        )
        assertEquals(
            ID3v11TagTest.ALBUM,
            v2Tag.getFirst(ID3v24FrameId.ALBUM.id)
        )
        assertEquals(
            ID3v11TagTest.TITLE,
            v2Tag.getFirst(ID3v24FrameId.TITLE.id)
        )
        assertEquals(
            ID3v11TagTest.YEAR,
            v2Tag.getFirst(ID3v24FrameId.YEAR.id)
        )
        assertEquals(
            ID3v11TagTest.ARTIST,
            (v2Tag
                .getFirstField(ID3v24FrameId.ARTIST.id)
                !!.frameBody as AbstractFrameBodyTextInfo).getFirstTextValue()
        )
    }

    /**
     * When try and write multiple text fields of the same type will now actually append the second value to the first
     * frame, getUniqueFieldCount() adjusted to show the number of values in frame but getFields() will just show number of frames
     * this fills a bit inconsistent but I haven't worked out a better way at the moment
     *
     * @throws Exception
     */
    @Test
    fun testWriteMultipleTextFields() {
        val testFile = copyAudioToTmp(
            "testV1.mp3",
            "testWriteMultipleText.mp3"
        )
        var f = MP3File.read(testFile)
        assertNull(f.getTag())
        f.setTag(ID3v24Tag())
        var tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(0, tagFields.size)

        //Add album artist sort field
        f.getTag()!!.addField(GenericFieldKey.ALBUM_ARTIST_SORT, "artist1")

        //Add another, jaudiotagger silently adds second value to the same frame because only one frame of this type
        //allowed, but text frames allow multiple values within them - I this is the correct (albeit confusing) behaviour
        f.getTag()!!.addField(GenericFieldKey.ALBUM_ARTIST_SORT, "artist2")

        //because added to the same frame, the number of fields is only one
        assertEquals(1, f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT).size)
        assertEquals(1, f.getTag()!!.getUniqueFieldCount())

        //but the field count includng subvalues takes this into account and shows 2
        assertEquals(2, f.getTag()!!.getFieldCount())

        assertEquals("artist1", f.getTag()!!.getValue(GenericFieldKey.ALBUM_ARTIST_SORT, 0))
        assertEquals("artist2", f.getTag()!!.getValue(GenericFieldKey.ALBUM_ARTIST_SORT, 1))
        //As can be seen from the longhand method
        val frame: ID3v24Frame = f.getTag()!!.getFirstField(GenericFieldKey.ALBUM_ARTIST_SORT) as ID3v24Frame
        assertEquals(
            "artist1\u0000artist2",
            (frame.frameBody as AbstractFrameBodyTextInfo).getText()
        )

        //We can get individual values back using longhand as well
        assertEquals(
            "artist1",
            (frame.frameBody as AbstractFrameBodyTextInfo).getValueAtIndex(0)
        )
        assertEquals(
            "artist2",
            (frame.frameBody as AbstractFrameBodyTextInfo).getValueAtIndex(1)
        )

        //TODO .. but we need a neater generic method
        assertEquals(1, f.getTag()!!.getUniqueFieldCount())
        tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(1, tagFields.size)
        f.commit()
        f = MP3File.read(testFile)
        assertEquals(1, f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT).size)
        assertEquals("artist1", f.getTag()!!.getFirst(GenericFieldKey.ALBUM_ARTIST_SORT))
        assertEquals("artist2", f.getTag()!!.getValue(GenericFieldKey.ALBUM_ARTIST_SORT, 1))
        assertEquals(1, f.getTag()!!.getUniqueFieldCount())
        assertEquals(2, f.getTag()!!.getFieldCount())
        assertEquals(1, f.getTag()!!.getUniqueFieldCount())
        tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(1, tagFields.size)
    }

    /**
     * TXXX frames are not treated as text frames regarding nul serpretd strings, only allowed one string
     */
    @Test
    fun testWriteMultipleTextTXXXFields() {
        val testFile = copyAudioToTmp(
            "testV1.mp3",
            "testWriteMultipleTextTXXX.mp3"
        )
        var f = MP3File.read(testFile)
        assertNull(f.getTag())
        f.setTag(ID3v24Tag())
        var tagFields = f.getTag()!!.getFields(GenericFieldKey.BARCODE)
        assertEquals(0, tagFields.size)
        f.getTag()!!.addField(GenericFieldKey.BARCODE, "xxxxxxxxxxxxxx")
        f.getTag()!!.addField(GenericFieldKey.BARCODE, "yyyyyyyyyyyyyy")
        assertEquals(1, f.getTag()!!.getFields(GenericFieldKey.BARCODE).size)
        assertEquals(1, f.getTag()!!.getUniqueFieldCount())
        tagFields = f.getTag()!!.getFields(GenericFieldKey.BARCODE)
        assertEquals(1, tagFields.size)
        assertEquals(2, f.getTag()!!.getAll(GenericFieldKey.BARCODE).size)
        f.commit()
        f = MP3File.read(testFile)
        assertEquals(1, f.getTag()!!.getFields(GenericFieldKey.BARCODE).size)
        assertEquals(1, f.getTag()!!.getUniqueFieldCount())
        tagFields = f.getTag()!!.getFields(GenericFieldKey.BARCODE)
        assertEquals(1, tagFields.size)
        assertEquals(2, f.getTag()!!.getAll(GenericFieldKey.BARCODE).size)
    }

    /**
     * TXXX frames are not treated as text frames regarding nul serpretd strings, only allowed one string
     */
    @Test
    fun testWriteMultipleDifferentTextTXXXFields() {
        val testFile = copyAudioToTmp(
            "testV1.mp3",
            "testWriteMultipleTextTXXX.mp3"
        )
        var f = MP3File.read(testFile)
        assertNull(f.getTag())
        f.setTag(ID3v24Tag())
        var tagFields = f.getTag()!!.getFields(GenericFieldKey.BARCODE)
        assertEquals(0, tagFields.size)
        f.getTag()!!.addField(GenericFieldKey.BARCODE, "xxxxxxxxxxxxxx")
        f.getTag()!!.addField(GenericFieldKey.MUSICBRAINZ_DISC_ID, "yyyyyyyyyyyyyy")
        assertEquals(1, f.getTag()!!.getFields(GenericFieldKey.BARCODE).size)
        assertEquals(1, f.getTag()!!.getFields(GenericFieldKey.MUSICBRAINZ_DISC_ID).size)
        assertEquals(2, f.getTag()!!.getFieldCount())
        tagFields = f.getTag()!!.getFields(GenericFieldKey.BARCODE)
        assertEquals(1, tagFields.size)
        f.commit()
        f = MP3File.read(testFile)
        assertEquals(1, f.getTag()!!.getFields(GenericFieldKey.BARCODE).size)
        assertEquals(1, f.getTag()!!.getFields(GenericFieldKey.MUSICBRAINZ_DISC_ID).size)
        assertEquals(2, f.getTag()!!.getFieldCount())
        tagFields = f.getTag()!!.getFields(GenericFieldKey.BARCODE)
        assertEquals(1, tagFields.size)
    }

    @Test
    fun testWriteMultipleFields() {
        val testFile = copyAudioToTmp(
            "testV1.mp3",
            "testWriteMultiple.mp3"
        )
        var f = MP3File.read(testFile)
        assertNull(f.getTag())
        f.setTag(ID3v24Tag())
        var tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(0, tagFields.size)
        f.getTag()!!.addField(GenericFieldKey.URL_OFFICIAL_RELEASE_SITE, "http://www,test.org")
        f.getTag()!!.addField(GenericFieldKey.URL_OFFICIAL_RELEASE_SITE, "http://www,test.org")
        assertEquals(1, f.getTag()!!.getFields(GenericFieldKey.URL_OFFICIAL_RELEASE_SITE).size)
        assertEquals(1, f.getTag()!!.getUniqueFieldCount())
        assertEquals(1, f.getTag()!!.getUniqueFieldCount())
        tagFields = f.getTag()!!.getFields(GenericFieldKey.URL_OFFICIAL_RELEASE_SITE)
        //assertEquals(1,tagFields.size);
        f.commit()
        f = MP3File.read(testFile)
        assertEquals(1, f.getTag()!!.getFields(GenericFieldKey.URL_OFFICIAL_RELEASE_SITE).size)
        assertEquals(1, f.getTag()!!.getUniqueFieldCount())
        assertEquals(1, f.getTag()!!.getUniqueFieldCount())
        tagFields = f.getTag()!!.getFields(GenericFieldKey.URL_OFFICIAL_RELEASE_SITE)
        assertEquals(1, tagFields.size)
    }

    @Test
    fun testDeleteFields() {
        val testFile = copyAudioToTmp("testV1.mp3")
        val mp3File: MP3File = MP3File.read(testFile)
        val v2Tag = ID3v23Tag()
        mp3File!!.setTag(v2Tag)
        mp3File!!.save()

        //Delete using generic key
        var f = MP3File.read(testFile)
        var tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(0, tagFields.size)
        f.getTag()!!.addField(GenericFieldKey.ALBUM_ARTIST_SORT, "artist1")
        tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(1, tagFields.size)
        f.getTag()!!.deleteField(GenericFieldKey.ALBUM_ARTIST_SORT)
        f.commit()

        //Delete using flac id
        f = MP3File.read(testFile)
        tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(0, tagFields.size)
        f.getTag()!!.addField(GenericFieldKey.ALBUM_ARTIST_SORT, "artist1")
        tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(1, tagFields.size)
        f.getTag()!!.deleteField("TSO2")
        tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(0, tagFields.size)
        f.commit()

        f = MP3File.read(testFile)
        tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(0, tagFields.size)
    }

//    /**
//     * Test Deleting tag and that deletion of tag is represented startight away
//     *
//     * @throws Exception
//     */
//    @Test
//    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
////    fun testDeleteTag() {
//        val testFile = copyAudioToTmp("test70.mp3")
//        var audioFile: MP3File = MP3File.read(testFile)
//
//        val v1tag: ID3v1Tag? = audioFile.getID3v1Tag()
//        assertTrue(audioFile.hasID3v1Tag())
//        audioFile.delete(v1tag)
//        assertFalse(audioFile.hasID3v1Tag())
//
//        val tag: AbstractID3v2Tag? = audioFile.getID3v2Tag()
//        assertTrue(audioFile.hasID3v2Tag())
//        audioFile.delete(tag)
//        assertFalse(audioFile.hasID3v2Tag())
//
//        audioFile = MP3File.read(testFile)
//        assertFalse(audioFile.hasID3v2Tag())
//        assertFalse(audioFile.hasID3v1Tag())
//    }

//    @Test
//    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
//    fun testWriteTagUsingAudioIOMethod() {
//        var exceptionCaught: Exception? = null
//        try {
//            val testFile = copyAudioToTmp("test70.mp3")
//            val audioFile: MP3File = MP3File.read(testFile)
//            MP3File.write(audioFile)
//        } catch (e: Exception) {
//            exceptionCaught = e
//        }
//        assertNull(exceptionCaught)
//    }

    @Test
    fun testWriteMultipleGenresToID3v24TagUsingDefault() {
        val testFile = copyAudioToTmp(
            "testV1.mp3",
            "testWriteMultipleV24.mp3"
        )
        var file = MP3File.read(testFile)
        assertNull(file.getTag())
        file.setTag(ID3v24Tag())
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        file.getTag()!!.addField(GenericFieldKey.GENRE, "Genre1")
        file.getTag()!!.addField(GenericFieldKey.GENRE, "Genre2")
        file.commit()
        file = MP3File.read(testFile)
        System.out.println(file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Genre1", file.getTag()!!.getFirst(GenericFieldKey.GENRE))

        TagOptionSingleton.isWriteMp3GenresAsText = false
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        file.getTag()!!.addField(GenericFieldKey.GENRE, "Death Metal")
        file.getTag()!!.addField(GenericFieldKey.GENRE, "23")
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))

        TagOptionSingleton.isWriteMp3GenresAsText = true
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        file.getTag()!!.addField(GenericFieldKey.GENRE, "Death Metal")
        file.getTag()!!.addField(GenericFieldKey.GENRE, "23")
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))
    }

    @Test
    fun testWriteMultipleGenresToID3v24TagUsingCreateField() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var file: MP3File? = null
        file = MP3File.read(testFile)
        assertNull(file.getID3v1Tag())
        file.setTag(ID3v24Tag())
        assertNotNull(file.getTag())
        var v24Tag = file.getTag() as ID3v24Tag
        var genreField: TagField? = v24Tag.createField(GenericFieldKey.GENRE, "Genre1")
        v24Tag.addField(genreField)
        genreField = v24Tag.createField(GenericFieldKey.GENRE, "Genre2")
        v24Tag.addField(genreField)
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Genre1", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Genre1", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Genre2", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))

        TagOptionSingleton.isWriteMp3GenresAsText = false
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        v24Tag = file.getTag() as ID3v24Tag
        genreField = v24Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v24Tag.addField(genreField)
        genreField = v24Tag.createField(GenericFieldKey.GENRE, "23")
        v24Tag.addField(genreField)
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))

        TagOptionSingleton.isWriteMp3GenresAsText = true
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        v24Tag = file.getTag() as ID3v24Tag
        genreField = v24Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v24Tag.addField(genreField)
        genreField = v24Tag.createField(GenericFieldKey.GENRE, "23")
        v24Tag.addField(genreField)
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))
    }

    @Test
    fun testWriteMultipleGenresToID3v24TagUsingV24CreateField() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var file: MP3File? = null
        file = MP3File.read(testFile)
        assertNull(file.getID3v1Tag())
        file.setTag(ID3v24Tag())
        assertNotNull(file.getTag())
        var v24Tag = file.getTag() as ID3v24Tag
        var genreField: TagField? = v24Tag.createField(GenericFieldKey.GENRE, "Genre1")
        v24Tag.addField(genreField)
        genreField = v24Tag.createField(GenericFieldKey.GENRE, "Genre2")
        v24Tag.addField(genreField)
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Genre1", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Genre1", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Genre2", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))

        TagOptionSingleton.isWriteMp3GenresAsText = false
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        v24Tag = file.getTag() as ID3v24Tag
        genreField = v24Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v24Tag.addField(genreField)
        genreField = v24Tag.createField(GenericFieldKey.GENRE, "23")
        v24Tag.addField(genreField)
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))

        TagOptionSingleton.isWriteMp3GenresAsText = true
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        v24Tag = file.getTag() as ID3v24Tag
        genreField = v24Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v24Tag.addField(genreField)
        genreField = v24Tag.createField(GenericFieldKey.GENRE, "23")
        v24Tag.addField(genreField)
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Death Metal", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Death Metal", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Pranks", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))
    }
}
