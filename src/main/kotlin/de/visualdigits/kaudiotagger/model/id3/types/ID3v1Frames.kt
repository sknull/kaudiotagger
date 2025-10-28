package de.visualdigits.kaudiotagger.model.id3.types

import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey

enum class ID3v1Frames(
    override val id: String,
    override val genericFieldKey: GenericFieldKey?,
    override val fieldKey: ID3v1FieldKey?,
    override val friendlyName: String,
    override val isCommon: Boolean,
    override val isBinary: Boolean,
    override val isMultipleAllowed: Boolean,
    override val isSupported: Boolean,
    override val isExtension: Boolean,
    override val isDiscardedIfFileAltered: Boolean
) : Frames {

    ARTIST("ARTIST", GenericFieldKey.ARTIST, ID3v1FieldKey.ARTIST, "Lead artist(s)/Lead performer(s)/Soloist(s)/Performing group", true, false, false, true, false, false),
    ALBUM("ALBUM", GenericFieldKey.ALBUM, ID3v1FieldKey.ALBUM, "Album/Movie/Show title", true, false, false, true, false, false),
    GENRE("GENRE", GenericFieldKey.GENRE, ID3v1FieldKey.GENRE, "Content type", true, false, false, true, false, false),
    TITLE("TITLE", GenericFieldKey.TITLE, ID3v1FieldKey.TITLE, "Title/Songname/Content description", true, false, false, true, false, false),
    YEAR("YEAR", GenericFieldKey.YEAR, ID3v1FieldKey.YEAR, "Year", true, false, false, true, false, false),
    TRACK("TRACK", GenericFieldKey.TRACK, ID3v1FieldKey.TRACK, "Track number/Position in setField", true, false, false, true, false, false),
    COMMENT("COMMENT", GenericFieldKey.COMMENT, ID3v1FieldKey.COMMENT, "Comments", true, false, false, true, false, false)
    ;

    companion object {
        fun fromId(id: String): ID3v1Frames? = entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: GenericFieldKey): ID3v1Frames? = entries.find { e -> fieldKey == e.genericFieldKey }

        fun commonFrames(): List<ID3v1Frames> = entries.filter { e -> e.isCommon }

        fun binaryFrames(): List<ID3v1Frames> = entries.filter { e -> e.isBinary }

        fun multipleFrames(): List<ID3v1Frames> = entries.filter { e -> e.isMultipleAllowed }

        fun supprtedFrames(): List<ID3v1Frames> = entries.filter { e -> e.isSupported }

        fun extensionFrames(): List<ID3v1Frames> = entries.filter { e -> e.isExtension }

        fun discardIfFileAltered(): List<ID3v1Frames> = entries.filter { e -> e.isDiscardedIfFileAltered }

        fun isMultipleAllowed(id: String): Boolean = multipleFrames().any { e -> e.id == id }

        fun isDiscardedIfFileAltered(id: String?): Boolean = discardIfFileAltered().any { e -> e.id == id }
    }
}