package de.visualdigits.kaudiotagger.model.exceptions

class UnableToCreateFileException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)