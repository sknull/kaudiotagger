package de.visualdigits.kaudiotagger.model.exceptions

class NoWritePermissionsException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)