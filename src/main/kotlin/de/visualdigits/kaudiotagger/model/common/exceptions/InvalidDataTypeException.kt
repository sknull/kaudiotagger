package de.visualdigits.kaudiotagger.model.common.exceptions

class InvalidDataTypeException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)