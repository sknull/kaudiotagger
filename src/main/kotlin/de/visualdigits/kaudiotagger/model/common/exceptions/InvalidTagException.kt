package de.visualdigits.kaudiotagger.model.common.exceptions

class InvalidTagException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)