package de.visualdigits.kaudiotagger.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.InputStream


object FileTypeUtil {
    internal val log: Logger = LoggerFactory.getLogger(FileTypeUtil::class.java)

    private const val BUFFER_SIZE = 4096
    private const val MAX_SIGNATURE_SIZE = 8

    // PDF files starts with: %PDF
    // MS office files starts with: (D0 CF 11 E0 A1 B1 1A E1)
    // Java does not support byte literals. Use int literals instead.
    // private static final int[] pdfSig = { 0x25, 0x50, 0x44, 0x46 };
    // private static final int[] msOfficeSig = { 0xd0, 0xcf, 0x11, 0xe0, 0xa1,
    // 0xb1, 0x1a, 0xe1 };
    val mp3v2Sig = arrayOf<Int?>(0x49, 0x44, 0x33)
    val mp3v1Sig_1 = arrayOf<Int?>(0xFF, 0xF3)
    val mp3v1Sig_2 = arrayOf<Int?>(0xFF, 0xFA)
    val mp3v1Sig_3 = arrayOf<Int?>(0xFF, 0xF2)
    val mp3v1Sig_4 = arrayOf<Int?>(0xFF, 0xFB)
    val mp4Sig = arrayOf<Int?>(
        0x00,
        0x00,
        0x00,
        null,
        0x66,
        0x74,
        0x79,
        0x70,
    )

    val signatureMap: MutableMap<String, Array<Int?>>
    val extensionMap: MutableMap<String, String>

    init {
        signatureMap = mutableMapOf<String, Array<Int?>>()
        signatureMap.put("MP3IDv2", mp3v2Sig)
        signatureMap.put("MP3IDv1_1", mp3v1Sig_1)
        signatureMap.put("MP3IDv1_2", mp3v1Sig_2)
        signatureMap.put("MP3IDv1_3", mp3v1Sig_3)
        signatureMap.put("MP3IDv1_4", mp3v1Sig_4)
        signatureMap.put("MP4", mp4Sig)

        extensionMap = mutableMapOf<String, String>()
        extensionMap.put("MP3IDv2", "mp3")
        extensionMap.put("MP3IDv1_1", "mp3")
        extensionMap.put("MP3IDv1_2", "mp3")
        extensionMap.put("MP3IDv1_3", "mp3")
        extensionMap.put("MP3IDv1_4", "mp3")
        extensionMap.put("MP4", "m4a")
        extensionMap.put("UNKNOWN", "")
    }

    fun getMagicFileType(f: File?): String {
        val buffer = ByteArray(BUFFER_SIZE)
        val `in`: InputStream = FileInputStream(f)
        try {
            var n = `in`.read(buffer, 0, BUFFER_SIZE)
            var m = n
            while ((m < MAX_SIGNATURE_SIZE) && (n > 0)) {
                n = `in`.read(buffer, m, BUFFER_SIZE - m)
                m += n
            }

            var fileType: String = "UNKNOWN"
            val i = signatureMap.keys.iterator()
            while (i.hasNext()

            ) {
                val key = i.next()
                if (matchesSignature(signatureMap.get(key)?:arrayOf(), buffer, m)) {
                    fileType = key
                    break
                }
            }
            return fileType
        } finally {
            `in`.close()
        }
    }

    private fun matchesSignature(
        signature: Array<Int?>,
        buffer: ByteArray,
        size: Int
    ): Boolean {
        if (size < signature.size) {
            return false
        }

        var b = true
        for (i in signature.indices) {
            if (signature[i] != null) {
                if (signature[i] != (0x00ff and buffer[i].toInt())) {
                    b = false
                    break
                }
            }
        }

        return b
    }

    fun getMagicExt(fileType: String?): String? {
        return extensionMap.get(fileType)
    }
}
