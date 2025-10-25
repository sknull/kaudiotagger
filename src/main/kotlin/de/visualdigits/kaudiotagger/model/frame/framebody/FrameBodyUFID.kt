package de.visualdigits.kaudiotagger.model.frame.framebody

import de.visualdigits.kaudiotagger.model.datatype.ByteArraySizeTerminated
import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v23FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import java.nio.ByteBuffer

/**
 * A UFID Framebody consists of an owner that identifies the server hosting the
 * unique identifier database, and the unique identifier itself which can be up to 64
 * bytes in length.
 */
class FrameBodyUFID: AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {

    /**
     * Creates a new FrameBodyUFID datatype.
     */
    constructor() {
        setOwner("")
        setUniqueIdentifier(byteArrayOf())
    }

    constructor(body: FrameBodyUFID) : super(body)

    /**
     * Creates a new FrameBodyUFID datatype.
     *
     * @param owner            url of the database
     * @param uniqueIdentifier unique identifier
     */
    constructor(owner: String, uniqueIdentifier: ByteArray) {
        setOwner(owner)
        setUniqueIdentifier(uniqueIdentifier)
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    constructor(
        identifier: String? = null,
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(identifier, byteBuffer, frameSize)

    /**
     * @return the url of the the database that this ufid is stored in
     */
    fun getOwner(): String? {
        return getObjectValue(DataTypes.OBJ_OWNER) as? String
    }

    /**
     * Set the owner of url of the the database that this ufid is stored in
     *
     * @param owner should be a valid url
     */
    fun setOwner(owner: String?) {
        setObjectValue(DataTypes.OBJ_OWNER, owner)
    }

    /**
     * @return the unique identifier (within the owners domain)
     */
    fun getUniqueIdentifier(): ByteArray? {
        return getObjectValue(DataTypes.OBJ_DATA) as? ByteArray
    }

    /**
     * Set the unique identifier (within the owners domain)
     *
     * @param uniqueIdentifier
     */
    fun setUniqueIdentifier(uniqueIdentifier: ByteArray?) {
        setObjectValue(DataTypes.OBJ_DATA, uniqueIdentifier)
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.UNIQUE_FILE_ID.id
    }

    override fun setupObjectList() {
        objectList.add(StringNullTerminated(DataTypes.OBJ_OWNER, this))
        objectList.add(ByteArraySizeTerminated(DataTypes.OBJ_DATA, this))
    }

    companion object {
        const val UFID_MUSICBRAINZ: String = "http://musicbrainz.org"
        const val UFID_ID3TEST: String = "http://www.id3.org/dummy/ufid.html"
    }
}
