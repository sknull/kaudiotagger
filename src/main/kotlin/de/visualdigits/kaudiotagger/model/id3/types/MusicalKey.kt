package de.visualdigits.kaudiotagger.model.id3.types

object MusicalKey {

    val GROUND_KEYS = listOf(
        "A",
        "B",
        "C",
        "D",
        "E",
        "F",
        "G",
    )

    val HALF_KEYS = listOf(
        "b",
        "#",
        "m"
    )

    val OFF_KEY = "o"

    fun isValid(key: String?): Boolean {
        return (key != null && key.isNotBlank()) &&
                ((key.length == 1 && GROUND_KEYS.contains(key)) || (key.length > 1 && GROUND_KEYS.contains(key.take(1)) && HALF_KEYS.contains(
                    key[1].toString()
                )))
    }
}