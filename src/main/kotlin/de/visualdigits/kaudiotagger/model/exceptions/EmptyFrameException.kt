package de.visualdigits.kaudiotagger.model.exceptions

class EmptyFrameException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)