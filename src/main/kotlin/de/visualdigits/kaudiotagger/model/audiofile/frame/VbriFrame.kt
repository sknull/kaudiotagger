package de.visualdigits.kaudiotagger.model.audiofile.frame

import de.visualdigits.kaudiotagger.model.audiofile.header.mp3.MPEGFrameHeader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

class VbriFrame {

    companion object {

        val log: Logger = LoggerFactory.getLogger(VbriFrame::class.java)

        //The offset into frame
        val VBRI_OFFSET: Int = MPEGFrameHeader.HEADER_SIZE + 32

        const val VBRI_HEADER_BUFFER_SIZE: Int = 120 //TODO this is just a guess, not right
        val MAX_BUFFER_SIZE_NEEDED_TO_READ_VBRI: Int = VBRI_OFFSET + VBRI_HEADER_BUFFER_SIZE
        const val VBRI_IDENTIFIER_BUFFER_SIZE: Int = 4
        const val VBRI_DELAY_BUFFER_SIZE: Int = 2
        const val VBRI_QUALITY_BUFFER_SIZE: Int = 2
        const val VBRI_AUDIOSIZE_BUFFER_SIZE: Int = 4
        const val VBRI_FRAMECOUNT_BUFFER_SIZE: Int = 4
        const val VBRI_TOC_ENTRY_BUFFER_SIZE: Int = 2
        const val BYTE_1: Int = 0
        const val BYTE_2: Int = 1
        const val BYTE_3: Int = 2
        const val BYTE_4: Int = 3

        /** Identifier */
        val VBRI_VBR_ID: ByteArray =
            byteArrayOf('V'.code.toByte(), 'B'.code.toByte(), 'R'.code.toByte(), 'I'.code.toByte())

        /**
         * Parse the VBRIFrame of an MP3File, cannot be called until we have validated that
         * this is a VBRIFrame
         *
         * @return
         */
        fun parseVBRIFrame(header: ByteBuffer?): VbriFrame? {
            return header?.let { h -> VbriFrame(h) }
        }

        /**
         * IS this a VBRI frame
         *
         * @param bb
         * @param mpegFrameHeader
         * @return raw header if this is a VBRI frame
         */
        fun isVbriFrame(
            bb: ByteBuffer?
        ): ByteBuffer? {
            if (bb == null) {
                return null
            }
            //We store this so can return here after scanning through buffer
            val startPosition = bb.position()
            log.debug("Checking VBRI Frame at$startPosition")

            bb.position(startPosition + VBRI_OFFSET)

            //Create header from here
            val header = bb.slice()

            // Return Buffer to start Point
            bb.position(startPosition)

            //Check Identifier
            val identifier = ByteArray(VBRI_IDENTIFIER_BUFFER_SIZE)
            header.get(identifier)
            if (!identifier.contentEquals(VBRI_VBR_ID)) {
                return null
            }
            log.debug("Found VBRI Frame")
            return header
        }
    }

    var isVbr = false
    var frameCount = -1
    var audioSize = -1
    var lameFrame: LameFrame? = null
    val encoder: String = "Fraunhofer"

    val header: ByteBuffer

    constructor(header: ByteBuffer) {
        this.header = header
        //Go to start of Buffer
        header.rewind()
        header.position(10)
        setAudioSize()
        setFrameCount()
    }

    /**
     * Set size of AudioData
     */
    private fun setAudioSize() {
        val frameSizeBuffer = ByteArray(VBRI_AUDIOSIZE_BUFFER_SIZE)
        header.get(frameSizeBuffer)
        audioSize =
            ((frameSizeBuffer[BYTE_1].toInt() shl 24) and -0x1000000) or
                    ((frameSizeBuffer[BYTE_2].toInt() shl 16) and 0x00FF0000) or
                    ((frameSizeBuffer[BYTE_3].toInt() shl 8) and 0x0000FF00) or
                    (frameSizeBuffer[BYTE_4].toInt() and 0x000000FF)
    }

    /**
     * Set count of frames
     */
    private fun setFrameCount() {
        val frameCountBuffer = ByteArray(VBRI_FRAMECOUNT_BUFFER_SIZE)
        header.get(frameCountBuffer)
        frameCount =
            ((frameCountBuffer[BYTE_1].toInt() shl 24) and -0x1000000) or
                    ((frameCountBuffer[BYTE_2].toInt() shl 16) and 0x00FF0000) or
                    ((frameCountBuffer[BYTE_3].toInt() shl 8) and 0x0000FF00) or
                    (frameCountBuffer[BYTE_4].toInt() and 0x000000FF)
    }

    /**
     * @return a string represntation
     */
    override fun toString(): String {
        return ("VBRIheader vbr:$isVbr frameCount:$frameCount audioFileSize:$audioSize encoder:$encoder")
    }
}