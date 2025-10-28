package de.visualdigits.kaudiotagger.model.lyrics3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.datatype.AbstractDataType
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidTagException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer

abstract class AbstractLyrics3v2FieldFrameBody: AbstractTagFrameBody {

    constructor()

    constructor(copyObject: AbstractLyrics3v2FieldFrameBody): super(copyObject)

    /**
     * This is called by superclass when attempt to read data from file.
     *
     * @param file
     * @return
     * @throws InvalidTagException
     * @throws IOException
     */
    fun readHeader(file: RandomAccessFile): Int {
        val size: Int
        val buffer = ByteArray(5)

        // read the 5 character size
        file.read(buffer, 0, 5)
        size = Integer.parseInt(String(buffer, 0, 5))

        if ((size == 0) && (!TagOptionSingleton.lyrics3KeepEmptyFieldIfRead)) {
            throw InvalidTagException("Lyircs3v2 Field has size of zero.")
        }

        return size
    }

    /**
     * This reads a frame body from its file into the appropriate FrameBody class
     * Read the data from the given file into this datatype. The file needs to
     * have its file pointer in the correct location. The size as indicated in the
     * header is passed to the frame constructor when reading from file.
     *
     * @param byteBuffer file to read
     * @throws IOException         on any I/O error
     * @throws InvalidTagException if there is any error in the data format.
     */
    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null) {
            return false
        }
        val size = getSize()
        //Allocate a buffer to the size of the Frame Body and read from file
        val buffer = ByteArray(size)
        byteBuffer.get(buffer)
        //Offset into buffer, incremented by length of previous MP3Object
        var offset = 0

        //Go through the ObjectList of the Frame reading the data into the
        //correct datatype.
        var `object`: AbstractDataType
        val iterator: MutableIterator<AbstractDataType> = objectList.listIterator()
        while (iterator.hasNext()) {
            //The read has extended further than the defined frame size
            if (offset > (size - 1)) {
                throw InvalidTagException("Invalid size for Frame Body")
            }

            //Get next Object and load it with data from the Buffer
            `object` = iterator.next()
            `object`.readByteArray(buffer, offset)
            //Increment Offset to start of next datatype.
            offset += `object`.getSize()
        }

        return false
    }

    /**
     * Write the contents of this datatype to the file at the position it is
     * currently at.
     *
     * @param file destination file
     * @throws IOException on any I/O error
     */
    open fun write(file: RandomAccessFile) {
        //Write the various fields to file in order
        var buffer: ByteArray?
        var `object`: AbstractDataType
        val iterator: MutableIterator<AbstractDataType> = objectList.listIterator()
        while (iterator.hasNext()) {
            `object` = iterator.next()
            buffer = `object`.writeByteArray()
            file.write(buffer)
        }
    }
}