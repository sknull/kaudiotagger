package de.visualdigits.kaudiotagger.model.tag

import java.io.IOException
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
     * @throws IOException
     */
    abstract fun write(file: RandomAccessFile)

    /**
     * Removes the specific tag from the file
     *
     * @param file MP3 file to append to.
     * @throws IOException on any I/O error
     */
    abstract fun delete(file: RandomAccessFile)

    /**
     * Determines whether another datatype is equal to this tag. It just compares
     * if they are the same class, then calls `super.equals(obj)`.
     *
     * @param obj The object to compare
     * @return if they are equal
     */
    override fun equals(obj: Any?): Boolean {
        return (obj is AbstractTag) && super.equals(obj)
    }
}