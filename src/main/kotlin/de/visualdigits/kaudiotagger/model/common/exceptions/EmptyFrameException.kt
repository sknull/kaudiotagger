package de.visualdigits.kaudiotagger.model.common.exceptions

class EmptyFrameException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)