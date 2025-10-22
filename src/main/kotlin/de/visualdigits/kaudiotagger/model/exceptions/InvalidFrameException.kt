package de.visualdigits.kaudiotagger.model.exceptions

class InvalidFrameException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)