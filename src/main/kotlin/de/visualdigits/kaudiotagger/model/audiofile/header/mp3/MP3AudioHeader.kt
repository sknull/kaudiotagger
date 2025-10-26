package de.visualdigits.kaudiotagger.model.audiofile.header.mp3

import de.visualdigits.kaudiotagger.model.audiofile.frame.VbriFrame
import de.visualdigits.kaudiotagger.model.audiofile.frame.XingFrame
import de.visualdigits.kaudiotagger.model.audiofile.header.AudioHeader
import de.visualdigits.kaudiotagger.model.exceptions.InvalidAudioFrameException
import de.visualdigits.kaudiotagger.util.ErrorMessage
import org.slf4j.LoggerFactory
import java.io.EOFException
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Represents the audio header of an MP3 File
 *
 *
 * The audio header consists of a number of
 * audio frames. Because we are not trying to play the audio but only extract some information
 * regarding the audio we only need to read the first  audio frames to ensure that we have correctly
 * identified them as audio frames and extracted the metadata we reuire.
 *
 *
 * Start of Audio id 0xFF (11111111) and then second byte anded with 0xE0(11100000).
 * For example 2nd byte doesnt have to be 0xE0 is just has to have the top 3 signicant
 * bits set. For example 0xFB (11111011) is a common occurence of the second match. The 2nd byte
 * defines flags to indicate various mp3 values.
 *
 *
 * Having found these two values we then read the header which comprises these two bytes plus a further
 * two to ensure this really is a MP3Header, sometimes the first frame is actually a dummy frame with summary information
 * held within about the whole file, typically using a Xing Header or LAme Header. This is most useful when the file
 * is variable bit rate, if the file is variable bit rate but does not use a summary header it will not be correctly
 * identified as a VBR frame and the track length will be incorrectly calculated. Strictly speaking MP3 means
 * Layer III file but MP2 Layer II), MP1 Layer I) and MPEG-2 files are sometimes used and named with
 * the .mp3 suffix so this library attempts to supports all these formats.
 */
open class MP3AudioHeader : AudioHeader {

    val log = LoggerFactory.getLogger(javaClass)
    
    var mp3FrameHeader: MPEGFrameHeader? = null
    var mp3XingFrame: XingFrame? = null
    var mp3VbriFrame: VbriFrame? = null
    var audioDataStartPosition: Long? = null
    var audioDataEndPosition: Long? = null
    var fileSize: Long = 0
    
    /**
     * Returns the byte position of the first MP3 Frame that the
     * `file` arguement refers to. This is the first byte of music
     * data and not the ID3 Tag Frame.
     *
     * @return the byte position of the first MP3 Frame
     */
    /**
     * Set the location of where the Audio file begins in the file
     *
     * @param this.mp3StartByte
     */
    var mp3StartByte: Long = 0

    /**
     * @return the the time each frame contributes to the audio in fractions of seconds
     */
    var timePerFrame = 0.0
    var trackLength = 0.0

    /**
     * @return The number of frames within the Audio File, calculated as accurately as possible
     */
    var numberOfFrames: Long = 0

    /**
     * @return The number of frames within the Audio File, calculated by dividing the filesize by
     * the number of frames, this may not be the most accurate method available.
     */
    var numberOfFramesEstimate: Long = 0
    var bitrate: Long = 0

    /**
     * @return encoder
     */
    var encoder: String? = ""

    constructor()

    /**
     * Search for the first MP3Header in the file
     *
     *
     * The search starts from the start of the file, it is usually safer to use the alternative constructor that
     * allows you to provide the length of the tag header as a parameter so the tag can be skipped over.
     *
     * @param seekFile
     * @throws IOException
     * @throws InvalidAudioFrameException
     */
    constructor(seekFile: File?) {
        if (seekFile == null || !seek(seekFile, 0)) {
            throw InvalidAudioFrameException(
                "No audio header found within${seekFile?.getName()?:"No file given"}" 
            )
        }
    }

    /**
     * Search for the first MP3Header in the file
     *
     *
     * Starts searching from location startByte, this is because there is likely to be an ID3TagHeader
     * before the start of the audio. If this tagHeader contains unsynchronized information there is a
     * possibility that it might be inaccurately identified as the start of the Audio data. Various checks
     * are done in this code to prevent this happening but it cannot be guaranteed.
     *
     *
     * Of course if the startByte provided overstates the length of the tag header, this could mean the
     * start of the MP3AudioHeader is missed, further checks are done within the MP3 class to recognize
     * if this has occurred and take appropriate action.
     *
     * @param seekFile
     * @param startByte
     * @throws IOException
     * @throws InvalidAudioFrameException
     */
    constructor(seekFile: File?, startByte: Long) {
        if (seekFile == null || !seek(seekFile, startByte)) {
            throw InvalidAudioFrameException(
                ErrorMessage.NO_AUDIO_HEADER_FOUND.getMsg(seekFile?.getName()?:"No file given")
            )
        }
    }

    /**
     * Returns true if the first MP3 frame can be found for the MP3 file
     *
     *
     * This is the first byte of  music data and not the ID3 Tag Frame.     *
     *
     * @param seekFile  MP3 file to seek
     * @param startByte if there is an ID3v2tag we dont want to start reading from the start of the tag
     * @return true if the first MP3 frame can be found
     * @throws IOException on any I/O error
     */
    fun seek(seekFile: File, startByte: Long): Boolean {

        return FileInputStream(seekFile).use { fis ->
            fis.getChannel().use { fc ->
                //Read into Byte Buffer in Chunks
                val bb = ByteBuffer.allocateDirect(FILE_BUFFER_SIZE)

                //Move FileChannel to the starting position (skipping over tag if any)
                fc.position(startByte)

                //Update filePointerCount

                //This is substantially faster than updating the filechannels position
                var filePointerCount: Long = startByte

                //Read from here into the byte buffer , doesn't move location of filepointer
                fc.read(bb, startByte)
                bb.flip()

                var syncFound = false
                try {
                    do {
                        //TODO remaining() is quite an expensive operation, isn't there a way we can work this out without
                        //interrogating the bytebuffer. Also this is rarely going to be true, and could be made less true
                        //by increasing FILE_BUFFER_SIZE
                        if (bb.remaining() <= MIN_BUFFER_REMAINING_REQUIRED) {
                            bb.clear()
                            fc.position(filePointerCount)
                            fc.read(bb, fc.position())
                            bb.flip()
                            if (bb.limit() <= MIN_BUFFER_REMAINING_REQUIRED) {
                                //No mp3 exists
                                syncFound = false
                            }
                        }
                        //log.debug("fc:"+fc.position() + "bb"+bb.position());
                        if (MPEGFrameHeader.isMPEGFrame(bb)) {
                            try {
                                log.debug("Found Possible header at:" + filePointerCount)

                                mp3FrameHeader = MPEGFrameHeader.parseMPEGHeader(bb)
                                syncFound = true

                                //if(2==1) use this line when you want to test getting the next frame without using xing
                                var header: ByteBuffer? = null
                                if ((XingFrame.isXingFrame(bb, mp3FrameHeader)?.also { header = it }) != null) {
                                    log.debug("Found Possible XingHeader")
                                    try {
                                        //Parses Xing frame without modifying position of main buffer
                                        mp3XingFrame = XingFrame.parseXingFrame(header)
                                    } catch (ex: InvalidAudioFrameException) {
                                        // We Ignore because even if Xing Header is corrupted
                                        //doesn't mean file is corrupted
                                    }
                                    break
                                } else if ((VbriFrame.isVbriFrame(bb)?.also { header = it }) != null
                                ) {
                                    log.debug("Found Possible VbriHeader")
                                    try {
                                        //Parses Vbri frame without modifying position of main buffer
                                        mp3VbriFrame = VbriFrame.parseVBRIFrame(header)
                                    } catch (ex: InvalidAudioFrameException) {
                                        // We Ignore because even if Vbri Header is corrupted
                                        //doesn't mean file is corrupted
                                    }
                                    break
                                } else {
                                    syncFound = isNextFrameValid(seekFile, filePointerCount, bb, fc)
                                    if (syncFound) {
                                        break
                                    }
                                }
                            } catch (ex: InvalidAudioFrameException) {
                                // We Ignore because likely to be incorrect sync bits ,
                                // will just continue in loop
                            }
                        }

                        //TODO position() is quite an expensive operation, isn't there a way we can work this out without
                        //interrogating the bytebuffer
                        bb.position(bb.position() + 1)
                        filePointerCount++
                    } while (!syncFound)
                } catch (e: EOFException) {
                    log.warn("Reached end of file without finding sync match", e)
                    syncFound = false
                } catch (e: IOException) {
                    log.error("IOException occurred whilst trying to find sync", e)
                    throw e
                }

                //Return to start of audio header
                log.debug("Return found matching mp3 header starting at$filePointerCount")
                fileSize = seekFile.length()
                this.mp3StartByte = filePointerCount
                setTimePerFrame()
                setNumberOfFrames()
                setTrackLength()
                setBitRate()
                setEncoder()

                syncFound
            }
        }
    }

    /**
     * Called in some circumstances to check the next frame to ensure we have the correct audio header
     *
     * @param seekFile
     * @param filePointerCount
     * @param bb
     * @param fc
     * @return true if frame is valid
     * @throws IOException
     */
    private fun isNextFrameValid(
        seekFile: File,
        filePointerCount: Long,
        bb: ByteBuffer,
        fc: FileChannel
    ): Boolean {
        log.debug(
            "Checking next frame${seekFile.getName()}:fpc:${filePointerCount}skipping to:${filePointerCount + (mp3FrameHeader?.getFrameLength() ?: 0)}"
        )
        var result = false

        var currentPosition = bb.position()

        //Our buffer is not large enough to fit in the whole of this frame, something must
        //have gone wrong because frames are not this large, so just return false
        //bad frame header
        if ((mp3FrameHeader?.getFrameLength()?:0) >
            (FILE_BUFFER_SIZE - MIN_BUFFER_REMAINING_REQUIRED)
        ) {
            log.debug(
                "Frame size is too large to be a frame:${(mp3FrameHeader?.getFrameLength()?:0)}"
            )
            return false
        }

        //Check for end of buffer if not enough room get some more
        if (bb.remaining() <=
            MIN_BUFFER_REMAINING_REQUIRED + (mp3FrameHeader?.getFrameLength()?:0)
        ) {
            log.debug(
                "Buffer too small, need to reload, buffer size:" + bb.remaining()
            )
            bb.clear()
            fc.position(filePointerCount)
            fc.read(bb, fc.position())
            bb.flip()
            //So now original buffer has been replaced, so set current position to start of buffer
            currentPosition = 0
            //Not enough left
            if (bb.limit() <= MIN_BUFFER_REMAINING_REQUIRED) {
                //No mp3 exists
                log.debug("Nearly at end of file, no header found:")
                return false
            }

            //Still Not enough left for next alleged frame size so giving up
            if (bb.limit() <=
                MIN_BUFFER_REMAINING_REQUIRED + (mp3FrameHeader?.getFrameLength()?:0)
            ) {
                //No mp3 exists
                log.debug(
                    "Nearly at end of file, no room for next frame, no header found:"
                )
                return false
            }
        }

        //Position bb to the start of the alleged next frame
        bb.position(bb.position() + (mp3FrameHeader?.getFrameLength()?:0))
        if (MPEGFrameHeader.isMPEGFrame(bb)) {
            try {
                MPEGFrameHeader.parseMPEGHeader(bb)
                log.debug("Check next frame confirms is an audio header ")
                result = true
            } catch (ex: InvalidAudioFrameException) {
                log.debug(
                    "Check next frame has identified this is not an audio header"
                )
                result = false
            }
        } else {
            log.debug("isMPEGFrame has identified this is not an audio header")
        }
        //Set back to the start of the previous frame
        bb.position(currentPosition)
        return result
    }

    /**
     * Set number of frames in this file, use Xing if exists otherwise ((File Size - Non Audio Part)/Frame Size)
     */
    fun setNumberOfFrames() {
        numberOfFramesEstimate =
            (fileSize - this.mp3StartByte) / (mp3FrameHeader?.getFrameLength()?:0)

        if (mp3XingFrame != null && mp3XingFrame?.isFrameCountEnabled == true) {
            numberOfFrames = mp3XingFrame?.frameCount?.toLong()?:0
        } else if (mp3VbriFrame != null) {
            numberOfFrames = mp3VbriFrame?.frameCount?.toLong()?:0
        } else {
            numberOfFrames = numberOfFramesEstimate
        }
    }

    /**
     * Set the time each frame contributes to the audio in fractions of seconds, the higher
     * the sampling rate the shorter the audio segment provided by the frame,
     * the number of samples is fixed by the MPEG Version and Layer
     */
    fun setTimePerFrame() {
        timePerFrame =
            (mp3FrameHeader?.getNoOfSamples()?:0) /
                    (mp3FrameHeader?.samplingRate?.toDouble()?:1.0)

        //Because when calculating framelength we may have altered the calculation slightly for MPEGVersion2
        //to account for mono/stereo we seem to have to make a corresponding modification to get the correct time
        if ((mp3FrameHeader?.version == MPEGFrameHeader.VERSION_2) ||
            (mp3FrameHeader?.version == MPEGFrameHeader.VERSION_2_5)
        ) {
            if ((mp3FrameHeader?.layer == MPEGFrameHeader.LAYER_II) ||
                (mp3FrameHeader?.layer == MPEGFrameHeader.LAYER_III)
            ) {
                if (mp3FrameHeader?.getNumberOfChannels() == 1) {
                    timePerFrame = timePerFrame / 2
                }
            }
        }
    }

    /**
     * Estimate the length of the audio track in seconds
     * Calculation is Number of frames multiplied by the Time Per Frame using the first frame as a prototype
     * Time Per Frame is the number of samples in the frame (which is defined by the MPEGVersion/Layer combination)
     * divided by the sampling rate, i.e the higher the sampling rate the shorter the audio represented by the frame is going
     * to be.
     */
    fun setTrackLength() {
        trackLength = numberOfFrames * this.timePerFrame
    }

    /**
     * Set bitrate in kbps, if Vbr use Xingheader if possible
     */
    fun setBitRate() {
        if (mp3XingFrame != null && mp3XingFrame?.isVbr == true) {
            if (mp3XingFrame?.isAudioSizeEnabled == true && (mp3XingFrame?.audioSize?:0) > 0
            ) {
                bitrate = (((mp3XingFrame?.audioSize?.toLong()?:0) *
                        CONVERTS_BYTE_TO_BITS) /
                        (timePerFrame * this.numberOfFrames * CONVERT_TO_KILOBITS)).toLong()
            } else {
                bitrate = (((fileSize - this.mp3StartByte) * CONVERTS_BYTE_TO_BITS) /
                        (timePerFrame * this.numberOfFrames * CONVERT_TO_KILOBITS)).toLong()
            }
        } else if (mp3VbriFrame != null) {
            if ((mp3VbriFrame?.audioSize?:0) > 0) {
                bitrate = (((mp3VbriFrame?.audioSize?.toLong()?:0) *
                        CONVERTS_BYTE_TO_BITS) /
                        (timePerFrame * this.numberOfFrames * CONVERT_TO_KILOBITS)).toLong()
            } else {
                bitrate = (((fileSize - this.mp3StartByte) * CONVERTS_BYTE_TO_BITS) /
                        (timePerFrame * this.numberOfFrames * CONVERT_TO_KILOBITS)).toLong()
            }
        } else {
            bitrate = mp3FrameHeader?.bitRate?.toLong()?:0
        }
    }

    fun setEncoder() {
        if (mp3XingFrame != null) {
            if (mp3XingFrame?.lameFrame != null) {
                encoder = mp3XingFrame?.lameFrame?.encoder
            }
        } else if (mp3VbriFrame != null) {
            encoder = mp3VbriFrame?.encoder
        }
    }

    fun getNoOfSamples(): Long {
        return numberOfFrames
    }

    /**
     * @return the audio file type
     */
    override fun getEncodingType(): String {
        return TYPE_MP3
    }

    /**
     * @return bitrate in kbps, no indicator is provided as to whether or not it is vbr
     */
    override fun getBitRateAsNumber(): Long {
        return bitrate
    }

    /**
     * @return the BitRate of the Audio, to distinguish cbr from vbr we add a '~'
     * for vbr.
     */
    override fun getBitRate(): String {
        if (mp3XingFrame != null && mp3XingFrame?.isVbr == true) {
            return isVbrIdentifier.toString() + bitrate.toString()
        } else if (mp3VbriFrame != null) {
            return isVbrIdentifier.toString() + bitrate.toString()
        } else {
            return bitrate.toString()
        }
    }

    /**
     * @return the sampling rate in Hz
     */
    override fun getSampleRateAsNumber(): Int {
        return mp3FrameHeader?.samplingRate?:0
    }

    /**
     * @return the number of bits per sample
     */
    override fun getBitsPerSample(): Int {
        //TODO: can it really be different in such an MP3 ? I think not.
        return 16
    }

    /**
     * @return the sampling rate as string
     */
    override fun getSampleRate(): String? {
        return mp3FrameHeader?.samplingRate.toString()
    }

    /**
     * @return MPEG Version (1-3)
     */
    fun getMpegVersion(): String? {
        return mp3FrameHeader?.versionAsString
    }

    /**
     * @return MPEG Layer (1-3)
     */
    fun getMpegLayer(): String? {
        return mp3FrameHeader?.layerAsString
    }

    /**
     * @return the format of the audio (i.e. MPEG-1 Layer3)
     */
    override fun getFormat(): String {
        return (mp3FrameHeader?.versionAsString +
                " " +
                mp3FrameHeader?.layerAsString
                )
    }

    /**
     * @return the Channel Mode such as Stero or Mono
     */
    override fun getChannels(): String? {
        return mp3FrameHeader?.channelModeAsString
    }

    /**
     * @return Emphasis
     */
    fun getEmphasis(): String? {
        return mp3FrameHeader?.emphasisAsString
    }

    /**
     * @return if the bitrate is variable, Xing header takes precedence if we have one
     */
    override fun isVariableBitRate(): Boolean {
        if (mp3XingFrame != null) {
            return mp3XingFrame?.isVbr == true
        } else if (mp3VbriFrame != null) {
            return mp3VbriFrame?.isVbr == true
        } else {
            return mp3FrameHeader?.isVariableBitRate() == true
        }
    }

    fun isProtected(): Boolean {
        return mp3FrameHeader?.isProtected == true
    }

    fun isPrivate(): Boolean {
        return mp3FrameHeader?.isPrivate == true
    }

    fun isCopyrighted(): Boolean {
        return mp3FrameHeader?.isCopyrighted == true
    }

    fun isOriginal(): Boolean {
        return mp3FrameHeader?.isOriginal == true
    }

    fun isPadding(): Boolean {
        return mp3FrameHeader?.isPadding == true
    }

    override fun isLossless(): Boolean {
        return false
    }

    /**
     * Return the length in user friendly format
     *
     * @return
     */
    fun getTrackLengthAsString(): String? {
        val timeIn: Date?
        try {
            val lengthInSecs = getTrackLength().toLong()
            synchronized(timeInFormat) {
                timeIn = timeInFormat.parse(lengthInSecs.toString())
            }

            if (lengthInSecs < NO_SECONDS_IN_HOUR) {
                synchronized(timeOutFormat) {
                    return timeOutFormat.format(timeIn)
                }
            } else {
                synchronized(timeOutOverAnHourFormat) {
                    return timeOutOverAnHourFormat.format(timeIn)
                }
            }
        } catch (pe: ParseException) {
            log.warn(
                "Unable to parse:" +
                        getPreciseTrackLength() +
                        " failed with ParseException:" +
                        pe.message
            )
            return ""
        }
    }

    override fun getTrackLength(): Int {
        return getPreciseTrackLength().toInt()
    }

    /**
     * @return Track Length in seconds
     */
    override fun getPreciseTrackLength(): Double {
        return trackLength
    }

    /**
     * TODO (Was originally added for Wavs)
     *
     * @return
     */
    override fun getByteRate(): Int? {
        return null
    }

    /**
     * TODO (Was origjnally added for Wavs)
     *
     * @return
     */
    override fun getAudioDataLength(): Long? {
        return 0
    }

    companion object {
        val timeInFormat = SimpleDateFormat(
            "ss",
            Locale.UK
        )
        val timeOutFormat = SimpleDateFormat(
            "mm:ss",
            Locale.UK
        )
        val timeOutOverAnHourFormat = SimpleDateFormat("kk:mm:ss", Locale.UK)
        private const val isVbrIdentifier = '~'
        private const val CONVERT_TO_KILOBITS = 1000
        private const val TYPE_MP3 = "mp3"
        private const val CONVERTS_BYTE_TO_BITS = 8

        /**
         * After testing the average location of the first MP3Header bit was at 5000 bytes so this is
         * why chosen as a default.
         */
        private const val FILE_BUFFER_SIZE = 5000
        val MIN_BUFFER_REMAINING_REQUIRED =
            MPEGFrameHeader.HEADER_SIZE + XingFrame.MAX_BUFFER_SIZE_NEEDED_TO_READ_XING
        private const val NO_SECONDS_IN_HOUR = 3600
    }

    /**
     * @return a string representation
     */
    override fun toString(): String {
        var s = "fileSize:$fileSize encoder:$encoder startByte:${mp3StartByte.toHexString()} numberOfFrames:$numberOfFrames numberOfFramesEst:$numberOfFramesEstimate timePerFrame:$timePerFrame bitrate:$bitrate trackLength:${this.getTrackLengthAsString()}"

        if (this.mp3FrameHeader != null) {
            s += mp3FrameHeader?.toString()
        } else {
            s += " mpegframeheader:false"
        }

        if (this.mp3XingFrame != null) {
            s += mp3XingFrame?.toString()
        } else {
            s += " mp3XingFrame:false"
        }

        if (this.mp3VbriFrame != null) {
            s += mp3VbriFrame?.toString()
        } else {
            s += " mp3VbriFrame:false"
        }
        return s
    }
}
