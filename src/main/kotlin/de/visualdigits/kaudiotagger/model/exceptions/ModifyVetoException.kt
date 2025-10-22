package de.visualdigits.kaudiotagger.model.exceptions

class ModifyVetoException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)