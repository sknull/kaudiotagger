package de.visualdigits.kaudiotagger.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.DataInput
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.nio.channels.FileChannel
import java.nio.channels.ReadableByteChannel
import java.nio.channels.WritableByteChannel
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import java.util.Locale
import kotlin.math.min

/**
 * Contains various frequently used static functions in the different tag formats.
 *
 * @author Raphael Slinckx
 */
object Utils {
    
    internal val log: Logger = LoggerFactory.getLogger(Utils::class.java)

    private const val MAX_BASE_TEMP_FILENAME_LENGTH = 20
    private val boxed2primitive: MutableMap<Class<*>?, Class<*>?> = mutableMapOf<Class<*>?, Class<*>?>()

    var BITS_IN_BYTE_MULTIPLIER: Int = 8
    var KILOBYTE_MULTIPLIER: Int = 1000

    init {
        boxed2primitive.put(Void::class.java, Void.TYPE)
        boxed2primitive.put(Byte::class.java, Byte::class.javaPrimitiveType)
        boxed2primitive.put(Short::class.java, Short::class.javaPrimitiveType)
        boxed2primitive.put(Char::class.java, Char::class.javaPrimitiveType)
        boxed2primitive.put(Int::class.java, Int::class.javaPrimitiveType)
        boxed2primitive.put(Long::class.java, Long::class.javaPrimitiveType)
        boxed2primitive.put(Float::class.java, Float::class.javaPrimitiveType)
        boxed2primitive.put(Double::class.java, Double::class.javaPrimitiveType)
    }

    /**
     * Returns the extension of the given file.
     * The extension is empty if there is no extension
     * The extension is the string after the last "."
     *
     * @param f The file whose extension is requested
     * @return The extension of the given file
     */
    fun getExtension(f: File): String {
        return getExtension(f.getName().lowercase(Locale.getDefault()))
    }

    fun getExtension(name: String): String {
        val i = name.lastIndexOf(".")
        if (i == -1) {
            return ""
        }

        return name.substring(i + 1)
    }

    /**
     * Returns the extension of the given file based on the file signature.
     * The extension is empty if the file signature is not recognized.
     *
     * @param f The file whose extension is requested
     * @return The extension of the given file
     */
    fun getMagicExtension(f: File?): String? {
        val fileType = FileTypeUtil.getMagicFileType(f)
        return FileTypeUtil.getMagicExt(fileType)
    }

    /**
     * Computes a number whereby the 1st byte is the least significant and the last
     * byte is the most significant. This version doesn't take a length,
     * and it returns an int rather than a long.
     *
     * @param b The byte array. Maximum length for valid results is 4 bytes.
     */
    fun getIntLE(b: ByteArray): Int {
        return getLongLE(ByteBuffer.wrap(b), 0, b.size - 1).toInt()
    }

    /**
     * Computes a number whereby the 1st byte is the least signifcant and the last
     * byte is the most significant.
     * So if storing a number which only requires one byte it will be stored in the first
     * byte.
     *
     * @param b The byte array @param start The starting offset in b
     * (b[offset]). The less significant byte @param end The end index
     * (included) in b (b[end]). The most significant byte
     * @return a long number represented by the byte sequence.
     */
    fun getLongLE(
        b: ByteBuffer,
        start: Int,
        end: Int
    ): Long {
        var number: Long = 0
        for (i in 0..<(end - start + 1)) {
            number += ((b.get(start + i).toInt() and 0xFF).toLong() shl (i * 8))
        }

        return number
    }

    /**
     * Computes a number whereby the 1st byte is the least significant and the last
     * byte is the most significant. end - start must be no greater than 4.
     *
     * @param b     The byte array
     * @param start The starting offset in b (b[offset]). The less
     * significant byte
     * @param end   The end index (included) in b (b[end])
     * @return a int number represented by the byte sequence.
     */
    fun getIntLE(b: ByteArray, start: Int, end: Int): Int {
        return getLongLE(ByteBuffer.wrap(b), start, end).toInt()
    }

    fun getLongLE(b: ByteArray, start: Int, end: Int): Long {
        return getLongLE(ByteBuffer.wrap(b), start, end)
    }

    /**
     * Computes a number whereby the 1st byte is the most significant and the last
     * byte is the least significant.
     *
     * @param b     The ByteBuffer
     * @param start The starting offset in b. The less
     * significant byte
     * @param end   The end index (included) in b
     * @return a short number represented by the byte sequence.
     */
    fun getShortBE(
        b: ByteBuffer,
        start: Int,
        end: Int
    ): Short {
        return getIntBE(b, start, end).toShort()
    }

    /**
     * Computes a number whereby the 1st byte is the most significant and the last
     * byte is the least significant.
     *
     * @param b     The ByteBuffer
     * @param start The starting offset in b. The less
     * significant byte
     * @param end   The end index (included) in b
     * @return an int number represented by the byte sequence.
     */
    fun getIntBE(
        b: ByteBuffer,
        start: Int,
        end: Int
    ): Int {
        return getLongBE(b, start, end).toInt()
    }

    /**
     * Computes a number whereby the 1st byte is the most significant and the last
     * byte is the least significant.
     *
     *
     * So if storing a number which only requires one byte it will be stored in the last
     * byte.
     *
     *
     * Will fail if end - start >= 8, due to the limitations of the long type.
     */
    fun getLongBE(
        b: ByteBuffer,
        start: Int,
        end: Int
    ): Long {
        var number: Long = 0
        for (i in 0..<(end - start + 1)) {
            number += (((b.get(end - i).toInt() and 0xFF)).toLong() shl (i * 8))
        }

        return number
    }

    /**
     * Convert int to byte representation - Big Endian (as used by mp4).
     *
     * @param size
     * @return byte representation
     */
    fun getSizeBEInt32(size: Int): ByteArray {
        val b = ByteArray(4)
        b[0] = ((size shr 24) and 0xFF).toByte()
        b[1] = ((size shr 16) and 0xFF).toByte()
        b[2] = ((size shr 8) and 0xFF).toByte()
        b[3] = (size and 0xFF).toByte()
        return b
    }

    /**
     * Convert short to byte representation - Big Endian (as used by mp4).
     *
     * @param size number to convert
     * @return byte representation
     */
    fun getSizeBEInt16(size: Short): ByteArray {
        val b = ByteArray(2)
        b[0] = ((size.toInt() shr 8) and 0xFF).toByte()
        b[1] = (size.toInt() and 0xFF).toByte()
        return b
    }

    /**
     * Convert int to byte representation - Little Endian (as used by ogg vorbis).
     *
     * @param size number to convert
     * @return byte representation
     */
    fun getSizeLEInt32(size: Int): ByteArray {
        val b = ByteArray(4)
        b[0] = (size and 0xff).toByte()
        b[1] = ((size ushr 8).toLong() and 0xffL).toByte()
        b[2] = ((size ushr 16).toLong() and 0xffL).toByte()
        b[3] = ((size ushr 24).toLong() and 0xffL).toByte()
        return b
    }

    /**
     * Convert a byte array to a Pascal string. The first byte is the byte count,
     * followed by that many active characters.
     *
     * @param bb
     * @return
     */
    fun readPascalString(bb: ByteBuffer): String {
        val len = u(bb.get()) //Read as unsigned value
        val buf = ByteArray(len)
        bb.get(buf)
        return String(buf, 0, len, StandardCharsets.ISO_8859_1)
    }

    /**
     * Used to convert (signed byte) to an integer as if signed byte was unsigned hence allowing
     * it to represent values 0 -> 255 rather than -128 -> 127.
     *
     * @param n
     * @return
     */
    fun u(n: Byte): Int {
        return n.toInt() and 0xFF
    }

    fun writePascalString(buffer: ByteBuffer, name: String) {
        buffer.put(name.length.toByte())
        buffer.put(name.toByteArray(StandardCharsets.ISO_8859_1))
    }

    /**
     * Reads bytes from a ByteBuffer as if they were encoded in the specified CharSet.
     *
     * @param buffer
     * @param offset   offset from current position
     * @param length   size of data to process
     * @param encoding
     * @return
     */
    fun getString(
        buffer: ByteBuffer,
        offset: Int,
        length: Int,
        encoding: Charset
    ): String {
        val b = ByteArray(length)
        buffer.position(buffer.position() + offset)
        buffer.get(b)
        return String(b, 0, length, encoding)
    }

    /**
     * Reads bytes from a ByteBuffer as if they were encoded in the specified CharSet.
     *
     * @param buffer
     * @param encoding
     * @return
     */
    fun getString(
        buffer: ByteBuffer,
        encoding: Charset
    ): String {
        val b = ByteArray(buffer.remaining())
        buffer.get(b)
        return String(b, encoding)
    }

    /**
     * Read a 32-bit big-endian unsigned integer using a DataInput.
     *
     *
     * Reads 4 bytes but returns as long
     */
    fun readUint32(di: DataInput): Long {
        val buf8 = byteArrayOf(0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00)
        di.readFully(buf8, 4, 4)
        return ByteBuffer.wrap(buf8).getLong()
    }

    /**
     * Read a 16-bit big-endian unsigned integer.
     *
     *
     * Reads 2 bytes but returns as an integer
     */
    fun readUint16(di: DataInput): Int {
        val buf = byteArrayOf(0x00, 0x00, 0x00, 0x00)
        di.readFully(buf, 2, 2)
        return ByteBuffer.wrap(buf).getInt()
    }

    /**
     * Read a string of a specified number of ASCII bytes.
     */
    fun readString(di: DataInput, charsToRead: Int): String {
        val buf = ByteArray(charsToRead)
        di.readFully(buf)
        return String(buf, StandardCharsets.US_ASCII)
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
        val filename = getMinBaseFilenameAllowedForTempFile(file)
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
        val s = file.nameWithoutExtension
        if (s.length >= 3) {
            return s
        }
        if (s.length == 1) {
            return s + "000"
        } else if (s.length == 1) {
            return s + "00"
        } else if (s.length == 2) {
            return s + "0"
        }
        return s
    }

    /**
     * Rename file, and if normal rename fails, try copy and delete instead.
     *
     * @param fromFile
     * @param toFile
     * @return
     */
    fun rename(fromFile: File, toFile: File): Boolean {
        log.debug(
            "Renaming From:" +
                    fromFile.absolutePath +
                    " to " +
                    toFile.absolutePath
        )

        if (toFile.exists()) {
            log.error("Destination File:" + toFile + " already exists")
            return false
        }

        //Rename File, could fail because being  used or because trying to rename over filesystems
        val result: Boolean
        try {
            result =
                Files.move(
                    fromFile.toPath(),
                    toFile.toPath(),
                    StandardCopyOption.REPLACE_EXISTING
                ) !=
                        null
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
        if (!result) {
            // Might be trying to rename over filesystem, so try copy and delete instead
            if (copy(fromFile, toFile)) {
                //If copy works but deletion of original file fails then it is because the file is being used
                //so we need to delete the file we have just created
                val deleteResult = fromFile.delete()
                if (!deleteResult) {
                    log.error("Unable to delete File:" + fromFile)
                    toFile.delete()
                    return false
                }
                return true
            } else {
                return false
            }
        }
        return true
    }

    /**
     * Copy a File.
     *
     *
     * ToDo refactor AbstractTestCase to use this method as it contains an exact duplicate.
     *
     * @param fromFile The existing File
     * @param toFile   The new File
     * @return `true` if and only if the renaming succeeded;
     * `false` otherwise
     */
    fun copy(fromFile: File, toFile: File): Boolean {
        try {
            copyThrowsOnException(fromFile, toFile)
            return true
        } catch (e: IOException) {
            log.error("Something went wrong", e)
            return false
        }
    }

    /**
     * Copy src file to dst file. FileChannels are used to maximize performance.
     *
     * @param source      source File
     * @param destination destination File which will be created or truncated, before copying, if it already exists
     */
    fun copyThrowsOnException(
        source: File,
        destination: File
    ) {
        // Must be done in a loop as there's no guarantee that a request smaller than request count will complete in one invocation.
        // Setting the transfer size more than about 1MB is pretty pointless because there is no asymptotic benefit. What you're trying
        // to achieve with larger transfer sizes is fewer context switches, and every time you double the transfer size you halve the
        // context switch cost. Pretty soon it vanishes into the noise.
        FileInputStream(source).use { inStream ->
            FileOutputStream(destination).use { outStream ->
                inStream.getChannel().use { inChannel ->
                    val size = inChannel.size()
                    outStream.getChannel().use { outChannel ->
                        var position: Long = 0
                        while (position < size) {
                            position += inChannel.transferTo(position, 1024L * 1024L, outChannel)
                        }
                    }
                }
            }
        }
    }

    fun write(to: ByteBuffer, from: ByteBuffer) {
        if (from.hasArray()) {
            to.put(
                from.array(),
                from.arrayOffset() + from.position(),
                min(to.remaining(), from.remaining())
            )
        } else {
            to.put(toArray(from))
        }
    }

    fun toArray(buffer: ByteBuffer): ByteArray {
        val result = ByteArray(buffer.remaining())
        buffer.duplicate().get(result)
        return result
    }

    fun skip(buffer: ByteBuffer, count: Int): Int {
        val toSkip = min(buffer.remaining(), count)
        buffer.position(buffer.position() + toSkip)
        return toSkip
    }

    fun read(buffer: ByteBuffer, count: Int): ByteBuffer {
        val slice = buffer.duplicate()
        val limit = buffer.position() + count
        slice.limit(limit)
        buffer.position(limit)
        return slice
    }

    fun reinterpretIntAsString(i: Int): String {
        return String(
            ByteBuffer.allocate(4).putInt(i).array(),
            StandardCharsets.ISO_8859_1
        )
    }

    fun reinterpretStringAsInt(str: String): Int {
        return ByteBuffer.wrap(str.toByteArray(StandardCharsets.ISO_8859_1)).getInt()
    }

    fun readBuf(buffer: ByteBuffer): ByteBuffer {
        val result = buffer.duplicate()
        buffer.position(buffer.limit())
        return result
    }

    /**
     * Reads 4 bytes and concatenates them into a String.
     * This pattern is used for ID's of various kinds.
     *
     * @param bytes
     * @return
     */
    fun readFourBytesAsChars(bytes: ByteBuffer): String? {
        if (bytes.remaining() < 4) {
            return null
        }

        val b = ByteArray(4)
        bytes.get(b)
        return String(b, StandardCharsets.ISO_8859_1)
    }

    /**
     * Read a string of a specified number of ASCII bytes.
     */
    fun readString(
        bytes: ByteBuffer,
        charsToRead: Int
    ): String {
        val buf = ByteArray(charsToRead)
        bytes.get(buf)
        return String(buf, StandardCharsets.US_ASCII)
    }

    fun readNullTermStringCharset(
        buffer: ByteBuffer,
        charset: Charset
    ): String {
        val fork = buffer.duplicate()
        while (buffer.hasRemaining() && buffer.get().toInt() != 0) {
        }
        fork.limit(buffer.position() - 1)
        return String(toArray(fork), charset)
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
        bytes.get(b)
        return String(b, StandardCharsets.ISO_8859_1)
    }

    /**
     * Used to convert (signed integer) to an long as if signed integer was unsigned hence allowing
     * it to represent full range of integral values.
     *
     * @param n
     * @return
     */
    fun u(n: Int): Long {
        return n.toLong() and 0xFFFFFFFFL
    }

    /**
     * Used to convert (signed short) to an integer as if signed short was unsigned hence allowing
     * it to represent values 0 -> 65536 rather than -32786 -> 32786
     *
     * @param n
     * @return
     */
    fun u(n: Short): Int {
        return n.toInt() and 0xFFFF
    }

    fun fetchFromChannel(ch: ReadableByteChannel, size: Int): ByteBuffer {
        val buf = ByteBuffer.allocate(size)
        readFromChannel(ch, buf)
        buf.flip()

        return buf
    }

    fun readFromChannel(
        channel: ReadableByteChannel,
        buffer: ByteBuffer
    ): Int {
        val rem = buffer.position()
        while (channel.read(buffer) != -1 && buffer.hasRemaining());

        return buffer.position() - rem
    }

    fun duplicate(bb: ByteBuffer): ByteBuffer {
        val out = ByteBuffer.allocate(bb.remaining())
        out.put(bb.duplicate())
        out.flip()
        return out
    }

    fun copy(
        _in: ReadableByteChannel,
        out: WritableByteChannel,
        amount: Long
    ) {
        var amount = amount
        val buf = ByteBuffer.allocate(0x10000)
        var read: Int
        do {
            buf.position(0)
            buf.limit(min(amount, buf.capacity().toLong()).toInt())
            read = _in.read(buf)
            if (read != -1) {
                buf.flip()
                out.write(buf)
                amount -= read.toLong()
            }
        } while (read != -1 && amount > 0)
    }

    /**
     * @param fc
     * @param size
     * @return
     */
    fun readFileDataIntoBufferLE(
        fc: FileChannel,
        size: Int
    ): ByteBuffer {
        val tagBuffer = ByteBuffer.allocateDirect(size)
        fc.read(tagBuffer)
        tagBuffer.position(0)
        tagBuffer.order(ByteOrder.LITTLE_ENDIAN)
        return tagBuffer
    }

    /**
     * @param fc
     * @param size
     * @return
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
     * @param length
     * @return true if length is an odd number
     */
    fun isOddLength(length: Long): Boolean {
        return (length and 1L) != 0L
    }

    fun <T> newInstance(clazz: Class<T?>, params: Array<Any?>): T? {
        try {
            return clazz.getConstructor(*classes(params)).newInstance(*params)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
    }

    private fun classes(params: Array<Any?>): Array<Class<*>?> {
        val classes = arrayOfNulls<Class<*>>(params.size)
        for (i in params.indices) {
            val cls = params[i]?.javaClass
            classes[i] = if (boxed2primitive.containsKey(cls))
                boxed2primitive.get(cls)
            else
                cls
        }
        return classes
    }

    fun writeBER32(buffer: ByteBuffer, value: Int) {
        buffer.put(((value shr 21) or 0x80).toByte())
        buffer.put(((value shr 14) or 0x80).toByte())
        buffer.put(((value shr 7) or 0x80).toByte())
        buffer.put((value and 0x7F).toByte())
    }

    fun readBER32(input: ByteBuffer): Int {
        var size = 0
        for (i in 0..3) {
            val b = input.get()
            size = (size shl 7) or (b.toInt() and 0x7f)
            if (((b.toInt() and 0xff) shr 7) == 0) {
                break
            }
        }
        return size
    }
}
