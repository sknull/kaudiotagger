package de.visualdigits.kaudiotagger.model.exceptions

class KeyNotFoundException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)