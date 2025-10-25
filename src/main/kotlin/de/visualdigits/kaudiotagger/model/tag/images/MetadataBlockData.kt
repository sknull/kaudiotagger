package de.visualdigits.kaudiotagger.model.tag.images

import java.nio.ByteBuffer

/**
 * This defines the interface required of the different metadata block types
 */
interface MetadataBlockData {

    /**
     * @return the rawdata as it will be written to file
     */
    fun getBytes(): ByteBuffer

    /**
     * @return the length in bytes that the data uses when written to file
     */
    fun length(): Int
}
