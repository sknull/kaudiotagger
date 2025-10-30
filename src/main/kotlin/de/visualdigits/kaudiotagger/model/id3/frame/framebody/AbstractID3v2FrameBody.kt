package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer

abstract class AbstractID3v2FrameBody : AbstractTagFrameBody {

    companion object {

        // avoid having to specify as a magic string
        val FRAME_BODY_PACKAGE: String = AbstractID3v2FrameBody::class.java.`package`.name

        const val TYPE_BODY: String = "body"
    }

    /**
     * Frame Body Size, originally this is size as indicated in frame header
     * when we come to writing data we recalculate it.
     */
    private var size = 0

    constructor()

    constructor(byteBuffer: ByteBuffer? = null, frameSize: Int = 0): this() {
        size = (frameSize)
        read(byteBuffer)
    }

    /**
     * Create Body based on another body
     *
     * @param copyObject
     */
    constructor(copyObject: AbstractID3v2FrameBody): super(copyObject)

    /**
     * This reads a frame body from a ByteBuffer into the appropriate FrameBody class and update the position of the
     * buffer to be just after the end of this frameBody
     *
     *
     * The ByteBuffer represents the tag and its position should be at the start of this frameBody. The size as
     * indicated in the header is passed to the frame constructor when reading from file.
     *
     * @param byteBuffer file to read
     */
    //TODO why don't we just slice byteBuffer, set limit to size and convert readByteArray to take a ByteBuffer
    //then we wouldn't have to temporary allocate space for the buffer, using lots of needless memory
    //and providing extra work for the garbage collector.
    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null) {
            return false
        }
        val sizeValue = getSize()
        log.debug("Reading body for${this.getIdentifier()}:$sizeValue")

        //Allocate a buffer to the size of the Frame Body and read from file
        val buffer = ByteArray(sizeValue)
        byteBuffer.get(buffer)

        //Offset into buffer, incremented by length of previous dataType
        //this offset is only used internally to decide where to look for the next
        //dataType within a frameBody, it does not decide where to look for the next frame body
        var offset = 0

        //Go through the ObjectList of the Frame reading the data into the
        objectList.forEach { o ->
            //correct dataType.
            log.debug("offset:$offset")

            //The read has extended further than the defined frame size (ok to extend upto
            //size because the next datatype may be of length 0.)
            if (offset > sizeValue) {
                log.warn("Invalid Size for FrameBody")
            }

            //Try and load it with data from the Buffer
            //if it fails frame is invalid
            try {
                o.readByteArray(buffer, offset)
            } catch (e: InvalidDataTypeException) {
                log.warn(
                    "Problem reading datatype within Frame Body:${e.message}"
                )
                throw e
            }
            //Increment Offset to start of next datatype.
            val size = o.getSize()
            offset += size
        }

        return false
    }

    /**
     * Are two bodies equal
     *
     * @param obj
     */
    override fun equals(obj: Any?): Boolean {
        return (obj is AbstractID3v2FrameBody) && super.equals(obj)
    }

    /**
     * Return size of frame body,if frameBody already exist will take this value from the frame header
     * but it is always recalculated before writing any changes back to disk.
     *
     * @return size in bytes of this frame body
     */
    override fun getSize(): Int {
        return size
    }

    /**
     * Set size based on size passed as parameter from frame header,
     * done before read
     *
     * @param size
     */
    fun setSize(size: Int) {
        this.size = size
    }

    /**
     * Set size based on size passed as parameter from frame header,
     * done before read
     *
     * @param amount
     */
    fun addSize(amount: Int) {
        this.size += amount
    }

    /**
     * Write the contents of this datatype to the byte array
     *
     * @param tagBuffer
     */
    open fun write(tagBuffer: ByteArrayOutputStream) {
        log.debug(
            "Writing frame body for${this.getIdentifier()}:Est Size:${getSize()}"
        )
        //Write the various fields to file in order
        objectList.forEach { o ->
            val objectData = o.writeByteArray()
            if (objectData != null) {
                try {
                    tagBuffer.write(objectData)
                } catch (ioe: IOException) {
                    //This could never happen coz not writing to file, so convert to RuntimeException
                    throw RuntimeException(ioe)
                }
            }
        }
        setDataSize()
        log.debug(
            "Written frame body for${this.getIdentifier()}:Real Size:${getSize()}"
        )
    }

    /**
     * Set size based on size of the DataTypes making up the body,done after write
     */
    fun setDataSize() {
        setSize(0)
        for (`object` in objectList) {
            addSize(`object`.getSize())
        }
    }

    /**
     * Return String Representation of Datatype     *
     */
    override fun createStructure() {
        MP3File.tagFormatter?.openHeadingElement(TYPE_BODY, "")
        for (nextObject in objectList) {
            nextObject.createStructure()
        }
        MP3File.tagFormatter?.closeHeadingElement(TYPE_BODY)
    }
}