package de.visualdigits.kaudiotagger.model.exceptions

class UnableToRenameFileException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)