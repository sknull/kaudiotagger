package de.visualdigits.kaudiotagger.model.exceptions

class TagNotFoundException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)