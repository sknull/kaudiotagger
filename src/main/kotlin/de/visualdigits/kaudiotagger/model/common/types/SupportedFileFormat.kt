package de.visualdigits.kaudiotagger.model.common.types

/**
 * Files formats currently supported by Library.
 * Each enum value is associated with a file suffix (extension).
 */
enum class SupportedFileFormat(filesuffix: String) {

//    OGG("ogg"),
    MP3("mp3"),
//    FLAC("flac"),
//    MP4("mp4"),
//    M4A("m4a"),
//    M4P("m4p"),
//    WMA("wma"),
//    WAV("wav"),
//    RA("ra"),
//    RM("rm"),
//    M4B("m4b"),
//    AIF("aif"),
//    AIFF("aiff"),
//    AIFC("aifc"),
//    DSF("dsf"),
//    OPUS("opus"),
    UNKNOWN("unknown");

    /**
     * Returns the file suffix (lower case without initial .) associated with the format.
     */
    val filesuffix: String = filesuffix.lowercase() // ensure lowercase

    companion object {
        val extensionMap: MutableMap<String?, SupportedFileFormat?>

        init {
            val values = entries.toTypedArray()
            extensionMap = HashMap<String?, SupportedFileFormat?>(values.size)
            for (i in values.indices) {
                val format = values[i]
                extensionMap.put(format.filesuffix, format)
            }
        }

        /**
         * Get the format from the file extension.
         *
         * @param fileExtension file extension
         * @return the format for the extension or UNKNOWN if the extension not recognized
         */
        fun fromExtension(fileExtension: String?): SupportedFileFormat {
            if (fileExtension == null) {
                return UNKNOWN
            }
            val format: SupportedFileFormat? = extensionMap[fileExtension.lowercase()]
            return if (format == null) UNKNOWN else format
        }
    }
}
