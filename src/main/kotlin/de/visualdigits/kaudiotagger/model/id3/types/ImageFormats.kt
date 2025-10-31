package de.visualdigits.kaudiotagger.model.id3.types

enum class ImageFormats(
    val id: String,
    val mimeType: String
) {
    JPG("JPG", "image/jpeg"),
    PNG("PNG", "image/png"),
    GIF("GIF", "image/gif"),
    BMP("BMP", "image/bmp"),
    TIF("TIF", "image/tiff"),
    PDF("PDF", "image/pdf"),
    PIC("PIC", "image/x-pict")
    ;

    companion object {

        fun fromId(id: String?): ImageFormats? = entries.find { e -> e.id == id }

        fun fromMimeType(mimeType: String?): ImageFormats? = entries.find { e -> e.mimeType == mimeType }

        fun mimeType(id: String?): String? = fromId(id)?.mimeType

        /**
         * @param data
         * @return correct mimetype for the image data represented by this byte data
         */
        fun mimeTypeFromBinarySignature(data: ByteArray): String? {
            return when {
                binaryDataIsPngFormat(data) -> {
                    "image/png"
                }
                binaryDataIsJpgFormat(data) -> {
                    "image/jpeg"
                }
                binaryDataIsGifFormat(data) -> {
                    "image/gif"
                }
                binaryDataIsBmpFormat(data) -> {
                    "image/bmp"
                }
                binaryDataIsPdfFormat(data) -> {
                    "image/pdf"
                }
                binaryDataIsTiffFormat(data) -> {
                    "image/tiff"
                }
                else -> {
                    null
                }
            }
        }

        /**
         * Is this binary data a bmp image
         *
         * @param data
         * @return true if binary data matches expected header for a bmp
         */
        fun binaryDataIsBmpFormat(data: ByteArray): Boolean {
            if (data.size < 2) {
                return false
            }
            // Read signature
            return (0x42 == (data[0].toInt() and 0xff)) && (0x4d == (data[1].toInt() and 0xff))
        }

        /**
         * Is this binary data a pdf image
         *
         *
         * Details at http:// en.wikipedia.org/wiki/Magic_number_%28programming%29
         *
         * @param data
         * @return true if binary data matches expected header for a pdf
         */
        fun binaryDataIsPdfFormat(data: ByteArray): Boolean {
            if (data.size < 4) {
                return false
            }
            // Read signature
            return ((0x25 == (data[0].toInt() and 0xff)) &&
                    (0x50 == (data[1].toInt() and 0xff)) &&
                    (0x44 == (data[2].toInt() and 0xff)) &&
                    (0x46 == (data[3].toInt() and 0xff))
                    )
        }

        /**
         * is this binary data a tiff image
         *
         *
         * Details at http:// en.wikipedia.org/wiki/Magic_number_%28programming%29
         *
         * @param data
         * @return true if binary data matches expected header for a tiff
         */
        fun binaryDataIsTiffFormat(data: ByteArray): Boolean {
            if (data.size < 4) {
                return false
            }
            // Read signature Intel
            return (((0x49 == (data[0].toInt() and 0xff)) &&
                    (0x49 == (data[1].toInt() and 0xff)) &&
                    (0x2a == (data[2].toInt() and 0xff)) &&
                    (0x00 == (data[3].toInt() and 0xff))) ||
                    ((0x4d == (data[0].toInt() and 0xff)) &&
                            (0x4d == (data[1].toInt() and 0xff)) &&
                            (0x00 == (data[2].toInt() and 0xff)) &&
                            (0x2a == (data[3].toInt() and 0xff)))
                    )
        }

        /**
         * @param data
         * @return true if the image format is a portable format recognised across operating systems
         */
        fun isPortableFormat(data: ByteArray): Boolean {
            return (binaryDataIsPngFormat(data) ||
                    binaryDataIsJpgFormat(data) ||
                    binaryDataIsGifFormat(data)
                    )
        }

        /**
         * Is this binary data a png image
         *
         * @param data
         * @return true if binary data matches expected header for a png
         */
        fun binaryDataIsPngFormat(data: ByteArray): Boolean {
            // Read signature
            if (data.size < 4) {
                return false
            }
            return ((0x89 == (data[0].toInt() and 0xff)) &&
                    (0x50 == (data[1].toInt() and 0xff)) &&
                    (0x4E == (data[2].toInt() and 0xff)) &&
                    (0x47 == (data[3].toInt() and 0xff))
                    )
        }

        /**
         * Is this binary data a jpg image
         *
         * @param data
         * @return true if binary data matches expected header for a jpg
         *
         *
         * Some details http:// www.obrador.com/essentialjpeg/headerinfo.htm
         */
        fun binaryDataIsJpgFormat(data: ByteArray): Boolean {
            if (data.size < 4) {
                return false
            }
            // Read signature
            // Can be Can be FF D8 FF DB (samsung) , FF D8 FF E0 (standard) or FF D8 FF E1 or some other formats
            // see http:// www.garykessler.net/library/file_sigs.html
            // FF D8 is SOI Marker, FFE0 or FFE1 is JFIF Marker
            return ((0xff == (data[0].toInt() and 0xff)) &&
                    (0xd8 == (data[1].toInt() and 0xff)) &&
                    (0xff == (data[2].toInt() and 0xff)) &&
                    (0xdb <= (data[3].toInt() and 0xff))
                    )
        }

        /**
         * Is this binary data a gif image
         *
         * @param data
         * @return true if binary data matches expected header for a gif
         */
        fun binaryDataIsGifFormat(data: ByteArray): Boolean {
            if (data.size < 3) {
                return false
            }
            // Read signature
            return ((0x47 == (data[0].toInt() and 0xff)) &&
                    (0x49 == (data[1].toInt() and 0xff)) &&
                    (0x46 == (data[2].toInt() and 0xff))
                    )
        }
    }
}