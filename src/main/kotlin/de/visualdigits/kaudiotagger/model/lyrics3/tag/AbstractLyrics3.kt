package de.visualdigits.kaudiotagger.model.lyrics3.tag

import de.visualdigits.kaudiotagger.model.common.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v1Tag
import java.io.RandomAccessFile

abstract class AbstractLyrics3: AbstractTag {

    companion object {

    }

    constructor()

    constructor(copyObject: AbstractLyrics3): super(copyObject)

    /**
     * @param file
     */
    override fun delete(file: RandomAccessFile) {
        ID3v1Tag()
    }
}