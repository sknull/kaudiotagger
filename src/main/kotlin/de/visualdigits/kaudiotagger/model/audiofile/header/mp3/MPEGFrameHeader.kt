package de.visualdigits.kaudiotagger.model.audiofile.header.mp3

import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidAudioFrameException
import de.visualdigits.kaudiotagger.util.AbstractTagDisplayFormatter
import de.visualdigits.kaudiotagger.util.FileConstants
import java.nio.ByteBuffer

/**
 * Represents a MPEGFrameHeader, an MP3 is made up of a number of frames each frame starts with a four
 * byte frame header.
 */
class MPEGFrameHeader {

    var mpegBytes: ByteArray = byteArrayOf()

    /**
     * Gets the mPEGVersion attribute of the MPEGFrame object
     *
     * @return The mPEGVersion value
     */
    /**
     * The version of this MPEG frame (see the constants)
     */
    var version: Int = 0
        private set

    var versionAsString: String? = null

    /**
     * Gets the layerVersion attribute of the MPEGFrame object
     *
     * @return The layerVersion value
     */
    /**
     * Contains the mpeg layer of this frame (see constants)
     */
    var layer: Int = 0
        private set

    var layerAsString: String? = null

    /**
     * Bitrate of this frame
     */
    var bitRate: Int? = null

    /**
     * Channel Mode of this Frame (see constants)
     */
    var channelMode: Int = 0
        private set

    /**
     * Channel Mode of this Frame As English String
     */
    var channelModeAsString: String? = null

    /**
     * Emphasis of this frame
     */
    var emphasis: Int = 0
        private set

    /**
     * Emphasis mode string
     */
    var emphasisAsString: String? = null
        private set

    /**
     * Mode Extension
     */
    var modeExtension: String? = null
        private set

    /**
     * Flag indicating if this frame has padding byte
     */
    var isPadding: Boolean = false
        private set

    /**
     * Flag indicating if this frame contains copyrighted material
     */
    var isCopyrighted: Boolean = false
        private set

    /**
     * Flag indicating if this frame contains original material
     */
    var isOriginal: Boolean = false
        private set

    /**
     * Flag indicating if this frame is protected
     */
    var isProtected: Boolean = false
        private set

    /**
     * Flag indicating if this frame is private
     */
    var isPrivate: Boolean = false
        private set

    var samplingRate: Int? = null

    /**
     * Hide Constructor
     */
    private constructor()

    /**
     * Try and create a new MPEG frame with the given byte array and decodes its contents
     * If decoding header causes a problem it is not a valid header
     *
     * @param b the array of bytes representing this mpeg frame
     */
    private constructor(b: ByteArray) {
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
    }

    /**
     * Gets the copyrighted attribute of the MPEGFrame object
     */
    private fun setCopyrighted() {
        isCopyrighted = (mpegBytes[BYTE_4].toInt() and MASK_MP3_COPY) != 0
    }

    /**
     * Set the version of this frame as an int value (see constants)
     *
     */
    private fun setVersion() {
        //MPEG Version
        version = ((mpegBytes[BYTE_2].toInt() and MASK_MP3_VERSION) shr 3).toByte().toInt()
        versionAsString = mpegVersionMap.get(version)
        if (versionAsString == null) {
            throw InvalidAudioFrameException("Invalid mpeg version")
        }
    }

    /**
     * Sets the original attribute of the MPEGFrame object
     */
    private fun setOriginal() {
        isOriginal = (mpegBytes[BYTE_4].toInt() and MASK_MP3_HOME) != 0
    }

    /**
     * Sets the attribute of the MPEGFrame object
     */
    private fun setProtected() {
        isProtected = (mpegBytes[BYTE_2].toInt() and MASK_MP3_PROTECTION) == 0x00
    }

    /**
     * Sets the private attribute of the MPEGFrame object
     */
    private fun setPrivate() {
        isPrivate = (mpegBytes[BYTE_3].toInt() and MASK_MP3_PRIVACY) != 0
    }

    /**
     * Get the setBitrate of this frame
     *
     */
    private fun setBitrate() {
        /* BitRate, get by checking header setBitrate bits and MPEG Version and Layer */
        val bitRateIndex =
            (mpegBytes[BYTE_3].toInt() and MASK_MP3_BITRATE) or
                    (mpegBytes[BYTE_2].toInt() and MASK_MP3_ID) or
                    (mpegBytes[BYTE_2].toInt() and MASK_MP3_LAYER)

        bitRate = bitrateMap.get(bitRateIndex)
        if (bitRate == null) {
            throw InvalidAudioFrameException("Invalid bitrate")
        }
    }

    /**
     * Set the Mpeg channel mode of this frame as a constant (see constants)
     *
     */
    private fun setChannelMode() {
        channelMode = (mpegBytes[BYTE_4].toInt() and MASK_MP3_MODE) ushr 6
        channelModeAsString = modeMap.get(channelMode)
        if (channelModeAsString == null) {
            throw InvalidAudioFrameException("Invalid channel mode")
        }
    }

    /**
     * Get the setEmphasis mode of this frame in a string representation
     *
     */
    private fun setEmphasis() {
        emphasis = mpegBytes[BYTE_4].toInt() and MASK_MP3_EMPHASIS
        emphasisAsString = emphasisMap.get(emphasis)
        if (this.emphasisAsString == null) {
            throw InvalidAudioFrameException("Invalid emphasis")
        }
    }

    /**
     * Set whether this frame uses padding bytes
     */
    private fun setPadding() {
        isPadding = (mpegBytes[BYTE_3].toInt() and MASK_MP3_PADDING) != 0
    }

    /**
     * Get the layer version of this frame as a constant int value (see constants)
     *
     */
    private fun setLayer() {
        layer = (mpegBytes[BYTE_2].toInt() and MASK_MP3_LAYER) ushr 1
        layerAsString = mpegLayerMap.get(layer)
        if (layerAsString == null) {
            throw InvalidAudioFrameException("Invalid Layer")
        }
    }

    /**
     * Sets the string representation of the mode extension of this frame
     *
     */
    private fun setModeExtension() {
        val index = (mpegBytes[BYTE_4].toInt() and MASK_MP3_MODE_EXTENSION) shr 4
        if (layer == LAYER_III) {
            modeExtension = modeExtensionLayerIIIMap.get(index)
            if (this.modeExtension == null) {
                throw InvalidAudioFrameException("Invalid Mode Extension")
            }
        } else {
            modeExtension = modeExtensionMap.get(index)
            if (this.modeExtension == null) {
                throw InvalidAudioFrameException("Invalid Mode Extension")
            }
        }
    }

    /**
     * set the sampling rate in Hz of this frame
     *
     */
    private fun setSamplingRate() {
        //Frequency
        val index = (mpegBytes[BYTE_3].toInt() and MASK_MP3_FREQUENCY) ushr 2
        val samplingRateMapForVersion = samplingRateMap[version] ?: throw InvalidAudioFrameException("Invalid version")
        samplingRate = samplingRateMapForVersion.get(index)
        if (samplingRate == null) {
            throw InvalidAudioFrameException("Invalid sampling rate")
        }
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

    /*
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
                        (getBitRate() * SCALE_BY_THOUSAND)) /
                        getSamplingRate() +
                        getPaddingLength()) *
                        LAYER_I_SLOT_SIZE
                        )

                LAYER_II -> return (((LAYER_II_FRAME_SIZE_COEFFICIENT) *
                        (getBitRate() * SCALE_BY_THOUSAND)) /
                        getSamplingRate() +
                        getPaddingLength() * LAYER_II_SLOT_SIZE
                        )

                LAYER_III -> if (this.channelMode == MODE_MONO) {
                    return (((LAYER_III_FRAME_SIZE_COEFFICIENT / 2) *
                            (getBitRate() * SCALE_BY_THOUSAND)) /
                            getSamplingRate() +
                            getPaddingLength() * LAYER_III_SLOT_SIZE
                            )
                } else {
                    return (((LAYER_III_FRAME_SIZE_COEFFICIENT) *
                            (getBitRate() * SCALE_BY_THOUSAND)) /
                            getSamplingRate() +
                            getPaddingLength() * LAYER_III_SLOT_SIZE
                            )
                }

                else -> throw RuntimeException("Mp3 Unknown Layer:" + layer)
            }

            VERSION_1 -> when (layer) {
                LAYER_I -> return (((LAYER_I_FRAME_SIZE_COEFFICIENT *
                        (getBitRate() * SCALE_BY_THOUSAND)) /
                        getSamplingRate() +
                        getPaddingLength()) *
                        LAYER_I_SLOT_SIZE
                        )

                LAYER_II -> return ((LAYER_II_FRAME_SIZE_COEFFICIENT *
                        (getBitRate() * SCALE_BY_THOUSAND)) /
                        getSamplingRate() +
                        getPaddingLength() * LAYER_II_SLOT_SIZE
                        )

                LAYER_III -> return ((LAYER_III_FRAME_SIZE_COEFFICIENT *
                        (getBitRate() * SCALE_BY_THOUSAND)) /
                        getSamplingRate() +
                        getPaddingLength() * LAYER_III_SLOT_SIZE
                        )

                else -> throw RuntimeException("Mp3 Unknown Layer:" + layer)
            }

            else -> throw RuntimeException("Mp3 Unknown Version:" + version)
        }
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

    fun getBitRate(): Int {
        return bitRate ?: 0
    }

    fun getSamplingRate(): Int {
        return samplingRate ?: 0
    }

    /**
     * Get the number of samples in a frame, all frames in a file have a set number of samples as defined by their MPEG Versiona
     * and Layer
     *
     * @return
     */
    fun getNoOfSamples(): Int {
        return samplesPerFrameMap[version]?.get(layer) ?: 0
    }

    fun isVariableBitRate(): Boolean {
        return false
    }

    companion object {
        const val HEADER_SIZE: Int = 4

        /**
         * Sync Value to identify the start of an MPEGFrame
         */
        const val SYNC_SIZE: Int = 2
        const val SYNC_BYTE1: Int = 0xFF
        const val SYNC_BYTE2: Int = 0xE0
        const val SYNC_BIT_ANDSAMPING_BYTE3: Int = 0xFC

        /**
         * Constants for MPEG Version
         */
        val mpegVersionMap: MutableMap<Int?, String> = HashMap<Int?, String>()
        const val VERSION_2_5: Int = 0
        const val VERSION_2: Int = 2
        const val VERSION_1: Int = 3

        /**
         * Constants for MPEG Layer
         */
        val mpegLayerMap: MutableMap<Int?, String> = HashMap<Int?, String>()
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
        val modeMap: MutableMap<Int?, String> = HashMap<Int?, String>()
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
        private const val BYTE_1 = 0
        private const val BYTE_2 = 1
        private const val BYTE_3 = 2
        private const val BYTE_4 = 3
        val header = ByteArray(HEADER_SIZE)

        /**
         * Bit Rates, the setBitrate varies for different Version and Layer
         */
        val bitrateMap: MutableMap<Int?, Int> = HashMap<Int?, Int>()

        /**
         * Constants for Emphasis
         */
        val emphasisMap: MutableMap<Int?, String?> = HashMap<Int?, String?>()
        val modeExtensionMap: MutableMap<Int?, String?> = HashMap<Int?, String?>()
        private const val MODE_EXTENSION_NONE = 0
        private const val MODE_EXTENSION_ONE = 1
        private const val MODE_EXTENSION_TWO = 2
        private const val MODE_EXTENSION_THREE = 3
        val modeExtensionLayerIIIMap: MutableMap<Int?, String?> = HashMap<Int?, String?>()
        private const val MODE_EXTENSION_OFF_OFF = 0
        private const val MODE_EXTENSION_ON_OFF = 1
        private const val MODE_EXTENSION_OFF_ON = 2
        private const val MODE_EXTENSION_ON_ON = 3

        /**
         * Sampling Rate in Hz
         */
        val samplingRateMap = mutableMapOf<Int, MutableMap<Int, Int>>()
        val samplingV1Map = mutableMapOf<Int, Int>()
        val samplingV2Map = mutableMapOf<Int, Int>()
        val samplingV25Map = mutableMapOf<Int, Int>()

        /* Samples Per Frame */
        val samplesPerFrameMap = mutableMapOf<Int, MutableMap<Int, Int>>()
        val samplesPerFrameV1Map = mutableMapOf<Int, Int>()
        val samplesPerFrameV2Map = mutableMapOf<Int, Int>()
        val samplesPerFrameV25Map = mutableMapOf<Int, Int>()
        private const val SCALE_BY_THOUSAND = 1000
        private const val LAYER_I_FRAME_SIZE_COEFFICIENT = 12
        private const val LAYER_II_FRAME_SIZE_COEFFICIENT = 144
        private const val LAYER_III_FRAME_SIZE_COEFFICIENT = 144

        /**
         * MP3 Frame Header bit mask
         */
        const val MASK_MP3_ID = FileConstants.BIT3

        /**
         * MP3 version, confusingly for MP3s the version is 1.
         */
        const val MASK_MP3_VERSION = FileConstants.BIT4 or FileConstants.BIT3

        /**
         * MP3 Layer, for MP3s the Layer is 3
         */
        const val MASK_MP3_LAYER = FileConstants.BIT2 or FileConstants.BIT1

        /**
         * Does it include a CRC Checksum at end of header, this can be used to check the header.
         */
        const val MASK_MP3_PROTECTION = FileConstants.BIT0

        /**
         * The setBitrate of this MP3
         */
        const val MASK_MP3_BITRATE = FileConstants.BIT7 or
                FileConstants.BIT6 or
                FileConstants.BIT5 or
                FileConstants.BIT4

        /**
         * The sampling/frequency rate
         */
        const val MASK_MP3_FREQUENCY = FileConstants.BIT3 + FileConstants.BIT2

        /**
         * An extra padding bit is sometimes used to make sure frames are exactly the right length
         */
        const val MASK_MP3_PADDING = FileConstants.BIT1

        /**
         * Private bit set, for application specific
         */
        const val MASK_MP3_PRIVACY = FileConstants.BIT0

        /**
         * Channel Mode, Stero/Mono/Dual Channel
         */
        const val MASK_MP3_MODE = FileConstants.BIT7 or FileConstants.BIT6

        /**
         * MP3 Frame Header bit mask
         */
        const val MASK_MP3_MODE_EXTENSION = FileConstants.BIT5 or FileConstants.BIT4

        /**
         * MP3 Frame Header bit mask
         */
        const val MASK_MP3_COPY = FileConstants.BIT3

        /**
         * MP3 Frame Header bit mask
         */
        const val MASK_MP3_HOME = FileConstants.BIT2

        /**
         * MP3 Frame Header bit mask
         */
        const val MASK_MP3_EMPHASIS = FileConstants.BIT1 or FileConstants.BIT0

        init {
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
         * Parse the MPEGFrameHeader of an MP3File, file pointer returns at end of the frame header
         *
         * @param bb the byte buffer containing the header
         * @return
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
         * @return The mPEGFrame value
         */
        fun isMPEGFrame(bb: ByteBuffer): Boolean {
            val position = bb.position()
            return (((bb.get(position).toInt() and SYNC_BYTE1) == SYNC_BYTE1) &&
                    ((bb.get(position + 1).toInt() and SYNC_BYTE2) == SYNC_BYTE2) &&
                    ((bb.get(position + 2).toInt() and SYNC_BIT_ANDSAMPING_BYTE3) !=
                            SYNC_BIT_ANDSAMPING_BYTE3)
                    )
        }
    }

    /**
     * @return a string represntation
     */
    override fun toString(): String {
        return (" mpeg frameheader: frame length:${getFrameLength()} version:$versionAsString layer:$layerAsString channelMode:$channelModeAsString noOfSamples:${getNoOfSamples()} samplingRate:$samplingRate isPadding:$isPadding isProtected:$isProtected isPrivate:$isPrivate isCopyrighted:$isCopyrighted isOriginal:$isCopyrighted isVariableBitRate${this.isVariableBitRate()} header as binary:${
            AbstractTagDisplayFormatter.displayAsBinary(
                mpegBytes[BYTE_1]
            )
        } ${AbstractTagDisplayFormatter.displayAsBinary(mpegBytes[BYTE_2])} ${
            AbstractTagDisplayFormatter.displayAsBinary(
                mpegBytes[BYTE_3]
            )
        } ${AbstractTagDisplayFormatter.displayAsBinary(mpegBytes[BYTE_4])}"
                )
    }
}
