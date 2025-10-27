package de.visualdigits.kaudiotagger.model.common.exceptions

class InvalidFrameIdentifierException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)