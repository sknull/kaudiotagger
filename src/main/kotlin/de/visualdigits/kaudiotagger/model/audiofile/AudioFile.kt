package de.visualdigits.kaudiotagger.model.audiofile

import de.visualdigits.kaudiotagger.model.audiofile.header.AudioHeader
import de.visualdigits.kaudiotagger.model.datatype.types.ID3V2Version
import de.visualdigits.kaudiotagger.model.datatype.types.SupportedFileFormat
import de.visualdigits.kaudiotagger.model.exceptions.NoReadPermissionsException
import de.visualdigits.kaudiotagger.model.exceptions.ReadOnlyFileException
import de.visualdigits.kaudiotagger.model.tag.Tag
import de.visualdigits.kaudiotagger.model.tag.id3.AbstractID3v2Tag
import de.visualdigits.kaudiotagger.model.tag.id3.ID3v22Tag
import de.visualdigits.kaudiotagger.model.tag.id3.ID3v23Tag
import de.visualdigits.kaudiotagger.model.tag.id3.ID3v24Tag
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import org.slf4j.LoggerFactory
import java.io.File
import java.io.FileNotFoundException
import java.io.RandomAccessFile
import java.util.Locale

/**
 *
 * This is the main object manipulated by the user representing an audiofile, its properties and its tag.
 *
 * The preferred way to obtain an `AudioFile` is to use the `AudioFileIO.read(File)` method.
 *
 * The `AudioHeader` contains every properties associated with the file itself (no meta-data), like the bitrate, the sampling rate, the encoding audioHeaders, etc.
 *
 * To get the meta-data contained in this file you have to get the `Tag` of this `AudioFile`
 *
 * @author Raphael Slinckx
 * @version $Id$
 * @see AudioFileIO
 *
 * @see Tag
 *
 * @since v0.01
 */
open class AudioFile {
    
    protected val log = LoggerFactory.getLogger(javaClass)

    /**
     * The physical file that this instance represents.
     */
    var file: File? = null

    /**
     * The Audio header info
     */
    var audioHeader: AudioHeader? = null

    /**
     *
     * Returns the tag contained in this AudioFile, the `Tag` contains any useful meta-data, like
     * artist, album, title, etc. If the file does not contain any tag the null is returned. Some audio formats do
     * not allow there to be no tag so in this case the reader would return an empty tag whereas for others such
     * as mp3 it is purely optional.
     *
     * @return Returns the tag contained in this AudioFile, or null if no tag exists.
     */
    /**
     * Assign a tag to this audio file
     *
     * @param tag Tag to be assigned
     */
    /**
     * The tag
     */
    private var tag: Tag? = null

    /**
     * Retrieve the file extension
     *
     * @return
     */
    /**
     * Set the file extension
     *
     * @param ext
     */
    /**
     * The tag
     */
    var ext: String? = null

    constructor()

    /**
     *
     * These constructors are used by the different readers, users should not use them, but use the `AudioFileIO.read(File)` method instead !.
     *
     * Create the AudioFile representing file f, the encoding audio headers and containing the tag
     *
     * @param f           The file of the audio file
     * @param audioHeader the encoding audioHeaders over this file
     * @param tag         the tag contained in this file or null if no tag exists
     */
    constructor(f: File, audioHeader: AudioHeader, tag: Tag?) {
        this.file = f
        this.audioHeader = audioHeader
        this.tag = tag
    }

    /**
     *
     * These constructors are used by the different readers, users should not use them, but use the `AudioFileIO.read(File)` method instead !.
     *
     * Create the AudioFile representing file denoted by pathnames, the encoding audio Headers and containing the tag
     *
     * @param s           The pathname of the audio file
     * @param audioHeader the encoding audioHeaders over this file
     * @param tag         the tag contained in this file
     */
    constructor(s: String, audioHeader: AudioHeader, tag: Tag?) {
        this.file = File(s)
        this.audioHeader = audioHeader
        this.tag = tag
    }

//    /**
//     *
//     * Write the tag contained in this AudioFile in the actual file on the disk, this is the same as calling the `AudioFileIO.write(this)` method.
//     *
//     * @throws NoWritePermissionsException if the file could not be written to due to file permissions
//     * @throws CannotWriteException        If the file could not be written/accessed, the extension wasn't recognized, or other IO error occured.
//     * @see AudioFileIO
//     */
//    open fun commit() {
//        AudioFileIO.write(this)
//    }
//
//    /**
//     *
//     * Delete any tags that exist in the fie , this is the same as calling the `AudioFileIO.delete(this)` method.
//     *
//     * @throws CannotWriteException If the file could not be written/accessed, the extension wasn't recognized, or other IO error occured.
//     * @see AudioFileIO
//     */
//    fun delete() {
//        AudioFileIO.delete(this)
//    }

    /**
     *
     * Returns a multi-line string with the file path, the encoding audioHeader, and the tag contents.
     *
     * @return A multi-line string with the file path, the encoding audioHeader, and the tag contents.
     * TODO Maybe this can be changed ?
     */
    override fun toString(): String {
        return ("AudioFile ${file?.absolutePath}  --------\n$audioHeader\n${if (tag == null) "" else tag.toString()}\n-------------------")
    }

    /**
     * Checks the file is accessible with the correct permissions, otherwise exception occurs
     *
     * @param file
     * @param readOnly
     * @return
     * @throws ReadOnlyFileException
     * @throws FileNotFoundException
     */
    protected fun checkFilePermissions(file: File, readOnly: Boolean): RandomAccessFile {
        val newFile: RandomAccessFile

        // These exists(), can read, can write checks are sprinkled around the code. Are these necessary? Why not just treat them as
        // exceptional conditions. They have to be handled anyway.
        checkFileExists(file)
        if (readOnly) {
            if (!file.canRead()) {
                log.error("Unable to read file:" + file)
                //                    log.error(Permissions.displayPermissions(path));
                throw NoReadPermissionsException(
                    ErrorMessage.GENERAL_READ_FAILED_DO_NOT_HAVE_PERMISSION_TO_READ_FILE.getMsg(
                        file
                    )
                )
            }
            newFile = RandomAccessFile(file, "r")
        } else {
            if (TagOptionSingleton.checkIsWritable && file.canWrite()
            ) {
                log.error("Unable to write file:" + file)
                //                    log.error(Permissions.displayPermissions(path));
                throw ReadOnlyFileException(
                    ErrorMessage.NO_PERMISSIONS_TO_WRITE_TO_FILE.getMsg(file)
                )
            }
            newFile = RandomAccessFile(file, "rw")
        }
        return newFile
    }

    /**
     * Check does file exist
     *
     * @param file
     * @throws FileNotFoundException if file not found
     */
    fun checkFileExists(file: File) {
        log.debug(
            "Reading file:" +
                    "path" +
                    file.path +
                    ":abs:" +
                    file.absolutePath
        )
        if (!file.exists()) {
            log.error("Unable to find:" + file.path)
            throw FileNotFoundException(
                ErrorMessage.UNABLE_TO_FIND_FILE.getMsg(file.path)
            )
        }
    }

    /**
     * Optional debugging method. Must override to do anything interesting.
     *
     * @return Empty string.
     */
    open fun displayStructureAsXML(): String? {
        return ""
    }

    /**
     * Optional debugging method. Must override to do anything interesting.
     *
     * @return
     */
    open fun displayStructureAsPlainText(): String? {
        return ""
    }

    /**
     * Get the tag or if the file doesn't have one at all, create a default tag and set it
     * as the tag of this file
     *
     * @return
     */
    fun getTagOrCreateAndSetDefault(): Tag? {
        val tag = getTagOrCreateDefault()
        setTag(tag)
        return tag
    }

    /**
     * Get the tag or if the file doesn't have one at all, create a default tag  and return
     *
     * @return
     */
    open fun getTagOrCreateDefault(): Tag? {
        val tag: Tag? = getTag()
        if (tag == null) {
            return createDefaultTag()
        }
        return tag
    }

    /**
     *
     * Returns the tag contained in this AudioFile, the `Tag` contains any useful meta-data, like
     * artist, album, title, etc. If the file does not contain any tag the null is returned. Some audio formats do
     * not allow there to be no tag so in this case the reader would return an empty tag whereas for others such
     * as mp3 it is purely optional.
     *
     * @return Returns the tag contained in this AudioFile, or null if no tag exists.
     */
    fun getTag(): Tag? {
        return tag
    }

    /**
     * Assign a tag to this audio file
     *
     * @param tag Tag to be assigned
     */
    open fun setTag(tag: Tag?) {
        this.tag = tag
    }

    /**
     * Create Default Tag
     *
     * @return
     */
    open fun createDefaultTag(): Tag? {
        val fileName = this.file!!.getName()
        val dotIndex = fileName.lastIndexOf('.')
        if (dotIndex > 0 && dotIndex < fileName.length - 1) {
            return SupportedFileFormat.fromExtension(
                fileName.substring(dotIndex + 1)
            ).createDefaultTag()
        }
        throw RuntimeException(
            "Unable to create default tag for this file format. No File extension found."
        )
    }

    /**
     * Get the tag and convert to the default tag version or if the file doesn't have one at all, create a default tag
     * set as tag for this file
     *
     *
     * Conversions are currently only necessary/available for formats that support ID3
     *
     * @return
     */
    open fun getTagAndConvertOrCreateAndSetDefault(): Tag? {
        /* TODO Currently only works for Dsf We need additional check here for Wav and Aif because they wrap the ID3 tag so never return
         * null for getTag() and the wrapper stores the location of the existing tag, would that be broken if tag set to something else
         * // TODO: 1/7/17 this comment may be outdated
         */
        val tag: Tag? = getTagOrCreateDefault()

        if (tag is AbstractID3v2Tag) {
            setTag(
                convertID3Tag(
                    tag as AbstractID3v2Tag?,
                    TagOptionSingleton.id3v2Version
                )
            )
        } else {
            setTag(tag)
        }
        return getTag()
    }

    /**
     * If using ID3 format convert tag from current version to another as specified by id3V2Version,
     *
     * @return the converted tag or the original if no conversion necessary
     */
    fun convertID3Tag(
        tag: AbstractID3v2Tag?,
        id3V2Version: ID3V2Version
    ): AbstractID3v2Tag? {
        if (tag is ID3v24Tag) {
            when (id3V2Version) {
                ID3V2Version.ID3_V22 -> return ID3v22Tag(tag)
                ID3V2Version.ID3_V23 -> return ID3v23Tag(tag)
                ID3V2Version.ID3_V24 -> return tag
            }
        } else if (tag is ID3v23Tag) {
            when (id3V2Version) {
                ID3V2Version.ID3_V22 -> return ID3v22Tag(tag)
                ID3V2Version.ID3_V23 -> return tag
                ID3V2Version.ID3_V24 -> return ID3v24Tag(tag)
            }
        } else if (tag is ID3v22Tag) {
            when (id3V2Version) {
                ID3V2Version.ID3_V22 -> return tag
                ID3V2Version.ID3_V23 -> return ID3v23Tag(tag)
                ID3V2Version.ID3_V24 -> return ID3v24Tag(tag)
            }
        }
        return null
    }

    companion object {
        /**
         * @param file
         * @return filename with audioFormat separator stripped off.
         */
        @JvmStatic
        fun getBaseFilename(file: File): String {
            val index = file.getName().lowercase(Locale.getDefault()).lastIndexOf(".")
            if (index > 0) {
                return file.getName().substring(0, index)
            }
            return file.getName()
        }
    }
}
