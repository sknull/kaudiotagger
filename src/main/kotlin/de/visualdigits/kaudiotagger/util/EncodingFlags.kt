package de.visualdigits.kaudiotagger.util

import org.slf4j.LoggerFactory

open class EncodingFlags(
    var flags: Int = 0
) {

    val log = LoggerFactory.getLogger(javaClass)

    companion object {

        const val TYPE_FLAGS: String = "encodingFlags"
    }

    open fun createStructure() {
    }

    fun resetFlags() {
        flags = 0
    }
}