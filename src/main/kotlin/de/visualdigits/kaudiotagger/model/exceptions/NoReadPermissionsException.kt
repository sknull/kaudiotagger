package de.visualdigits.kaudiotagger.model.exceptions

class NoReadPermissionsException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)