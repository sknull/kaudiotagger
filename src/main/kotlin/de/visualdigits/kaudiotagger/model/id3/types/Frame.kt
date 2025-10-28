package de.visualdigits.kaudiotagger.model.id3.types

import de.visualdigits.kaudiotagger.model.common.types.FieldKey

interface Frame {

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