package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.common.frame.AbstractTagFrame
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.ID3v24Frame
import org.slf4j.Logger
import org.slf4j.LoggerFactory

object ID3TextEncodingConversion {

    val log: Logger = LoggerFactory.getLogger(ID3TextEncodingConversion::class.java)

    /**
     * Check the text encoding is valid for this header type and is appropriate for
     * user text encoding options.                                             *
     *
     *
     * This is called before writing any frames that use text encoding
     *
     * @param header       used to identify the ID3tagtype
     * @param textEncoding currently set
     * @return valid encoding according to version type and user options
     */
    fun getTextEncoding(
        header: AbstractTagFrame?,
        textEncoding: Byte
    ): Byte {
        // Should not happen, assume v23 and provide a warning
        when (header) {
            null -> {
                log.warn("Header has not yet been set for this framebody")
                return if (TagOptionSingleton.resetTextEncodingForExistingFrames) {
                    TagOptionSingleton.id3v23DefaultTextEncoding.id
                } else {
                    convertV24textEncodingToV23textEncoding(textEncoding)
                }
            }
            is ID3v24Frame -> {
                return if (TagOptionSingleton.resetTextEncodingForExistingFrames) {
                    // Replace with default
                    TagOptionSingleton.id3v24DefaultTextEncoding.id
                } else {
                    // All text encodings supported nothing to do
                    textEncoding
                }
            }

            else -> {
                return if (TagOptionSingleton.resetTextEncodingForExistingFrames) {
                    // Replace with default
                    TagOptionSingleton.id3v23DefaultTextEncoding.id
                } else {
                    // If text encoding is an unsupported v24 one we use unicode v23 equivalent
                    convertV24textEncodingToV23textEncoding(textEncoding)
                }
            }
        }
    }

    /**
     * Convert v24 text encoding to a valid v23 encoding
     *
     * @param textEncoding
     * @return valid encoding
     */
    private fun convertV24textEncodingToV23textEncoding(
        textEncoding: Byte
    ): Byte {
        // Convert to equivalent UTF16 format
        return if (textEncoding == TextEncoding.UTF_16BE.id) {
            TextEncoding.UTF_16.id
        } else if (textEncoding == TextEncoding.UTF_8.id) {
            TextEncoding.ISO_8859_1.id
        } else {
            textEncoding
        }
    }

    /**
     * Sets the text encoding to best Unicode type for the version
     *
     * @param header
     * @return
     */
    fun getUnicodeTextEncoding(header: AbstractTagFrame?): Byte {
        return when (header) {
            null -> {
                log.warn("Header has not yet been set for this framebody")
                TextEncoding.UTF_16.id
            }
            is ID3v24Frame -> {
                TagOptionSingleton.id3v24UnicodeTextEncoding.id
            }

            else -> {
                TextEncoding.UTF_16.id
            }
        }
    }
}