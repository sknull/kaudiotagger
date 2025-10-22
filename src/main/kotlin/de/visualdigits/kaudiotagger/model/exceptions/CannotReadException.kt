package de.visualdigits.kaudiotagger.model.exceptions

class CannotReadException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)