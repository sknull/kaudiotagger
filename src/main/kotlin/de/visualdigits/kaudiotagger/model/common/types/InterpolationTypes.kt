package de.visualdigits.kaudiotagger.model.common.types

enum class InterpolationTypes(
    val id: Int,
    val friendlyName: String
): ByteRepresentation {

    BAND(0, "Band"),
    LINEAR(1, "Linear"),
    ;

    companion object {

        fun getValueToIdMap(): Map<String, Long> = entries.associate { e -> Pair(e.friendlyName, e.id.toLong()) }

        fun getIdToValueMap(): Map<Long, String> = entries.associate { e -> Pair(e.id.toLong(), e.friendlyName) }

    }

    override fun toByte(): Byte = id.toByte()
}