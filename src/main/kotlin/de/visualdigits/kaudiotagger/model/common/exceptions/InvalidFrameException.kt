package de.visualdigits.kaudiotagger.model.common.exceptions

class InvalidFrameException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)