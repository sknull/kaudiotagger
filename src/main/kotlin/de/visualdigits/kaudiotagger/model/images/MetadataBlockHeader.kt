package de.visualdigits.kaudiotagger.model.images

import de.visualdigits.kaudiotagger.model.common.types.BlockType
import de.visualdigits.kaudiotagger.util.ErrorMessage
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel


/**
 * Metadata Block Header
 */
class MetadataBlockHeader {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    val isLastBlock: Boolean
    val dataLength: Int
    val bytes: ByteArray
    val blockType: BlockType?

    /**
     * Construct header by reading bytes
     *
     * @param rawdata
     */
    constructor(rawdata: ByteBuffer) {
        isLastBlock = ((rawdata[0].toInt() and 0x80) ushr 7) == 1
        val type = rawdata[0].toInt() and 0x7F
        if (type < BlockType.entries.size) {
            blockType = BlockType.entries[type]
            dataLength =
                (u(rawdata[1].toInt()) shl 16) +
                        (u(rawdata[2].toInt()) shl 8) +
                        (u(rawdata[3].toInt()))
            bytes = ByteArray(HEADER_LENGTH)
            for (i in 0..<HEADER_LENGTH) {
                bytes[i] = rawdata[i]
            }
        } else {
            error(ErrorMessage.FLAC_NO_BLOCKTYPE.getMsg(type))
        }
    }

    private fun u(i: Int): Int {
        return i and 0xFF
    }

    /**
     * Construct a new header in order to write metadatablock to file
     *
     * @param isLastBlock
     * @param blockType
     * @param dataLength
     */
    constructor(
        isLastBlock: Boolean,
        blockType: BlockType,
        dataLength: Int
    ) {
        val rawdata = ByteBuffer.allocate(HEADER_LENGTH)
        this.blockType = blockType
        this.isLastBlock = isLastBlock
        this.dataLength = dataLength

        val type: Byte
        if (isLastBlock) {
            type = (0x80 or blockType.id).toByte()
        } else {
            type = blockType.id.toByte()
        }
        rawdata.put(type)

        // Size is 3Byte BigEndian int
        rawdata.put(((dataLength and 0xFF0000) ushr 16).toByte())
        rawdata.put(((dataLength and 0xFF00) ushr 8).toByte())
        rawdata.put((dataLength and 0xFF).toByte())

        bytes = ByteArray(HEADER_LENGTH)
        for (i in 0..<HEADER_LENGTH) {
            bytes[i] = rawdata[i]
        }
    }

    override fun toString(): String {
        return ("BlockType:" +
                blockType +
                " DataLength:" +
                dataLength +
                " isLastBlock:" +
                isLastBlock
                )
    }

    fun getBytesWithoutIsLastBlockFlag(): ByteArray {
        bytes[0] = (bytes[0].toInt() and 0x7F).toByte()
        return bytes
    }

    companion object {
        const val BLOCK_TYPE_LENGTH: Int = 1
        const val BLOCK_LENGTH: Int = 3
            val HEADER_LENGTH: Int = BLOCK_TYPE_LENGTH + BLOCK_LENGTH

        /**
         * Create header by reading from file
         *
         * @param fc
         * @return
             */
        @JvmStatic
            fun readHeader(fc: FileChannel): MetadataBlockHeader {
            val rawdata = ByteBuffer.allocate(HEADER_LENGTH)
            val bytesRead = fc.read(rawdata)
            if (bytesRead < HEADER_LENGTH) {
                throw IOException(
                    "Unable to read required number of databytes read:" +
                            bytesRead +
                            ":required:" +
                            HEADER_LENGTH
                )
            }
            rawdata.rewind()
            return MetadataBlockHeader(rawdata)
        }
    }
}
