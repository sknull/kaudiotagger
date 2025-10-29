package de.visualdigits.kaudiotagger.model.common.types

enum class ChannelType(
    val id: Int,
    val friendlyName: String
): ByteRepresentation {

    OTHER(0x00, "Other"),
    MASTER_VOLUME(0x01, "Master volume"),
    FRONT_RIGHT(0x02, "Front right"),
    FRONT_LEFT(0x03, "Front left"),
    BACK_RIGHT(0x04, "Back right"),
    BACK_LEFT(0x05, "Back left"),
    FRONT_CENTRE(0x06, "Front centre"),
    BACK_CENTRE(0x07, "Back centre"),
    SUBWOOFER(0x08, "Subwoofer"),
    ;

    companion object {

        fun getValueToIdMap(): Map<String, Long> = entries.associate { e -> Pair(e.friendlyName, e.id.toLong()) }

        fun getIdToValueMap(): Map<Long, String> = entries.associate { e -> Pair(e.id.toLong(), e.friendlyName) }
    }

    override fun toByte(): Byte = id.toByte()
}