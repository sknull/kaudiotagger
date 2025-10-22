package de.visualdigits.kaudiotagger.model.exceptions

class TagException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)