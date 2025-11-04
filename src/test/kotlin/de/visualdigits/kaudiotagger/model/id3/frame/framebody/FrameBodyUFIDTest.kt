package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyUFIDTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyUFID? = null
        fb = FrameBodyUFID()
        fb.setOwner(TEST_OWNER)
        fb.setUniqueIdentifier(TEST_OBJECT_DATA)

        assertEquals(ID3v24FrameId.UNIQUE_FILE_ID.id, fb!!.getIdentifier())
        assertEquals(TextEncoding.ISO_8859_1.id, fb.getTextEncoding())
        assertEquals(TEST_OWNER, fb.getOwner())
        assertEquals(TEST_OBJECT_DATA, fb.getObjectValue(DataTypes.OBJ_DATA))
        assertEquals(TEST_OBJECT_DATA, fb.getUniqueIdentifier())
    }

    companion object {
        val TEST_OWNER: String = FrameBodyUFID.UFID_MUSICBRAINZ
        val TEST_OBJECT_DATA: ByteArray = ByteArray(2)
    }
}
