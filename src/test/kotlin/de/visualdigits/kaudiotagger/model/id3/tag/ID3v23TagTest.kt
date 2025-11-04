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
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v23Frame
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyCOMM
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTALB
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTCON
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTDRL
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIT2
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTPE1
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTRCK
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTYER
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyUnsupported
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertInstanceOf
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.lang.String

class ID3v23TagTest : AbstractTestCase() {
    
    @Test
    fun testReadID3v1ID3v23Tag() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV1Cbr128ID3v1v2.mp3")

        var mp3File: MP3File? = null

        try {
            mp3File = MP3File.read(testFile)
        } catch (e: Exception) {
            exceptionCaught = e
        }

        assertNull(exceptionCaught)
        assertNotNull(mp3File!!.getID3v1Tag())
        assertNotNull(mp3File!!.getID3v2Tag())
    }

    @Test
    fun testReadID3v23Tag() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV1Cbr128ID3v2.mp3")

        var mp3File: MP3File? = null

        try {
            mp3File = MP3File.read(testFile)
        } catch (e: Exception) {
            exceptionCaught = e
        }

        assertNull(exceptionCaught)
        assertNull(mp3File!!.getID3v1Tag())
        assertNotNull(mp3File!!.getID3v2Tag())
    }

    @Test
    fun testReadPaddedID3v23Tag() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV1Cbr128ID3v2pad.mp3")

        var mp3File: MP3File? = null

        try {
            mp3File = MP3File.read(testFile)
        } catch (e: Exception) {
            exceptionCaught = e
        }

        assertNull(exceptionCaught)
        assertNull(mp3File!!.getID3v1Tag())
        assertNotNull(mp3File!!.getID3v2Tag())
    }

    @Test
    fun testDeleteID3v23Tag() {
        var exceptionCaught: Exception? = null
        val testFile = copyAudioToTmp("testV1Cbr128ID3v1v2.mp3")

        var mp3File: MP3File? = null

        try {
            mp3File = MP3File.read(testFile)
        } catch (e: Exception) {
            exceptionCaught = e
        }

        assertNull(exceptionCaught)
        assertNotNull(mp3File!!.getID3v1Tag())
        assertNotNull(mp3File!!.getID3v2Tag())
    }

    @Test
    fun testCreateIDv23Tag() {
        val v2Tag = ID3v23Tag()
        assertEquals(2.toByte().toInt(), v2Tag.getRelease())
        assertEquals(3.toByte().toInt(), v2Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v2Tag.getRevision())
    }

    @Test
    fun testCreateID3v23FromID3v11() {
        val v11Tag = ID3v11TagTest.initialisedTag
        val v2Tag = ID3v23Tag(v11Tag)
        assertNotNull(v11Tag)
        assertNotNull(v2Tag)
        assertEquals(
            ID3v11TagTest.ARTIST,
            ((v2Tag.getFrame(
                ID3v23FrameId.ARTIST.id
            ) as ID3v23Frame).frameBody as FrameBodyTPE1).getText()
        )
        assertEquals(
            ID3v11TagTest.ALBUM,
            ((v2Tag.getFrame(
                ID3v23FrameId.ALBUM.id
            ) as ID3v23Frame).frameBody as FrameBodyTALB).getText()
        )
        assertEquals(
            ID3v11TagTest.COMMENT,
            ((v2Tag.getFrame(
                ID3v23FrameId.COMMENT.id
            ) as ID3v23Frame).frameBody as FrameBodyCOMM).getText()
        )
        assertEquals(
            ID3v11TagTest.TITLE,
            ((v2Tag.getFrame(
                ID3v23FrameId.TITLE.id
            ) as ID3v23Frame).frameBody as FrameBodyTIT2).getText()
        )
        assertEquals(
            ID3v11TagTest.TRACK_VALUE,
            String.valueOf(
                ((v2Tag.getFrame(
                    ID3v23FrameId.TRACK.id
                ) as ID3v23Frame).frameBody as FrameBodyTRCK).getTrackNo()
            )
        )
        assertTrue(
            ((v2Tag.getFrame(
                ID3v23FrameId.GENRE.id
            ) as ID3v23Frame).frameBody as FrameBodyTCON).getText()!!.endsWith(ID3v11TagTest.GENRE_VAL)
        )
        assertEquals(
            ID3v11TagTest.YEAR,
            ((v2Tag.getFrame(
                ID3v23FrameId.TYER.id
            ) as ID3v23Frame).frameBody as FrameBodyTYER).getText()
        )

        assertEquals(2.toByte().toInt(), v2Tag.getRelease())
        assertEquals(3.toByte().toInt(), v2Tag.getMajorVersion())
        assertEquals(0.toByte().toInt(), v2Tag.getRevision())
    }

    /**
     * Test converting a v24 tag to a v23 tag, the v24 tag contains:
     *
     *
     * A frame which is known in v24 and v23
     *
     * @throws Exception
     */
    @Test
    fun testCreateID3v23FromID3v24knownInV3() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var mp3File: MP3File? = null
        mp3File = MP3File.read(testFile)

        //Add v24 tag to mp3 with single tdrl frame (which is only supported in v24)
        val tag = ID3v24Tag()
        val framebodyTdrl: FrameBodyTIT2 = FrameBodyTIT2()
        framebodyTdrl.setText("title")
        val tdrlFrame: ID3v24Frame = ID3v24Frame("TIT2")
        tdrlFrame.frameBody = framebodyTdrl
        tag.setFrame(tdrlFrame)
        mp3File!!.setTag(tag)
        mp3File!!.save()

        //Reread from File
        mp3File = MP3File.read(testFile)

        //Convert to v23 ,frame converted and marked as unsupported
        var v23tag = ID3v23Tag(mp3File!!.getID3v2TagAsv24())
        var v23frame: ID3v23Frame? = v23tag.getFrame("TIT2") as ID3v23Frame?
        assertNotNull(v23frame)
        assertInstanceOf(FrameBodyTIT2::class.java, v23frame!!.frameBody)

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File!!.setTag(v23tag)
        mp3File!!.save()

        //Reread from File
        mp3File = MP3File.read(testFile)

        //Convert to v23 ,frame should still exist
        v23tag = mp3File!!.getID3v2Tag() as ID3v23Tag
        v23frame = v23tag.getFrame("TIT2") as ID3v23Frame?
        assertNotNull(v23frame)
        assertInstanceOf(FrameBodyTIT2::class.java, v23frame!!.frameBody)

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File!!.setTag(v23tag)
        mp3File!!.save()

        //Check when converted to v24 has value been maintained
        assertEquals(
            "title",
            ((mp3File
                .getID3v2TagAsv24()
                .getFrame("TIT2") as ID3v24Frame).frameBody as FrameBodyTIT2).getText()
        )
    }

    /**
     * Test converting a v24 tag to a v23 tag, the v24 tag contains:
     *
     *
     * A frame which is known in v24 but not v23
     *
     * @throws Exception
     */
    @Test
    fun testCreateID3v23FromID3v24UnknownInV3() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var mp3File: MP3File? = null
        mp3File = MP3File.read(testFile)

        //Add v24 tag to mp3 with single tdrl frame (which is only supported in v24)
        val tag = ID3v24Tag()
        val framebodyTdrl: FrameBodyTDRL = FrameBodyTDRL()
        framebodyTdrl.setText("2008")
        val tdrlFrame: ID3v24Frame = ID3v24Frame("TDRL")
        tdrlFrame.frameBody = framebodyTdrl
        tag.setFrame(tdrlFrame)
        mp3File!!.setTag(tag)
        mp3File!!.save()

        //Reread from File
        mp3File = MP3File.read(testFile)

        //Convert to v23 ,frame converted and marked as unsupported
        var v23tag = ID3v23Tag(mp3File!!.getID3v2TagAsv24())
        var v23frame: ID3v23Frame? = v23tag.getFrame("TDRL") as ID3v23Frame?
        assertNotNull(v23frame)
        assertInstanceOf(FrameBodyUnsupported::class.java, v23frame!!.frameBody)

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File!!.setTag(v23tag)
        mp3File!!.save()

        //Reread from File
        mp3File = MP3File.read(testFile)

        //Convert to v23 ,frame should still exist
        v23tag = mp3File!!.getID3v2Tag() as ID3v23Tag
        v23frame = v23tag.getFrame("TDRL") as ID3v23Frame?
        assertNotNull(v23frame)
        assertInstanceOf(FrameBodyUnsupported::class.java, v23frame!!.frameBody)

        //Save as v23 tag (side effect convert v23 to v24 tag as well)
        mp3File!!.setTag(v23tag)
        mp3File!!.save()

        //Check value maintained, can only see as bytes
        var v23FrameBody: FrameBodyUnsupported =
            v23frame!!.frameBody as FrameBodyUnsupported
        assertEquals(
            '2'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![1]
        )
        assertEquals(
            '0'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![2]
        )
        assertEquals(
            '0'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![3]
        )
        assertEquals(
            '8'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![4]
        )

        //Reread from File
        mp3File = MP3File.read(testFile)
        v23tag = mp3File!!.getID3v2Tag() as ID3v23Tag
        v23frame = v23tag.getFrame("TDRL") as ID3v23Frame?
        assertNotNull(v23frame)
        assertInstanceOf(FrameBodyUnsupported::class.java, v23frame!!.frameBody)

        //Check value maintained, can only see as bytes
        v23FrameBody = v23frame!!.frameBody as FrameBodyUnsupported
        assertEquals(
            '2'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![1]
        )
        assertEquals(
            '0'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![2]
        )
        assertEquals(
            '0'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![3]
        )
        assertEquals(
            '8'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![4]
        )

        //Convert V24 representation to V23, and then save this
        v23tag = ID3v23Tag(mp3File!!.getID3v2TagAsv24())
        mp3File!!.setTag(v23tag)
        mp3File!!.save()

        //Reread from File
        mp3File = MP3File.read(testFile)
        v23tag = mp3File!!.getID3v2Tag() as ID3v23Tag
        v23frame = v23tag.getFrame("TDRL") as ID3v23Frame?
        assertNotNull(v23frame)
        assertInstanceOf(FrameBodyUnsupported::class.java, v23frame!!.frameBody)

        //Check value maintained, can only see as bytes
        v23FrameBody = v23frame!!.frameBody as FrameBodyUnsupported
        assertEquals(
            '2'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![1]
        )
        assertEquals(
            '0'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![2]
        )
        assertEquals(
            '0'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![3]
        )
        assertEquals(
            '8'.code.toByte(),
            (v23FrameBody.getObjectValue(DataTypes.OBJ_DATA) as ByteArray?)!![4]
        )
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

    @Test
    fun testWriteMultipleGenresToID3v23TagUsingDefault() {
        val testFile = copyAudioToTmp("testV1Cbr128ID3v2.mp3")
        var file: MP3File? = null
        file = MP3File.read(testFile)
        assertNull(file.getID3v1Tag())
        assertNotNull(file.getID3v2Tag())
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
    fun testWriteMultipleGenresToID3v23TagUsingCreateField() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var file: MP3File? = null
        file = MP3File.read(testFile)
        assertNull(file.getID3v1Tag())
        file.setTag(ID3v23Tag())
        assertNotNull(file.getTag())
        var v23Tag = file.getTag() as ID3v23Tag
        var genreField = v23Tag.createField(GenericFieldKey.GENRE, "Genre1")
        v23Tag.addField(genreField)
        genreField = v23Tag.createField(GenericFieldKey.GENRE, "Genre2")
        v23Tag.addField(genreField)
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Genre1", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Genre1", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Genre2", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))

        TagOptionSingleton.isWriteMp3GenresAsText = false
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        v23Tag = file.getTag() as ID3v23Tag
        genreField = v23Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v23Tag.addField(genreField)
        genreField = v23Tag.createField(GenericFieldKey.GENRE, "(23)")
        v23Tag.addField(genreField)
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
        v23Tag = file.getTag() as ID3v23Tag
        genreField = v23Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v23Tag.addField(genreField)
        genreField = v23Tag.createField(GenericFieldKey.GENRE, "23")
        v23Tag.addField(genreField)
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
    fun testWriteMultipleGenresToID3v23TagUsingV23CreateField() {
        val testFile = copyAudioToTmp("testV1.mp3")
        var file: MP3File? = null
        file = MP3File.read(testFile)
        assertNull(file.getID3v1Tag())
        file.setTag(ID3v23Tag())
        assertNotNull(file.getTag())
        var v23Tag = file.getTag() as ID3v23Tag
        var genreField = v23Tag.createField(GenericFieldKey.GENRE, "Genre1")
        v23Tag.addField(genreField)
        genreField = v23Tag.createField(GenericFieldKey.GENRE, "Genre2")
        v23Tag.addField(genreField)
        file.commit()
        file = MP3File.read(testFile)
        assertEquals("Genre1", file.getTag()!!.getFirst(GenericFieldKey.GENRE))
        assertEquals("Genre1", file.getTag()!!.getValue(GenericFieldKey.GENRE, 0))
        assertEquals("Genre2", file.getTag()!!.getValue(GenericFieldKey.GENRE, 1))

        TagOptionSingleton.isWriteMp3GenresAsText = false
        file.getTag()!!.deleteField(GenericFieldKey.GENRE)
        v23Tag = file.getTag() as ID3v23Tag
        genreField = v23Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v23Tag.addField(genreField)
        genreField = v23Tag.createField(GenericFieldKey.GENRE, "(23)")
        v23Tag.addField(genreField)
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
        v23Tag = file.getTag() as ID3v23Tag
        genreField = v23Tag.createField(GenericFieldKey.GENRE, "Death Metal")
        v23Tag.addField(genreField)
        genreField = v23Tag.createField(GenericFieldKey.GENRE, "23")
        v23Tag.addField(genreField)
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
