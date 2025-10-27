package de.visualdigits.kaudiotagger.model.common.exceptions

class InvalidAudioFrameException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)