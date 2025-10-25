package de.visualdigits.kaudiotagger.model.datatype.types

import de.visualdigits.kaudiotagger.model.tag.Tag
import de.visualdigits.kaudiotagger.util.TagOptionSingleton

/**
 * Files formats currently supported by Library.
 * Each enum value is associated with a file suffix (extension).
 */
enum class SupportedFileFormat(filesuffix: String) {

//    OGG("ogg") {
//        override fun createDefaultTag(): Tag {
//            return VorbisCommentTag.createNewTag()
//        }
//    },
    MP3("mp3") {
        override fun createDefaultTag(): Tag {
            return TagOptionSingleton.createDefaultID3Tag()
        }
    },
//    FLAC("flac") {
//        override fun createDefaultTag(): Tag {
//            return FlacTag(VorbisCommentTag.createNewTag(), ArrayList<MetadataBlockDataPicture?>())
//        }
//    },
//    MP4("mp4") {
//        override fun createDefaultTag(): Tag {
//            return Mp4Tag()
//        }
//    },
//    M4A("m4a") {
//        override fun createDefaultTag(): Tag {
//            return Mp4Tag()
//        }
//    },
//    M4P("m4p") {
//        override fun createDefaultTag(): Tag {
//            return Mp4Tag()
//        }
//    },
//    WMA("wma") {
//        override fun createDefaultTag(): Tag {
//            return AsfTag()
//        }
//    },
//    WAV("wav") {
//        override fun createDefaultTag(): Tag {
//            return WavTag(TagOptionSingleton.getInstance().getWavOptions())
//        }
//    },
//    RA("ra") {
//        override fun createDefaultTag(): Tag {
//            return RealTag()
//        }
//    },
//    RM("rm") {
//        override fun createDefaultTag(): Tag {
//            return RealTag()
//        }
//    },
//    M4B("m4b") {
//        override fun createDefaultTag(): Tag {
//            return Mp4Tag()
//        }
//    },
//    AIF("aif") {
//        override fun createDefaultTag(): Tag {
//            return AiffTag()
//        }
//    },
//    AIFF("aiff") {
//        override fun createDefaultTag(): Tag {
//            return AiffTag()
//        }
//    },
//    AIFC("aifc") {
//        override fun createDefaultTag(): Tag {
//            return AiffTag()
//        }
//    },
//    DSF("dsf") {
//        override fun createDefaultTag(): Tag {
//            return Dsf.createDefaultTag()
//        }
//    },
//    OPUS("opus") {
//        override fun createDefaultTag(): Tag {
//            return VorbisCommentTag.createNewTag()
//        }
//    },
    UNKNOWN("") {
        override fun createDefaultTag(): Tag? {
            throw RuntimeException(
                "Unable to create default tag for this file format:" + name
            )
        }
    };

    /**
     * Returns the file suffix (lower case without initial .) associated with the format.
     */
    val filesuffix: String

    /**
     * Constructor for internal use by this enum.
     */
    init {
        this.filesuffix = filesuffix.lowercase() // ensure lowercase
    }

    /**
     * Create for this format
     *
     * @return the default tag for the given type
     * @throws RuntimeException if can't create the default tag
     */
    // TODO: 1/7/17 should we have a more specific type of runtime exception? RuntimeException is copied from legacy
    abstract fun createDefaultTag(): Tag?

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
                return SupportedFileFormat.UNKNOWN
            }
            val format: SupportedFileFormat? = extensionMap.get(
                fileExtension.lowercase()
            )
            return if (format == null) SupportedFileFormat.UNKNOWN else format
        }
    }
}
