package de.visualdigits.kaudiotagger.model.frame.framebody.id3

import com.sun.jdi.InvalidStackFrameException
import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer

abstract class AbstractID3v2FrameBody : AbstractTagFrameBody {

    companion object {

        const val TYPE_BODY: String = "body"
    }

    constructor()

    constructor(size: Int): super(size)

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): this() {
        setSize(frameSize)
        read(byteBuffer)
    }

    constructor(
        identifier: String? = null,
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(identifier, byteBuffer, frameSize)

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
     * @throws InvalidFrameException if unable to construct a frameBody from the ByteBuffer
     */
    //TODO why don't we just slice byteBuffer, set limit to size and convert readByteArray to take a ByteBuffer
    //then we wouldn't have to temporary allocate space for the buffer, using lots of needless memory
    //and providing extra work for the garbage collector.
    override fun read(byteBuffer: ByteBuffer?) {
        if (byteBuffer == null) {
            return
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
        for (`object` in objectList) { //correct dataType.
            log.debug("offset:$offset")

            //The read has extended further than the defined frame size (ok to extend upto
            //size because the next datatype may be of length 0.)
            if (offset > sizeValue) {
                log.warn("Invalid Size for FrameBody")
                throw InvalidStackFrameException("Invalid size for Frame Body")
            }

            //Try and load it with data from the Buffer
            //if it fails frame is invalid
            try {
                `object`.readByteArray(buffer, offset)
            } catch (e: InvalidDataTypeException) {
                log.warn(
                    "Problem reading datatype within Frame Body:${e.message}"
                )
                throw e
            }
            //Increment Offset to start of next datatype.
            offset += `object`.getSizeValue()
        }
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
     * Write the contents of this datatype to the byte array
     *
     * @param tagBuffer
     */
    open fun write(tagBuffer: ByteArrayOutputStream) {
        log.debug(
            "Writing frame body for${this.getIdentifier()}:Est Size:${getSize()}"
        )
        //Write the various fields to file in order
        for (`object` in objectList) {
            val objectData = `object`.writeByteArray()
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
            addSize(`object`.getSizeValue())
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