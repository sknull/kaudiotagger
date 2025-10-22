package de.visualdigits.kaudiotagger.model.kframe

import de.visualdigits.kaudiotagger.model.kfield.KFieldKey

interface KFrame<T : Enum<T>> {

    val id: String
    val fieldKey: KFieldKey?
    val friendlyName: String
    val isCommon: Boolean
    val isBinary: Boolean
    val isMultipleAllowed: Boolean
    val isSupported: Boolean
    val isExtension: Boolean
    val isDiscardedIfFileAltered: Boolean
}