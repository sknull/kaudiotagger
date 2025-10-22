package de.visualdigits.kaudiotagger.model.audiofile.header.mp3

import de.visualdigits.kaudiotagger.util.FileConstants
import java.nio.ByteBuffer
import kotlin.experimental.and
import kotlin.experimental.or

class MPEGFrameHeader(
    b: ByteArray
) {

    companion object {

        const val HEADER_SIZE: Int = 4

        /**
         * Sync Value to identify the start of an MPEGFrame
         */
        const val SYNC_SIZE: Int = 2
        const val SYNC_BYTE1: Byte = 0xFF.toByte()
        const val SYNC_BYTE2: Byte = 0xE0.toByte()
        const val SYNC_BIT_ANDSAMPING_BYTE3: Byte = 0xFC.toByte()

        /**
         * Constants for MPEG Version
         */
        val mpegVersionMap: MutableMap<Int, String> = mutableMapOf()
        const val VERSION_2_5: Int = 0
        const val VERSION_2: Int = 2
        const val VERSION_1: Int = 3

        /**
         * Constants for MPEG Layer
         */
        val mpegLayerMap: MutableMap<Int, String> = mutableMapOf()
        const val LAYER_I: Int = 3
        const val LAYER_II: Int = 2
        const val LAYER_III: Int = 1

        /**
         * Slot Size is dependent on Layer
         */
        const val LAYER_I_SLOT_SIZE: Int = 4
        const val LAYER_II_SLOT_SIZE: Int = 1
        const val LAYER_III_SLOT_SIZE: Int = 1

        /**
         * Constants for Channel mode
         */
        val modeMap: MutableMap<Int, String> = mutableMapOf()
        const val MODE_STEREO: Int = 0
        const val MODE_JOINT_STEREO: Int = 1
        const val MODE_DUAL_CHANNEL: Int = 2
        const val MODE_MONO: Int = 3
        const val EMPHASIS_NONE: Int = 0
        const val EMPHASIS_5015MS: Int = 1
        const val EMPHASIS_RESERVED: Int = 2
        const val EMPHASIS_CCITT: Int = 3

        /**
         * Constants for MP3 Frame header, each frame has a basic header of
         * 4 bytes
         */
        const val BYTE_1: Int = 0
        const val BYTE_2: Int = 1
        const val BYTE_3: Int = 2
        const val BYTE_4: Int = 3
        val header: ByteArray = ByteArray(HEADER_SIZE)

        /**
         * Bit Rates, the setBitrate varies for different Version and Layer
         */
        val bitrateMap: MutableMap<Int, Int> = mutableMapOf()

        /**
         * Constants for Emphasis
         */
        val emphasisMap: MutableMap<Int, String> = mutableMapOf()
        val modeExtensionMap: MutableMap<Int, String> = mutableMapOf()
        const val MODE_EXTENSION_NONE: Int = 0
        const val MODE_EXTENSION_ONE: Int = 1
        const val MODE_EXTENSION_TWO: Int = 2
        const val MODE_EXTENSION_THREE: Int = 3
        val modeExtensionLayerIIIMap: MutableMap<Int, String> = mutableMapOf()
        const val MODE_EXTENSION_OFF_OFF: Int = 0
        const val MODE_EXTENSION_ON_OFF: Int = 1
        const val MODE_EXTENSION_OFF_ON: Int = 2
        const val MODE_EXTENSION_ON_ON: Int = 3

        /**
         * Sampling Rate in Hz
         */
        val samplingRateMap: MutableMap<Int, MutableMap<Int, Int>> = mutableMapOf()
        val samplingV1Map: MutableMap<Int, Int> = mutableMapOf()
        val samplingV2Map: MutableMap<Int, Int> = mutableMapOf()
        val samplingV25Map: MutableMap<Int, Int> = mutableMapOf()
        /* Samples Per Frame */
        val samplesPerFrameMap: MutableMap<Int, MutableMap<Int, Int>> = mutableMapOf()
        val samplesPerFrameV1Map: MutableMap<Int, Int> = mutableMapOf()
        val samplesPerFrameV2Map: MutableMap<Int, Int> = mutableMapOf()
        val samplesPerFrameV25Map: MutableMap<Int, Int> = mutableMapOf()
        const val SCALE_BY_THOUSAND: Int = 1000
        const val LAYER_I_FRAME_SIZE_COEFFICIENT: Int = 12
        const val LAYER_II_FRAME_SIZE_COEFFICIENT: Int = 144
        const val LAYER_III_FRAME_SIZE_COEFFICIENT: Int = 144

        /**
         * MP3 Frame Header bit mask
         */
        val MASK_MP3_ID: Byte = FileConstants.BIT3

        /**
         * MP3 version, confusingly for MP3s the version is 1.
         */
        val MASK_MP3_VERSION: Byte = FileConstants.BIT4 or  FileConstants.BIT3

        /**
         * MP3 Layer, for MP3s the Layer is 3
         */
        val MASK_MP3_LAYER: Byte = FileConstants.BIT2 or FileConstants.BIT1

        /**
         * Does it include a CRC Checksum at end of header, this can be used to check the header.
         */
        val MASK_MP3_PROTECTION: Byte = FileConstants.BIT0

        /**
         * The setBitrate of this MP3
         */
        val MASK_MP3_BITRATE: Byte = FileConstants.BIT7 or
                FileConstants.BIT6 or
                FileConstants.BIT5 or
                FileConstants.BIT4

        /**
         * The sampling/frequency rate
         */
        val MASK_MP3_FREQUENCY: Byte = (FileConstants.BIT3 + FileConstants.BIT2).toByte()

        /**
         * An extra padding bit is sometimes used to make sure frames are exactly the right length
         */
        val MASK_MP3_PADDING: Byte = FileConstants.BIT1

        /**
         * bit set, for application specific
         */
        val MASK_MP3_PRIVACY: Byte = FileConstants.BIT0

        /**
         * Channel Mode, Stero/Mono/Dual Channel
         */
        val MASK_MP3_MODE: Byte = FileConstants.BIT7 or FileConstants.BIT6

        /**
         * MP3 Frame Header bit mask
         */
        val MASK_MP3_MODE_EXTENSION: Byte = FileConstants.BIT5 or FileConstants.BIT4

        /**
         * MP3 Frame Header bit mask
         */
        val MASK_MP3_COPY: Byte = FileConstants.BIT3

        /**
         * MP3 Frame Header bit mask
         */
        val MASK_MP3_HOME: Byte = FileConstants.BIT2

        /**
         * MP3 Frame Header bit mask
         */
        val MASK_MP3_EMPHASIS: Byte = FileConstants.BIT1 or FileConstants.BIT0


        /**
         * Parse the MPEGFrameHeader of an MP3File, file pointer returns at end of the frame header
         *
         * @param bb the byte buffer containing the header
         *
         * @return MPEGFrameHeader
         */
        fun parseMPEGHeader(bb: ByteBuffer): MPEGFrameHeader {
            val position = bb.position()
            bb.get(header, 0, HEADER_SIZE)
            bb.position(position)
            val frameHeader = MPEGFrameHeader(header)

            return frameHeader
        }

        /**
         * Gets the MPEGFrame attribute of the MPEGFrame object
         *
         * @param bb
         *
         * @return The mPEGFrame value
         */
        fun isMPEGFrame(bb: ByteBuffer): Boolean {
            val position = bb.position()
            return (((bb.get(position) and SYNC_BYTE1) == SYNC_BYTE1) &&
                    ((bb.get(position + 1.toByte()) and SYNC_BYTE2) == SYNC_BYTE2) &&
                    ((bb.get(position + 2.toByte()) and SYNC_BIT_ANDSAMPING_BYTE3) !=
                            SYNC_BIT_ANDSAMPING_BYTE3)
                    )
        }
    }

    var mpegBytes: ByteArray

    /**
     * The version of this MPEG frame (see the constants)
     */
    var version = 0

    var versionAsString: String = ""

    /**
     * Contains the mpeg layer of this frame (see constants)
     */
    var layer = 0

    var layerAsString: String = ""

    /**
     * Bitrate of this frame
     */
    var bitRate: Int = 0

    /**
     * Channel Mode of this Frame (see constants)
     */
    var channelMode = 0

    /**
     * Channel Mode of this Frame As English String
     */
    var channelModeAsString: String = ""

    /**
     * Emphasis of this frame
     */
    var emphasis = 0

    /**
     * Emphasis mode string
     */
    var emphasisAsString: String = ""

    /**
     * Mode Extension
     */
    var modeExtension: String = ""

    /**
     * Flag indicating if this frame has padding byte
     */
    var isPadding = false

    /**
     * Flag indicating if this frame contains copyrighted material
     */
    var isCopyrighted = false

    /**
     * Flag indicating if this frame contains original material
     */
    var isOriginal = false

    /**
     * Flag indicating if this frame is protected
     */
    var isProtected = false

    /**
     * Flag indicating if this frame is private
     */
    var isPrivate = false

    var samplingRate: Int = 0

    init {
        mpegBytes = b
        setBitrate()
        setVersion()
        setLayer()
        setProtected()
        setSamplingRate()
        setPadding()
        setPrivate()
        setChannelMode()
        setModeExtension()
        setCopyrighted()
        setOriginal()
        setEmphasis()

        mpegVersionMap.put(VERSION_2_5, "MPEG-2.5")
        mpegVersionMap.put(VERSION_2, "MPEG-2")
        mpegVersionMap.put(VERSION_1, "MPEG-1")

        mpegLayerMap.put(LAYER_I, "Layer 1")
        mpegLayerMap.put(LAYER_II, "Layer 2")
        mpegLayerMap.put(LAYER_III, "Layer 3")


        // MPEG-1, Layer I (E)
        bitrateMap.put(0x1E, 32)
        bitrateMap.put(0x2E, 64)
        bitrateMap.put(0x3E, 96)
        bitrateMap.put(0x4E, 128)
        bitrateMap.put(0x5E, 160)
        bitrateMap.put(0x6E, 192)
        bitrateMap.put(0x7E, 224)
        bitrateMap.put(0x8E, 256)
        bitrateMap.put(0x9E, 288)
        bitrateMap.put(0xAE, 320)
        bitrateMap.put(0xBE, 352)
        bitrateMap.put(0xCE, 384)
        bitrateMap.put(0xDE, 416)
        bitrateMap.put(0xEE, 448)

        // MPEG-1, Layer II (C)
        bitrateMap.put(0x1C, 32)
        bitrateMap.put(0x2C, 48)
        bitrateMap.put(0x3C, 56)
        bitrateMap.put(0x4C, 64)
        bitrateMap.put(0x5C, 80)
        bitrateMap.put(0x6C, 96)
        bitrateMap.put(0x7C, 112)
        bitrateMap.put(0x8C, 128)
        bitrateMap.put(0x9C, 160)
        bitrateMap.put(0xAC, 192)
        bitrateMap.put(0xBC, 224)
        bitrateMap.put(0xCC, 256)
        bitrateMap.put(0xDC, 320)
        bitrateMap.put(0xEC, 384)

        // MPEG-1, Layer III (A)
        bitrateMap.put(0x1A, 32)
        bitrateMap.put(0x2A, 40)
        bitrateMap.put(0x3A, 48)
        bitrateMap.put(0x4A, 56)
        bitrateMap.put(0x5A, 64)
        bitrateMap.put(0x6A, 80)
        bitrateMap.put(0x7A, 96)
        bitrateMap.put(0x8A, 112)
        bitrateMap.put(0x9A, 128)
        bitrateMap.put(0xAA, 160)
        bitrateMap.put(0xBA, 192)
        bitrateMap.put(0xCA, 224)
        bitrateMap.put(0xDA, 256)
        bitrateMap.put(0xEA, 320)

        // MPEG-2, Layer I (6)
        bitrateMap.put(0x16, 32)
        bitrateMap.put(0x26, 48)
        bitrateMap.put(0x36, 56)
        bitrateMap.put(0x46, 64)
        bitrateMap.put(0x56, 80)
        bitrateMap.put(0x66, 96)
        bitrateMap.put(0x76, 112)
        bitrateMap.put(0x86, 128)
        bitrateMap.put(0x96, 144)
        bitrateMap.put(0xA6, 160)
        bitrateMap.put(0xB6, 176)
        bitrateMap.put(0xC6, 192)
        bitrateMap.put(0xD6, 224)
        bitrateMap.put(0xE6, 256)

        // MPEG-2, Layer II (4)
        bitrateMap.put(0x14, 8)
        bitrateMap.put(0x24, 16)
        bitrateMap.put(0x34, 24)
        bitrateMap.put(0x44, 32)
        bitrateMap.put(0x54, 40)
        bitrateMap.put(0x64, 48)
        bitrateMap.put(0x74, 56)
        bitrateMap.put(0x84, 64)
        bitrateMap.put(0x94, 80)
        bitrateMap.put(0xA4, 96)
        bitrateMap.put(0xB4, 112)
        bitrateMap.put(0xC4, 128)
        bitrateMap.put(0xD4, 144)
        bitrateMap.put(0xE4, 160)

        // MPEG-2, Layer III (2)
        bitrateMap.put(0x12, 8)
        bitrateMap.put(0x22, 16)
        bitrateMap.put(0x32, 24)
        bitrateMap.put(0x42, 32)
        bitrateMap.put(0x52, 40)
        bitrateMap.put(0x62, 48)
        bitrateMap.put(0x72, 56)
        bitrateMap.put(0x82, 64)
        bitrateMap.put(0x92, 80)
        bitrateMap.put(0xA2, 96)
        bitrateMap.put(0xB2, 112)
        bitrateMap.put(0xC2, 128)
        bitrateMap.put(0xD2, 144)
        bitrateMap.put(0xE2, 160)

        modeMap.put(MODE_STEREO, "Stereo")
        modeMap.put(MODE_JOINT_STEREO, "Joint Stereo")
        modeMap.put(MODE_DUAL_CHANNEL, "Dual")
        modeMap.put(MODE_MONO, "Mono")

        emphasisMap.put(EMPHASIS_NONE, "None")
        emphasisMap.put(EMPHASIS_5015MS, "5015MS")
        emphasisMap.put(EMPHASIS_RESERVED, "Reserved")
        emphasisMap.put(EMPHASIS_CCITT, "CCITT")

        modeExtensionMap.put(MODE_EXTENSION_NONE, "4-31")
        modeExtensionMap.put(MODE_EXTENSION_ONE, "8-31")
        modeExtensionMap.put(MODE_EXTENSION_TWO, "12-31")
        modeExtensionMap.put(MODE_EXTENSION_THREE, "16-31")

        modeExtensionLayerIIIMap.put(MODE_EXTENSION_OFF_OFF, "off-off")
        modeExtensionLayerIIIMap.put(MODE_EXTENSION_ON_OFF, "on-off")
        modeExtensionLayerIIIMap.put(MODE_EXTENSION_OFF_ON, "off-on")
        modeExtensionLayerIIIMap.put(MODE_EXTENSION_ON_ON, "on-on")

        samplingV1Map.put(0, 44100)
        samplingV1Map.put(1, 48000)
        samplingV1Map.put(2, 32000)

        samplingV2Map.put(0, 22050)
        samplingV2Map.put(1, 24000)
        samplingV2Map.put(2, 16000)

        samplingV25Map.put(0, 11025)
        samplingV25Map.put(1, 12000)
        samplingV25Map.put(2, 8000)

        samplingRateMap.put(VERSION_1, samplingV1Map)
        samplingRateMap.put(VERSION_2, samplingV2Map)
        samplingRateMap.put(VERSION_2_5, samplingV25Map)

        samplesPerFrameV1Map.put(LAYER_I, 384)
        samplesPerFrameV1Map.put(LAYER_II, 1152)
        samplesPerFrameV1Map.put(LAYER_III, 1152)

        samplesPerFrameV2Map.put(LAYER_I, 384)
        samplesPerFrameV2Map.put(LAYER_II, 1152)
        samplesPerFrameV2Map.put(LAYER_III, 1152)

        samplesPerFrameV25Map.put(LAYER_I, 384)
        samplesPerFrameV25Map.put(LAYER_II, 1152)
        samplesPerFrameV25Map.put(LAYER_III, 1152)

        samplesPerFrameMap.put(VERSION_1, samplesPerFrameV1Map)
        samplesPerFrameMap.put(VERSION_2, samplesPerFrameV2Map)
        samplesPerFrameMap.put(VERSION_2_5, samplesPerFrameV25Map)
    }

    /**
     * Gets the copyrighted attribute of the MPEGFrame object
     */
    fun setCopyrighted() {
        isCopyrighted = (mpegBytes[BYTE_4] and  MASK_MP3_COPY) != 0.toByte()
    }

    /**
     * Set the version of this frame as an int value (see constants)
     */
    fun setVersion() {
        //MPEG Version
        version =
            ((mpegBytes[BYTE_2] and MASK_MP3_VERSION).toInt() shr 3)
        versionAsString = mpegVersionMap[version]?:error("version not found")
    }

    /**
     * Sets the original attribute of the MPEGFrame object
     */
    fun setOriginal() {
        isOriginal = (mpegBytes[BYTE_4] and MASK_MP3_HOME) != 0.toByte()
    }

    /**
     * Sets the attribute of the MPEGFrame object
     */
    fun setProtected() {
        isProtected = (mpegBytes[BYTE_2] and MASK_MP3_PROTECTION) == 0x00.toByte()
    }

    /**
     * Sets the attribute of the MPEGFrame object
     */
    fun setPrivate() {
        isPrivate = (mpegBytes[BYTE_3] and MASK_MP3_PRIVACY) != 0.toByte()
    }

    /**
     * Get the setBitrate of this frame
     */
    fun setBitrate() {
        /* BitRate, get by checking header setBitrate bits and MPEG Version and Layer */
        val bitRateIndex =
            ((mpegBytes[BYTE_3] and MASK_MP3_BITRATE) or
                    (mpegBytes[BYTE_2] and MASK_MP3_ID) or
                    (mpegBytes[BYTE_2] and MASK_MP3_LAYER)).toInt()

        bitRate = bitrateMap[bitRateIndex]?:error("bitRateIndex not found")
    }

    /**
     * Set the Mpeg channel mode of this frame as a constant (see constants)
     */
    fun setChannelMode() {
        channelMode = (mpegBytes[BYTE_4] and MASK_MP3_MODE).toInt() ushr 6
        channelModeAsString = modeMap[channelMode]?:error("channelMode not found")
    }

    /**
     * Get the setEmphasis mode of this frame in a string representation
     */
    fun setEmphasis() {
        emphasis = (mpegBytes[BYTE_4] and MASK_MP3_EMPHASIS).toInt()
        emphasisAsString = emphasisMap[emphasis]?:error("emphasis not found")
    }

    fun getEmphasisAsString(): String {
        return emphasisAsString
    }

    /**
     * Set whether this frame uses padding bytes
     */
    fun setPadding() {
        isPadding = (mpegBytes[BYTE_3] and MASK_MP3_PADDING) != 0.toByte()
    }

    /**
     * Get the layer version of this frame as a constant int value (see constants)
     */
    fun setLayer() {
        layer = (mpegBytes[BYTE_2] and MASK_MP3_LAYER).toInt() ushr 1
        layerAsString = mpegLayerMap[layer]?:error("layer not found")
    }

    /**
     * Sets the string representation of the mode extension of this frame
     */
    fun setModeExtension() {
        val index = (mpegBytes[BYTE_4] and MASK_MP3_MODE_EXTENSION).toInt() shr 4
        if (layer == LAYER_III) {
            modeExtension = modeExtensionLayerIIIMap[index]?:error("index not found")
        } else {
            modeExtension = modeExtensionMap[index]?:error("index not found")
        }
    }

    /**
     * set the sampling rate in Hz of this frame
     */
    fun setSamplingRate() {
        //Frequency
        val index = (mpegBytes[BYTE_3] and MASK_MP3_FREQUENCY).toInt() ushr 2
        val samplingRateMapForVersion = samplingRateMap[version]!!
        samplingRate = samplingRateMapForVersion[index]?:error("index not found")
    }

    /**
     * Gets the paddingLength attribute of the MPEGFrame object
     *
     * @return The paddingLength value
     */
    fun getPaddingLength(): Int {
        if (isPadding) {
            return 1
        } else {
            return 0
        }
    }

    /**
     * Get the number of samples in a frame, all frames in a file have a set number of samples as defined by their MPEG Versiona
     * and Layer
     *
     * @return Int
     */
    fun getNoOfSamples(): Int {
        return (samplesPerFrameMap[version]?:error("version not found"))[layer]?:error("layer not found")
    }

    /**
     * Gets the number of channels
     *
     * @return The setChannelMode value
     */
    fun getNumberOfChannels(): Int {
        when (channelMode) {
            MODE_DUAL_CHANNEL -> return 2
            MODE_JOINT_STEREO -> return 2
            MODE_MONO -> return 1
            MODE_STEREO -> return 2
            else -> return 0
        }
    }

    /**
     * Gets this frame length in bytes, value should always be rounded down to the nearest byte (not rounded up)
     *
     * Calculation is Bitrate (scaled to bps) divided by sampling frequency (in Hz), The larger the bitrate the larger
     * the frame but the more samples per second the smaller the value, also have to take into account frame padding
     * Have to multiple by a coefficient constant depending upon the layer it is encoded in,
     */
    fun getFrameLength(): Int {
        when (version) {
            VERSION_2, VERSION_2_5 -> when (layer) {
                LAYER_I -> return (((LAYER_I_FRAME_SIZE_COEFFICIENT *
                        (bitRate * SCALE_BY_THOUSAND)) /
                        samplingRate +
                        getPaddingLength()) *
                        LAYER_I_SLOT_SIZE
                        )

                LAYER_II -> return (((LAYER_II_FRAME_SIZE_COEFFICIENT) *
                        (bitRate * SCALE_BY_THOUSAND)) /
                        samplingRate +
                        getPaddingLength() * LAYER_II_SLOT_SIZE
                        )

                LAYER_III -> if (channelMode == MODE_MONO) {
                    return (((LAYER_III_FRAME_SIZE_COEFFICIENT / 2) *
                            (bitRate * SCALE_BY_THOUSAND)) /
                            samplingRate +
                            getPaddingLength() * LAYER_III_SLOT_SIZE
                            )
                } else {
                    return (((LAYER_III_FRAME_SIZE_COEFFICIENT) *
                            (bitRate * SCALE_BY_THOUSAND)) /
                            samplingRate +
                            getPaddingLength() * LAYER_III_SLOT_SIZE
                            )
                }

                else -> throw RuntimeException("Mp3 Unknown Layer:" + layer)
            }

            VERSION_1 -> when (layer) {
                LAYER_I -> return (((LAYER_I_FRAME_SIZE_COEFFICIENT *
                        (bitRate * SCALE_BY_THOUSAND)) /
                        samplingRate +
                        getPaddingLength()) *
                        LAYER_I_SLOT_SIZE
                        )

                LAYER_II -> return ((LAYER_II_FRAME_SIZE_COEFFICIENT *
                        (bitRate * SCALE_BY_THOUSAND)) /
                        samplingRate +
                        getPaddingLength() * LAYER_II_SLOT_SIZE
                        )

                LAYER_III -> return ((LAYER_III_FRAME_SIZE_COEFFICIENT *
                        (bitRate * SCALE_BY_THOUSAND)) /
                        samplingRate +
                        getPaddingLength() * LAYER_III_SLOT_SIZE
                        )

                else -> throw RuntimeException("Mp3 Unknown Layer:" + layer)
            }

            else -> throw RuntimeException("Mp3 Unknown Version:" + version)
        }
    }
}