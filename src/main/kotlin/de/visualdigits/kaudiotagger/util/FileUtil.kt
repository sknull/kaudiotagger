package de.visualdigits.kaudiotagger.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.FileLock

object FileUtil {

    val log: Logger = LoggerFactory.getLogger(FileUtil.javaClass)

    // The max size we try to write in one go to avoid out of memory errors (10mb)
    const val MAXIMUM_WRITABLE_CHUNK_SIZE: Long = 10000000

    fun checkFilePermissions(file: File, readOnly: Boolean): RandomAccessFile {
        val newFile: RandomAccessFile

        // These exists(), can read, can write checks are sprinkled around the code. Are these necessary? Why not just treat them as
        // exceptional conditions. They have to be handled anyway.
        checkFileExists(file)
        if (readOnly) {
            if (!file.canRead()) {
                log.error("Unable to read file:$file")
                error(ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(file))
            }
            newFile = RandomAccessFile(file, "r")
        } else {
            if (TagOptionSingleton.checkIsWritable && file.canWrite()
            ) {
                log.error("Unable to write file:$file")
                error(ErrorMessage.NO_PERMISSIONS_TO_WRITE_TO_FILE.getMsg(file))
            }
            newFile = RandomAccessFile(file, "rw")
        }
        return newFile
    }

    fun checkFileExists(file: File) {
        log.debug("Reading file:path${file.path}:abs:${file.absolutePath}")
        if (!file.exists()) {
            log.error("Unable to find:" + file.path)
            throw FileNotFoundException(
                ErrorMessage.UNABLE_TO_FIND_FILE.getMsg(file.path)
            )
        }
    }

    /**
     * Adjust the length of the  padding at the beginning of the MP3 file, this is only called when there is currently
     * not enough space before the start of the audio to write the tag.
     *
     *
     * A new file will be created with enough size to fit the `ID3v2` tag.
     * The old file will be deleted, and the new file renamed.
     *
     * @param paddingSize This is total size required to store tag before audio
     * @param audioStart
     * @param file        The file to adjust the padding length of
     * rather than a regular file or cannot be opened for any other
     * reason
     */
    fun adjustPadding(file: File?, paddingSize: Int, audioStart: Long) {
        if (file == null) {
            return
        }
        log.debug("Need to move audio file to accommodate tag")

        // Create buffer holds the necessary padding
        val paddingBuffer = ByteBuffer.wrap(ByteArray(paddingSize))

        // Create Temporary File and write channel, make sure it is locked

        val paddedFile = File.createTempFile(Utils.getBaseFilenameForTempFile(file), ".new", file.getParentFile())
        try {
            FileOutputStream(paddedFile).use { fouts ->
                fouts.getChannel().use { fcOut ->
                    FileInputStream(file).use { fins ->
                        fins.getChannel().use { fcIn ->
                            // Write padding to new file (this is where the tag will be written to later)
                            val written = fcOut.write(paddingBuffer).toLong()

                            // Write rest of file starting from audio
                            log.debug("Copying:" + (file.length() - audioStart) + "bytes")

                            // If the amount to be copied is very large we split into 10MB lumps to try and avoid
                            // out of memory errors
                            val audiolength = file.length() - audioStart
                            if (audiolength <= MAXIMUM_WRITABLE_CHUNK_SIZE) {
                                fcIn.position(audioStart)
                                val written2 = fcOut.transferFrom(fcIn, paddingSize.toLong(), audiolength)
                                log.debug("Written padding:$written Data:$written2")
                                if (written2 != audiolength) {
                                    throw RuntimeException(ErrorMessage.MP3_UNABLE_TO_ADJUST_PADDING.getMsg(audiolength, written2))
                                }
                            } else {
                                val noOfChunks = audiolength / MAXIMUM_WRITABLE_CHUNK_SIZE
                                val lastChunkSize = audiolength % MAXIMUM_WRITABLE_CHUNK_SIZE
                                var written2: Long = 0
                                for (i in 0..<noOfChunks) {
                                    written2 += fcIn.transferTo(
                                        audioStart + (i * MAXIMUM_WRITABLE_CHUNK_SIZE),
                                        MAXIMUM_WRITABLE_CHUNK_SIZE,
                                        fcOut
                                    )
                                }
                                written2 += fcIn.transferTo(
                                    audioStart + (noOfChunks * MAXIMUM_WRITABLE_CHUNK_SIZE),
                                    lastChunkSize,
                                    fcOut
                                )
                                log.debug("Written padding:$written Data:$written2")
                                if (written2 != audiolength) {
                                    throw RuntimeException(ErrorMessage.MP3_UNABLE_TO_ADJUST_PADDING.getMsg(audiolength, written2))
                                }
                            }
                        }
                    }
                }
            }

            // Store original modification time
            val lastModified = file.lastModified()

            // Replace file with paddedFile
            replaceFile(file, paddedFile)
            if (!paddedFile.setLastModified(lastModified)) log.warn("Could not set last modified: $file")
        } catch (e: IOException) {
            if (!paddedFile.delete()) log.warn("Could not delete file: $paddedFile")
            throw e
        }
    }

    /**
     * Replace originalFile with the contents of newFile
     *
     * Both files must exist in the same folder so that there are no problems with filesystem mount points
     *
     * @param newFile
     * @param originalFile
     */
    fun replaceFile(originalFile: File?, newFile: File?) {
        if (originalFile == null || newFile == null) {
            return
        }
        // Rename Original File to make a backup in case problem with new file
        var originalFileBackup = File(
            originalFile.getAbsoluteFile().getParentFile().path,
            originalFile.nameWithoutExtension + ".old"
        )
        // If already exists modify the suffix
        var count = 1
        while (originalFileBackup.exists()) {
            originalFileBackup = File(
                originalFile.getAbsoluteFile().getParentFile().path,
                originalFile.nameWithoutExtension + ".old" + count
            )
            count++
        }

        if (!originalFile.renameTo(originalFileBackup)) {
            log.warn(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_FILE_TO_BACKUP.getMsg(
                originalFile.absolutePath,
                originalFileBackup.getName()
            ))
            if (!newFile.delete()) log.warn("Could not delete new file: $newFile")
            error(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_FILE_TO_BACKUP.getMsg(originalFile.absolutePath, originalFileBackup.getName()))
        }

        // Rename new Temporary file to the final file
        if (!newFile.renameTo(originalFile)) {
            // Renamed failed so lets do some checks rename the backup back to the original file
            // New File doesnt exist
            if (!newFile.exists()) {
                log.warn(ErrorMessage.GENERAL_WRITE_FAILED_NEW_FILE_DOESNT_EXIST.getMsg(newFile.absolutePath))
            }

            // Rename the backup back to the original
            if (!originalFileBackup.renameTo(originalFile)) {
                log.warn(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_BACKUP_TO_ORIGINAL.getMsg(
                        originalFileBackup.absolutePath,
                        originalFile.getName()
                    )
                )
            }

            log.warn(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE.getMsg(originalFile.absolutePath, newFile.getName()))
            if (!newFile.delete()) log.warn("Could not delete new file: $newFile")
            error(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE.getMsg(originalFile.absolutePath, newFile.getName()))
        } else {
            // Rename was okay so we can now deleteField the backup of the original
            if (!originalFileBackup.delete()) {
                // Not a disaster but can't deleteField the backup so make a warning
                log.warn(ErrorMessage.GENERAL_WRITE_WARNING_UNABLE_TO_DELETE_BACKUP_FILE.getMsg(originalFileBackup.absolutePath))
            }
        }
    }

    /**
     * Get file lock for writing too file
     *
     * @param fileChannel
     * @param filePath
     * @return lock or null if locking is not supported
     * because indicates a programming error
     */
    fun getFileLockForWriting(
        fileChannel: FileChannel,
        filePath: String?
    ): FileLock {
        log.debug("locking fileChannel for $filePath")
        return try {
            fileChannel.tryLock()
        } catch (_: IOException) { // Assumes locking is not supported on this platform so just returns null
            null
        } ?: throw IOException(ErrorMessage.GENERAL_WRITE_FAILED_FILE_LOCKED.getMsg(filePath))
    }
}