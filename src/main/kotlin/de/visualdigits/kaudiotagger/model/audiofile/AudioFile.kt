package de.visualdigits.kaudiotagger.model.audiofile

import de.visualdigits.kaudiotagger.model.tag.Tag
import de.visualdigits.kaudiotagger.util.ErrorMessage
import org.slf4j.LoggerFactory
import java.io.File
import java.io.RandomAccessFile

abstract class AudioFile<A : de.visualdigits.kaudiotagger.model.audiofile.header.AudioHeader>(
    val file: File
) {

    companion object {

        inline fun <reified T : de.visualdigits.kaudiotagger.model.audiofile.AudioFile<T>> read(file: File): T? {
            return when (file.extension) {
                "mp3" -> _root_ide_package_.de.visualdigits.kaudiotagger.model.audiofile.mp3.Mp3File.Companion.read(file)
                else -> null
            } as? T
        }
    }

    val log = LoggerFactory.getLogger(javaClass)

    /**
     * The Audio header info
     */
    var audioHeader: A? = null

    /**
     * The tag
     */
    var tag: Tag? = null

    /**
     * The tag
     */
    var extension: String? = null

    /**
     * Checks the file is accessible with the correct permissions, otherwise exception occurs
     *
     * @param file
     * @param readOnly
     *
     * @return RandomAccessFile
     */
    fun checkFilePermissions(
        file: File,
        readOnly: Boolean
    ): RandomAccessFile {
        val newFile: RandomAccessFile

        // These exists(), can read, can write checks are sprinkled around the code. Are these necessary? Why not just treat them as
        // exceptional conditions. They have to be handled anyway.
        checkFileExists(file)
        if (readOnly) {
            if (!file.canRead()) {
                log.error("Unable to read file:" + file)
                error(
                    ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(
                        file
                    )
                )
            }
            newFile = RandomAccessFile(file, "r")
        } else {
            newFile = RandomAccessFile(file, "rw")
        }
        return newFile
    }

    /**
     * Check does file exist
     *
     * @param file
     */
    fun checkFileExists(file: File) {
        log.debug(
            "Reading file:" +
                    "path" +
                    file.getPath() +
                    ":abs:" +
                    file.getAbsolutePath()
        )
        if (!file.exists()) {
            log.error("Unable to find:" + file.getPath())
            error(
                ErrorMessage.UNABLE_TO_FIND_FILE.getMsg(file.getPath())
            )
        }
    }
}