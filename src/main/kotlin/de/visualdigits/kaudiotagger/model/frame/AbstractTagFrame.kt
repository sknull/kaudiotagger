package de.visualdigits.kaudiotagger.model.frame

import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.tag.AbstractTagItem
import de.visualdigits.kaudiotagger.util.ID3Tags
import java.nio.ByteBuffer

abstract class AbstractTagFrame: AbstractTagItem {

    var frameBody: AbstractTagFrameBody? = null

    constructor()

    constructor(
        identifier: String? = null,
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(identifier, byteBuffer, frameSize)

    /**
     * This constructs the bodies copy constructor this in turn invokes
     * * bodies objectlist.
     *
     * @param copyObject
     */
    constructor(copyObject: AbstractTagFrame): this() {
        this.frameBody = copyObject.frameBody?.let { fb ->
            ID3Tags.copyObject(fb) as? AbstractTagFrameBody
        }
        this.frameBody?.header = this
    }

    /**
     * Returns true if this datatype and it's body is a subset of the argument.
     * This datatype is a subset if the argument is the same class.
     *
     * @param obj datatype to determine if subset of
     * @return true if this datatype and it's body is a subset of the argument.
     */
    override fun isSubsetOf(obj: Any?): Boolean {
        if (obj !is AbstractTagFrame) {
            return false
        }

        if ((frameBody == null) && (obj.frameBody == null)) {
            return true
        }

        if ((frameBody == null) || (obj.frameBody == null)) {
            return false
        }

        return (frameBody?.isSubsetOf(obj.frameBody) == true && super.isSubsetOf(obj))
    }

    override fun toString(): String {
        return frameBody?.toString()?:""
    }
}