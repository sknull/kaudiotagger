package de.visualdigits.kaudiotagger.model.audiofile.header.mp3

import de.visualdigits.kaudiotagger.model.audiofile.frame.VbriFrame
import de.visualdigits.kaudiotagger.model.audiofile.frame.XingFrame
import de.visualdigits.kaudiotagger.model.audiofile.header.AudioHeader
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Represents the audio header of an MP3 File
 *
 * <p>The audio header consists of a number of
 * audio frames. Because we are not trying to play the audio but only extract some information
 * regarding the audio we only need to read the first  audio frames to ensure that we have correctly
 * identified them as audio frames and extracted the metadata we reuire.
 *
 * <p>Start of Audio id 0xFF (11111111) and then second byte anded with 0xE0(11100000).
 * For example 2nd byte doesnt have to be 0xE0 is just has to have the top 3 signicant
 * bits set. For example 0xFB (11111011) is a common occurence of the second match. The 2nd byte
 * defines flags to indicate various mp3 values.
 *
 * <p>Having found these two values we then read the header which comprises these two bytes plus a further
 * two to ensure this really is a MP3Header, sometimes the first frame is actually a dummy frame with summary information
 * held within about the whole file, typically using a Xing Header or LAme Header. This is most useful when the file
 * is variable bit rate, if the file is variable bit rate but does not use a summary header it will not be correctly
 * identified as a VBR frame and the track length will be incorrectly calculated. Strictly speaking MP3 means
 * Layer III file but MP2 Layer II), MP1 Layer I) and MPEG-2 files are sometimes used and named with
 * the .mp3 suffix so this library attempts to supports all these formats.
 */
class MP3AudioHeader(
    val seekFile: File,
    var startByte: Long = 0
) : AudioHeader {

    val log = LoggerFactory.getLogger(javaClass)

    companion object {

        private val timeInFormat: SimpleDateFormat = SimpleDateFormat("ss", Locale.UK)
        private val timeOutFormat: SimpleDateFormat = SimpleDateFormat("mm:ss", Locale.UK)
        private val timeOutOverAnHourFormat: SimpleDateFormat = SimpleDateFormat("kk:mm:ss", Locale.UK)
        private const val isVbrIdentifier: Char = '~'
        private const val CONVERT_TO_KILOBITS: Int = 1000
        private const val TYPE_MP3: String = "mp3"
        private const val CONVERTS_BYTE_TO_BITS: Int = 8

        /**
         * After testing the average location of the first MP3Header bit was at 5000 bytes so this is
         * why chosen as a default.
         */
        private const val FILE_BUFFER_SIZE: Int = 5000
        private val MIN_BUFFER_REMAINING_REQUIRED: Int = MPEGFrameHeader.Companion.HEADER_SIZE + XingFrame.Companion.MAX_BUFFER_SIZE_NEEDED_TO_READ_XING
        private const val NO_SECONDS_IN_HOUR: Int = 3600
    }

    var mp3FrameHeader: MPEGFrameHeader? = null
    var mp3XingFrame: XingFrame? = null
    var mp3VbriFrame: VbriFrame? = null

    private var audioDataStartPosition: Long? = null
    private var audioDataEndPosition: Long? = null
    private var fileSize: Long = 0
    private var timePerFrame = 0.0
    private var trackLength = 0.0
    var numberOfFrames: Long = 0
    private var numberOfFramesEstimate: Long = 0
    private var bitrate: Long = 0
    private var encoder: String? = ""

    /**
     * Returns true if the first MP3 frame can be found for the MP3 file
     *
     *
     * This is the first byte of  music data and not the ID3 Tag Frame.
     *
     * @param seekFile  MP3 file to seek
     * @param startByte if there is an ID3v2tag we don't want to start reading from the start of the tag
     *
     * @return true if the first MP3 frame can be found
     */
    fun seek(startByte: Long = 0): Boolean {
        //References to Xing/VRbi Header
        var header: ByteBuffer?

        val fis = FileInputStream(seekFile)
        val fc = fis.getChannel()

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
                //     interrogating the bytebuffer. Also this is rarely going to be true, and could be made less true
                //     by increasing FILE_BUFFER_SIZE
                if (bb.remaining() <= MIN_BUFFER_REMAINING_REQUIRED) {
                    bb.clear()
                    fc.position(filePointerCount)
                    fc.read(bb, fc.position())
                    bb.flip()
                    if (bb.limit() <= MIN_BUFFER_REMAINING_REQUIRED) {
                        //No mp3 exists
                        return false
                    }
                }
                //log.debug("fc:"+fc.position() + "bb"+bb.position());
                if (MPEGFrameHeader.Companion.isMPEGFrame(bb)) {
                    try {
                        if (log.isDebugEnabled()) {
                            log.debug("Found Possible header at:" + filePointerCount)
                        }

                        mp3FrameHeader = MPEGFrameHeader.Companion.parseMPEGHeader(bb)
                        syncFound = true

                        //if(2==1) use this line when you want to test getting the next frame without using xing
                        header = XingFrame.Companion.isXingFrame(bb, mp3FrameHeader)
                        if (header != null) {
                            log.debug("Found Possible XingHeader")
                            try {
                                //Parses Xing frame without modifying position of main buffer
                                mp3XingFrame = XingFrame.Companion.parseXingFrame(header)
                            } catch (_: Exception) {
                                // We Ignore because even if Xing Header is corrupted
                                //doesn't mean file is corrupted
                            }
                            break
                        } else {
                            header = VbriFrame.Companion.isVbriFrame(bb)
                            if (header != null) {
                                log.debug("Found Possible VbriHeader")
                                try {
                                    //Parses Vbri frame without modifying position of main buffer
                                    mp3VbriFrame = VbriFrame.Companion.parseVBRIFrame(header)
                                } catch (_: Exception) {
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
                        }
                    } catch (_: Exception) {
                        // We Ignore because likely to be incorrect sync bits ,
                        // will just continue in loop
                    }
                }

                //TODO position() is quite an expensive operation, isn't there a way we can work this out without
                //interrogating the bytebuffer
                bb.position(bb.position() + 1)
                filePointerCount++
            } while (!syncFound)
        } catch (ex: Exception) {
            log.warn("Reached end of file without finding sync match")
            syncFound = false
        } finally {
            fc?.close()
            fis.close()
        }

        //Return to start of audio header
        log.debug("Return found matching mp3 header starting at$filePointerCount")
        fileSize = seekFile.length()
        this.startByte = filePointerCount
        setTimePerFrame()
        setNumberOfFrames()
        setTrackLength()
        setBitRate()
        setEncoder()

        return syncFound
    }

    /**
     * Called in some circumstances to check the next frame to ensure we have the correct audio header
     *
     * @param seekFile
     * @param filePointerCount
     * @param bb
     * @param fc
     * @return true if frame is valid
     */
    private fun isNextFrameValid(
        seekFile: File,
        filePointerCount: Long,
        bb: ByteBuffer,
        fc: FileChannel
    ): Boolean {
        if (log.isDebugEnabled()) {
            log.debug(
                "Checking next frame" +
                        seekFile.getName() +
                        ":fpc:" +
                        filePointerCount +
                        "skipping to:" +
                        (filePointerCount + (mp3FrameHeader?.getFrameLength()?:0))
            )
        }
        var result = false

        var currentPosition = bb.position()

        //Our buffer is not large enough to fit in the whole of this frame, something must
        //have gone wrong because frames are not this large, so just return false
        //bad frame header
        if ((mp3FrameHeader?.getFrameLength()?:0) >
            (FILE_BUFFER_SIZE - MIN_BUFFER_REMAINING_REQUIRED)
        ) {
            log.debug(
                "Frame size is too large to be a frame:" +
                        (mp3FrameHeader?.getFrameLength()?:0)
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
                MIN_BUFFER_REMAINING_REQUIRED + ((mp3FrameHeader?.getFrameLength()?:0)?:0)
            ) {
                //No mp3 exists
                log.debug(
                    "Nearly at end of file, no room for next frame, no header found:"
                )
                return false
            }
        }

        //Position bb to the start of the alleged next frame
        bb.position(bb.position() + ((mp3FrameHeader?.getFrameLength()?:0)?:0))
        if (MPEGFrameHeader.isMPEGFrame(bb)) {
            try {
                MPEGFrameHeader.parseMPEGHeader(bb)
                log.debug("Check next frame confirms is an audio header ")
                result = true
            } catch (_: Exception) {
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
        numberOfFramesEstimate = (fileSize - startByte) / ((mp3FrameHeader?.getFrameLength()?:0)?:0).toLong()

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
            (mp3FrameHeader?.getNoOfSamples()?:0) / (mp3FrameHeader?.samplingRate?.toDouble()?:1.0)

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
        trackLength = numberOfFrames * timePerFrame
    }

    /**
     * Set bitrate in kbps, if Vbr use Xingheader if possible
     */
    fun setBitRate() {
        if (mp3XingFrame != null && mp3XingFrame?.vbr == true) {
            if (mp3XingFrame?.isAudioSizeEnabled == true && (mp3XingFrame?.audioSize?:-1) > 0
            ) {
                bitrate = (((mp3XingFrame?.audioSize?:-1).toLong() *
                        CONVERTS_BYTE_TO_BITS) /
                        (timePerFrame * numberOfFrames * CONVERT_TO_KILOBITS)).toLong()
            } else {
                bitrate = (((fileSize - startByte) * CONVERTS_BYTE_TO_BITS) /
                        (timePerFrame * numberOfFrames * CONVERT_TO_KILOBITS)).toLong()
            }
        } else if (mp3VbriFrame != null) {
            if ((mp3VbriFrame?.audioSize?:-1) > 0) {
                bitrate = (((mp3VbriFrame?.audioSize?:-1).toLong() *
                        CONVERTS_BYTE_TO_BITS) /
                        (timePerFrame * numberOfFrames * CONVERT_TO_KILOBITS)).toLong()
            } else {
                bitrate = (((fileSize - startByte) * CONVERTS_BYTE_TO_BITS) /
                        (timePerFrame * numberOfFrames * CONVERT_TO_KILOBITS)).toLong()
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

    /**
     * @return the BitRate of the Audio, to distinguish cbr from vbr we add a '~'
     * for vbr.
     */
    override fun getBitRate(): String {
        if (mp3XingFrame != null && mp3XingFrame?.vbr == true) {
            return isVbrIdentifier.toString() + bitrate.toString()
        } else if (mp3VbriFrame != null) {
            return isVbrIdentifier.toString() + bitrate.toString()
        } else {
            return bitrate.toString()
        }
    }

    /**
     * @return if the bitrate is variable, Xing header takes precedence if we have one
     */
    override fun isVariableBitRate(): Boolean {
        if (mp3XingFrame != null) {
            return mp3XingFrame?.vbr == true
        } else if (mp3VbriFrame != null) {
            return mp3VbriFrame?.vbr == true
        } else {
            return false
        }
    }

    /**
     * Return the length in user friendly format
     *
     * @return
     */
    fun getTrackLengthAsString(): String? {
        val timeIn: Date?
        try {
            val lengthInSecs = trackLength.toLong()
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
                        trackLength +
                        " failed with ParseException:" +
                        pe.message
            )
            return ""
        }
    }
}