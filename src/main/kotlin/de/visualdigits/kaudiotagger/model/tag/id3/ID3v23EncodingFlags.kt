package de.visualdigits.kaudiotagger.model.tag.id3

import de.visualdigits.kaudiotagger.model.frame.id3.ID3v23Frame
import de.visualdigits.kaudiotagger.util.EncodingFlags
import de.visualdigits.kaudiotagger.util.FileConstants
import kotlin.experimental.and
import kotlin.experimental.inv
import kotlin.experimental.or

class ID3v23EncodingFlags(
    val id3v23Frame: ID3v23Frame? = null,
    flags: Int = 0
): EncodingFlags(flags) {

    companion object {
        const val TYPE_COMPRESSION: String = "compression"
        const val TYPE_ENCRYPTION: String = "encryption"
        const val TYPE_GROUPIDENTITY: String = "groupidentity"

        /**
         * Frame is compressed
         */
        val MASK_COMPRESSION: Int = FileConstants.BIT7

        /**
         * Frame is encrypted
         */
        val MASK_ENCRYPTION: Int = FileConstants.BIT6

        /**
         * Frame is part of a group
         */
        val MASK_GROUPING_IDENTITY: Int = FileConstants.BIT5
    }

    fun logEnabledFlags() {
        if (isNonStandardFlags()) {
            log.warn(
                "${id3v23Frame?.getIdentifier()}:Unknown Encoding Flags:${flags.toHexString()}"
            )
        }
        if (isCompression()) {
            log.warn("${id3v23Frame?.getIdentifier()} is compressed")
        }

        if (isEncryption()) {
            log.warn("${id3v23Frame?.getIdentifier()} is encrypted")
        }

        if (isGrouping()) {
            log.warn("${id3v23Frame?.getIdentifier()} is grouped")
        }
    }

    fun isNonStandardFlags(): Boolean {
        return (((flags and FileConstants.BIT4) > 0) ||
                ((flags and FileConstants.BIT3) > 0) ||
                ((flags and FileConstants.BIT2) > 0) ||
                ((flags and FileConstants.BIT1) > 0) ||
                ((flags and FileConstants.BIT0) > 0)
                )
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

    fun setCompression() {
        flags = flags or MASK_COMPRESSION
    }

    fun setEncryption() {
        flags = flags or MASK_ENCRYPTION
    }

    fun setGrouping() {
        flags = flags or  MASK_GROUPING_IDENTITY
    }

    fun unsetCompression() {
        flags = flags and  MASK_COMPRESSION.inv()
    }

    fun unsetEncryption() {
        flags = flags and MASK_ENCRYPTION.inv()
    }

    fun unsetGrouping() {
        flags = flags and MASK_GROUPING_IDENTITY.inv()
    }

    fun unsetNonStandardFlags() {
        if (isNonStandardFlags()) {
            log.warn(
                "${id3v23Frame?.getIdentifier()}:Unsetting Unknown Encoding Flags:${flags.toHexString()}"
            )
            flags = flags and FileConstants.BIT4.inv()
            flags = flags and FileConstants.BIT3.inv()
            flags = flags and FileConstants.BIT2.inv()
            flags = flags and FileConstants.BIT1.inv()
            flags = flags and FileConstants.BIT0.inv()
        }
    }
}