package de.visualdigits.kaudiotagger.model.common.types

interface Frames {

    val id: String
    val genericFieldKey: FieldKey?
    val fieldKey: FieldKey?
    val friendlyName: String
    val isCommon: Boolean
    val isBinary: Boolean
    val isMultipleAllowed: Boolean
    val isSupported: Boolean
    val isExtension: Boolean
    val isDiscardedIfFileAltered: Boolean
}