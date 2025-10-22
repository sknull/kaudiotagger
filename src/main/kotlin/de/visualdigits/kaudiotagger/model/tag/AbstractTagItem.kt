package de.visualdigits.kaudiotagger.model.tag

import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

abstract class AbstractTagItem {

    val log = LoggerFactory.getLogger(javaClass)

    constructor()

    constructor(copyObject: AbstractTagItem?) {
        // no copy constructor in super class
    }

    /**
     * @param byteBuffer file to read from
     */
    abstract fun read(byteBuffer: ByteBuffer)

    /**
     * Return the ID3v2 Frame Identifier, must be implemented by concrete subclasses
     *
     * @return the frame identifier
     */
    abstract fun getIdentifier(): String?

    /**
     * Return size of this item
     *
     * @return size of this item
     */
    abstract fun getSize(): Int

    /**
     * Returns true if this datatype is a subset of the argument. This instance
     * is a subset if it is the same class as the argument.
     *
     * @param obj datatype to determine subset of
     * @return true if this instance and its entire datatype array list is a
     * subset of the argument.
     */
    open fun isSubsetOf(obj: Any?): Boolean {
        return obj is AbstractTagItem
    }

    /**
     * Returns true if this datatype and its body equals the argument and its
     * body. this datatype is equal if and only if they are the same class
     *
     * @param obj datatype to determine equality of
     * @return true if this datatype and its body are equal
     */
    override fun equals(obj: Any?): Boolean {
        if (this == obj) {
            return true
        }
        return obj is AbstractTagItem
    }
}