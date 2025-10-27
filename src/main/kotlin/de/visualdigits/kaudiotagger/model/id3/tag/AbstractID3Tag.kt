package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.common.tag.AbstractTag

abstract class AbstractID3Tag: AbstractTag {

    companion object {

        const val TAG_RELEASE: String = "ID3v"
    }

    constructor()

    constructor(copyObject: AbstractID3Tag): super(copyObject)

    /**
     * Get full version
     */
    override fun getIdentifier(): String? {
        return ("$TAG_RELEASE${getRelease()}.${getMajorVersion()}.${getRevision()}")
    }

    /**
     * Retrieve the Release
     *
     * @return
     */
    abstract fun getRelease(): Int

    /**
     * Retrieve the Major Version
     *
     * @return
     */
    abstract fun getMajorVersion(): Int

    /**
     * Retrieve the Revision
     *
     * @return
     */
    abstract fun getRevision(): Int
}