package de.visualdigits.kaudiotagger.model.exceptions

class FieldDataInvalidException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)