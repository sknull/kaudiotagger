package de.visualdigits.kaudiotagger.model.datatype.types

enum class EventTimingTypes(
    val id: Int,
    val friendlyName: String
) {

    PADDING_HAS_NO_MEANING(0x00, "Padding (has no meaning)"),
    END_OF_INITIAL_SILENCE(0x01, "End of initial silence"),
    INTRO_START(0x02, "Intro start"),
    MAIN_PART_START(0x03, "Main part start"),
    OUTRO_START(0x04, "Outro start"),
    OUTRO_END(0x05, "Outro end"),
    VERSE_START(0x06, "Verse start"),
    REFRAIN_START(0x07, "Refrain start"),
    INTERLUDE_START(0x08, "Interlude start"),
    THEME_START(0x09, "Theme start"),
    VARIATION_START(0x0A, "Variation start"),
    KEY_CHANGE(0x0B, "Key change"),
    TIME_CHANGE(0x0C, "Time change"),
    MOMENTARY_UNWANTED_NOISE_SNAP_CRACKLE_AND_POP(0x0D, "Momentary unwanted noise (Snap, Crackle & Pop)"),
    SUSTAINED_NOISE(0x0E, "Sustained noise"),
    SUSTAINED_NOISE_END(0x0F, "Sustained noise end"),
    INTRO_END(0x10, "Intro end"),
    MAIN_PART_END(0x11, "Main part end"),
    VERSE_END(0x12, "Verse end"),
    REFRAIN_END(0x13, "Refrain end"),
    THEME_END(0x14, "Theme end"),
    PROFANITY(0x15, "Profanity"),
    PROFANITY_END(0x16, "Profanity end"),

    // 0x17-0xDF  reserved for future use

    NOT_PREDEFINED_SYNCH_0(0xE0, "Not predefined synch 0"),
    NOT_PREDEFINED_SYNCH_1(0xE1, "Not predefined synch 1"),
    NOT_PREDEFINED_SYNCH_2(0xE2, "Not predefined synch 2"),
    NOT_PREDEFINED_SYNCH_3(0xE3, "Not predefined synch 3"),
    NOT_PREDEFINED_SYNCH_4(0xE4, "Not predefined synch 4"),
    NOT_PREDEFINED_SYNCH_5(0xE5, "Not predefined synch 5"),
    NOT_PREDEFINED_SYNCH_6(0xE6, "Not predefined synch 6"),
    NOT_PREDEFINED_SYNCH_7(0xE7, "Not predefined synch 7"),
    NOT_PREDEFINED_SYNCH_8(0xE8, "Not predefined synch 8"),
    NOT_PREDEFINED_SYNCH_9(0xE9, "Not predefined synch 9"),
    NOT_PREDEFINED_SYNCH_A(0xEA, "Not predefined synch A"),
    NOT_PREDEFINED_SYNCH_B(0xEB, "Not predefined synch B"),
    NOT_PREDEFINED_SYNCH_C(0xEC, "Not predefined synch C"),
    NOT_PREDEFINED_SYNCH_D(0xED, "Not predefined synch D"),
    NOT_PREDEFINED_SYNCH_E(0xEE, "Not predefined synch E"),
    NOT_PREDEFINED_SYNCH_F(0xEF, "Not predefined synch F"),

    // 0xF0-0xFC  reserved for future use

    AUDIO_END_START_OF_SILENCE(0xFD, "Audio end (start of silence)"),
    AUDIO_FILE_ENDS(0xFE, "Audio file ends"),
    ;

    companion object {

        fun fromId(id: Int): EventTimingTypes? = entries.find { e -> id == e.id }

        fun getValueToIdMap(): Map<String, Int> = entries.associate { e -> Pair(e.friendlyName, e.id) }

        fun getIdToValueMap(): Map<Int, String> = entries.associate { e -> Pair(e.id, e.friendlyName) }

    }
}