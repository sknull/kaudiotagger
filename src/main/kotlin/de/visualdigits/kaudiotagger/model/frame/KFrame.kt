package de.visualdigits.kaudiotagger.model.frame

import de.visualdigits.kaudiotagger.model.field.KFieldKey

interface KFrame<T : Enum<T>> {

    val id: String
    val fieldKey: KFieldKey?
    val friendlyName: String
    val isCommon: Boolean
    val isBinary: Boolean
    val isMultipleAllowed: Boolean
    val isSupported: Boolean
    val isExtension: Boolean
    val shouldBeDiscardedOnChange: Boolean

    fun commonFrames(): List<T>

    fun binaryFrames(): List<T>

    fun multipleFrames(): List<T>

    fun supprtedFrames(): List<T>

    fun extensionFrames(): List<T>

    fun discardIfFileAltered(): List<T>

    fun flags(): List<Boolean> = listOf(isCommon, isBinary, isMultipleAllowed, isSupported, isExtension, shouldBeDiscardedOnChange)
}