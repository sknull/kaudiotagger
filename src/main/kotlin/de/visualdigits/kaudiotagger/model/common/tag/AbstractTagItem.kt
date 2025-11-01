package de.visualdigits.kaudiotagger.model.common.tag

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.ByteBuffer

abstract class AbstractTagItem {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    constructor()

    constructor(copyObject: AbstractTagItem?) {
        // no copy constructor in super class
    }

    /**
     * ID string that usually corresponds to the class name, but can be
     * displayed to the user. It is not indended to identify each individual
     * instance.
     *
     * @return ID string
     */
    abstract fun getIdentifier(): String?

    /**
     * Return size of this item
     *
     * @return size of this item
     */
    abstract fun getSize(): Int

    abstract fun read(byteBuffer: ByteBuffer?): Boolean

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
}