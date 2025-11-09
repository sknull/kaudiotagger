package de.visualdigits.kaudiotagger.model.audiofile

import de.visualdigits.kaudiotagger.model.audiofile.header.AudioHeader
import de.visualdigits.kaudiotagger.model.id3.tag.AbstractID3v2Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v1Tag
import de.visualdigits.kaudiotagger.model.images.Artwork
import de.visualdigits.kaudiotagger.model.lyrics3.tag.AbstractLyrics3
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

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
 */
open class AudioFile {
    
    val log: Logger = LoggerFactory.getLogger(javaClass)

    /**
     * The physical file that this instance represents.
     */
    var file: File? = null

    /**
     * The Audio header info
     */
    var audioHeader: AudioHeader? = null

    var tagV1: ID3v1Tag? = null

    var tagV2: AbstractID3v2Tag? = null

    var lyrics3: AbstractLyrics3? = null

    constructor()

    override fun toString(): String {
        return ("===================\nFilename: ${file?.name}\nHeader: $audioHeader\n-------------------\n${listOfNotNull(tagV1, tagV2).joinToString("\n-------------------\n") { t -> "${t.javaClass.simpleName}:\n$t" }}\n===================")
    }

    /**
     * @return a list of all artwork in this file using the format independent Artwork class
     */
    open fun getArtworkList(): List<Artwork> {
        return listOf()
    }

    open fun displayStructureAsXML(): String? {
        return ""
    }

    open fun displayStructureAsPlainText(): String? {
        return ""
    }
}
