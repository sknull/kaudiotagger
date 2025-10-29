package de.visualdigits.kaudiotagger.model.audiofile

import de.visualdigits.kaudiotagger.model.audiofile.header.AudioHeader
import de.visualdigits.kaudiotagger.model.common.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.common.tag.Tag
import de.visualdigits.kaudiotagger.model.common.types.SupportedTag
import de.visualdigits.kaudiotagger.model.id3.tag.AbstractID3v2Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v22Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.id3.types.ID3v2Version
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
    
    val log = LoggerFactory.getLogger(javaClass)

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
    val tags: MutableMap<SupportedTag, AbstractTag> = mutableMapOf()

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
     * Returns a multi-line string with the file path, the encoding audioHeader, and the tag contents.
     *
     * @return A multi-line string with the file path, the encoding audioHeader, and the tag contents.
     * TODO Maybe this can be changed ?
     */
    override fun toString(): String {
        return ("AudioFile ${file?.name}  --------\n$audioHeader\n${tags.map { (k, v) -> "${k.name}:\n$v\n-------------------"}}\n===================")
    }

    /**
     * Checks the file is accessible with the correct permissions, otherwise exception occurs
     *
     * @param file
     * @param readOnly
     * @return
     */
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

    /**
     * Check does file exist
     *
     * @param file
     */
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
     * If using ID3 format convert tag from current version to another as specified by id3V2Version,
     *
     * @return the converted tag or the original if no conversion necessary
     */
    fun convertID3Tag(
        tag: AbstractID3v2Tag?,
        id3V2Version: ID3v2Version
    ): AbstractID3v2Tag? {
        if (tag is ID3v24Tag) {
            when (id3V2Version) {
                ID3v2Version.ID3_V22 -> return ID3v22Tag(tag)
                ID3v2Version.ID3_V23 -> return ID3v23Tag(tag)
                ID3v2Version.ID3_V24 -> return tag
            }
        } else if (tag is ID3v23Tag) {
            when (id3V2Version) {
                ID3v2Version.ID3_V22 -> return ID3v22Tag(tag)
                ID3v2Version.ID3_V23 -> return tag
                ID3v2Version.ID3_V24 -> return ID3v24Tag(tag)
            }
        } else if (tag is ID3v22Tag) {
            when (id3V2Version) {
                ID3v2Version.ID3_V22 -> return tag
                ID3v2Version.ID3_V23 -> return ID3v23Tag(tag)
                ID3v2Version.ID3_V24 -> return ID3v24Tag(tag)
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
