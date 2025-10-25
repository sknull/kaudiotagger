package de.visualdigits.kaudiotagger.model.exceptions

class InvalidAudioFrameException(
    message: String? = null,
    cause: Throwable? = null
) : Exception(message, cause)