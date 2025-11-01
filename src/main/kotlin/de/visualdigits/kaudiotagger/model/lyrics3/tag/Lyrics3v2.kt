package de.visualdigits.kaudiotagger.model.lyrics3.tag

import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidTagException
import de.visualdigits.kaudiotagger.model.common.tag.AbstractTag
import de.visualdigits.kaudiotagger.model.common.types.SupportedTag
import de.visualdigits.kaudiotagger.model.id3.frame.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v1Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag
import de.visualdigits.kaudiotagger.model.lyrics3.field.Lyrics3v2Field
import de.visualdigits.kaudiotagger.model.lyrics3.field.framebody.FieldFrameBodyIND
import de.visualdigits.kaudiotagger.model.lyrics3.field.framebody.FieldFrameBodyLYR
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.RandomAccessFile
import java.nio.ByteBuffer

class Lyrics3v2 : AbstractLyrics3 {

    var fieldMap = mutableMapOf<String, Lyrics3v2Field>()

    /**
     * Creates a new Lyrics3v2 datatype.
     */
    constructor()

    constructor(copyObject: Lyrics3v2) : super(copyObject) {
        copyObject.fieldMap.keys.forEach { key ->
            val newObject = Lyrics3v2Field(copyObject.fieldMap[key] ?:error("No field with id '$key'"))
            fieldMap[key] = newObject
        }
    }

    /**
     * Creates a new Lyrics3v2 datatype.
     *
     * @param mp3tag
     */
    constructor(mp3tag: AbstractTag) {
        if (mp3tag is Lyrics3v2) {
            throw UnsupportedOperationException(
                "Copy Constructor not called. Please type cast the argument"
            )
        } else if (mp3tag is Lyrics3v1) {
            val newField = Lyrics3v2Field(
                FieldFrameBodyLYR(mp3tag.lyric)
            )
            fieldMap[newField.getIdentifier()] = newField
        } else {
            var newField: Lyrics3v2Field?

            ID3v24Tag(mp3tag).frameMap.values.forEach { frame ->
                newField = Lyrics3v2Field(frame as AbstractID3v2Frame)
                fieldMap[newField.getIdentifier()] = newField
            }
        }
    }

    /**
     * Creates a new Lyrics3v2 datatype.
     *
     * @param byteBuffer
     */
    constructor(byteBuffer: ByteBuffer) {
        this.read(byteBuffer)
    }

    override fun supportedTag(): SupportedTag = SupportedTag.Lyrics3V2Tag

    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null || !seek(byteBuffer)) {
            return false
        }

        val lyricSize: Int = seekSize()

        // reset file pointer to the beginning of the tag;
        seek(byteBuffer)

        fieldMap = mutableMapOf()

        var lyric: Lyrics3v2Field?

        // read each of the fields
        while ((byteBuffer.position()) < (lyricSize - 11)) {
            try {
                lyric = Lyrics3v2Field(byteBuffer)
                setField(lyric)
            } catch (ex: InvalidTagException) {
                // keep reading until we're done
            }
        }

        return true
    }

    /**
     * @param field
     */
    fun setField(field: Lyrics3v2Field) {
        fieldMap[field.getIdentifier()] = field
    }

    /**
     * TODO implement
     *
     * @param byteBuffer
     * @return
     */
    override fun seek(byteBuffer: ByteBuffer): Boolean {
        return false
    }

    /**
     *
     * @return
     */
    private fun seekSize(): Int {
        return -1
    }

    /**
     * Gets the value of the frame identified by identifier
     *
     * @param identifier The three letter code
     * @return The value associated with the identifier
     */
    fun getField(identifier: String?): Lyrics3v2Field? {
        return fieldMap[identifier]
    }

    fun getFieldCount(): Int {
        return fieldMap.size
    }

    /**
     * @param identifier
     * @return
     */
    fun hasField(identifier: String?): Boolean {
        return fieldMap.containsKey(identifier)
    }

    /**
     * @param identifier
     */
    fun removeField(identifier: String?) {
        fieldMap.remove(identifier)
    }

    /**
     * @param file
     * @return
     */
    fun seek(file: RandomAccessFile): Boolean {
        val buffer = ByteArray(11)
        var lyricEnd: String?
        var filePointer: Long

        // check right before the ID3 1.0 tag for the lyrics tag
        file.seek(file.length() - 128 - 9)
        file.read(buffer, 0, 9)
        lyricEnd = String(buffer, 0, 9)

        if (lyricEnd == "LYRICS200") {
            filePointer = file.filePointer
        } else {
            // check the end of the file for a lyrics tag incase an ID3
            // tag wasn't placed after it.
            file.seek(file.length() - 9)
            file.read(buffer, 0, 9)
            lyricEnd = String(buffer, 0, 9)

            if (lyricEnd == "LYRICS200") {
                filePointer = file.filePointer
            } else {
                return false
            }
        }

        // read the 6 bytes for the length of the tag
        filePointer -= (9 + 6).toLong()
        file.seek(filePointer)
        file.read(buffer, 0, 6)

        val lyricSize: Long = String(buffer, 0, 6).toInt().toLong()

        // read the lyrics begin tag if it exists.
        file.seek(filePointer - lyricSize)
        file.read(buffer, 0, 11)
        val lyricStart = String(buffer, 0, 11)

        return lyricStart == "LYRICSBEGIN"
    }

    override fun toString(): String {
        val iterator = fieldMap.values.iterator()
        var field: Lyrics3v2Field
        var str = getIdentifier() + " " + this.getSize() + "\n"

        while (iterator.hasNext()) {
            field = iterator.next()
            str += (field.toString() + "\n")
        }

        return str
    }

    override fun getIdentifier(): String {
        return "Lyrics3v2.00"
    }

    override fun getSize(): Int {
        var size = 0
        val iterator = fieldMap.values.iterator()
        var field: Lyrics3v2Field

        while (iterator.hasNext()) {
            field = iterator.next()
            size += field.getSize()
        }

        // include LYRICSBEGIN, but not 6 char size or LYRICSEND
        return 11 + size
    }

    /**
     * @param file
     */
    override fun write(file: RandomAccessFile) {
        var offset = 0

        val size: Long
        val buffer = ByteArray(6 + 9)
        var field: Lyrics3v2Field?
        ID3v1Tag()

        delete(file)
        file.seek(file.length())

        val filePointer: Long = file.filePointer

        var str = "LYRICSBEGIN"

        for (i in 0..<str.length) {
            buffer[i] = str[i].code.toByte()
        }

        file.write(buffer, 0, str.length)

        // IND needs to go first. lets createField/update it and write it first.
        updateField("IND")
        field = fieldMap["IND"]
        field?.write(file)

        val iterator = fieldMap.values.iterator()

        while (iterator.hasNext()) {
            field = iterator.next()

            val id = field.getIdentifier()
            val save = TagOptionSingleton.lyrics3SaveFieldMap[id]?:false

            if ((id != "IND") && save) {
                field.write(file)
            }
        }

        size = file.filePointer - filePointer

        str = size.toString()

        for (i in 0..<(6 - str.length)) {
            buffer[i] = '0'.code.toByte()
        }

        offset += (6 - str.length)

        for (i in 0..<str.length) {
            buffer[i + offset] = str[i].code.toByte()
        }

        offset += str.length

        str = "LYRICS200"

        for (i in 0..<str.length) {
            buffer[i + offset] = str[i].code.toByte()
        }

        offset += str.length

        file.write(buffer, 0, offset)

    }

    /**
     * @param identifier
     */
    fun updateField(identifier: String) {
        var lyrField: Lyrics3v2Field?

        if (identifier == "IND") {
            val lyricsPresent = fieldMap.containsKey("LYR")
            var timeStampPresent = false

            if (lyricsPresent) {
                lyrField = fieldMap["LYR"]

                val lyrBody = lyrField?.frameBody as? FieldFrameBodyLYR
                timeStampPresent = lyrBody?.hasTimeStamp() == true
            }

            lyrField = Lyrics3v2Field(
                FieldFrameBodyIND(lyricsPresent, timeStampPresent)
            )
            setField(lyrField)
        }
    }
}
