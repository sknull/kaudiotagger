package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.field.TagTextField
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ID3v11TagTest : AbstractTestCase() {

    companion object {
        const val ARTIST: String = "artist"
        const val ALBUM: String = "album"
        const val COMMENT: String = "comment"
        const val TITLE: String = "title"
        const val TRACK_VALUE: String = "10"
        val GENRE_VAL: String = "Country"
        const val YEAR: String = "1971"

        val initialisedTag: ID3v11Tag
            /**
             * Provides an initilised object to be used in other tests
             * to prevent code duplication
             *
             * @return ID3v11Tag
             */
            get() {
                val v11Tag = ID3v11Tag()
                v11Tag.setArtist(ARTIST)
                v11Tag.setAlbum(ALBUM)
                v11Tag.setComment(COMMENT)
                v11Tag.setTitle(TITLE)
                v11Tag.setTrackValue(TRACK_VALUE)
                v11Tag.setGenreVal(GENRE_VAL)
                v11Tag.setYear(YEAR)
                return v11Tag
            }
    }

    @Test
    fun testCreateID3v11Tag() {
        val v11Tag = ID3v11Tag()
        v11Tag.setArtist(ARTIST)
        v11Tag.setAlbum(ALBUM)
        v11Tag.setComment(COMMENT)
        v11Tag.setTitle(TITLE)
        v11Tag.setTrackValue(TRACK_VALUE)
        v11Tag.setGenreVal(GENRE_VAL)
        v11Tag.setYear(YEAR)

        assertEquals(1.toByte().toInt(), v11Tag.getRelease())
        assertEquals(1.toByte().toInt(), v11Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v11Tag.getRevision())

        assertEquals(ARTIST, v11Tag.getFirst(GenericFieldKey.ARTIST))
        assertEquals(ALBUM, v11Tag.getFirst(GenericFieldKey.ALBUM))
        assertEquals(COMMENT, v11Tag.getComment())
        assertEquals(TITLE, v11Tag.getFirst(GenericFieldKey.TITLE))
        assertEquals(TRACK_VALUE, v11Tag.getFirst(GenericFieldKey.TRACK))
        assertEquals(GENRE_VAL, v11Tag.getFirst(GenericFieldKey.GENRE))
        assertEquals(YEAR, v11Tag.getFirst(GenericFieldKey.YEAR))

        //Check with entagged interface
        assertEquals(
            ID3v1TagTest.ARTIST,
            (v11Tag.getArtistTag().get(0) as TagTextField).getContent()
        )
        assertEquals(
            ID3v1TagTest.ALBUM,
            (v11Tag.getAlbumTag().get(0) as TagTextField).getContent()
        )
        assertEquals(
            ID3v1TagTest.COMMENT,
            (v11Tag.getCommentTag().get(0) as TagTextField).getContent()
        )
        assertEquals(
            ID3v1TagTest.TITLE,
            (v11Tag.getTitleTag().get(0) as TagTextField).getContent()
        )
        assertEquals(
            ID3v1TagTest.GENRE_VAL,
            (v11Tag.getGenreTag().get(0) as TagTextField).getContent()
        )
        assertEquals(
            ID3v1TagTest.TRACK_VALUE,
            (v11Tag.getTrackTag().get(0) as TagTextField).getContent()
        )
        assertEquals(
            ID3v1TagTest.YEAR,
            (v11Tag.getYearTag().get(0) as TagTextField).getContent()
        )

        v11Tag.setField(GenericFieldKey.TRACK, "3")
        assertEquals("3", v11Tag.getFirst(GenericFieldKey.TRACK))
    }

    @Test
    fun testCreateID3v11FromID3v24() {
        val v2Tag = ID3v24Tag()
        val v1Tag = ID3v11Tag(v2Tag)
        assertNotNull(v1Tag)
        assertEquals(1.toByte().toInt(), v1Tag.getRelease())
        assertEquals(1.toByte().toInt(), v1Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v1Tag.getRevision())
    }

    @Test
    fun testCreateID3v11FromID3v23() {
        val v2Tag = ID3v23Tag()
        val v1Tag = ID3v11Tag(v2Tag)
        assertNotNull(v1Tag)
        assertEquals(1.toByte().toInt(), v1Tag.getRelease())
        assertEquals(1.toByte().toInt(), v1Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v1Tag.getRevision())
    }

    @Test
    fun testCreateID3v11FromID3v22() {
        val v2Tag = ID3v22Tag()
        val v1Tag = ID3v11Tag(v2Tag)
        assertNotNull(v1Tag)
        assertEquals(1.toByte().toInt(), v1Tag.getRelease())
        assertEquals(1.toByte().toInt(), v1Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v1Tag.getRevision())
    }

    @Test
    fun testNewInterface() {
        val v1Tag = ID3v11Tag()
        assertTrue(v1Tag.isEmpty())

        val field = ID3v1TagField(GenericFieldKey.ARTIST.name, "artist")
        v1Tag.setField(field)
        val field1 = v1Tag.getFields(GenericFieldKey.ARTIST).get(0) as TagTextField
        assertEquals(
            "artist",
            (field1).getContent()
        )
        assertEquals("artist", v1Tag.getFirst(GenericFieldKey.ARTIST))
        assertEquals(
            "artist",
            ((v1Tag.getArtistTag().get(0)) as TagTextField).getContent()
        )
        assertEquals(
            "artist",
            (v1Tag.getFirstField(GenericFieldKey.ARTIST.name) as TagTextField).getContent()
        )
        assertEquals(
            "artist",
            ((v1Tag
                .getFields(GenericFieldKey.ARTIST)
                .get(0)) as TagTextField).getContent()
        )

        v1Tag.setField(ID3v1TagField(GenericFieldKey.ALBUM.name, "album"))
        assertEquals(
            "album",
            (v1Tag.getFields(GenericFieldKey.ALBUM).get(0) as TagTextField).getContent()
        )
        assertEquals("album", v1Tag.getFirst(GenericFieldKey.ALBUM))
        assertEquals(
            "album",
            ((v1Tag.getAlbumTag().get(0)) as TagTextField).getContent()
        )
        assertEquals(
            "album",
            (v1Tag.getFirstField(GenericFieldKey.ALBUM.name) as TagTextField).getContent()
        )

        v1Tag.setField(ID3v1TagField(GenericFieldKey.TITLE.name, "title"))
        assertEquals(
            "title",
            (v1Tag.getFields(GenericFieldKey.TITLE).get(0) as TagTextField).getContent()
        )
        assertEquals("title", v1Tag.getFirst(GenericFieldKey.TITLE))
        assertEquals(
            "title",
            ((v1Tag.getTitleTag().get(0)) as TagTextField).getContent()
        )
        assertEquals(
            "title",
            (v1Tag.getFirstField(GenericFieldKey.TITLE.name) as TagTextField).getContent()
        )

        v1Tag.setField(ID3v1TagField(GenericFieldKey.YEAR.name, "year"))
        assertEquals(
            "year",
            (v1Tag.getFields(GenericFieldKey.YEAR).get(0) as TagTextField).getContent()
        )
        assertEquals("year", v1Tag.getFirst(GenericFieldKey.YEAR))
        assertEquals(
            "year",
            ((v1Tag.getYearTag().get(0)) as TagTextField).getContent()
        )
        assertEquals(
            "year",
            (v1Tag.getFirstField(GenericFieldKey.YEAR.name) as TagTextField).getContent()
        )

        v1Tag.setField(ID3v1TagField(GenericFieldKey.GENRE.name, "Country"))
        assertEquals(
            "Country",
            (v1Tag.getFields(GenericFieldKey.GENRE).get(0) as TagTextField).getContent()
        )
        assertEquals("Country", v1Tag.getFirst(GenericFieldKey.GENRE))
        assertEquals(
            "Country",
            ((v1Tag.getGenreTag().get(0)) as TagTextField).getContent()
        )
        assertEquals(
            "Country",
            (v1Tag.getFirstField(GenericFieldKey.GENRE.name) as TagTextField).getContent()
        )

        v1Tag.setField(ID3v1TagField(GenericFieldKey.COMMENT.name, "comment"))
        assertEquals(
            "comment",
            (v1Tag.getFields(GenericFieldKey.COMMENT).get(0) as TagTextField).getContent()
        )
        assertEquals("comment", v1Tag.getComment())
        assertEquals(
            "comment",
            ((v1Tag.getFields(GenericFieldKey.COMMENT).get(0)) as TagTextField).getContent()
        )
        assertEquals(
            "comment",
            (v1Tag.getFirstField(GenericFieldKey.COMMENT.name) as TagTextField).getContent()
        )

        v1Tag.setField(ID3v1TagField(GenericFieldKey.TRACK.name, "5"))
        val fields = v1Tag.getFields(GenericFieldKey.TRACK)
        assertEquals(
            "5",
            (fields.get(0) as TagTextField).getContent()
        )
        assertEquals("5", v1Tag.getFirst(GenericFieldKey.TRACK))
        assertEquals("5", ((v1Tag.getTrackTag().get(0)) as TagTextField).getContent())
        assertEquals(
            "5",
            (v1Tag.getFirstField(GenericFieldKey.TRACK.name) as TagTextField).getContent()
        )

        //Check nothing been overwritten
        assertEquals("year", v1Tag.getFirst(GenericFieldKey.YEAR))
        assertEquals("Country", v1Tag.getFirst(GenericFieldKey.GENRE))
        assertEquals("title", v1Tag.getFirst(GenericFieldKey.TITLE))
        assertEquals("album", v1Tag.getFirst(GenericFieldKey.ALBUM))
        assertEquals("artist", v1Tag.getFirst(GenericFieldKey.ARTIST))

        //Delete artist field
        v1Tag.deleteField(GenericFieldKey.ARTIST)
        assertNull(v1Tag.getFirst(GenericFieldKey.ARTIST))
        assertEquals("year", v1Tag.getFirst(GenericFieldKey.YEAR))
        assertEquals("Country", v1Tag.getFirst(GenericFieldKey.GENRE))
        assertEquals("title", v1Tag.getFirst(GenericFieldKey.TITLE))
        assertEquals("album", v1Tag.getFirst(GenericFieldKey.ALBUM))

        //Not Empty
        assertFalse(v1Tag.isEmpty())

        v1Tag.deleteField(GenericFieldKey.ALBUM)
        v1Tag.deleteField(GenericFieldKey.YEAR)
        v1Tag.deleteField(GenericFieldKey.GENRE)
        v1Tag.deleteField(GenericFieldKey.TITLE)
        v1Tag.deleteField(GenericFieldKey.COMMENT)
        v1Tag.setField(ID3v1TagField(GenericFieldKey.COMMENT.name, ""))
        v1Tag.deleteField(GenericFieldKey.TRACK)
        //Empty
        assertTrue(v1Tag.isEmpty())
    }

    @Test
    fun testSaveID3v11TagToFile() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var mp3File: MP3File = MP3File.read(testFile)

        //Create v11 Tag
        var tag = ID3v11Tag()
        tag.setArtist(ARTIST)
        tag.setAlbum(ALBUM)
        tag.setComment(COMMENT)
        tag.setTitle(TITLE)
        tag.setGenreVal(GENRE_VAL)
        tag.setYear(YEAR)
        tag.setTrackValue(TRACK_VALUE)
        //Save tag to file
        mp3File!!.setTag(tag)
        mp3File!!.save()

        //Reload
        mp3File = MP3File.read(testFile)
        tag = mp3File!!.getID3v1Tag() as ID3v11Tag
        assertEquals(ARTIST, tag.getFirst(GenericFieldKey.ARTIST))
        assertEquals(ALBUM, tag.getFirst(GenericFieldKey.ALBUM))
        assertEquals(COMMENT, tag.getComment())
        assertEquals(TITLE, tag.getFirst(GenericFieldKey.TITLE))
        assertEquals(GENRE_VAL, tag.getFirst(GenericFieldKey.GENRE))
        assertEquals(YEAR, tag.getFirst(GenericFieldKey.YEAR))
        assertEquals(YEAR, tag.getFirst(GenericFieldKey.YEAR))
        assertEquals(TRACK_VALUE, tag.getFirst(GenericFieldKey.TRACK))

        tag.setField(GenericFieldKey.TRACK, "3")
        mp3File!!.save()
        mp3File = MP3File.read(testFile)
        tag = mp3File!!.getID3v1Tag() as ID3v11Tag
        assertEquals("3", tag.getFirst(GenericFieldKey.TRACK))
    }
}
