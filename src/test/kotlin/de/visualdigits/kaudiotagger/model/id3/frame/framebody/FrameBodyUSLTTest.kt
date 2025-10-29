package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.AbstractTestCase
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.nio.file.Files

class FrameBodyUSLTTest : AbstractTestCase() {

    @Test
    fun testWriteUnicodeBody() {
        val fb = FrameBodyUSLT(
            textEncoding = TextEncoding.UTF_16.id,
            language = "eng",
            description = "",
            text = UTF16_REQUIRED
        )
        val baos = ByteArrayOutputStream()
        fb.write(baos)
        val fos = FileOutputStream(Files.createTempFile("kaudiotagger", "TEST.TXT").toFile())
        fos.write(baos.toByteArray())
        val frameBody: ByteArray = baos.toByteArray()
        val correctBits = makeByteArray(
            intArrayOf(
                0x01,
                'e'.code,
                'n'.code,
                'g'.code,
                0xff,
                0xfe,
                0x00,
                0x00,
                0xff,
                0xfe,
                0x26,
                0x20,
            )
        )
        assertArrayEquals(correctBits, frameBody)
    }

    private fun cmp(a: ByteArray, b: ByteArray): String? {
        if (a.size != b.size) {
            return ("length of byte arrays differ (${a.size}!=${b.size})")
        }
        for (i in a.indices) {
            if (a[i] != b[i]) {
                return ("byte arrays differ at offset $i (${a[i]}!=${b[i]})")
            }
        }
        return null
    }

    private fun makeByteArray(ints: IntArray): ByteArray {
        val bs = ByteArray(ints.size)
        for (i in ints.indices) {
            bs[i] = ints[i].toByte()
        }
        return bs
    }

    companion object {
        const val UTF16_REQUIRED: String = "\u2026"
    }
}
