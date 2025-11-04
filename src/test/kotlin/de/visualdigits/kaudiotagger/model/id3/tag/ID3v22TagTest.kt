package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v22Frame
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractFrameBodyTextInfo
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyCOMM
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTALB
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTCON
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTDRC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIT2
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTPE1
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTRCK
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.condition.EnabledIf

class ID3v22TagTest : AbstractTestCase() {

    @Test
    fun testCreateIDv22Tag() {
        val v2Tag = ID3v22Tag()
        assertEquals(2.toByte().toInt(), v2Tag.getRelease())
        assertEquals(2.toByte().toInt(), v2Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v2Tag.getRevision())
    }

    @Test
    fun testCreateID3v22FromID3v11() {
        val v11Tag = ID3v11TagTest.initialisedTag
        val v2Tag = ID3v22Tag(v11Tag)
        assertNotNull(v11Tag)
        assertNotNull(v2Tag)
        assertEquals(
            ID3v11TagTest.ARTIST,
            ((v2Tag.getFrame(
                ID3v22FrameId.ARTIST.id
            ) as ID3v22Frame).frameBody as FrameBodyTPE1).getText()
        )
        assertEquals(
            ID3v11TagTest.ALBUM,
            ((v2Tag.getFrame(
                ID3v22FrameId.ALBUM.id
            ) as ID3v22Frame).frameBody as FrameBodyTALB).getText()
        )
        assertEquals(
            ID3v11TagTest.COMMENT,
            ((v2Tag.getFrame(
                ID3v22FrameId.COMMENT.id
            ) as ID3v22Frame).frameBody as FrameBodyCOMM).getText()
        )
        assertEquals(
            ID3v11TagTest.TITLE,
            ((v2Tag.getFrame(
                ID3v22FrameId.TITLE.id
            ) as ID3v22Frame).frameBody as FrameBodyTIT2).getText()
        )
        assertEquals(
            ID3v11TagTest.TRACK_VALUE,
            java.lang.String.valueOf(
                ((v2Tag.getFrame(
                    ID3v22FrameId.TRACK.id
                ) as ID3v22Frame).frameBody as FrameBodyTRCK).getTrackNo()
            )
        )
        val tCON = (v2Tag.getFrame(
            ID3v22FrameId.GENRE.id
        ) as ID3v22Frame).frameBody as FrameBodyTCON
        assertTrue(
            tCON.getText()?.endsWith(ID3v11TagTest.GENRE_VAL) == true
        )

        //TODO:Note confusingly V22 YEAR Frame shave v2 identifier but use TDRC behind the scenes, is confusing
        assertEquals(
            ID3v11TagTest.YEAR,
            ((v2Tag.getFrame(
                ID3v22FrameId.TYER.id
            ) as ID3v22Frame).frameBody as FrameBodyTDRC).getText()
        )

        assertEquals(2.toByte().toInt(), v2Tag.getRelease())
        assertEquals(2.toByte().toInt(), v2Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v2Tag.getRevision())
    }

    @Test
    fun testCreateIDv22TagAndSave() {
        var exception: java.lang.Exception? = null
        try {
            val testFile = copyAudioToTmp("testV1.mp3")
            var mp3File: MP3File = MP3File.read(testFile)
            var v2Tag = ID3v22Tag()
            v2Tag.setField(GenericFieldKey.TITLE, "fred")
            v2Tag.setField(GenericFieldKey.ARTIST, "artist")
            v2Tag.setField(GenericFieldKey.ALBUM, "album")

            assertEquals(2.toByte().toInt(), v2Tag.getRelease())
            assertEquals(2.toByte().toInt(), v2Tag.getMajorVersion())
            assertEquals(0.toByte().toInt(), v2Tag.getRevision())
            mp3File!!.setTag(v2Tag)
            mp3File!!.save()

            //Read using new Interface
            val v22File = MP3File.read(testFile)
            assertEquals("fred", v22File.getTag()!!.getFirst(GenericFieldKey.TITLE))
            assertEquals("artist", v22File.getTag()!!.getFirst(GenericFieldKey.ARTIST))
            assertEquals("album", v22File.getTag()!!.getFirst(GenericFieldKey.ALBUM))

            //Read using old Interface
            mp3File = MP3File.read(testFile)
            v2Tag = mp3File!!.getID3v2Tag() as ID3v22Tag
            val frame: ID3v22Frame? = v2Tag.getFrame(
                ID3v22FrameId.TITLE.id
            ) as ID3v22Frame?
            assertEquals(
                "fred",
                (frame!!.frameBody as AbstractFrameBodyTextInfo).getText()
            )
        } catch (e: java.lang.Exception) {
            exception = e
        }
        assertNull(exception)
    }

    @Test
    @EnabledIf("executeAlsoWithMissingResources") // to be configured in AbsractBaseTestCase
    fun testv22TagWithUnneccessaryTrailingNulls() {
        var exception: java.lang.Exception? = null
        try {
            val testFile = copyAudioToTmp("test24.mp3")
            val af = MP3File.read(testFile)
            val m: MP3File? = af as MP3File?

            //Read using new Interface getFirst method with key
            assertEquals(
                "*Listen to images:*",
                "*" + af.getTag()!!.getFirst(GenericFieldKey.TITLE) + ":*"
            )
            assertEquals("Clean:", af.getTag()!!.getFirst(GenericFieldKey.ALBUM) + ":")
            assertEquals(
                "Cosmo Vitelli:",
                af.getTag()!!.getFirst(GenericFieldKey.ARTIST) + ":"
            )
            assertEquals(
                "Electronica/Dance:",
                af.getTag()!!.getFirst(GenericFieldKey.GENRE) + ":"
            )
            assertEquals("2003:", af.getTag()!!.getFirst(GenericFieldKey.YEAR) + ":")

            //Read using new Interface getFirst method with String
            assertEquals(
                "Listen to images:",
                af.getTag()!!.getFirst(ID3v22FrameId.TITLE.id) + ":"
            )
            assertEquals(
                "Clean:",
                af.getTag()!!.getFirst(ID3v22FrameId.ALBUM.id) + ":"
            )
            assertEquals(
                "Cosmo Vitelli:",
                af.getTag()!!.getFirst(ID3v22FrameId.ARTIST.id) + ":"
            )
            assertEquals(
                "Electronica/Dance:",
                af.getTag()!!.getFirst(ID3v22FrameId.GENRE.id) + ":"
            )
            assertEquals(
                "2003:",
                af.getTag()!!.getFirst(ID3v22FrameId.TYER.id) + ":"
            )
            assertEquals(
                "1:",
                af.getTag()!!.getFirst(ID3v22FrameId.TRACK.id) + ":"
            )

            //Read using new Interface getFirst methods for common fields
            assertEquals(
                "Listen to images:",
                af.getTag()!!.getFirst(GenericFieldKey.TITLE) + ":"
            )
            assertEquals(
                "Cosmo Vitelli:",
                af.getTag()!!.getFirst(GenericFieldKey.ARTIST) + ":"
            )
            assertEquals("Clean:", af.getTag()!!.getFirst(GenericFieldKey.ALBUM) + ":")
            assertEquals(
                "Electronica/Dance:",
                af.getTag()!!.getFirst(GenericFieldKey.GENRE) + ":"
            )
            assertEquals("2003:", af.getTag()!!.getFirst(GenericFieldKey.YEAR) + ":")

            //Read using old Interface
            val v2Tag = m!!.getID3v2Tag() as ID3v22Tag
            var frame: ID3v22Frame? = v2Tag.getFrame(
                ID3v22FrameId.TITLE.id
            ) as ID3v22Frame?
            assertEquals(
                "Listen to images:",
                (frame!!.frameBody as AbstractFrameBodyTextInfo).getText() + ":"
            )
            frame = v2Tag.getFrame(ID3v22FrameId.ARTIST.id) as ID3v22Frame?
            assertEquals(
                "Cosmo Vitelli:",
                (frame!!.frameBody as AbstractFrameBodyTextInfo).getText() + ":"
            )
            frame = v2Tag.getFrame(ID3v22FrameId.ALBUM.id) as ID3v22Frame?
            assertEquals(
                "Clean:",
                (frame!!.frameBody as AbstractFrameBodyTextInfo).getText() + ":"
            )
            frame = v2Tag.getFrame(ID3v22FrameId.GENRE.id) as ID3v22Frame?
            assertEquals(
                "Electronica/Dance:",
                (frame!!.frameBody as AbstractFrameBodyTextInfo).getText() + ":"
            )
            frame = v2Tag.getFrame(ID3v22FrameId.TYER.id) as ID3v22Frame?
            assertEquals(
                "2003:",
                (frame!!.frameBody as AbstractFrameBodyTextInfo).getText() + ":"
            )
            frame = v2Tag.getFrame(ID3v22FrameId.TRACK.id) as ID3v22Frame?
            assertEquals("01/11:", (frame!!.frameBody as FrameBodyTRCK).getText() + ":")
        } catch (e: java.lang.Exception) {
            exception = e
        }
        assertNull(exception)
    }

    @Test
    fun testDeleteFields() {
        val testFile = copyAudioToTmp("testV1.mp3")
        val mp3File: MP3File = MP3File.read(testFile)
        val v2Tag = ID3v22Tag()
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
        f.getTag()!!.deleteField("TS2")
        tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(0, tagFields.size)
        f.commit()

        f = MP3File.read(testFile)
        tagFields = f.getTag()!!.getFields(GenericFieldKey.ALBUM_ARTIST_SORT)
        assertEquals(0, tagFields.size)
    }

    @Test
    fun testWriteMultipleGenresToID3v22TagUsingDefault() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var file: MP3File? = null
        file = MP3File.read(testFile)
        assertNull(file.getID3v1Tag())
        file.setTag(ID3v22Tag())
        assertNotNull(file.getTag())
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        file.getTag()!!.addField(GenericFieldKey.GENRE, "Genre1")
        file.getTag()!!.addField(GenericFieldKey.GENRE, "Genre2")
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Genre1", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Genre1", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Genre2", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))

        TagOptionSingleton.isWriteMp3GenresAsText = false
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        file.getTag()!!.addField(GenericFieldKey.GENRE, "Death Metal")
        file.getTag()!!.addField(GenericFieldKey.GENRE, "(23)")
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
    fun testWriteMultipleGenresToID3v22TagUsingCreateField() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var file: MP3File? = null
        file = MP3File.read(testFile)
        assertNull(file.getID3v1Tag())
        file.setTag(ID3v22Tag())
        assertNotNull(file.getTag())
        var v22Tag = file.getTag() as ID3v22Tag
        var genreField = v22Tag.createField(GenericFieldKey.GENRE, "Genre1")
        v22Tag.addField(genreField)
        genreField = v22Tag.createField(GenericFieldKey.GENRE, "Genre2")
        v22Tag.addField(genreField)
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Genre1", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Genre1", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Genre2", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))

        TagOptionSingleton.isWriteMp3GenresAsText = false
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        v22Tag = file.getTag() as ID3v22Tag
        genreField = v22Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v22Tag.addField(genreField)
        genreField = v22Tag.createField(GenericFieldKey.GENRE, "(23)")
        v22Tag.addField(genreField)
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
        v22Tag = file.getTag() as ID3v22Tag
        genreField = v22Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v22Tag.addField(genreField)
        genreField = v22Tag.createField(GenericFieldKey.GENRE, "23")
        v22Tag.addField(genreField)
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
    fun testWriteMultipleGenresToID3v22TagUsingV22CreateField() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var file: MP3File? = null
        file = MP3File.read(testFile)
        assertNull(file.getID3v1Tag())
        file.setTag(ID3v22Tag())
        assertNotNull(file.getTag())
        var v22Tag = file.getTag() as ID3v22Tag
        var genreField = v22Tag.createField(GenericFieldKey.GENRE, "Genre1")
        v22Tag.addField(genreField)
        genreField = v22Tag.createField(GenericFieldKey.GENRE, "Genre2")
        v22Tag.addField(genreField)
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Genre1", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Genre1", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Genre2", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))

        TagOptionSingleton.isWriteMp3GenresAsText = false
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        v22Tag = file.getTag() as ID3v22Tag
        genreField = v22Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v22Tag.addField(genreField)
        genreField = v22Tag.createField(GenericFieldKey.GENRE, "(23)")
        v22Tag.addField(genreField)
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
        v22Tag = file.getTag() as ID3v22Tag
        genreField = v22Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v22Tag.addField(genreField)
        genreField = v22Tag.createField(GenericFieldKey.GENRE, "23")
        v22Tag.addField(genreField)
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
