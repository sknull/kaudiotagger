package de.visualdigits.kaudiotagger.model.common.types

enum class InterpolationTypes(
    val id: Int,
    val friendlyName: String
) {

    BAND(0, "Band"),
    LINEAR(1, "Linear"),
    ;

    companion object {

        fun getValueToIdMap(): Map<String, Int> = entries.associate { e -> Pair(e.friendlyName, e.id) }

        fun getIdToValueMap(): Map<Int, String> = entries.associate { e -> Pair(e.id, e.friendlyName) }

    }
}