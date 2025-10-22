package de.visualdigits.kaudiotagger.model.kframe

import de.visualdigits.kaudiotagger.model.kfield.KFieldKey

enum class ID3v1KFrame(
    override val id: String,
    override val fieldKey: KFieldKey?,
    override val friendlyName: String,
    override val isCommon: Boolean,
    override val isBinary: Boolean,
    override val isMultipleAllowed: Boolean,
    override val isSupported: Boolean,
    override val isExtension: Boolean,
    override val isDiscardedIfFileAltered: Boolean
) : KFrame<ID3v1KFrame> {

    ARTIST("ARTIST", KFieldKey.ALBUM_ARTIST, "Lead artist(s)/Lead performer(s)/Soloist(s)/Performing group", true, false, false, true, false, false),
    ALBUM("ALBUM", KFieldKey.ALBUM, "Album/Movie/Show title", true, false, false, true, false, false),
    GENRE("GENRE", KFieldKey.GENRE, "Content type", true, false, false, true, false, false),
    TITLE("TITLE", KFieldKey.TITLE, "Title/Songname/Content description", true, false, false, true, false, false),
    YEAR("YEAR", KFieldKey.YEAR, "Year", true, false, false, true, false, false),
    TRACK("TRACK", KFieldKey.TRACK, "Track number/Position in setField", true, false, false, true, false, false),
    COMMENT("COMMENT", KFieldKey.COMMENT, "Comments", true, false, false, true, false, false)
    ;

    companion object {
        fun fromId(id: String): ID3v1KFrame? = entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: KFieldKey): ID3v1KFrame? = entries.find { e -> fieldKey == e.fieldKey }

        fun commonFrames(): List<ID3v1KFrame> = entries.filter { e -> e.isCommon }

        fun binaryFrames(): List<ID3v1KFrame> = entries.filter { e -> e.isBinary }

        fun multipleFrames(): List<ID3v1KFrame> = entries.filter { e -> e.isMultipleAllowed }

        fun supprtedFrames(): List<ID3v1KFrame> = entries.filter { e -> e.isSupported }

        fun extensionFrames(): List<ID3v1KFrame> = entries.filter { e -> e.isExtension }

        fun discardIfFileAltered(): List<ID3v1KFrame> = entries.filter { e -> e.isDiscardedIfFileAltered }

        fun isMultipleAllowed(id: String): Boolean = multipleFrames().any { e -> e.id == id }

        fun isDiscardedIfFileAltered(id: String?): Boolean = discardIfFileAltered().any { e -> e.id == id }
    }
}