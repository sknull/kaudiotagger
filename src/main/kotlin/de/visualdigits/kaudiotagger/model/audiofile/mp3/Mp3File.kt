package de.visualdigits.kaudiotagger.model.audiofile.mp3

import de.visualdigits.kaudiotagger.model.audiofile.AudioFile
import de.visualdigits.kaudiotagger.model.audiofile.header.mp3.MP3AudioHeader
import de.visualdigits.kaudiotagger.model.tag.id3.AbstractID3v2Tag
import de.visualdigits.kaudiotagger.model.tag.id3.ID3v11Tag
import de.visualdigits.kaudiotagger.model.tag.id3.ID3v1Tag
import de.visualdigits.kaudiotagger.model.tag.id3.ID3v22Tag
import de.visualdigits.kaudiotagger.model.tag.id3.ID3v23Tag
import de.visualdigits.kaudiotagger.model.tag.id3.ID3v24Tag
import de.visualdigits.kaudiotagger.util.ErrorMessage
import java.io.File
import java.io.FileInputStream
import java.io.RandomAccessFile
import java.nio.ByteBuffer

/**
 * @param file        MP3 file
 * @param loadOptions decide what tags to load
 * @param readOnly    causes the files to be opened readonly
 */
class Mp3File(
    file: File,
    val loadOptions: Int = LOAD_ALL,
    val readOnly: Boolean = false
) : AudioFile<MP3AudioHeader>(
    file
) {

    companion object {

        /** Load ID3V1tag if exists */
        const val LOAD_IDV1TAG: Int = 2

        /** Load ID3V2tag if exists */
        const val LOAD_IDV2TAG: Int = 4

        /** This option is currently ignored */
        const val LOAD_LYRICS3: Int = 8
        val LOAD_ALL: Int = LOAD_IDV1TAG or LOAD_IDV2TAG or LOAD_LYRICS3

        fun read(file: File) = Mp3File(file)
    }

    /**
     * the ID3v2 tag that this file contains.
     */
    private var id3v2tag: AbstractID3v2Tag? = null

    /**
     * Representation of the idv2 tag as a idv24 tag
     */
    private var id3v2Asv24tag: ID3v24Tag? = null

    /**
     * The ID3v1 tag that this file contains.
     */
    private var id3v1tag: ID3v1Tag? = null

    init {
        //Check File accessibility
        checkFilePermissions(this.file, readOnly).use<RandomAccessFile, Unit> { newFile ->

            //Read ID3v2 tag size (if tag exists) to allow audioHeader parsing to skip over tag
            val tagSizeReportedByHeader = AbstractID3v2Tag.Companion.getV2TagSizeIfExists(this.file)
            log.debug("TagHeaderSize:${tagSizeReportedByHeader.toHexString()}")
            audioHeader = MP3AudioHeader(this.file, tagSizeReportedByHeader)

            //If the audio header is not straight after the end of the tag then search from start of file
            if (tagSizeReportedByHeader != audioHeader?.startByte) {
                log.debug("First header found after tag:{}", audioHeader)
                audioHeader = checkAudioStart(
                    tagSizeReportedByHeader,
                    audioHeader as MP3AudioHeader
                )
            }

            //Read v1 tags (if any)
            readV1Tag(newFile, loadOptions)

            //Read v2 tags (if any)
            readV2Tag(this.file, loadOptions, (audioHeader as MP3AudioHeader).startByte.toInt())

            //If we have a v2 tag use that, if we do not but have v1 tag use that
            //otherwise use nothing
            //rather than just returning specific ID3v22 tag, would it be better to return v24 version ?
            if (id3v2tag != null) {
                tag = id3v2tag
            } else if (id3v1tag != null) {
                tag = id3v1tag
            }
        }
    }

    /**
     * Read v1 tag
     *
     * @param newFile
     * @param loadOptions
     */
    private fun readV1Tag(newFile: RandomAccessFile, loadOptions: Int) {
        if ((loadOptions and LOAD_IDV1TAG) != 0) {
            log.debug("Attempting to read id3v1tags")
            try {
                id3v1tag = ID3v11Tag(newFile)
            } catch (_: Exception) {
                log.debug("No ids3v11 tag found")
            }

            try {
                if (id3v1tag == null) {
                    id3v1tag = ID3v1Tag(newFile)
                }
            } catch (_: Exception) {
                log.debug("No id3v1 tag found")
            }
        }
    }

    /**
     * Read V2tag if exists
     *
     * @param file
     * @param loadOptions
     */
    private fun readV2Tag(file: File, loadOptions: Int, startByte: Int) {
        //We know where the actual Audio starts so load all the file from start to that point into
        //a buffer then we can read the IDv2 information without needing any more File I/O
        if (startByte >= AbstractID3v2Tag.Companion.TAG_HEADER_LENGTH) {
            log.debug("Attempting to read id3v2tags")
            val bb = FileInputStream(file).use { fis ->
                fis.channel.use { fc ->
                    val bb = ByteBuffer.allocate(startByte)
                    // XXX: don't change it to map
                    // https://stackoverflow.com/questions/28378713/bytebuffer-getbyte-int-int-failed-on-android-ics-and-jb
                    fc.read(bb, 0)
                    bb
                }
            }

            try {
                bb.rewind()

                if ((loadOptions and LOAD_IDV2TAG) != 0) {
                    log.debug("Attempting to read id3v2tags")
                    try {
                        setID3v2Tag(ID3v24Tag(bb))
                    } catch (_: Exception) {
                        log.debug("No id3v24 tag found")
                    }

                    try {
                        if (id3v2tag == null) {
                            setID3v2Tag(ID3v23Tag(bb))
                        }
                    } catch (_: Exception) {
                        log.debug("No id3v23 tag found")
                    }

                    try {
                        if (id3v2tag == null) {
                            setID3v2Tag(ID3v22Tag(bb))
                        }
                    } catch (_: Exception) {
                        log.debug("No id3v22 tag found")
                    }
                }
            } finally {
                bb.clear()
            }
        } else {
            log.debug("Not enough room for valid id3v2 tag:$startByte")
        }
    }

    /**
     * Sets the v2 tag to the v2 tag provided as an argument.
     * Also store a v24 version of tag as v24 is the interface to be used
     * when talking with client applications.
     *
     * @param id3v2tag
     */
    fun setID3v2Tag(id3v2tag: AbstractID3v2Tag) {
        this.id3v2tag = id3v2tag
        if (id3v2tag is ID3v24Tag) {
            this.id3v2Asv24tag = this.id3v2tag as ID3v24Tag
        } else {
            this.id3v2Asv24tag = ID3v24Tag(id3v2tag)
        }
    }

    /**
     * Regets the audio header starting from start of file, and write appropriate logging to indicate
     * potential problem to user.
     *
     * @param startByte
     * @param firstHeaderAfterTag
     *
     * @return MP3AudioHeader
     */
    private fun checkAudioStart(
        startByte: Long,
        firstHeaderAfterTag: MP3AudioHeader
    ): MP3AudioHeader {
        val headerTwo: MP3AudioHeader

        log.warn(
            ErrorMessage.MP3_ID3TAG_LENGTH_INCORRECT.getMsg(
                file.path,
                startByte.toHexString(),
                firstHeaderAfterTag.startByte.toHexString()
            )
        )

        //because we cant agree on start location we reread the audioheader from the start of the file, at least
        //this way we cant overwrite the audio, although we might overwrite part of the tag if we write this file
        //back later
        val headerOne = MP3AudioHeader(file, 0)
        log.debug("Checking from start:{}", headerOne)

        //Although the id3 tag size appears to be incorrect at least we have found the same location for the start
        //of audio whether we start searching from start of file or at the end of the alleged of file so no real
        //problem
        if (firstHeaderAfterTag.startByte == headerOne.startByte) {
            log.debug(
                ErrorMessage.MP3_START_OF_AUDIO_CONFIRMED.getMsg(
                    file.path,
                    headerOne.startByte.toHexString()
                )
            )
            return firstHeaderAfterTag
        } else {
            //We get a different value if read from start, can't guarantee 100% correct lets do some more checks
            log.debug(
                (ErrorMessage.MP3_RECALCULATED_POSSIBLE_START_OF_MP3_AUDIO.getMsg(
                    file.path,
                    headerOne.startByte.toHexString()
                ))
            )

            //Same frame count so probably both audio headers with newAudioHeader being the first one
            if (firstHeaderAfterTag.numberOfFrames == headerOne.numberOfFrames
            ) {
                log.warn(
                    (ErrorMessage.MP3_RECALCULATED_START_OF_MP3_AUDIO.getMsg(
                        file.path,
                        headerOne.startByte.toHexString()
                    ))
                )

                return headerOne
            }

            //If the size reported by the tag header is a little short and there is only nulls between the recorded value
            //and the start of the first audio found then we stick with the original header as more likely that currentHeader
            //DataInputStream not really a header
            if (isFilePortionNull(startByte.toInt(), firstHeaderAfterTag.startByte.toInt())
            ) {
                return firstHeaderAfterTag
            }

            //Skip to the next header (header 2, counting from start of file)
            headerTwo = MP3AudioHeader(
                file,
                headerOne.startByte + (headerOne.mp3FrameHeader?.getFrameLength() ?: 0)
            )

            //It matches the header we found when doing the original search from after the ID3Tag therefore it
            //seems that newAudioHeader was a false match and the original header was correct
            if (headerTwo.startByte == firstHeaderAfterTag.startByte
            ) {
                log.warn(
                    (ErrorMessage.MP3_START_OF_AUDIO_CONFIRMED.getMsg(
                        file.path,
                        firstHeaderAfterTag.startByte.toHexString()
                    ))
                )

                return firstHeaderAfterTag
            }

            //It matches the frameCount the header we just found so lends weight to the fact that the audio does indeed start at new header
            //however it maybe that neither are really headers and just contain the same data being misrepresented as headers.
            if (headerTwo.numberOfFrames == headerOne.numberOfFrames) {
                log.warn(
                    (ErrorMessage.MP3_RECALCULATED_START_OF_MP3_AUDIO.getMsg(
                        file.path,
                        headerOne.startByte.toHexString()
                    ))
                )

                return headerOne
            } else {
                log.warn(
                    (ErrorMessage.MP3_RECALCULATED_START_OF_MP3_AUDIO.getMsg(
                        file.path,
                        firstHeaderAfterTag.startByte.toHexString()
                    ))
                )

                return firstHeaderAfterTag
            }
        }
    }

    /**
     * @param startByte
     * @param endByte
     *
     * @return true if all the bytes between in the file between startByte and endByte are null, false
     * otherwise
     */
    private fun isFilePortionNull(startByte: Int, endByte: Int): Boolean {
        log.debug("Checking file portion:${startByte.toHexString()}:${endByte.toHexString()}")
        FileInputStream(file).use { fis ->
            fis.channel.use { fc ->
                fc.position(startByte.toLong())
                val bb = ByteBuffer.allocateDirect(endByte - startByte)
                fc.read(bb)
                while (bb.hasRemaining()) {
                    if (bb.get().toInt() != 0) {
                        return false
                    }
                }
            }
        }

        return true
    }
}