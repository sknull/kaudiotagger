package de.visualdigits.kaudiotagger.model.common.tag

import java.io.RandomAccessFile
import java.nio.ByteBuffer

abstract class AbstractTag : AbstractTagItem {

    companion object {

        const val TYPE_TAG: String = "tag"
    }

    constructor()

    constructor(copyObject: AbstractTag): super(copyObject)

    /**
     * Looks for this tag in the buffer
     *
     * @param byteBuffer
     * @return returns true if found, false otherwise.
     */
    abstract fun seek(byteBuffer: ByteBuffer): Boolean

    /**
     * Writes the tag to the file
     *
     * @param file
     */
    abstract fun write(file: RandomAccessFile)

    /**
     * Removes the specific tag from the file
     *
     * @param file MP3 file to append to.
     */
    abstract fun delete(file: RandomAccessFile)
}