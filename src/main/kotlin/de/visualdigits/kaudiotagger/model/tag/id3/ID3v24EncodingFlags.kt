package de.visualdigits.kaudiotagger.model.tag.id3

import de.visualdigits.kaudiotagger.model.frame.id3.ID3v24Frame
import de.visualdigits.kaudiotagger.util.EncodingFlags
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.FileConstants

class ID3v24EncodingFlags(
    val id3v24Frame: ID3v24Frame? = null,
    flags: Int = 0
): EncodingFlags(flags) {

    companion object {
        const val TYPE_COMPRESSION: String = "compression"
        const val TYPE_ENCRYPTION: String = "encryption"
        const val TYPE_GROUPIDENTITY: String = "groupidentity"
        const val TYPE_FRAMEUNSYNCHRONIZATION: String = "frameUnsynchronisation"
        const val TYPE_DATALENGTHINDICATOR: String = "dataLengthIndicator"

        /**
         * Frame is part of a group
         */
        val MASK_GROUPING_IDENTITY: Int = FileConstants.BIT6

        /**
         * Frame is compressed
         */
        val MASK_COMPRESSION: Int = FileConstants.BIT3

        /**
         * Frame is encrypted
         */
        val MASK_ENCRYPTION: Int = FileConstants.BIT2

        /**
         * Unsynchronisation
         */
        val MASK_FRAME_UNSYNCHRONIZATION: Int = FileConstants.BIT1

        /**
         * Length
         */
        val MASK_DATA_LENGTH_INDICATOR: Int = FileConstants.BIT0
    }


    fun logEnabledFlags() {
        if (isNonStandardFlags()) {
            log.warn(
                "${id3v24Frame?.getIdentifier()}:Unknown Encoding Flags:${flags.toHexString()}"
            )
        }
        if (isCompression()) {
            log.warn(
                ErrorMessage.MP3_FRAME_IS_COMPRESSED.getMsg(
                    id3v24Frame?.getIdentifier()
                )
            )
        }

        if (isEncryption()) {
            log.warn(
                ErrorMessage.MP3_FRAME_IS_ENCRYPTED.getMsg(
                    id3v24Frame?.getIdentifier()
                )
            )
        }

        if (isGrouping()) {
            log.debug(
                ErrorMessage.MP3_FRAME_IS_GROUPED.getMsg(
                    id3v24Frame?.getIdentifier()
                )
            )
        }

        if (isUnsynchronised()) {
            log.debug(
                ErrorMessage.MP3_FRAME_IS_UNSYNCHRONISED.getMsg(
                    id3v24Frame?.getIdentifier()
                )
            )
        }

        if (isDataLengthIndicator()) {
            log.debug(
                ErrorMessage.MP3_FRAME_IS_DATA_LENGTH_INDICATOR.getMsg(
                    id3v24Frame?.getIdentifier()
                )
            )
        }
    }

    fun isCompression(): Boolean {
        return (flags and MASK_COMPRESSION) > 0
    }

    fun isEncryption(): Boolean {
        return (flags and MASK_ENCRYPTION) > 0
    }

    fun isGrouping(): Boolean {
        return (flags and MASK_GROUPING_IDENTITY) > 0
    }

    fun isUnsynchronised(): Boolean {
        return (flags and MASK_FRAME_UNSYNCHRONIZATION) > 0
    }

    fun isDataLengthIndicator(): Boolean {
        return (flags and MASK_DATA_LENGTH_INDICATOR) > 0
    }

    fun isNonStandardFlags(): Boolean {
        return (((flags and FileConstants.BIT7) > 0) ||
                ((flags and FileConstants.BIT5) > 0) ||
                ((flags and FileConstants.BIT4) > 0)
                )
    }

    fun setCompression() {
        flags = flags or  MASK_COMPRESSION
    }

    fun setEncryption() {
        flags = flags or MASK_ENCRYPTION
    }

    fun setGrouping() {
        flags = flags or MASK_GROUPING_IDENTITY
    }

    fun setUnsynchronised() {
        flags = flags or MASK_FRAME_UNSYNCHRONIZATION
    }

    fun setDataLengthIndicator() {
        flags = flags or MASK_DATA_LENGTH_INDICATOR
    }

    fun unsetCompression() {
        flags = flags and MASK_COMPRESSION.inv()
    }

    fun unsetEncryption() {
        flags = flags and MASK_ENCRYPTION.inv()
    }

    fun unsetGrouping() {
        flags = flags and MASK_GROUPING_IDENTITY.inv()
    }

    fun unsetUnsynchronised() {
        flags = flags and MASK_FRAME_UNSYNCHRONIZATION.inv()
    }

    fun unsetDataLengthIndicator() {
        flags = flags and MASK_DATA_LENGTH_INDICATOR.inv()
    }

    fun unsetNonStandardFlags() {
        if (isNonStandardFlags()) {
            log.warn(
                "${id3v24Frame?.getIdentifier()}:Unsetting Unknown Encoding Flags:${flags.toHexString()}"
            )
            flags = flags and FileConstants.BIT7.inv()
            flags = flags and FileConstants.BIT5.inv()
            flags = flags and FileConstants.BIT4.inv()
        }
    }
}