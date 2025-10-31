package de.visualdigits.kaudiotagger.util

import org.slf4j.Logger
import org.slf4j.LoggerFactory

open class StatusFlags(
    var originalFlags: Int = 0,
    var writeFlags: Int = 0
) {

    val log: Logger = LoggerFactory.getLogger(javaClass)

    companion object {

        const val TYPE_FLAGS: String = "statusFlags"
    }

    open fun createStructure() {
    }
}