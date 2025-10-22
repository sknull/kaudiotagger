package de.visualdigits.kaudiotagger.model.exceptions

class InvalidTagException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)