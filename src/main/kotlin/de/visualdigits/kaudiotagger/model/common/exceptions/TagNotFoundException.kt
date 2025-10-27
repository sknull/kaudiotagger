package de.visualdigits.kaudiotagger.model.common.exceptions

class TagNotFoundException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)