package de.visualdigits.kaudiotagger.model.audiofile.frame

import de.visualdigits.kaudiotagger.model.audiofile.header.mp3.MPEGFrameHeader
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

class XingFrame {

    companion object {

        val log: Logger = LoggerFactory.getLogger(XingFrame::class.java)

        //The offset into first frame varies based on the MPEG frame properties
        const val MPEG_VERSION_1_MODE_MONO_OFFSET: Int = 21
        const val MPEG_VERSION_1_MODE_STEREO_OFFSET: Int = 36
        const val MPEG_VERSION_2_MODE_MONO_OFFSET: Int = 13
        const val MPEG_VERSION_2_MODE_STEREO_OFFSET: Int = 21

        const val XING_HEADER_BUFFER_SIZE: Int = 120
        val MAX_BUFFER_SIZE_NEEDED_TO_READ_XING: Int = MPEG_VERSION_1_MODE_STEREO_OFFSET +
                XING_HEADER_BUFFER_SIZE +
                LameFrame.LAME_HEADER_BUFFER_SIZE
        const val XING_IDENTIFIER_BUFFER_SIZE: Int = 4
        const val XING_FLAG_BUFFER_SIZE: Int = 4
        const val XING_FRAMECOUNT_BUFFER_SIZE: Int = 4
        const val XING_AUDIOSIZE_BUFFER_SIZE: Int = 4
        const val BYTE_1: Int = 0
        const val BYTE_2: Int = 1
        const val BYTE_3: Int = 2
        const val BYTE_4: Int = 3

        /**
         * Use when it is a VBR (Variable Bitrate) file
         */
        val XING_VBR_ID: ByteArray =
            byteArrayOf('X'.code.toByte(), 'i'.code.toByte(), 'n'.code.toByte(), 'g'.code.toByte())

        /**
         * Use when it is a CBR (Constant Bitrate) file
         */
        val XING_CBR_ID: ByteArray =
            byteArrayOf('I'.code.toByte(), 'n'.code.toByte(), 'f'.code.toByte(), 'o'.code.toByte())

        /**
         * Parse the XingFrame of an MP3File, cannot be called until we have validated that
         * this is a XingFrame
         *
         * @return XingFrame
         */
        fun parseXingFrame(header: ByteBuffer?): XingFrame? {
            return header?.let { h -> XingFrame(h) }
        }

        /**
         * IS this a Xing frame
         *
         * @param bb
         * @param mpegFrameHeader
         *
         * @return ByteBuffer
         */
        fun isXingFrame(
            bb: ByteBuffer,
            mpegFrameHeader: MPEGFrameHeader?
        ): ByteBuffer? {
            if (mpegFrameHeader == null) {
                return null
            }

            //We store this so can return here after scanning through buffer
            val startPosition = bb.position()

            //Get to Start of where Xing Frame Should be ( we dont know if it is one at this point)
            if (mpegFrameHeader.version == MPEGFrameHeader.Companion.VERSION_1) {
                if (mpegFrameHeader.channelMode == MPEGFrameHeader.Companion.MODE_MONO) {
                    bb.position(startPosition + MPEG_VERSION_1_MODE_MONO_OFFSET)
                } else {
                    bb.position(startPosition + MPEG_VERSION_1_MODE_STEREO_OFFSET)
                }
            }
            //MPEGVersion 2 and 2.5
            else {
                if (mpegFrameHeader.channelMode == MPEGFrameHeader.Companion.MODE_MONO) {
                    bb.position(startPosition + MPEG_VERSION_2_MODE_MONO_OFFSET)
                } else {
                    bb.position(startPosition + MPEG_VERSION_2_MODE_STEREO_OFFSET)
                }
            }

            //Create header from here
            val header = bb.slice()

            // Return Buffer to start Point
            bb.position(startPosition)

            //Check Identifier
            val identifier = ByteArray(XING_IDENTIFIER_BUFFER_SIZE)
            header.get(identifier)
            if (!identifier.contentEquals(XING_VBR_ID) && !identifier.contentEquals(XING_CBR_ID)) {
                return null
            }
            log.debug("Found Xing Frame")

            return header
        }
    }

    var isVbr = false
    var isFrameCountEnabled = false
    var frameCount = -1
    var isAudioSizeEnabled = false
    var audioSize = -1
    var lameFrame: LameFrame? = null

    val header: ByteBuffer

    constructor(header: ByteBuffer) {
        this.header = header
        //Go to start of Buffer
        header.rewind()

        //Set Vbr
        setVbr()

        //Read Flags, only the fourth byte of interest to us
        val flagBuffer = ByteArray(XING_FLAG_BUFFER_SIZE)
        header.get(flagBuffer)

        //Read FrameCount if flag set
        if ((flagBuffer[XingFrame.BYTE_4].toInt() and (1).toByte().toInt()) != 0) {
            setFrameCount()
        }

        //Read Size if flag set
        if ((flagBuffer[XingFrame.BYTE_4].toInt() and (1 shl 1).toByte().toInt()) != 0) {
            setAudioSize()
        }

        //TODO TOC
        //TODO VBR Quality

        //Look for LAME Header as long as we have enough bytes to do it properly
        if (header.limit() >=
            XingFrame.XING_HEADER_BUFFER_SIZE + LameFrame.LAME_HEADER_BUFFER_SIZE
        ) {
            header.position(XingFrame.XING_HEADER_BUFFER_SIZE)
            lameFrame = LameFrame.parseLameFrame(header)
        }
    }

    /**
     * Set whether or not VBR, (Xing can also be used for CBR though this is less useful)
     */
    private fun setVbr() {
        //Is it VBR or CBR
        val identifier = ByteArray(XING_IDENTIFIER_BUFFER_SIZE)
        header.get(identifier)
        if (identifier.contentEquals(XING_VBR_ID)) {
            log.debug("Is Vbr")
            isVbr = true
        }
    }

    /**
     * Set count of frames
     */
    private fun setFrameCount() {
        val frameCountBuffer = ByteArray(XING_FRAMECOUNT_BUFFER_SIZE)
        header.get(frameCountBuffer)
        isFrameCountEnabled = true
        frameCount = ((frameCountBuffer[BYTE_1].toInt() shl 24) and -0x1000000) or
                    ((frameCountBuffer[BYTE_2].toInt() shl 16) and 0x00FF0000) or
                    ((frameCountBuffer[BYTE_3].toInt() shl 8) and 0x0000FF00) or
                    (frameCountBuffer[BYTE_4].toInt() and 0x000000FF)
    }

    /**
     * Set size of AudioData
     */
    private fun setAudioSize() {
        val frameSizeBuffer = ByteArray(XING_AUDIOSIZE_BUFFER_SIZE)
        header.get(frameSizeBuffer)
        isAudioSizeEnabled = true
        audioSize = ((frameSizeBuffer[BYTE_1].toInt() shl 24) and -0x1000000) or
                    ((frameSizeBuffer[BYTE_2].toInt() shl 16) and 0x00FF0000) or
                    ((frameSizeBuffer[BYTE_3].toInt() shl 8) and 0x0000FF00) or
                    (frameSizeBuffer[BYTE_4].toInt() and 0x000000FF)
    }

    /**
     * @return a string representation
     */
    override fun toString(): String {
        return ("xingheader vbr:$isVbr frameCountEnabled:$isFrameCountEnabled frameCount:$frameCount audioSizeEnabled:$isAudioSizeEnabled audioFileSize:$audioSize")
    }
}