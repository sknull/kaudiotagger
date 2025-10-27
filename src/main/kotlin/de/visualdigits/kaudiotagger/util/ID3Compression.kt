package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameException
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer
import java.util.zip.DataFormatException
import java.util.zip.Inflater

object ID3Compression {

    val log: Logger = LoggerFactory.getLogger(ID3Compression::class.java)

    /**
     * Decompress realFrameSize bytes to decompressedFrameSize bytes and return as ByteBuffer
     *
     * @param byteBuffer
     * @param decompressedFrameSize
     * @param realFrameSize
     * @return
     * @throws InvalidFrameException
     */
    fun uncompress(
        identifier: String?,
        byteBuffer: ByteBuffer,
        decompressedFrameSize: Int,
        realFrameSize: Int
    ): ByteBuffer {
        log.debug(
            "About to decompress $realFrameSize bytes, expect result to be:$decompressedFrameSize bytes"
        )
        // Decompress the bytes into this buffer, size initialized from header field
        val result = ByteArray(decompressedFrameSize)
        val input = ByteArray(realFrameSize)

        //Store position ( just after frame header and any extra bits)
        //Read frame data into array, and then put buffer back to where it was
        val position = byteBuffer.position()
        byteBuffer.get(input, 0, realFrameSize)
        byteBuffer.position(position)

        val decompresser = Inflater()
        decompresser.setInput(input)
        try {
            val inflatedTo = decompresser.inflate(result)
            log.debug("Decompressed to $inflatedTo bytes")
        } catch (dfe: DataFormatException) {
            log.debug("Unable to decompress this frame:$identifier", dfe)

            //Update position of main buffer, so no attempt is made to reread these bytes
            byteBuffer.position(byteBuffer.position() + realFrameSize)
            throw InvalidFrameException(
                ErrorMessage.ID3_UNABLE_TO_DECOMPRESS_FRAME.getMsg(
                    identifier,
                    dfe.message
                )
            )
        }
        decompresser.end()
        
        return ByteBuffer.wrap(result)
    }
}