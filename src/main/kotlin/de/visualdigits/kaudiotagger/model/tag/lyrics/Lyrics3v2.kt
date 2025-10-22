package de.visualdigits.kaudiotagger.model.tag.lyrics

import de.visualdigits.kaudiotagger.model.exceptions.InvalidTagException
import de.visualdigits.kaudiotagger.model.exceptions.TagException
import de.visualdigits.kaudiotagger.model.frame.id3.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.kfield.Lyrics3v2Field
import de.visualdigits.kaudiotagger.model.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.tag.id3.ID3v24Tag
import java.nio.ByteBuffer

class Lyrics3v2: AbstractLyrics3 {

    /**
     *
     */
    private var fieldMap: MutableMap<String, Lyrics3v2Field> = mutableMapOf()

    /**
     * Creates a new Lyrics3v2 datatype.
     */
    constructor() {
    }

    constructor(copyObject: Lyrics3v2): super(copyObject) {
        copyObject.fieldMap.keys.forEach { identifier ->
            val newObject = copyObject.fieldMap.get(identifier)
            newObject?.also { no -> fieldMap[identifier] = no }
        }
    }

    /**
     * Creates a new Lyrics3v2 datatype.
     *
     * @param mp3tag
     * @throws UnsupportedOperationException
     */
    constructor(mp3tag: AbstractTag) {
        if (mp3tag != null) {
            // upgrade the tag to lyrics3v2
            if (mp3tag is Lyrics3v2) {
                throw UnsupportedOperationException(
                    "Copy Constructor not called. Please type cast the argument"
                )
            } else if (mp3tag is Lyrics3v1) {
                val newField: Lyrics3v2Field?
                newField = Lyrics3v2Field(
                    FieldFrameBodyLYR(mp3tag.getLyric())
                )
                fieldMap.put(newField.getIdentifier(), newField)
            } else {
                var newField: Lyrics3v2Field?
                val iterator: MutableIterator<AbstractID3v2Frame>
                iterator = (ID3v24Tag(mp3tag)).iterator()

                while (iterator.hasNext()) {
                    try {
                        newField = Lyrics3v2Field(iterator.next())

                        if (newField != null) {
                            fieldMap.put(newField.getIdentifier(), newField)
                        }
                    } catch (ex: TagException) {
                        //invalid frame to createField lyrics3 field. ignore and keep going
                    }
                }
            }
        }
    }

    /**
     * Creates a new Lyrics3v2 datatype.
     *
     * @param byteBuffer
     */
    constructor(byteBuffer: ByteBuffer) {
        try {
            this.read(byteBuffer)
        } catch (e: TagException) {
            log.error("Something went wrong", e)
        }
    }

    override fun read(byteBuffer: ByteBuffer) {
        val filePointer: Long
        val lyricSize: Int

        if (seek(byteBuffer)) {
            lyricSize = seekSize(byteBuffer)
        } else {
            throw TagNotFoundException("Lyrics3v2.00 Tag Not Found")
        }

        // reset file pointer to the beginning of the tag;
        seek(byteBuffer)
        filePointer = byteBuffer.position().toLong()

        fieldMap = HashMap<String?, Lyrics3v2Field>()

        var lyric: Lyrics3v2Field

        // read each of the fields
        while ((byteBuffer.position()) < (lyricSize - 11)) {
            try {
                lyric = Lyrics3v2Field(byteBuffer)
                setField(lyric)
            } catch (ex: InvalidTagException) {
                // keep reading until we're done
            }
        }
    }

    /**
     * @param field
     */
    fun setField(field: Lyrics3v2Field) {
        fieldMap.put(field.getIdentifier(), field)
    }

    /**
     * TODO implement
     *
     * @param byteBuffer
     * @return
     * @throws IOException
     */
    override fun seek(byteBuffer: ByteBuffer): Boolean {
        return false
    }

    /**
     * TODO
     *
     * @param byteBuffer
     * @return
     */
    private fun seekSize(byteBuffer: ByteBuffer): Int = -1
}