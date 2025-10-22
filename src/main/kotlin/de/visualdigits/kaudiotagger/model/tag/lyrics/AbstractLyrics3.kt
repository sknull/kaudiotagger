package de.visualdigits.kaudiotagger.model.tag.lyrics

import de.visualdigits.kaudiotagger.model.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.tag.id3.ID3v1Tag
import java.io.RandomAccessFile

abstract class AbstractLyrics3: AbstractTag {

    constructor()

    constructor(copyObject: AbstractLyrics3): super(copyObject)

    /**
     * @param file
     */
    override fun delete(file: RandomAccessFile) {
        var filePointer: Long
        val id3v1tag = ID3v1Tag()
    }
}