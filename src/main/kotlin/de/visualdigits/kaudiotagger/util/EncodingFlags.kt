package de.visualdigits.kaudiotagger.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class EncodingFlags(
    var flags: Int = 0
) {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    companion object {

        const val TYPE_FLAGS: String = "encodingFlags"
    }

    fun resetFlags() {
        flags = 0
    }
}