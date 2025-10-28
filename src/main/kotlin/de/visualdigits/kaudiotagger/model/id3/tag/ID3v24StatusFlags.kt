package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import de.visualdigits.kaudiotagger.util.FileConstants
import de.visualdigits.kaudiotagger.util.StatusFlags

class ID3v24StatusFlags: StatusFlags {

    companion object {

        const val TYPE_TAGALTERPRESERVATION: String = "typeTagAlterPreservation"
        const val TYPE_FILEALTERPRESERVATION: String = "typeFileAlterPreservation"
        const val TYPE_READONLY: String = "typeReadOnly"

        /**
         * Discard frame if tag altered
         */
        val MASK_TAG_ALTER_PRESERVATION: Int = FileConstants.BIT6

        /**
         * Discard frame if audio part of file altered
         */
        val MASK_FILE_ALTER_PRESERVATION: Int = FileConstants.BIT5

        /**
         * Frame tagged as read only
         */
        val MASK_READ_ONLY: Int = FileConstants.BIT4
    }

    var id3v24Frame: ID3v24Frame? = null

    /**
     * Use this constructor when convert a v23 frame
     *
     * @param statusFlags
     */
    constructor(id3v24Frame: ID3v24Frame, statusFlags: StatusFlags?): this(id3v24Frame) {
        originalFlags = convertV3ToV4Flags(statusFlags?.originalFlags?:0)
        writeFlags = originalFlags
        modifyFlags()
    }

    constructor(
        id3v24Frame: ID3v24Frame? = null,
        flags: Int = 0
    ): super(flags, flags) {
        this.id3v24Frame = id3v24Frame
        modifyFlags()
    }

    /**
     * Makes modifications to flags based on specification and frameid
     */
    fun modifyFlags() {
        val str = id3v24Frame?.getIdentifier()
        if (ID3v24Frames.isDiscardedIfFileAltered(str)) {
            writeFlags = writeFlags or  MASK_FILE_ALTER_PRESERVATION
            writeFlags = writeFlags and  MASK_TAG_ALTER_PRESERVATION.inv()
        } else {
            writeFlags = writeFlags and MASK_FILE_ALTER_PRESERVATION.inv()
            writeFlags = writeFlags and MASK_TAG_ALTER_PRESERVATION.inv()
        }
    }

    /**
     * Convert V3 Flags to equivalent V4 Flags
     *
     * @param v3Flag
     * @return
     */
    private fun convertV3ToV4Flags(v3Flag: Int): Int {
        var v4Flag = 0
        if ((v3Flag and ID3v23StatusFlags.MASK_FILE_ALTER_PRESERVATION.toInt()) != 0
        ) {
            v4Flag = v4Flag or MASK_FILE_ALTER_PRESERVATION
        }
        if ((v3Flag and ID3v23StatusFlags.MASK_TAG_ALTER_PRESERVATION.toInt()) != 0) {
            v4Flag = v4Flag or MASK_TAG_ALTER_PRESERVATION
        }
        return v4Flag
    }
}