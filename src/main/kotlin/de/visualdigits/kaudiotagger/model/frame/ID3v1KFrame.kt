package de.visualdigits.kaudiotagger.model.frame

import de.visualdigits.kaudiotagger.model.field.KFieldKey

enum class ID3v1KFrame(
    override val id: String,
    override val fieldKey: KFieldKey?,
    override val friendlyName: String,
    override val isCommon: Boolean,
    override val isBinary: Boolean,
    override val isMultipleAllowed: Boolean,
    override val isSupported: Boolean,
    override val isExtension: Boolean,
    override val shouldBeDiscardedOnChange: Boolean
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
        fun fromId(id: String): ID3v1KFrame? = ID3v1KFrame.entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: KFieldKey): ID3v1KFrame? = ID3v1KFrame.entries.find { e -> fieldKey == e.fieldKey }
    }

    override fun commonFrames(): List<ID3v1KFrame> = ID3v1KFrame.entries.filter { e -> e.isCommon }

    override fun binaryFrames(): List<ID3v1KFrame> = ID3v1KFrame.entries.filter { e -> e.isBinary }

    override fun multipleFrames(): List<ID3v1KFrame> = ID3v1KFrame.entries.filter { e -> e.isMultipleAllowed }

    override fun supprtedFrames(): List<ID3v1KFrame> = ID3v1KFrame.entries.filter { e -> e.isSupported }

    override fun extensionFrames(): List<ID3v1KFrame> = ID3v1KFrame.entries.filter { e -> e.isExtension }

    override fun discardIfFileAltered(): List<ID3v1KFrame> = ID3v1KFrame.entries.filter { e -> e.shouldBeDiscardedOnChange }
}