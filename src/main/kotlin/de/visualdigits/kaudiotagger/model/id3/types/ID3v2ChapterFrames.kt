package de.visualdigits.kaudiotagger.model.id3.types

import de.visualdigits.kaudiotagger.model.common.types.FieldKey
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey

enum class ID3v2ChapterFrames(
    override val id: String,
    override val genericFieldKey: GenericFieldKey?,
    override val fieldKey: FieldKey?,
    override val friendlyName: String,
    override val isCommon: Boolean,
    override val isBinary: Boolean,
    override val isMultipleAllowed: Boolean,
    override val isSupported: Boolean,
    override val isExtension: Boolean,
    override val isDiscardedIfFileAltered: Boolean
) : Frames {
    CHAPTER("CHAP", null, null, "Chapter", false, false, false, true, false, false),
    TABLE_OF_CONTENT("CTOC", null, null, "Table of contents", false, false, false, true, false, false)
    ;

    companion object {

        fun contains(id: String): Boolean = ID3v22Frames.entries.any { e -> id == e.id }

        fun fromId(id: String): ID3v2ChapterFrames? = entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: GenericFieldKey): ID3v2ChapterFrames? = entries.find { e -> fieldKey == e.fieldKey }

        fun commonFrames(): List<ID3v2ChapterFrames> = entries.filter { e -> e.isCommon }

        fun binaryFrames(): List<ID3v2ChapterFrames> = entries.filter { e -> e.isBinary }

        fun multipleFrames(): List<ID3v2ChapterFrames> = entries.filter { e -> e.isMultipleAllowed }

        fun supprtedFrames(): List<ID3v2ChapterFrames> = entries.filter { e -> e.isSupported }

        fun extensionFrames(): List<ID3v2ChapterFrames> = entries.filter { e -> e.isExtension }

        fun discardIfFileAltered(): List<ID3v2ChapterFrames> = entries.filter { e -> e.isDiscardedIfFileAltered }

        fun isSupported(id: String?): Boolean = ID3v22Frames.supprtedFrames().any { e -> e.id == id }

        fun isExtension(id: String?): Boolean = ID3v22Frames.extensionFrames().any { e -> e.id == id }

        fun isCommon(id: String?): Boolean = ID3v22Frames.commonFrames().any { e -> e.id == id }

        fun isBinary(id: String?): Boolean = ID3v22Frames.binaryFrames().any { e -> e.id == id }

        fun isMultipleAllowed(id: String?): Boolean = multipleFrames().any { e -> e.id == id }
    }
}