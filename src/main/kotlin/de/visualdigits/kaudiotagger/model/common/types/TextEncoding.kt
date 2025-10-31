package de.visualdigits.kaudiotagger.model.common.types

import java.nio.charset.Charset
import java.nio.charset.StandardCharsets

enum class TextEncoding(
    val id: Byte,
    val charSet: Charset
): ByteRepresentation {

    // Supported ID3 charset ids
    ISO_8859_1(0, StandardCharsets.ISO_8859_1),
    UTF_16(1, StandardCharsets.UTF_16), // We use UTF-16 with LE byte-ordering and byte
                                                     // order mark by default also use BOM with BE byte ordering.
    UTF_16BE(2, StandardCharsets.UTF_16BE),
    UTF_8(3, StandardCharsets.UTF_8),
    US_ASCII(-1, StandardCharsets.US_ASCII)
    ;

    companion object {

        /**
         * The number of bytes used to hold the text encoding field size.
         */
        const val TEXT_ENCODING_FIELD_SIZE: Int = 1

        fun fromId(id: Int?): TextEncoding? = fromId(id?.toByte())

        fun fromId(id: Byte?): TextEncoding? = entries.find { e -> id == e.id }

        fun fromCharset(charSet: Charset): TextEncoding? = entries.find { e -> charSet == e.charSet }

        fun getValueToIdMap(): Map<String, Long> = entries.associate { e -> Pair(e.charSet.name(), e.id.toLong()) }

        fun getIdToValueMap(): Map<Long, String> = entries.associate { e -> Pair(e.id.toLong(), e.charSet.name()) }
    }


    var value: String? = null

    override fun toByte(): Byte = id.toByte()
}