package de.visualdigits.kaudiotagger.model.exceptions

class UnableToModifyFileException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)