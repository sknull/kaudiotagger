package de.visualdigits.kaudiotagger.model.audiofile.mp3

import de.visualdigits.kaudiotagger.model.audiofile.AudioFile
import de.visualdigits.kaudiotagger.model.audiofile.header.mp3.MP3AudioHeader
import de.visualdigits.kaudiotagger.model.common.tag.ID3Tag
import de.visualdigits.kaudiotagger.model.common.types.LoadOptions
import de.visualdigits.kaudiotagger.model.id3.tag.AbstractID3v2Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v11Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v1Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v22Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.lyrics3.tag.AbstractLyrics3
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer

/**
 * This class represents a physical MP3 File
 */
class MP3File : AudioFile {

    companion object {

        private const val MINIMUM_FILESIZE = 150

        fun read(file: File?, readOnly: Boolean = false, loadOptions: LoadOptions = LoadOptions.LOAD_ALL): MP3File {
            return file?.let { f ->
                val mp3File = MP3File()
                mp3File.readFile(f, readOnly, loadOptions)
                mp3File
            }?:error("File was null")
        }
    }

    /**
     * Creates a new empty MP3File datatype that is not associated with a
     * specific file.
     */
    constructor()

    private fun readFile(file: File, readOnly: Boolean, loadOptions: LoadOptions) {
        this.file = file

        // Check File accessibility
        checkFilePermissions(file, readOnly).use { newFile ->
            // Read ID3v2 tag size (if tag exists) to allow audioHeader parsing to skip over tag
            val tagSizeReportedByHeader = AbstractID3v2Tag.getV2TagSizeIfExists(file)
            log.debug("TagHeaderSize:" + tagSizeReportedByHeader.toHexString())
            audioHeader = MP3AudioHeader(file, tagSizeReportedByHeader)

            // If the audio header is not straight after the end of the tag then search from start of file
            if (tagSizeReportedByHeader != (audioHeader as MP3AudioHeader).mp3StartByte) {
                log.debug("First header found after tag:$audioHeader")
                audioHeader = checkAudioStart(
                    tagSizeReportedByHeader,
                    (audioHeader as MP3AudioHeader)
                )
            }

            readV1Tag(newFile, loadOptions)
            readV2Tag(file, loadOptions, (audioHeader as MP3AudioHeader).mp3StartByte.toInt())
        }
    }

    private fun readV1Tag(file: RandomAccessFile, loadOptions: LoadOptions) {
        if (loadOptions == LoadOptions.LOAD_IDV1TAG || loadOptions == LoadOptions.LOAD_ALL) {
            log.debug("Attempting to read id3v1tags")
            ID3v1Tag.read(file)?.also { tag -> tagV1 = tag }
            ID3v11Tag.read(file)?.also { tag -> tagV1 = tag }
        }
    }

    /**
     * Read V2tag if exists
     *
     *
     * @param file
     * @param loadOptions
     */
    private fun readV2Tag(file: File, loadOptions: LoadOptions, startByte: Int) {
        // We know where the actual Audio starts so load all the file from start to that point into
        // a buffer then we can read the IDv2 information without needing any more File I/O
        if (startByte >= AbstractID3v2Tag.TAG_HEADER_LENGTH) {
            log.debug("Attempting to read id3v2tags")
            val bb = FileInputStream(file).use { fis ->
                fis.getChannel().use { fc ->
                    val bb = ByteBuffer.allocate(startByte)
                    // XXX: don't change it to map
                    // https:// stackoverflow.com/questions/28378713/bytebuffer-getbyte-int-int-failed-on-android-ics-and-jb
                    fc.read(bb, 0)
                    bb
                }
            }

            try {
                bb.rewind()
                if ((loadOptions == LoadOptions.LOAD_IDV2TAG || loadOptions == LoadOptions.LOAD_ALL)) {
                    log.debug("Attempting to read id3v2tags")
                    ID3v22Tag.read(bb)?.also { tag -> tagV2 = tag }
                    ID3v23Tag.read(bb)?.also { tag -> tagV2 = tag }
                    ID3v24Tag.read(bb)?.also { tag -> tagV2 = tag }
                }
            } finally {
                bb.clear()
            }
        } else {
            log.debug("Not enough room for valid id3v2 tag:$startByte")
        }
    }

    /**
     * Regets the audio header starting from start of file, and write appropriate logging to indicate
     * potential problem to user.
     *
     * @param startByte
     * @param firstHeaderAfterTag
     * @return
     */
    private fun checkAudioStart(
        startByte: Long,
        firstHeaderAfterTag: MP3AudioHeader
    ): MP3AudioHeader {
        log.warn(ErrorMessage.MP3_ID3TAG_LENGTH_INCORRECT.getMsg(file?.path, startByte.toHexString(), firstHeaderAfterTag.mp3StartByte.toHexString()))

        // because we cant agree on start location we reread the audioheader from the start of the file, at least
        // this way we cant overwrite the audio although we might overwrite part of the tag if we write this file
        // back later
        val headerOne = MP3AudioHeader(file, 0)
        log.debug("Checking from start:" + headerOne)

        // Although the id3 tag size appears to be incorrect at least we have found the same location for the start
        // of audio whether we start searching from start of file or at the end of the alleged of file so no real
        // problem
        return if (firstHeaderAfterTag.mp3StartByte == headerOne.mp3StartByte) {
            log.debug(ErrorMessage.MP3_START_OF_AUDIO_CONFIRMED.getMsg(file?.path, headerOne.mp3StartByte.toHexString()))
            firstHeaderAfterTag
        } else {
            // We get a different value if read from start, can't guarantee 100% correct lets do some more checks
            log.debug((ErrorMessage.MP3_RECALCULATED_POSSIBLE_START_OF_MP3_AUDIO.getMsg(file?.path, headerOne.mp3StartByte.toHexString())))

            // Same frame count so probably both audio headers with newAudioHeader being the first one
            if (firstHeaderAfterTag.numberOfFrames == headerOne.numberOfFrames) {
                log.warn(
                    (ErrorMessage.MP3_RECALCULATED_START_OF_MP3_AUDIO.getMsg(
                        file?.path,
                        headerOne.mp3StartByte.toHexString()
                    ))
                )
                headerOne
            } else if (isFilePortionNull(
                    startByte.toInt(),
                    firstHeaderAfterTag.mp3StartByte.toInt()
                )
            ) {
                firstHeaderAfterTag
            } else {
                // Skip to the next header (header 2, counting from start of file)
                val headerTwo = MP3AudioHeader(file, headerOne.mp3StartByte + (headerOne.mp3FrameHeader?.getFrameLength() ?: 0))

                // It matches the header we found when doing the original search from after the ID3Tag therefore it
                // seems that newAudioHeader was a false match and the original header was correct
                if (headerTwo.mp3StartByte == firstHeaderAfterTag.mp3StartByte) {
                    log.warn((ErrorMessage.MP3_START_OF_AUDIO_CONFIRMED.getMsg(file?.path, firstHeaderAfterTag.mp3StartByte.toHexString())))
                    firstHeaderAfterTag
                } else if (headerTwo.numberOfFrames == headerOne.numberOfFrames) {
                    log.warn(
                        (ErrorMessage.MP3_RECALCULATED_START_OF_MP3_AUDIO.getMsg(file?.path, headerOne.mp3StartByte.toHexString()))
                    )
                    headerOne
                } else {
                    log.warn((ErrorMessage.MP3_RECALCULATED_START_OF_MP3_AUDIO.getMsg(file?.path, firstHeaderAfterTag.mp3StartByte.toHexString())))
                    firstHeaderAfterTag
                }
            }
        }
    }

    /**
     * @param startByte
     * @param endByte
     * @return
     * @return true if all the bytes between in the file between startByte and endByte are null, false
     * otherwise
     */
    private fun isFilePortionNull(startByte: Int, endByte: Int): Boolean {
        log.debug("Checking file portion:$startByte:${endByte.toHexString()}")
        FileInputStream(file).use { fis ->
            fis.getChannel().use { fc ->
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

    /**
     * Extracts the raw ID3v2 tag data into a file.
     *
     *
     * This provides access to the raw data before manipulation, the data is written from the start of the file
     * to the start of the Audio Data. This is primarily useful for manipulating corrupted tags that are not
     * (fully) loaded using the standard methods.
     *
     * @param outputFile to write the data to
     * @return
     */
    fun extractid3v2tagDataIntoFile(outputFile: File): File? {
        val startByte = (audioHeader as MP3AudioHeader).mp3StartByte.toInt()
        return if (startByte >= 0) {
            // Read byte into buffer
            file?.also { f ->
                FileInputStream(f).use { fis ->
                    fis.getChannel().use { fc ->
                        val bb = ByteBuffer.allocate(startByte)
                        fc.read(bb)

                        // Write bytes to outputFile
                        FileOutputStream(outputFile).use { out ->
                            out.write(bb.array())
                        }
                    }
                }
            }
            outputFile
        } else {
            null
        }
    }

    /**
     * Return audio header
     *
     * @return
     */
    fun getMP3AudioHeader(): MP3AudioHeader? {
        return audioHeader as? MP3AudioHeader
    }

    /**
     * Used by tags when writing to calculate the location of the music file
     *
     * @param file
     * @return the location within the file that the audio starts
     */
    fun getMP3StartByte(file: File?): Long {
        try {
            // Read ID3v2 tag size (if tag exists) to allow audio header parsing to skip over tag
            val startByte = AbstractID3v2Tag.getV2TagSizeIfExists(file)

            var audioHeader = MP3AudioHeader(file, startByte)
            if (startByte != audioHeader.mp3StartByte) {
                log.debug("First header found after tag:" + audioHeader)
                audioHeader = checkAudioStart(startByte, audioHeader)
            }
            return audioHeader.mp3StartByte
        } catch (ioe: IOException) {
            throw ioe
        }
    }

    /**
     * Saves the tags in this dataType to the file referred to by this dataType.
     *
     * @throws IOException  on any I/O error
     * @throws TagException on any exception generated by this library.
     */
    fun save() {
        this.file?.also { f -> save(f) }
    }

    /**
     * Saves the tags in this dataType to the file argument. It will be saved as
     * TagConstants.MP3_FILE_SAVE_WRITE
     *
     * @param fileToSave file to save the this dataTypes tags to
     * @throws FileNotFoundException if unable to find file
     * @throws IOException           on any I/O error
     */
    fun save(fileToSave: File) {
        // Ensure we are dealing with absolute filepaths not relative ones
        val file = fileToSave.getAbsoluteFile()

        log.debug("Saving  : " + file.path)

        // Checks before starting write
        precheck(file)

        // ID3v2 Tag
        if (TagOptionSingleton.id3v2Save) {
            val id3v2tag = getID3v2Tag()
            if (id3v2tag == null) {
                RandomAccessFile(file, "rw").use { rfile ->
                    (ID3v24Tag()).delete(rfile)
                    (ID3v23Tag()).delete(rfile)
                    (ID3v22Tag()).delete(rfile)
                }
                log.debug("Deleting ID3v2 tag:" + file.getName())
            } else {
                log.debug("Writing ID3v2 tag:" + file.getName())
                    val mp3AudioHeader = this.audioHeader as MP3AudioHeader
                    val mp3StartByte: Long = mp3AudioHeader.mp3StartByte
                    val newMp3StartByte: Long = id3v2tag.write(file, mp3StartByte)
                    if (mp3StartByte != newMp3StartByte) {
                        log.debug("New mp3 start byte: $newMp3StartByte")
                        mp3AudioHeader.mp3StartByte = newMp3StartByte
                }
            }
        }
        RandomAccessFile(file, "rw").use { rfile ->
            // Lyrics 3 Tag
            if (TagOptionSingleton.lyrics3Save) {
                val lyrics3tag = getLyrics3Tag()
                lyrics3tag?.write(rfile)
            }
            // ID3v1 tag
            if (TagOptionSingleton.id3v1Save) {
                val id3v1tag = getID3v1Tag()
                log.debug("Processing ID3v1")
                if (id3v1tag == null) {
                    log.debug("Deleting ID3v1")
                    (ID3v1Tag()).delete(rfile)
                } else {
                        log.debug("Saving ID3v1")
                        id3v1tag.write(rfile)
                }
            }
        }
    }

    private fun write(tag: AbstractID3v2Tag, file: File) {
        log.debug("Writing ID3v2 tag: ${file.getName()}")
        val mp3AudioHeader = this.audioHeader as? MP3AudioHeader
        val mp3StartByte = mp3AudioHeader?.mp3StartByte ?: 0
        val newMp3StartByte = tag.write(file, mp3StartByte)
        if (mp3StartByte != newMp3StartByte) {
            log.debug("New mp3 start byte: $newMp3StartByte")
            mp3AudioHeader?.mp3StartByte = newMp3StartByte
        }
    }

    /**
     * Check can write to file
     *
     * @param file
     */
    fun precheck(file: File) {
        if (!file.exists()) {
            log.error(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_NOT_FOUND.getMsg(file.getName()))
            throw IOException(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_NOT_FOUND.getMsg(file.getName()))
        }

        if (TagOptionSingleton.checkIsWritable && !file.canWrite()) {
            log.error(ErrorMessage.GENERAL_WRITE_FAILED.getMsg(file.getName()))
            throw IOException(ErrorMessage.GENERAL_WRITE_FAILED.getMsg(file.getName()))
        }

        if (file.length() <= MINIMUM_FILESIZE) {
            log.error(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_IS_TOO_SMALL.getMsg(file.getName()))
            throw IOException(ErrorMessage.GENERAL_WRITE_FAILED_BECAUSE_FILE_IS_TOO_SMALL.getMsg(file.getName()))
        }
    }

    /**
     * Returns the highest tag.
     */
    fun getTag(): ID3Tag? = (tagV2?:tagV1) as? ID3Tag

    /**
     * Returns the highest v1 tag.
     */
    fun getID3v1Tag(): ID3v1Tag? = tagV1

    fun hasID3v1Tag(): Boolean = tagV1 != null

    /**
     * Returns the highest v2 tag.
     */
    fun getID3v2Tag(): AbstractID3v2Tag? = tagV2

    fun hasID3v2Tag(): Boolean = tagV2 != null

    fun getLyrics3Tag(): AbstractLyrics3? = lyrics3

    fun getID3v2TagAsv24(): ID3v24Tag {
        return when (val tag = getID3v2Tag()) {
            is ID3v24Tag -> tag
            else -> ID3v24Tag(tag)
        }
    }

    fun setTag(tag: ID3Tag?) {
        when (tag) {
            is ID3v1Tag -> tagV1 = tag
            is AbstractID3v2Tag -> tagV2 = tag
            is AbstractLyrics3 -> lyrics3 = tag
        }
    }

    /**
     * Get the ID3v2 tag and convert to preferred version or if the file doesn't have one at all
     * create a default tag of preferred version and set it. The file may already contain a ID3v1 tag but because
     * this is not terribly useful the v1tag is not considered for this problem.
     *
     * @return
     */
    fun getTagAndConvertOrCreateAndSetDefault(): ID3Tag {
        convertID3Tag(getTagOrCreateDefault(), TagOptionSingleton.id3v2Version)?.also { t -> setTag(t) }
        return getTag()?:error("Could create tag")
    }

    /**
     * Overridden to only consider ID3v2 Tag
     *
     * @return
     */
    fun getTagOrCreateDefault(): AbstractID3v2Tag {
        return getID3v2Tag() ?: return createDefaultTag()
    }

    /**
     * Create Default Tag
     *
     * @return
     */
    fun createDefaultTag(): AbstractID3v2Tag {
        return TagOptionSingleton.createDefaultID3Tag()
    }

    /**
     * Overridden for compatibility with merged code
     *
     * @throws NoWritePermissionsException if the file could not be written to due to file permissions
     * @throws CannotWriteException
     */
    fun commit() {
        try {
            save()
        } catch (e: Exception) {
            log.error("Could not commit file", e)
        }
    }
}
