package de.visualdigits.kaudiotagger.util

import org.slf4j.LoggerFactory

open class StatusFlags(
    var originalFlags: Int = 0,
    var writeFlags: Int = 0
) {

    val log = LoggerFactory.getLogger(javaClass)

    companion object {

        const val TYPE_FLAGS: String = "statusFlags"
    }

    open fun createStructure() {
    }

    override fun equals(obj: Any?): Boolean {
        if (this === obj) {
            return true
        }

        if (obj !is StatusFlags) {
            return false
        }

        return (EqualsUtil.areEqual(originalFlags, obj.originalFlags) &&
                EqualsUtil.areEqual(writeFlags, obj.writeFlags)
                )
    }
}