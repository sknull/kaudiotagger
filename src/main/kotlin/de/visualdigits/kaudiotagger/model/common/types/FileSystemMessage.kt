package de.visualdigits.kaudiotagger.model.common.types

enum class FileSystemMessage(
    val message: String
) {
    ACCESS_IS_DENIED("Access is denied"),
    PERMISSION_DENIED("Permission denied"); // message from a *nix OS
}