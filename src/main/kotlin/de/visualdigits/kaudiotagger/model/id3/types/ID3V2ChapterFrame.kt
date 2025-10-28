package de.visualdigits.kaudiotagger.model.id3.types

import de.visualdigits.kaudiotagger.model.common.types.FieldKey
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey

enum class ID3V2ChapterFrame(
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
) : Frame {
    CHAPTER("CHAP", null, null, "Chapter", false, false, false, true, false, false),
    TABLE_OF_CONTENT("CTOC", null, null, "Table of contents", false, false, false, true, false, false)
    ;

    companion object {

        fun contains(id: String): Boolean = ID3V22Frame.entries.any { e -> id == e.id }

        fun fromId(id: String): ID3V2ChapterFrame? = entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: GenericFieldKey): ID3V2ChapterFrame? = entries.find { e -> fieldKey == e.fieldKey }

        fun commonFrames(): List<ID3V2ChapterFrame> = entries.filter { e -> e.isCommon }

        fun binaryFrames(): List<ID3V2ChapterFrame> = entries.filter { e -> e.isBinary }

        fun multipleFrames(): List<ID3V2ChapterFrame> = entries.filter { e -> e.isMultipleAllowed }

        fun supprtedFrames(): List<ID3V2ChapterFrame> = entries.filter { e -> e.isSupported }

        fun extensionFrames(): List<ID3V2ChapterFrame> = entries.filter { e -> e.isExtension }

        fun discardIfFileAltered(): List<ID3V2ChapterFrame> = entries.filter { e -> e.isDiscardedIfFileAltered }

        fun isSupported(id: String?): Boolean = ID3V22Frame.supprtedFrames().any { e -> e.id == id }

        fun isExtension(id: String?): Boolean = ID3V22Frame.extensionFrames().any { e -> e.id == id }

        fun isCommon(id: String?): Boolean = ID3V22Frame.commonFrames().any { e -> e.id == id }

        fun isBinary(id: String?): Boolean = ID3V22Frame.binaryFrames().any { e -> e.id == id }

        fun isMultipleAllowed(id: String?): Boolean = multipleFrames().any { e -> e.id == id }
    }
}