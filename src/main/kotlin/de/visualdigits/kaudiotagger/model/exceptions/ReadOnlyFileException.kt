package de.visualdigits.kaudiotagger.model.exceptions

class ReadOnlyFileException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)