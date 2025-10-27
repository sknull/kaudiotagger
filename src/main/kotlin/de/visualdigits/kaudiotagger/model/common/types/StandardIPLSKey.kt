package de.visualdigits.kaudiotagger.model.common.types

enum class StandardIPLSKey(
    val key: String
) {
    ENGINEER("engineer"),
    MIXER("mix"),
    DJMIXER("DJ-mix"),
    PRODUCER("producer"),
    ARRANGER("arranger")
    ;

    companion object {

        fun isKey(key: String): Boolean {
            return entries.any { e -> key == e.key }
        }

        fun fromKey(key: String): StandardIPLSKey? {
            return entries.find { e -> key == e.key }
        }

    }
}