package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.id3.tag.AbstractID3v2Tag.Companion.MAXIMUM_WRITABLE_CHUNK_SIZE
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.FileLock

object FileUtil {

    val log: Logger = LoggerFactory.getLogger(FileUtil.javaClass)

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
                    // Create read channel from original file
                    // TODO lock so cant be modified by anything else whilst reading from it ?
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
            paddedFile.setLastModified(lastModified)
        } catch (e: IOException) {
            paddedFile.delete()
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
            newFile.delete()
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
                // TODO now if this happens we are left with testfile.old instead of testfile.mp3
                log.warn(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_BACKUP_TO_ORIGINAL.getMsg(
                        originalFileBackup.absolutePath,
                        originalFile.getName()
                    )
                )
            }

            log.warn(ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE.getMsg(originalFile.absolutePath, newFile.getName()))
            newFile.delete()
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
     *
     * TODO:this appears to have little effect on Windows Vista
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
        } catch (exception: IOException) { // Assumes locking is not supported on this platform so just returns null
            null
        } ?: throw IOException(ErrorMessage.GENERAL_WRITE_FAILED_FILE_LOCKED.getMsg(filePath))
    }
}