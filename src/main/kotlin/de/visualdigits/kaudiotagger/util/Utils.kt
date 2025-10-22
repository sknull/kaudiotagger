package de.visualdigits.kaudiotagger.util

import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

object Utils {

    const val MAX_BASE_TEMP_FILENAME_LENGTH: Int = 20

    var BITS_IN_BYTE_MULTIPLIER: Int = 8
    var KILOBYTE_MULTIPLIER: Int = 1000

    /**
     * Reads bytes from a ByteBuffer as if they were encoded in the specified CharSet.
     *
     * @param buffer
     * @param offset   offset from current position
     * @param length   size of data to process
     * @param encoding
     *
     * @return String
     */
    fun getString(
        buffer: ByteBuffer,
        offset: Int,
        length: Int,
        encoding: Charset
    ): String {
        val b = ByteArray(length)
        buffer.position(buffer.position() + offset);
        buffer.get(b);

        return String(b, 0, length, encoding);
    }

    /**
     * Get a base for temp file, this should be long enough so that it easy to work out later what file the temp file
     * was created for if it is left lying round, but not ridiculously long as this can cause problems with max filename
     * limits and is not very useful.
     *
     * @param file
     * @return
     */
    fun getBaseFilenameForTempFile(file: File): String {
        val filename: String = getMinBaseFilenameAllowedForTempFile(file)
        if (filename.length <= MAX_BASE_TEMP_FILENAME_LENGTH) {
            return filename
        }
        return filename.substring(0, MAX_BASE_TEMP_FILENAME_LENGTH)
    }

    /**
     * @param file
     * @return filename with audioformat separator stripped of, lengthened to ensure not too small for valid tempfile
     * creation.
     */
    fun getMinBaseFilenameAllowedForTempFile(file: File): String {
        val s: String = file.nameWithoutExtension
        return if (s.length >= 3) {
            s
        } else if (s.length == 0) {
            s + "000"
        } else if (s.length == 1) {
            s + "00"
        } else if (s.length == 2) {
            s + "0"
        } else {
            s
        }
    }

    /**
     * @param fc
     * @param size
     *
     * @return ByteBuffer
     */
    fun readFileDataIntoBufferBE(
        fc: FileChannel,
        size: Int
    ): ByteBuffer {
        val tagBuffer = ByteBuffer.allocateDirect(size)
        fc.read(tagBuffer)
        tagBuffer.position(0)
        tagBuffer.order(ByteOrder.BIG_ENDIAN)
        return tagBuffer
    }

    /**
     * Reads 3 bytes and concatenates them into a String.
     * This pattern is used for ID's of various kinds.
     *
     * @param bytes
     * @return
     */
    fun readThreeBytesAsChars(bytes: ByteBuffer): String {
        val b = ByteArray(3)
        bytes.get(b);
        return String(b, StandardCharsets.ISO_8859_1);
    }

    /**
     * Reads 4 bytes and concatenates them into a String.
     * This pattern is used for ID's of various kinds.
     *
     * @param bytes
     * @return
     * @throws java.io.IOException
     */
    fun readFourBytesAsChars(bytes: ByteBuffer): String? {
        if (bytes.remaining() < 4) {
            return null;
        }

        val b = ByteArray(4)
        bytes.get(b);
        return String(b, StandardCharsets.ISO_8859_1);
    }
}