package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.field.TagTextField
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class ID3v1TagTest : AbstractTestCase() {

    companion object {
        const val ARTIST: String = "artist"
        const val ALBUM: String = "album"
        const val COMMENT: String = "comment"
        const val TITLE: String = "title"
        const val TRACK_VALUE: String = "10"
        val GENRE_VAL: String = "Country"
        const val YEAR: String = "1971"

        val initialisedTag: ID3v1Tag
            /**
             * Provides an initialised object to be used in other tests
             * to prevent code duplication
             *
             * @return ID3v1Tag
             */
            get() {
                val v1Tag = ID3v1Tag()
                v1Tag.setArtist(ARTIST)
                v1Tag.setAlbum(ALBUM)
                v1Tag.setComment(COMMENT)
                v1Tag.setTitle(TITLE)
                v1Tag.setGenreVal(GENRE_VAL)
                v1Tag.setYear(YEAR)
                return v1Tag
            }
    }

    @Test
    fun testCreateID3v1Tag() {
        val v1Tag = ID3v1Tag()
        v1Tag.setArtist(ARTIST)
        v1Tag.setAlbum(ALBUM)
        v1Tag.setComment(COMMENT)
        v1Tag.setTitle(TITLE)
        v1Tag.setGenreVal(GENRE_VAL)
        v1Tag.setYear(YEAR)

        assertEquals(1.toByte().toInt(), v1Tag.getRelease())
        assertEquals(0.toByte().toInt(), v1Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v1Tag.getRevision())

        //Check with old interface
        assertEquals(ARTIST, v1Tag.getFirst(GenericFieldKey.ARTIST))
        assertEquals(ALBUM, v1Tag.getFirst(GenericFieldKey.ALBUM))
        assertEquals(COMMENT, v1Tag.getComment())
        assertEquals(TITLE, v1Tag.getFirst(GenericFieldKey.TITLE))
        assertEquals(GENRE_VAL, v1Tag.getFirst(GenericFieldKey.GENRE))
        assertEquals(YEAR, v1Tag.getFirst(GenericFieldKey.YEAR))

        //Check with entagged interface
        assertEquals(
            ARTIST,
            (v1Tag.getArtistTag().get(0) as TagTextField).getContent()
        )
        assertEquals(
            ALBUM,
            (v1Tag.getAlbumTag().get(0) as TagTextField).getContent()
        )
        assertEquals(
            COMMENT,
            (v1Tag.getCommentTag().get(0) as TagTextField).getContent()
        )
        assertEquals(
            TITLE,
            (v1Tag.getTitleTag().get(0) as TagTextField).getContent()
        )
        assertEquals(
            GENRE_VAL,
            (v1Tag.getGenreTag().get(0) as TagTextField).getContent()
        )
        assertEquals(
            YEAR,
            (v1Tag.getYearTag().get(0) as TagTextField).getContent()
        )
    }

    @Test
    fun testSaveID3v1TagToFile() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var mp3File = MP3File.read(testFile)

        //Create v1 Tag
        var tag: ID3v1Tag? = ID3v1Tag()
        tag!!.setArtist(ARTIST)
        tag!!.setAlbum(ALBUM)
        tag!!.setComment(COMMENT)
        tag!!.setTitle(TITLE)
        tag!!.setGenreVal(GENRE_VAL)
        tag!!.setYear(YEAR)

        //Save tag to file
        mp3File!!.setTag(tag)
        mp3File!!.save()

        //Reload
        mp3File = MP3File.read(testFile)
        tag = mp3File!!.getID3v1Tag()
        assertEquals(ARTIST, tag!!.getFirst(GenericFieldKey.ARTIST))
        assertEquals(ALBUM, tag!!.getFirst(GenericFieldKey.ALBUM))
        assertEquals(COMMENT, tag!!.getComment())
        assertEquals(TITLE, tag!!.getFirst(GenericFieldKey.TITLE))
        assertEquals(GENRE_VAL, tag!!.getFirst(GenericFieldKey.GENRE))
        assertEquals(YEAR, tag!!.getFirst(GenericFieldKey.YEAR))

        tag!!.setField(GenericFieldKey.TRACK, "3")
        mp3File!!.save()
        mp3File = MP3File.read(testFile)
        tag = mp3File!!.getID3v1Tag()
        assertNull(tag!!.getFirst(GenericFieldKey.TRACK))
    }

    @Test
    fun testSaveID3v1TagToFileUsingTagInterface() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var file = MP3File.read(testFile)

        //Create v1 Tag
        var tag = file.getTag()
        if (tag == null) {
            file.setTag(ID3v1Tag())
            tag = file.getTag()
        }
        tag!!.setField(GenericFieldKey.ARTIST, ARTIST)
        tag!!.setField(GenericFieldKey.ALBUM, ALBUM)
        tag!!.setField(GenericFieldKey.COMMENT, COMMENT)
        tag!!.setField(GenericFieldKey.TITLE, TITLE)
        tag!!.setField(GenericFieldKey.GENRE, GENRE_VAL)
        tag!!.setField(GenericFieldKey.YEAR, YEAR)

        //Save tag changes to file
        file.setTag(tag)
        file.commit()

        //Reload
        file = MP3File.read(testFile)
        tag = file.getTag()
        assertEquals(ARTIST, tag!!.getFirst(GenericFieldKey.ARTIST))
        assertEquals(ALBUM, tag!!.getFirst(GenericFieldKey.ALBUM))
        assertEquals(COMMENT, tag!!.getFirst(GenericFieldKey.COMMENT))
        assertEquals(TITLE, tag!!.getFirst(GenericFieldKey.TITLE))
        assertEquals(GENRE_VAL, tag!!.getFirst(GenericFieldKey.GENRE))
        assertEquals(YEAR, tag!!.getFirst(GenericFieldKey.YEAR))
    }

    @Test
    fun testCreateID3v1FromID3v24() {
        val v2Tag = ID3v24Tag()
        val v1Tag = ID3v1Tag(v2Tag)
        assertNotNull(v1Tag)
        assertEquals(1.toByte().toInt(), v1Tag.getRelease())
        assertEquals(0.toByte().toInt(), v1Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v1Tag.getRevision())
    }

    @Test
    fun testCreateID3v1FromID3v23() {
        val v2Tag = ID3v23Tag()
        val v1Tag = ID3v1Tag(v2Tag)
        assertNotNull(v1Tag)
        assertEquals(1.toByte().toInt(), v1Tag.getRelease())
        assertEquals(0.toByte().toInt(), v1Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v1Tag.getRevision())
    }

    @Test
    fun testCreateID3v1FromID3v22() {
        val v2Tag = ID3v22Tag()
        val v1Tag = ID3v1Tag(v2Tag)
        assertNotNull(v1Tag)
        assertEquals(1.toByte().toInt(), v1Tag.getRelease())
        assertEquals(0.toByte().toInt(), v1Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v1Tag.getRevision())
    }

    @Test
    fun testNewInterface() {
        var exceptionCaught: Exception? = null
        val v1Tag = ID3v1Tag()
        assertTrue(v1Tag.isEmpty())

        v1Tag.setField(ID3v1TagField(GenericFieldKey.ARTIST.name, "artist"))
        assertEquals(
            "artist",
            (v1Tag.getFields(GenericFieldKey.ARTIST).get(0) as TagTextField).getContent()
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
        assertEquals(
            "album",
            (v1Tag.getFirstField(GenericFieldKey.ALBUM.name) as TagTextField).getContent()
        )

        v1Tag.setField(ID3v1TagField(GenericFieldKey.TITLE.name, "title"))
        assertEquals(
            "title",
            (v1Tag.getFields(GenericFieldKey.TITLE).get(0) as TagTextField).getContent()
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
        assertEquals(
            "year",
            (v1Tag.getFirstField(GenericFieldKey.YEAR.name) as TagTextField).getContent()
        )

        v1Tag.setField(ID3v1TagField(GenericFieldKey.GENRE.name, "Country"))
        assertEquals(
            "Country",
            (v1Tag.getFields(GenericFieldKey.GENRE).get(0) as TagTextField).getContent()
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
        assertEquals(
            "comment",
            (v1Tag.getFirstField(GenericFieldKey.COMMENT.name) as TagTextField).getContent()
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
        //Empty
        assertTrue(v1Tag.isEmpty())
    }
}
