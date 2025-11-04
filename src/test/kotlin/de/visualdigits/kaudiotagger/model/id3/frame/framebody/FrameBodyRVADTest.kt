package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.AbstractTestCase
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class FrameBodyRVADTest : AbstractTestCase() {
    @Test
    fun testCreateFrameBody() {
        var fb: FrameBodyRVAD? = null
        fb = FrameBodyRVAD()

        assertEquals(
            ID3v23FrameId.RELATIVE_VOLUME_ADJUSTMENT.id,
            fb!!.getIdentifier()
        )
    }

    @Test
    fun testCreateFrameBodyEmptyConstructor() {
        var fb: FrameBodyRVAD? = null
        fb = FrameBodyRVAD()
        fb.setObjectValue(DataTypes.OBJ_DATA, TEST_BYTES)

        assertEquals(
            ID3v23FrameId.RELATIVE_VOLUME_ADJUSTMENT.id,
            fb!!.getIdentifier()
        )
        assertEquals(TEST_BYTES, fb.getObjectValue(DataTypes.OBJ_DATA))
    }

    companion object {
        var TEST_BYTES: ByteArray = makeByteArray(intArrayOf(0x03, 0x04))

        fun getInitialisedBody(): FrameBodyRVAD {
            val fb = FrameBodyRVAD()
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
