package de.visualdigits.kaudiotagger.model.frame

import de.visualdigits.kaudiotagger.model.field.KFieldKey

enum class ID3v2ChapterKFrame(
    override val id: String,
    override val fieldKey: KFieldKey?,
    override val friendlyName: String,
    override val isCommon: Boolean,
    override val isBinary: Boolean,
    override val isMultipleAllowed: Boolean,
    override val isSupported: Boolean,
    override val isExtension: Boolean,
    override val shouldBeDiscardedOnChange: Boolean
) : KFrame<ID3v2ChapterKFrame> {
    CHAPTER("CHAP", null, "Chapter", false, false, false, true, false, false),
    TABLE_OF_CONTENT("CTOC", null, "Table of contents", false, false, false, true, false, false)
    ;

    companion object {
        fun fromId(id: String): ID3v2ChapterKFrame? = ID3v2ChapterKFrame.entries.find { e -> id == e.id }

        fun fromFieldKey(fieldKey: KFieldKey): ID3v2ChapterKFrame? = ID3v2ChapterKFrame.entries.find { e -> fieldKey == e.fieldKey }
    }

    override fun commonFrames(): List<ID3v2ChapterKFrame> = ID3v2ChapterKFrame.entries.filter { e -> e.isCommon }

    override fun binaryFrames(): List<ID3v2ChapterKFrame> = ID3v2ChapterKFrame.entries.filter { e -> e.isBinary }

    override fun multipleFrames(): List<ID3v2ChapterKFrame> = ID3v2ChapterKFrame.entries.filter { e -> e.isMultipleAllowed }

    override fun supprtedFrames(): List<ID3v2ChapterKFrame> = ID3v2ChapterKFrame.entries.filter { e -> e.isSupported }

    override fun extensionFrames(): List<ID3v2ChapterKFrame> = ID3v2ChapterKFrame.entries.filter { e -> e.isExtension }

    override fun discardIfFileAltered(): List<ID3v2ChapterKFrame> = ID3v2ChapterKFrame.entries.filter { e -> e.shouldBeDiscardedOnChange }
}