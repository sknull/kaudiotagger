package de.visualdigits.kaudiotagger.model.common.exceptions

class TagException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)