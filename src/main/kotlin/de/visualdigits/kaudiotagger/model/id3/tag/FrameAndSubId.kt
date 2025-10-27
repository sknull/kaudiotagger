package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey

/**
 * This class had to be created to minimize the duplicate code in concrete subclasses
 * of this class. It is required in some cases when using the fieldKey enums because enums
 * cannot be sub classed. We want to use enums instead of regular classes because they are
 * much easier for end users to  to use.
 */
class FrameAndSubId(
    val genericKey: GenericFieldKey? = null,
    val frameId: String,
    val subId: String? = null
)