package de.visualdigits.kaudiotagger.model.exceptions

class InvalidFrameIdentifierException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)