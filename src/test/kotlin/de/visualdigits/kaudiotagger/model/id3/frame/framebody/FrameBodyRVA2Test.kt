package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

class FrameBodyRVA2Test : AbstractTestCase() {

    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyRVA2? = null
        fb = FrameBodyRVA2()

    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyRVA2? = null
        fb = FrameBodyRVA2()
        fb.setObjectValue(DataTypes.OBJ_DATA, TEST_BYTES)

        assertEquals(
            ID3v24FrameId.RELATIVE_VOLUME_ADJUSTMENT2.id,
            fb!!.getIdentifier()
        )
        assertEquals(TEST_BYTES, fb.getObjectValue(DataTypes.OBJ_DATA))
    }

    companion object {

        var TEST_BYTES: ByteArray = makeByteArray(intArrayOf(0x01, 0x2))

        fun getInitialisedBody(): FrameBodyRVA2 {
            val fb = FrameBodyRVA2()

            fb.setObjectValue(DataTypes.OBJ_DATA, TEST_BYTES)
            return fb
        }

        private fun makeByteArray(ints: IntArray): ByteArray {
            val bs = ByteArray(ints.size)
            for (i in ints.indices) {
                bs[i] = ints[i].toByte()
            }
            return bs
        }
    }
}
