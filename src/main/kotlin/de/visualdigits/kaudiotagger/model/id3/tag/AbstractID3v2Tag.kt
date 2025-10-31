package de.visualdigits.kaudiotagger.model.id3.tag

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.exceptions.KeyNotFoundException
import de.visualdigits.kaudiotagger.model.common.field.TagField
import de.visualdigits.kaudiotagger.model.common.frame.AggregatedFrame
import de.visualdigits.kaudiotagger.model.common.frame.TyerTdatAggregatedFrame
import de.visualdigits.kaudiotagger.model.common.tag.Tag
import de.visualdigits.kaudiotagger.model.common.types.GenericFieldKey
import de.visualdigits.kaudiotagger.model.common.types.StandardIPLSKey
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.frame.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractFrameBodyNumberTotal
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractFrameBodyPairs
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractFrameBodyTextInfo
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyAPIC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyCOMM
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyEncrypted
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyIPLS
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyPIC
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyPOPM
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIPL
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTMCL
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTXXX
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyUFID
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyUSLT
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyWOAR
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyWXXX
import de.visualdigits.kaudiotagger.model.id3.types.ID3v22FrameId
import de.visualdigits.kaudiotagger.model.id3.types.Languages
import de.visualdigits.kaudiotagger.model.id3.types.PictureTypes
import de.visualdigits.kaudiotagger.model.images.Artwork
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.FileUtil.adjustPadding
import de.visualdigits.kaudiotagger.util.FileUtil.getFileLockForWriting
import de.visualdigits.kaudiotagger.util.ID3SyncSafeInteger
import de.visualdigits.kaudiotagger.util.Utils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.charset.StandardCharsets
import java.util.TreeSet

abstract class AbstractID3v2Tag : AbstractID3Tag, Tag {

    companion object {
        // Tag ID as held in file
        val TAG_ID: ByteArray = byteArrayOf('I'.code.toByte(), 'D'.code.toByte(), '3'.code.toByte())
        const val TAGID: String = "ID3"

        // The tag header is the same for ID3v2 versions
        const val TAG_HEADER_LENGTH: Int = 10
        const val FIELD_TAGID_LENGTH: Int = 3
        const val FIELD_TAG_MAJOR_VERSION_LENGTH: Int = 1
        const val FIELD_TAG_MINOR_VERSION_LENGTH: Int = 1
        const val FIELD_TAG_FLAG_LENGTH: Int = 1
        const val FIELD_TAG_SIZE_LENGTH: Int = 4
        const val FIELD_TAG_MAJOR_VERSION_POS: Int = 3
        const val TYPE_HEADER: String = "header"
        const val TYPE_BODY: String = "body"
        const val FIELD_TAGID_POS: Int = 0
        const val FIELD_TAG_MINOR_VERSION_POS: Int = 4
        const val FIELD_TAG_FLAG_POS: Int = 5
        const val FIELD_TAG_SIZE_POS: Int = 6
        const val TAG_SIZE_INCREMENT: Int = 100

        /**
         * Holds the ids of invalid duplicate frames
         */
        const val TYPE_DUPLICATEFRAMEID: String = "duplicateFrameId"

        /**
         * Holds count the number of bytes used up by invalid duplicate frames
         */
        const val TYPE_DUPLICATEBYTES: String = "duplicateBytes"

        /**
         * Holds count the number bytes used up by empty frames
         */
        const val TYPE_EMPTYFRAMEBYTES: String = "emptyFrameBytes"

        /**
         * Holds the size of the tag as reported by the tag header
         */
        const val TYPE_FILEREADSIZE: String = "fileReadSize"

        /**
         * Holds count of invalid frames, (frames that could not be read)
         */
        const val TYPE_INVALIDFRAMES: String = "invalidFrames"

        // The max size we try to write in one go to avoid out of memory errors (10mb)
        const val MAXIMUM_WRITABLE_CHUNK_SIZE: Long = 10000000

        /**
         * Determines if file contain an id3 tag and if so positions the file pointer just after the end
         * of the tag.
         *
         *
         * This method is used by non mp3s (such as .ogg and .flac) to determine if they contain an id3 tag
         *
         * @param raf
         * @return
         */
        fun isId3Tag(raf: RandomAccessFile): Boolean {
            if (!isID3V2Header(raf)) {
                return false
            }
            // So we have a tag
            val tagHeader = ByteArray(FIELD_TAG_SIZE_LENGTH)
            raf.seek(
                raf.filePointer +
                        FIELD_TAGID_LENGTH +
                        FIELD_TAG_MAJOR_VERSION_LENGTH +
                        FIELD_TAG_MINOR_VERSION_LENGTH +
                        FIELD_TAG_FLAG_LENGTH
            )
            raf.read(tagHeader)
            val bb = ByteBuffer.wrap(tagHeader)

            val size = ID3SyncSafeInteger.bufferToValue(bb)
            raf.seek((size + TAG_HEADER_LENGTH).toLong())

            return true
        }

        /**
         * True if files has a ID3v2 header
         *
         * @param raf
         * @return
         */
        fun isID3V2Header(raf: RandomAccessFile): Boolean {
            val start = raf.filePointer
            val tagIdentifier = ByteArray(FIELD_TAGID_LENGTH)
            raf.read(tagIdentifier)
            raf.seek(start)

            return tagIdentifier.contentEquals(TAG_ID)
        }

        private fun isID3V2Header(fc: FileChannel): Boolean {
            val start = fc.position()
            val headerBuffer = Utils.readFileDataIntoBufferBE(
                fc,
                FIELD_TAGID_LENGTH
            )
            fc.position(start)
            val s = Utils.readThreeBytesAsChars(headerBuffer)

            return s == TAGID
        }

        /**
         * Checks to see if the file contains an ID3tag and if so return its size as reported in
         * the tag header  and return the size of the tag (including header), if no such tag exists return
         * zero.
         *
         * @param file
         * @return the end of the tag in the file or zero if no tag exists.
         */
        fun getV2TagSizeIfExists(file: File?): Long {
            if (file == null) {
                return 0
            }
            val bb = FileInputStream(file).use { fis ->
                fis.getChannel().use { fc ->
                    val bb = ByteBuffer.allocate(TAG_HEADER_LENGTH)
                    fc.read(bb)
                    bb.flip()
                    if (bb.limit() < (TAG_HEADER_LENGTH)) {
                        return 0
                    }
                    bb
                }
            }

            // ID3 identifier
            val tagIdentifier = ByteArray(FIELD_TAGID_LENGTH)
            bb.get(tagIdentifier, 0, FIELD_TAGID_LENGTH)
            if (!(tagIdentifier.contentEquals(TAG_ID))) {
                return 0
            }

            // Is it valid Major Version
            val majorVersion = bb.get().toInt()
            if ((majorVersion != ID3v22Tag.MAJOR_VERSION) &&
                (majorVersion != ID3v23Tag.MAJOR_VERSION) &&
                (majorVersion != ID3v24Tag.MAJOR_VERSION)
            ) {
                return 0
            }

            // Skip Minor Version
            bb.get()

            // Skip Flags
            bb.get()

            // Get size as recorded in frame header
            return (TAG_HEADER_LENGTH + ID3SyncSafeInteger.bufferToValue(bb)).toLong()
        }
    }

    /**
     * Map of all frames for this tag
     */
    val frameMap: MutableMap<String, Any> = mutableMapOf()

    /**
     * Map of all encrypted frames, these cannot be unencrypted by jaudiotagger
     */
    val encryptedFrameMap: MutableMap<String, Any> = mutableMapOf()
    var duplicateFrameId: String = ""
    var duplicateBytes: Int = 0
    var emptyFrameBytes: Int = 0
    var fileReadBytes: Int = 0
    var invalidFrames: Int = 0

    /**
     * Empty Constructor
     */
    constructor()

    /**
     * This constructor is used when a tag is created as a duplicate of another
     * tag of the same type and version.
     *
     * @param copyObject
     */
    constructor(copyObject: AbstractID3v2Tag) : super(copyObject)

    /**
     * Return tag size based upon the sizes of the tags rather than the physical
     * no of bytes between start of ID3Tag and start of Audio Data.Should be extended
     * by subclasses to include header.
     *
     * @return size of the tag
     */
    @Suppress("UNCHECKED_CAST")
    override fun getSize(): Int {
        var sum = 0
        frameMap.values.forEach { frame ->
            when (frame) {
                is AbstractID3v2Frame -> frame.getSize()
                is AggregatedFrame -> frame.getFrames().sumOf { f -> f.getSize() }
                is MutableList<*> -> (frame as? List<AbstractID3v2Frame>)?.let { f -> f.sumOf { e -> e.getSize() } }
                else -> 0
            }
        }

        return sum
    }

    /**
     * Add frame to the frame map
     *
     * @param frameId
     * @param next
     */
    open fun loadFrameIntoMap(frameId: String?, next: AbstractID3v2Frame) {
        if (next.frameBody is FrameBodyEncrypted) {
            loadFrameIntoSpecifiedMap(encryptedFrameMap, frameId, next)
        } else {
            loadFrameIntoSpecifiedMap(frameMap, frameId, next)
        }
    }

    /**
     * Copy primitives apply to all tags
     *
     * @param copyObject
     */
    open fun copyPrimitives(copyObject: AbstractID3v2Tag) {
        log.debug("Copying Primitives")
        // Primitives type variables common to all IDv2 Tags
        this.duplicateFrameId = copyObject.duplicateFrameId
        this.duplicateBytes = copyObject.duplicateBytes
        this.emptyFrameBytes = copyObject.emptyFrameBytes
        this.fileReadBytes = copyObject.fileReadBytes
        this.invalidFrames = copyObject.invalidFrames
    }

    /**
     * Copy frame into map, whilst accounting for multiple frame of same type which can occur even if there were
     * not frames of the same type in the original tag
     *
     * @param id
     * @param newFrame
     */
    @Suppress("UNCHECKED_CAST")
    fun copyFrameIntoMap(
        id: String?,
        newFrame: AbstractID3v2Frame
    ) {
        val identifier = newFrame.getIdentifier()
        if (frameMap.containsKey(identifier)) {
            when (val o = frameMap[identifier]) {
                is AbstractID3v2Frame -> {
                    processDuplicateFrame(newFrame, o)
                }

                is AggregatedFrame -> {
                    log.error("Duplicated Aggregate Frame, ignoring:$id")
                }

                is MutableList<*> -> {
                    (o as? MutableList<Any>)?.add(newFrame)
                }

                else -> {
                    log.error("Unknown frame class:discarding:${o?.javaClass}")
                }
            }
        } else {
            frameMap[identifier ?: ""] = newFrame
        }
    }

    /**
     * If frame already exists default behaviour is to just add another one, but can be overrridden if
     * special handling required
     *
     * @param newFrame
     * @param existingFrame
     */
    open fun processDuplicateFrame(
        newFrame: AbstractID3v2Frame,
        existingFrame: AbstractID3v2Frame
    ) {
        val list = mutableListOf(existingFrame, newFrame)
        frameMap[newFrame.getIdentifier() ?: ""] = list
    }

    /**
     * Copy frames from another tag,
     *
     * @param copyObject
     */
    // TODO Copy Encrypted frames needs implementing
    @Suppress("UNCHECKED_CAST")
    fun copyFrames(copyObject: AbstractID3v2Tag) {
        frameMap.clear()
        encryptedFrameMap.clear()

        // Copy Frames that are a valid 2.4 type
        copyObject.frameMap.forEach { (id, value) ->
            // SingleFrames
            when (value) {
                is AbstractID3v2Frame -> {
                    addFrame(value)
                }

                is TyerTdatAggregatedFrame -> {
                    value.getFrames().forEach { frame ->
                        addFrame(frame)
                    }
                }

                is List<*> -> {
                    (value as List<AbstractID3v2Frame>).forEach { frame ->
                        addFrame(frame)
                    }
                }
            }
        }
    }

    /**
     * Add the frame converted to the correct version
     *
     * @param frame
     */
    abstract fun addFrame(frame: AbstractID3v2Frame)

    /**
     * Return whether tag has frame with this identifier
     *
     *
     * Warning the match is only done against the identifier so if a tag contains a frame with an unsupported body
     * but happens to have an identifier that is valid for another version of the tag it will return true
     *
     * @param identifier frameId to lookup
     * @return true if tag has frame with this identifier
     */
    fun hasFrame(identifier: String): Boolean {
        return frameMap.containsKey(identifier)
    }

    /**
     * For single frames return the frame in this tag with given identifier if it exists, if multiple frames
     * exist with the same identifier it will return a list containing all the frames with this identifier
     *
     *
     * Warning the match is only done against the identifier so if a tag contains a frame with an unsupported body
     * but happens to have an identifier that is valid for another version of the tag it will be returned.
     *
     * @param identifier is an ID3Frame identifier
     * @return matching frame, or list of matching frames
     */
    // TODO:This method is problematic because sometimes it returns a list and sometimes a frame, we need to
    // replace with two separate methods as in the tag interface.
    fun getFrame(identifier: String?): Any? {
        return frameMap[identifier]
    }

    /**
     * Return all frames which start with the identifier, this
     * can be more than one which is useful if trying to retrieve
     * similar frames e.g TIT1,TIT2,TIT3 ... and don't know exactly
     * which ones there are.
     *
     *
     * Warning the match is only done against the identifier so if a tag contains a frame with an unsupported body
     * but happens to have an identifier that is valid for another version of the tag it will be returned.
     *
     * @param identifier
     * @return an iterator of all the frames starting with a particular identifier
     */
    fun getFrameOfType(identifier: String): Set<Any?> {
        return frameMap.keys.mapNotNull { key ->
            if (key.startsWith(identifier)) {
                val o = frameMap[key]
                o as? List<*> ?: listOf(o)
            } else null
        }.flatten().toSet()
    }

    /**
     * Decides what to with the frame that has just been read from file.
     * If the frame is an allowable duplicate frame and is a duplicate we add all
     * frames into an List and add the ArrayList to the HashMap. if not allowed
     * to be duplicate we store the number of bytes in the duplicateBytes variable and discard
     * the frame itself.
     *
     * @param frameId
     * @param next
     */
    @Suppress("UNCHECKED_CAST")
    open fun loadFrameIntoSpecifiedMap(
        map: MutableMap<String, Any>,
        frameId: String?,
        next: AbstractID3v2Frame
    ) {
        if ((ID3v22FrameId.isMultipleAllowed(frameId)) ||
            (ID3v22FrameId.isMultipleAllowed(frameId)) ||
            (ID3v22FrameId.isMultipleAllowed(frameId))
        ) {
            // If a frame already exists of this type
            if (map.containsKey(frameId)) {
                val o = map[frameId]
                if (o is MutableList<*>) {
                    val multiValues = o as MutableList<AbstractID3v2Frame>
                    multiValues.add(next)
                    log.debug("Adding Multi Frame(1)$frameId")
                } else {
                    val multiValues = mutableListOf<AbstractID3v2Frame>()
                    multiValues.add(o as AbstractID3v2Frame)
                    multiValues.add(next)
                    frameId?.also { fid -> map[fid] = multiValues }
                    log.debug("Adding Multi Frame(2)$frameId")
                }
            } else {
                log.debug("Adding Multi FrameList(3)$frameId")
                frameId?.also { fid -> map[fid] = next }
            }
        } else if (map.containsKey(frameId)) {
            log.warn("Ignoring Duplicate Frame:$frameId")
            // If we have multiple duplicate frames in a tag separate them with semicolons
            if (this.duplicateFrameId.isNotEmpty()) {
                this.duplicateFrameId += ";"
            }
            this.duplicateFrameId += frameId
            this.duplicateBytes += (frameMap[frameId] as AbstractID3v2Frame).frameSize
        } else {
            log.debug("Adding Frame$frameId")
            frameId?.also { fid -> map[fid] = next }
        }
    }

    /**
     * Write all the frames to the byteArrayOutputStream
     *
     *
     *
     * Currently Write all frames, defaults to the order in which they were loaded, newly
     * created frames will be at end of tag.
     *
     * @return ByteBuffer Contains all the frames written within the tag ready for writing to file
     */
    fun writeFramesToBuffer(): ByteArrayOutputStream {
        val bodyBuffer = ByteArrayOutputStream()
        writeFramesToBufferStream(frameMap, bodyBuffer)
        writeFramesToBufferStream(encryptedFrameMap, bodyBuffer)
        return bodyBuffer
    }

    /**
     * Write frames in map to bodyBuffer
     *
     * @param map
     * @param bodyBuffer
     */
    @Suppress("UNCHECKED_CAST")
    private fun writeFramesToBufferStream(
        map: MutableMap<String, Any>,
        bodyBuffer: ByteArrayOutputStream
    ) {
        // Sort keys into Preferred Order
        val sortedWriteOrder = TreeSet(
            getPreferredFrameOrderComparator()
        )
        sortedWriteOrder.addAll(map.keys)

        var frame: AbstractID3v2Frame
        for (id in sortedWriteOrder) {
            val o = map.get(id)
            if (o is AbstractID3v2Frame) {
                frame = o
                frame.write(bodyBuffer)
            } else if (o is AggregatedFrame) {
                for (next in o.getFrames()) {
                    next.write(bodyBuffer)
                }
            } else {
                val multiFrames = o as MutableList<AbstractID3v2Frame>
                for (nextFrame in multiFrames) {
                    nextFrame.write(bodyBuffer)
                }
            }
        }
    }

    /**
     * @return comparator used to order frames in preferred order for writing to file
     * so that most important frames are written first.
     */
    abstract fun getPreferredFrameOrderComparator(): Comparator<String>

    /**
     * Write tag to file.
     *
     * @param file
     * @param audioStartByte
     * @return new audioStartByte - different only if the audio content had to be moved
     */
    abstract fun write(file: File?, audioStartByte: Long): Long

    /**
     * This method determines the total tag size taking into account
     * the preferredSize and the min size required for new tag. For mp3
     * preferred size is the location of the audio, for other formats
     * preferred size is the size of the existing tag
     *
     * @param tagSize
     * @param preferredSize
     * @return
     */
    fun calculateTagSize(tagSize: Int, preferredSize: Int): Int {
        /** We can fit in the tag so no adjustments required  */
        if (tagSize <= preferredSize) {
            return preferredSize
        }
        /** There is not enough room as we need to move the audio file we might
         * as well increase it more than neccessary for future changes
         */
        return tagSize + TAG_SIZE_INCREMENT
    }

    /**
     * Write the data from the buffer to the file
     *
     * @param file
     * @param headerBuffer
     * @param bodyByteBuffer
     * @param padding
     * @param sizeIncPadding
     * @param audioStartLocation
     */
    fun writeBufferToFile(
        file: File?,
        headerBuffer: ByteBuffer?,
        bodyByteBuffer: ByteArray,
        padding: Int,
        sizeIncPadding: Int,
        audioStartLocation: Long
    ) {
        if (file == null) {
            return
        }

        // We need to adjust location of audio file if true
        if (sizeIncPadding > audioStartLocation) {
            log.debug("Adjusting Padding")
            adjustPadding(file, sizeIncPadding, audioStartLocation)
        }

        try {
            RandomAccessFile(file, "rw").getChannel().use { fc ->
                getFileLockForWriting(fc, file.path).use { fileLock ->
                    fc.write(headerBuffer)
                    fc.write(ByteBuffer.wrap(bodyByteBuffer))
                    fc.write(ByteBuffer.wrap(ByteArray(padding)))
                    fileLock.release()
                }
            }
        } catch (e: FileNotFoundException) {
            log.error(e.message, e)
            if (e.message?.contains("Access is denied") ?: false || e.message?.contains("Permission denied") ?: false) {
                log.error(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.path))
            } else {
                log.error(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.path))
            }
            throw e
        } catch (e: IOException) {
            log.error(e.message, e)
            if (e.message == "Access is denied") {
                log.error(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.getParentFile().path))
            } else {
                log.error(ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(file.getParentFile().path))
            }
            throw e
        }
    }

    /**
     * Does a tag of the correct version exist in this file.
     *
     * @param byteBuffer to search through
     * @return true if tag exists.
     */
    override fun seek(byteBuffer: ByteBuffer): Boolean {
        byteBuffer.rewind()
        log.debug("ByteBuffer pos:${byteBuffer.position()}:limit${byteBuffer.limit()}:cap${byteBuffer.capacity()}")

        val tagIdentifier = ByteArray(FIELD_TAGID_LENGTH)
        byteBuffer.get(tagIdentifier, 0, FIELD_TAGID_LENGTH)
        if (!(tagIdentifier.contentEquals(TAG_ID))) {
            return false
        }
        // Major Version
        val major = byteBuffer.get().toInt()
        if (major != getMajorVersion()) {
            return false
        }
        // Minor Version
        val minor = byteBuffer.get().toInt()
        return minor == getRevision()
    }

    /**
     * Convert the frame to the correct frame(s)
     *
     * @param frame
     * @return
     */
    abstract fun convertFrame(
        frame: AbstractID3v2Frame
    ): MutableList<AbstractID3v2Frame>

    /**
     * Delete Tag
     *
     * @param file to delete the tag from
     */
    // TODO should clear all data and preferably recover lost space and go upto end of mp3s
    override fun delete(file: RandomAccessFile) {
        // this works by just erasing the "ID3" tag at the beginning
        // of the file
        val buffer = ByteArray(FIELD_TAGID_LENGTH)
        // Read into Int Buffer
        val fc = file.getChannel()
        fc.position()
        val byteBuffer = ByteBuffer.allocate(TAG_HEADER_LENGTH)
        fc.read(byteBuffer, 0)
        byteBuffer.flip()
        if (seek(byteBuffer)) {
            file.seek(0L)
            file.write(buffer)
        }
    }

    /**
     * Write tag to file.
     *
     * @param file
     */
    override fun write(file: RandomAccessFile) {
    }

    override fun addField(genericKey: GenericFieldKey, vararg values: String) {
        val tagfield: TagField = createField(genericKey, *values)
        addField(tagfield)
    }

    /**
     * Create field and then set within tag itself
     *
     * @param artwork
     */
    override fun addField(artwork: Artwork) {
        this.addField(createField(artwork))
    }

    /**
     * Add new field
     *
     *
     * There is a special handling if adding another text field of the same type, in this case the value will
     * be appended to the existing field, separated by the null character.
     *
     * @param tagField
     */
    @Suppress("UNCHECKED_CAST")
    override fun addField(tagField: TagField) {
        if ((tagField !is AbstractID3v2Frame) &&
            (tagField !is AggregatedFrame)
        ) {
            error("Field $tagField is not of type AbstractID3v2Frame or AggregatedFrame")
        }

        val fieldId = tagField.getIdentifier() ?: error("No id")
        if (tagField is AbstractID3v2Frame) {
            val o = frameMap[fieldId]

            // No frame of this type
            if (o == null) {
                frameMap[fieldId] = tagField
            } else if (o is MutableList<*>) {
                val list = o as MutableList<TagField>
                addNewFrameOrAddField(list, frameMap, null, tagField)
            } else {
                val existingFrame = o as AbstractID3v2Frame
                val list = mutableListOf<TagField>()
                addNewFrameOrAddField(list, frameMap, existingFrame, tagField)
            }
        } else {
            frameMap[fieldId] = tagField
        }
    }

    /**
     * Handles adding of a new field that's shares a frame with other fields, so modifies the existing frame rather
     * than creating a new frame for these special cases
     *
     * @param list
     * @param frameMap
     * @param existingFrame
     * @param frame
     */
    private fun addNewFrameOrAddField(
        list: MutableList<TagField>,
        frameMap: MutableMap<String, Any>,
        existingFrame: AbstractID3v2Frame?,
        frame: AbstractID3v2Frame
    ) {
        val mergedList = mutableListOf<TagField?>()
        mergedList.add(existingFrame)

        /**
         * If the frame is a TXXX frame then we add an extra string to the existing frame
         * if same description otherwise we create a new frame
         */
        val frameBody = frame.frameBody
        if (frameBody is FrameBodyTXXX) {
            var match = false
            val i: MutableIterator<TagField?> = mergedList.listIterator()
            while (i.hasNext()) {
                val existingFrameBody =
                    (i.next() as AbstractID3v2Frame).frameBody as FrameBodyTXXX
                if (frameBody.getDescription() == existingFrameBody.getDescription()
                ) {
                    frameBody.getText()?.also { t -> existingFrameBody.addTextValue(t) }
                    match = true
                    break
                }
            }
            if (!match) {
                addNewFrameToMap(list, frameMap, existingFrame, frame)
            }
        } else if (frameBody is FrameBodyWXXX) {
            var match = false
            val i: MutableIterator<TagField?> = mergedList.listIterator()
            while (i.hasNext()) {
                val existingFrameBody =
                    (i.next() as AbstractID3v2Frame).frameBody as FrameBodyWXXX
                if (frameBody.getDescription() == existingFrameBody.getDescription()
                ) {
                    existingFrameBody.addUrlLink(frameBody.getUrlLink())
                    match = true
                    break
                }
            }
            if (!match) {
                addNewFrameToMap(list, frameMap, existingFrame, frame)
            }
        } else if (frameBody is AbstractFrameBodyTextInfo) {
            val existingFrameBody = existingFrame?.frameBody as? AbstractFrameBodyTextInfo
            frameBody.getText()?.also { t -> existingFrameBody?.addTextValue(t) }
        } else if (frameBody is AbstractFrameBodyPairs) {
            val existingFrameBody = existingFrame?.frameBody as? AbstractFrameBodyPairs
            existingFrameBody?.addPair(frameBody.getText())
        } else if (frameBody is AbstractFrameBodyNumberTotal) {
            val existingFrameBody = existingFrame?.frameBody as? AbstractFrameBodyNumberTotal

            frameBody.getNumber()?.let {
                if (it > 0) {
                    existingFrameBody?.setNumber(frameBody.getNumberAsText() ?: "0")
                }
            }

            frameBody.getTotal()?.let {
                if (it > 0) {
                    frameBody.getTotalAsText()?.also { t -> existingFrameBody?.setTotal(t) }
                }
            }
        } else {
            addNewFrameToMap(list, frameMap, existingFrame, frame)
        }
    }

    /**
     * Add another frame to the map
     *
     * @param list
     * @param frameMap
     * @param existingFrame
     * @param frame
     */
    private fun addNewFrameToMap(
        list: MutableList<TagField>,
        frameMap: MutableMap<String, Any>,
        existingFrame: AbstractID3v2Frame?,
        frame: AbstractID3v2Frame
    ) {
        if (list.isEmpty()) {
            existingFrame?.also { ef -> list.add(ef) }
            list.add(frame)
            frameMap[frame.getIdentifier() ?: error("No id")] = list
        } else {
            list.add(frame)
        }
    }

    /**
     * Delete all instance of artwork Field
     *
     */
    override fun deleteArtworkField() {
        this.deleteField(GenericFieldKey.COVER_ART)
    }

    /**
     * Create Frame of correct ID3 version with the specified id
     *
     * @param id
     * @return
     */
    abstract fun createFrame(id: String): AbstractID3v2Frame

    /**
     * Create Frame for Id3 Key
     *
     *
     * Only textual data supported at the moment, should only be used with frames that
     * support a simple string argument.
     *
     * @param formatKey
     * @param values
     * @return
     */
    fun doCreateTagField(formatKey: FrameAndSubId, vararg values: String): TagField {
        val value: String = values[0]

        val frame = createFrame(formatKey.frameId)
        if (frame.frameBody is FrameBodyUFID) {
            (frame.frameBody as FrameBodyUFID).setOwner(formatKey.subId)
            (frame.frameBody as FrameBodyUFID).setUniqueIdentifier(
                value.toByteArray(StandardCharsets.ISO_8859_1)
            )
        } else if (frame.frameBody is FrameBodyTXXX) {
            (frame.frameBody as? FrameBodyTXXX)?.setDescription(formatKey.subId)
            (frame.frameBody as FrameBodyTXXX).setText(value)
        } else if (frame.frameBody is FrameBodyWXXX) {
            (frame.frameBody as FrameBodyWXXX).setDescription(formatKey.subId)
            (frame.frameBody as FrameBodyWXXX).setUrlLink(value)
        } else if (frame.frameBody is FrameBodyCOMM) {
            // Set description if set
            if (formatKey.subId != null) {
                (frame.frameBody as FrameBodyCOMM).setDescription(formatKey.subId)
                // Special Handling for Media Monkey Compatability
                if ((frame.frameBody as FrameBodyCOMM).isMediaMonkeyFrame()) {
                    (frame.frameBody as FrameBodyCOMM).setLanguage(
                        Languages.MEDIA_MONKEY_ID
                    )
                }
            }
            (frame.frameBody as FrameBodyCOMM).setText(value)
        } else if (frame.frameBody is FrameBodyUSLT) {
            (frame.frameBody as FrameBodyUSLT).setDescription("")
            (frame.frameBody as FrameBodyUSLT).setLyric(value)
        } else if (frame.frameBody is FrameBodyWOAR) {
            (frame.frameBody as FrameBodyWOAR).setUrlLink(value)
        } else if (frame.frameBody is AbstractFrameBodyTextInfo) {
            (frame.frameBody as AbstractFrameBodyTextInfo).setText(value)
        } else if (frame.frameBody is FrameBodyPOPM) {
            (frame.frameBody as FrameBodyPOPM).parseString(value)
        } else if (frame.frameBody is FrameBodyIPLS) {
            if (formatKey.subId != null) {
                ((frame.frameBody) as FrameBodyIPLS).addPair(
                    formatKey.subId,
                    value
                )
            } else {
                if (values.size >= 2) {
                    ((frame.frameBody) as FrameBodyIPLS).addPair(values[0], values[1])
                } else {
                    ((frame.frameBody) as FrameBodyIPLS).addPair(values[0])
                }
            }
        } else if (frame.frameBody is FrameBodyTIPL) {
            ((frame.frameBody) as? FrameBodyTIPL)?.addPair(formatKey.subId, value)
        } else if (frame.frameBody is FrameBodyTMCL) {
            if (values.size >= 2) {
                ((frame.frameBody) as FrameBodyTMCL).addPair(values[0], values[1])
            } else {
                ((frame.frameBody) as FrameBodyTMCL).addPair(values[0])
            }
        } else if ((frame.frameBody is FrameBodyAPIC) ||
            (frame.frameBody is FrameBodyPIC)
        ) {
            throw UnsupportedOperationException(
                ErrorMessage.ARTWORK_CANNOT_BE_CREATED_WITH_THIS_METHOD.getMsg()
            )
        } else {
            error("Field with key of:${formatKey.frameId}:does not accept cannot parse data:$value")
        }
        return frame
    }

    abstract fun getFrameAndSubIdFromGenericKey(genericKey: GenericFieldKey?): FrameAndSubId

    // TODO
    /**
     * Maps the generic key to the id3 key and return the list of values for this field as strings
     *
     * @param genericKey
     * @return
     */
    override fun getAll(genericKey: GenericFieldKey): List<String> {
        // Special case here because the generic key to frameid/subid mapping is identical for trackno versus tracktotal
        // and discno versus disctotal so we have to handle here, also want to ignore index parameter.
        val fields = getFields(genericKey)
        val values = mutableListOf<String>()
        if (ID3NumberTotalFields.isNumber(genericKey)) {
            if (fields.isNotEmpty()) {
                val frame = fields.get(0) as AbstractID3v2Frame
                (frame.frameBody as? AbstractFrameBodyNumberTotal)?.getNumberAsText()?.also { v -> values.add(v) }
            }
            return values
        } else if (ID3NumberTotalFields.isTotal(genericKey)) {
            if (fields.isNotEmpty()) {
                val frame = fields.get(0) as AbstractID3v2Frame
                (frame.frameBody as? AbstractFrameBodyNumberTotal)?.getTotalAsText()?.also { t -> values.add(t) }
            }
            return values
        } else if (genericKey == GenericFieldKey.RATING) {
            if (fields.isNotEmpty()) {
                val frame = fields.get(0) as AbstractID3v2Frame
                values.add(
                    java.lang.String.valueOf((frame.frameBody as FrameBodyPOPM).getRating())
                )
            }
            return values
        } else {
            return this.doGetValues(getFrameAndSubIdFromGenericKey(genericKey))
        }
    }

    /**
     * Retrieve the first value that exists for this generic key
     *
     * @param genericKey
     * @return
     */
    override fun getFirst(genericKey: GenericFieldKey?): String? {
        return genericKey?.let { id -> getValue(id, 0) }
    }

    /**
     * Retrieve the first value that exists for this identifier
     *
     *
     * If the value is a String it returns that, otherwise returns a summary of the fields information
     *
     * @param id
     * @return
     */
    override fun getFirst(id: String): String? {
        return getFirstField(id)?.let { id -> getTextValueForFrame(id) } ?: ""
    }

    fun setFrame(frame: AbstractID3v2Frame) {
        frame.getIdentifier()?.also { id -> frameMap[id] = frame }
    }

    /**
     * Retrieve the value that exists for this generic key and this index
     *
     *
     * Have to do some special mapping for certain generic keys because they share frame
     * with another generic key.
     *
     * @param genericKey
     * @return
     */
    open fun getValue(genericKey: GenericFieldKey, index: Int): String? {
        // Special case here because the generic key to frameid/subid mapping is identical for trackno versus tracktotal
        // and discno versus disctotal so we have to handle here, also want to ignore index parameter.
        if (ID3NumberTotalFields.isNumber(genericKey) ||
            ID3NumberTotalFields.isTotal(genericKey)
        ) {
            val fields = getFields(genericKey)
            if (fields.isNotEmpty()) {
                // Should only be one frame so ignore index value, and we ignore multiple values within the frame
                // it would make no sense if it existed.
                val frame = fields.get(0) as AbstractID3v2Frame
                if (ID3NumberTotalFields.isNumber(genericKey)) {
                    return (frame.frameBody as AbstractFrameBodyNumberTotal
                            ).getNumberAsText()
                } else if (ID3NumberTotalFields.isTotal(genericKey)) {
                    return (frame.frameBody as AbstractFrameBodyNumberTotal
                            ).getTotalAsText()
                }
            } else {
                return ""
            }
        } else if (genericKey == GenericFieldKey.RATING) {
            val fields = getFields(genericKey)
            if (fields.size > index) {
                val frame = fields.get(index) as AbstractID3v2Frame
                return (frame.frameBody as FrameBodyPOPM).getRating().toString()
            } else {
                return ""
            }
        }

        val frameAndSubId = getFrameAndSubIdFromGenericKey(genericKey)
        return doGetValueAtIndex(frameAndSubId, index)
    }

    /**
     * Get field(s) for this generic key
     *
     *
     * This will return the number of underlying frames of this type, for example if you have added two TCOM field
     * values these will be stored within a single frame so only one field will be returned not two. This can be
     * confusing because getValues() would return two values.
     *
     * @param genericKey
     *
     * @return List<TagField>
     */
    override fun getFields(genericKey: GenericFieldKey?): List<TagField> {
        return getFields(getFrameAndSubIdFromGenericKey(genericKey).frameId)
    }

    /**
     * Retrieve the values that exists for this id3 frame id
     */
    @Suppress("UNCHECKED_CAST")
    open fun getFields(id: String): List<TagField> {
        return when (val o = getFrame(id)) {
            null -> listOf()
            is List<*> -> (o as List<TagField>).toList()
            is AbstractID3v2Frame -> listOf(o as TagField)
            else -> throw RuntimeException("Found entry in frameMap that was not a frame or a list:$o")
        }
    }

    /**
     * Does this tag contain a field with the specified id
     *
     * @see Tag.hasField
     */
    override fun hasField(id: String): Boolean {
        return hasFrame(id)
    }

    /**
     * Create a list of values for this (sub)frame
     *
     *
     * This method  does all the complex stuff of splitting multiple values in one frame into separate values.
     *
     * @param formatKey
     * @return
     */
    fun doGetValues(formatKey: FrameAndSubId): List<String> {
        val values = mutableListOf<String>()
        if (formatKey.subId != null) {
            // Get list of frames that this uses
            getFields(formatKey.frameId).forEach { field ->
                val next = (field as AbstractID3v2Frame).frameBody
                if (next is FrameBodyTXXX) {
                    if (next.getDescription() == formatKey.subId
                    ) {
                        values.addAll((next.getValues()))
                    }
                } else if (next is FrameBodyWXXX) {
                    if (next.getDescription() == formatKey.subId
                    ) {
                        values.addAll((next.getUrlLinks()))
                    }
                } else if (next is FrameBodyCOMM) {
                    if (next.getDescription() == formatKey.subId
                    ) {
                        values.addAll((next.getValues()))
                    }
                } else if (next is FrameBodyUFID) {
                    if (next.getOwner() == formatKey.subId) {
                        if (next.getUniqueIdentifier() != null) {
                            values.add(String(next.getUniqueIdentifier() ?: byteArrayOf()))
                        }
                    }
                } else if (next is AbstractFrameBodyPairs) {
                    next.getPairing()?.mapping?.forEach { entry ->
                        if (entry.first == formatKey.subId) {
                            values.add(entry.second)
                        }
                    }
                } else {
                    error("Need to implement getFields(GenericFieldKey genericKey) for:${next?.javaClass}")
                }
            }
        } else if ((formatKey.genericKey == GenericFieldKey.PERFORMER) ||
            (formatKey.genericKey == GenericFieldKey.INVOLVED_PERSON)
        ) {
            getFields(formatKey.frameId).forEach { field ->
                val next = (field as AbstractID3v2Frame).frameBody
                if (next is AbstractFrameBodyPairs) {
                    next.getPairing()?.mapping?.forEach { entry ->
                        if (!StandardIPLSKey.isKey(entry.first)) {
                            if (!entry.second.isEmpty()) {
                                if (!entry.first.isEmpty()) {
                                    values.add(entry.second)
                                } else {
                                    values.add(entry.second)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            getFields(formatKey.frameId).forEach { next ->
                val frame = next as? AbstractID3v2Frame
                if (frame != null) {
                    val fb = frame.frameBody
                    if (fb is AbstractFrameBodyTextInfo) {
                        values.addAll(fb.getValues())
                    } else {
                        getTextValueForFrame(frame)?.also { v -> values.add(v) }
                    }
                }
            }
        }
        return values
    }

    /**
     * @param frame
     * @return
     */
    private fun getTextValueForFrame(frame: AbstractID3v2Frame): String? {
        return frame.frameBody?.getUserFriendlyValue()
    }

    /**
     * Get the value at the index, we massage the values so that the index as used in the generic interface rather
     * than simply taking the frame index. For example if two composers have been added then then they can be retrieved
     * individually using index=0, index=1 despite the fact that both internally will be stored in a single TCOM frame.
     *
     * @param formatKey
     * @param index     the index specified by the user
     * @return
     */
    fun doGetValueAtIndex(formatKey: FrameAndSubId, index: Int): String? {
        val values = doGetValues(formatKey)
        if (values.size > index) {
            return values.get(index)
        }
        return ""
    }

    /**
     * Create a link to artwork, this is not recommended because the link may be broken if the mp3 or image
     * file is moved
     *
     * @param url specifies the link, it could be a local file or could be a full url
     * @return
     */
    fun createLinkedArtworkField(url: String): TagField {
        val frame = createFrame(
            getFrameAndSubIdFromGenericKey(GenericFieldKey.COVER_ART).frameId
        )
        val body = frame.frameBody
        if (body is FrameBodyAPIC) {
            body.setObjectValue(
                DataTypes.OBJ_PICTURE_DATA,
                url.toByteArray(StandardCharsets.ISO_8859_1)
            )
            body.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, PictureTypes.DEFAULT_ID)
            body.setObjectValue(DataTypes.OBJ_MIME_TYPE, FrameBodyAPIC.IMAGE_IS_URL)
            body.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
        } else if (body is FrameBodyPIC) {
            body.setObjectValue(
                DataTypes.OBJ_PICTURE_DATA,
                url.toByteArray(StandardCharsets.ISO_8859_1)
            )
            body.setObjectValue(DataTypes.OBJ_PICTURE_TYPE, PictureTypes.DEFAULT_ID)
            body.setObjectValue(
                DataTypes.OBJ_IMAGE_FORMAT,
                FrameBodyAPIC.IMAGE_IS_URL
            )
            body.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
        }
        return frame
    }

    /**
     * Some frames are used to store a number/total value, we have to consider both values when requested to delete a
     * key relating to one of them
     *
     * @param formatKey
     * @param numberFieldKey
     * @param totalFieldKey
     * @param deleteNumberFieldKey
     */
    private fun deleteNumberTotalFrame(
        formatKey: FrameAndSubId,
        numberFieldKey: GenericFieldKey,
        totalFieldKey: GenericFieldKey,
        deleteNumberFieldKey: Boolean
    ) {
        if (deleteNumberFieldKey) {
            val total = this.getFirst(totalFieldKey)
            if (isEmpty()) {
                doDeleteTagField(formatKey)
            } else {
                val frame = this.getFrame(
                    formatKey.frameId
                ) as AbstractID3v2Frame
                val frameBody =
                    frame.frameBody as AbstractFrameBodyNumberTotal
                frameBody.setNumber(0)
            }
        } else {
            if (isEmpty()) {
                doDeleteTagField(formatKey)
            } else {
                val frame = this.getFrame(
                    formatKey.frameId
                ) as AbstractID3v2Frame
                val frameBody =
                    frame.frameBody as AbstractFrameBodyNumberTotal
                frameBody.setTotal(0)
            }
        }
    }

    /**
     * Delete fields with this generic key
     *
     *
     * If generic key maps to multiple frames then do special processing here rather doDeleteField()
     *
     * @param genericKey
     */
    override fun deleteField(genericKey: GenericFieldKey) {
        val formatKey = getFrameAndSubIdFromGenericKey(genericKey)

        when (genericKey) {
            GenericFieldKey.TRACK -> deleteNumberTotalFrame(
                formatKey,
                GenericFieldKey.TRACK,
                GenericFieldKey.TRACK_TOTAL,
                true
            )

            GenericFieldKey.TRACK_TOTAL -> deleteNumberTotalFrame(
                formatKey,
                GenericFieldKey.TRACK,
                GenericFieldKey.TRACK_TOTAL,
                false
            )

            GenericFieldKey.DISC_NO -> deleteNumberTotalFrame(
                formatKey,
                GenericFieldKey.DISC_NO,
                GenericFieldKey.DISC_TOTAL,
                true
            )

            GenericFieldKey.DISC_TOTAL -> deleteNumberTotalFrame(
                formatKey,
                GenericFieldKey.DISC_NO,
                GenericFieldKey.DISC_TOTAL,
                false
            )

            GenericFieldKey.MOVEMENT_NO -> deleteNumberTotalFrame(
                formatKey,
                GenericFieldKey.MOVEMENT_NO,
                GenericFieldKey.MOVEMENT_TOTAL,
                true
            )

            GenericFieldKey.MOVEMENT_TOTAL -> deleteNumberTotalFrame(
                formatKey,
                GenericFieldKey.MOVEMENT_NO,
                GenericFieldKey.MOVEMENT_TOTAL,
                false
            )

            else -> doDeleteTagField(formatKey)
        }
    }

    /**
     * Internal delete method, for deleting/modifying an individual ID3 frame
     *
     * @param formatKey
     */
    fun doDeleteTagField(formatKey: FrameAndSubId) {
        // Get list of frames that this uses
        val list = getFields(formatKey.frameId)
        list.forEach { field ->
            val next = (field as AbstractID3v2Frame).frameBody
            if (next is FrameBodyTXXX) {
                if (next.getDescription() == formatKey.subId
                ) {
                    if (list.size == 1) {
                        removeFrame(formatKey.frameId)
                    }
                }
            } else if (next is FrameBodyCOMM) {
                if (next.getDescription() == formatKey.subId
                ) {
                    if (list.size == 1) {
                        removeFrame(formatKey.frameId)
                    }
                }
            } else if (next is FrameBodyWXXX) {
                if (next.getDescription() == formatKey.subId
                ) {
                    if (list.size == 1) {
                        removeFrame(formatKey.frameId)
                    }
                }
            } else if (next is FrameBodyUFID) {
                if (next.getOwner() == formatKey.subId) {
                    if (list.size == 1) {
                        removeFrame(formatKey.frameId)
                    }
                }
            } else if (next is FrameBodyTIPL) {
                val nextPairing = next.getPairing()?.mapping
                    ?.filter { nextPair -> nextPair.first != formatKey.subId }
                if (nextPairing?.isEmpty() == true) {
                    removeFrame(formatKey.frameId)
                }
            } else if (next is FrameBodyIPLS) {
                val nextPairing = next.getPairing()?.mapping
                    ?.filter { nextPair -> nextPair.first != formatKey.subId }
                if (nextPairing?.isEmpty() == true) {
                    removeFrame(formatKey.frameId)
                }
            } else {
                throw RuntimeException(
                    "Need to implement getFields(GenericFieldKey genericKey) for:" +
                            next?.javaClass
                )
            }
        }
    }

    /**
     * Remove frame(s) with this identifier from tag
     *
     * @param identifier frameId to look for
     */
    fun removeFrame(identifier: String?) {
        log.debug("Removing frame with identifier:$identifier")
        frameMap.remove(identifier)
    }

    /**
     * Does this tag contain a field with the specified key
     *
     * @param genericKey The field id to look for.
     * @return true if has field , false if does not or if no mapping for key exists
     */
    override fun hasField(genericKey: GenericFieldKey): Boolean {
        try {
            return getFirstField(genericKey) != null
        } catch (knfe: KeyNotFoundException) {
            log.error(knfe.message, knfe)
            return false
        }
    }

    override fun getFirstField(genericKey: GenericFieldKey): TagField? {
        return getFields(genericKey).firstOrNull()
    }

    /**
     * Retrieve the first tag field that exists for this identifier
     *
     * @param id
     * @return tag field or null if doesn't exist
     */
    @Suppress("UNCHECKED_CAST")
    override fun getFirstField(id: String?): AbstractID3v2Frame? {
        val obj = getFrame(id) ?: return null
        return if (obj is MutableList<*>) {
            (obj as? MutableList<AbstractID3v2Frame>)?.get(0)
        } else {
            obj as AbstractID3v2Frame
        }
    }

    override fun hasCommonFields(): Boolean {
        return true
    }

    // TODO is this a special field?
    /**
     * Is this tag empty
     *
     * @see org.jaudiotagger.tag.Tag.isEmpty
     */
    override fun isEmpty(): Boolean {
        return frameMap.size == 0
    }

    /**
     * Create a new field
     *
     *
     * Only MUSICIAN field make use of Varargs values field
     *
     * @param genericKey is the generic key
     * @param values
     * @return
     */
    override fun createField(genericKey: GenericFieldKey, vararg values: String): TagField {
        val value: String = values[0]
        val formatKey = getFrameAndSubIdFromGenericKey(genericKey)

        // FrameAndSubId does not contain enough info for these fields to be able to work out what to update
        // that is why we need the extra processing here instead of doCreateTagField()
        if (ID3NumberTotalFields.isNumber(genericKey)) {
            val frame = createFrame(formatKey.frameId)
            val framebody =
                frame.frameBody as AbstractFrameBodyNumberTotal
            framebody.setNumber(value)
            return frame
        } else if (ID3NumberTotalFields.isTotal(genericKey)) {
            val frame = createFrame(formatKey.frameId)
            val framebody =
                frame.frameBody as AbstractFrameBodyNumberTotal
            framebody.setTotal(value)
            return frame
        } else {
            return doCreateTagField(formatKey, *values)
        }
    }

    override fun createCompilationField(value: Boolean): TagField {
        if (value) {
            return createField(GenericFieldKey.IS_COMPILATION, "1")
        } else {
            return createField(GenericFieldKey.IS_COMPILATION, "0")
        }
    }

    override fun getArtworkList(): List<Artwork> {
        return getFields(GenericFieldKey.COVER_ART).mapNotNull { next ->
            when (val coverArt = (next as AbstractID3v2Frame).frameBody) {
                is FrameBodyPIC -> Artwork(
                    binaryData = coverArt.getImageData(),
                    mimeType = coverArt.getMimeType(),
                    isLinked = coverArt.isImageUrl(),
                    imageUrl = coverArt.getImageUrl(),
                    pictureType = coverArt.getPictureType(),
                )

                is FrameBodyAPIC -> Artwork(
                    binaryData = coverArt.getImageData(),
                    mimeType = coverArt.getMimeType(),
                    isLinked = coverArt.isImageUrl(),
                    imageUrl = coverArt.getImageUrl(),
                    pictureType = coverArt.getPictureType(),
                )

                else -> null
            }

        }
    }

    open fun createStructure() {
        createStructureHeader()
        createStructureBody()
    }

    fun createStructureHeader() {
        MP3File.tagFormatter?.addElement(
            TYPE_DUPLICATEBYTES,
            this.duplicateBytes
        )
        MP3File.tagFormatter?.addElement(
            TYPE_DUPLICATEFRAMEID,
            this.duplicateFrameId
        )
        MP3File.tagFormatter?.addElement(
            TYPE_EMPTYFRAMEBYTES,
            this.emptyFrameBytes
        )
        MP3File.tagFormatter?.addElement(
            TYPE_FILEREADSIZE,
            this.fileReadBytes
        )
        MP3File.tagFormatter?.addElement(
            TYPE_INVALIDFRAMES,
            this.invalidFrames
        )
    }

    @Suppress("UNCHECKED_CAST")
    fun createStructureBody() {
        MP3File.tagFormatter?.openHeadingElement(TYPE_BODY, "")

        var frame: AbstractID3v2Frame
        for (o in frameMap.values) {
            if (o is AbstractID3v2Frame) {
                frame = o
                frame.createStructure()
            } else {
                val multiFrames = o as List<AbstractID3v2Frame>
                multiFrames.forEach { frame ->
                    frame.createStructure()
                }
            }
        }
        MP3File.tagFormatter?.closeHeadingElement(TYPE_BODY)
    }

    /**
     * @return iterator of all fields, multiple values for the same Id (e.g multiple TXXX frames) count as separate
     * fields
     */
    @Suppress("UNCHECKED_CAST")
    override fun getFields(): MutableIterator<TagField?> {
        // Iterator of each different frameId in this tag
        val it = this.frameMap.entries.iterator()

        // Iterator used by hasNext() so doesn't effect next()
        val itHasNext = this.frameMap.entries.iterator()

        return object : MutableIterator<TagField?> {
            var latestEntry: MutableMap.MutableEntry<String, Any>? = null

            // this iterates through frames through for a particular frameId
            private var fieldsIt: MutableIterator<TagField?>? = null

            // TODO assumes if have entry its valid, but what if empty list but very different to check this
            // without causing a side effect on next() so leaving for now
            override fun hasNext(): Boolean {
                // Check Current frameId, does it contain more values
                if (fieldsIt != null) {
                    if (fieldsIt?.hasNext() == true) {
                        return true
                    }
                }

                // No remaining entries return false
                if (!itHasNext.hasNext()) {
                    return false
                }

                // Issue #236
                // TODO assumes if have entry its valid, but what if empty list but very different to check this
                // without causing a side effect on next() so leaving for now
                return itHasNext.hasNext()
            }

            override fun next(): TagField? {
                // Hasn't been initialized yet
                if (fieldsIt == null) {
                    changeIt()
                }

                fieldsIt?.let {
                    if (!it.hasNext()) {
                        changeIt()
                    }
                }

                if (fieldsIt == null) {
                    throw NoSuchElementException()
                }
                return fieldsIt?.next()
            }

            fun changeIt() {
                if (!it.hasNext()) {
                    return
                }

                while (it.hasNext()) {
                    val e = it.next()
                    latestEntry = itHasNext.next()
                    if (e.value is MutableList<*>) {
                        val l = e.value as MutableList<TagField>
                        // If list is empty (which it shouldn't be) we skip over this entry
                        if (l.size == 0) {
                            continue
                        } else {
                            fieldsIt = l.iterator()
                            break
                        }
                    } else {
                        // TODO must be a better way
                        val l: MutableList<TagField> = mutableListOf<TagField>()
                        l.add(e.value as TagField)
                        fieldsIt = l.iterator()
                        break
                    }
                }
            }

            override fun remove() {
                fieldsIt?.remove()
            }
        }
    }

    override fun setField(genericKey: GenericFieldKey, vararg values: String) {
        setField(createField(genericKey, *values))
    }

    /**
     * Set Field
     *
     * @param field
     * @throws FieldDataInvalidException
     */
    @Suppress("UNCHECKED_CAST")
    override fun setField(field: TagField) {
        if ((field !is AbstractID3v2Frame) && (field !is AggregatedFrame)) {
            error("Field $field is not of type AbstractID3v2Frame nor AggregatedFrame")
        }

        val identifier = field.getIdentifier() ?: error("No identifier")
        if (field is AbstractID3v2Frame) {
            val obj = frameMap[identifier]

            // If no frame of this type exist or if multiples are not allowed
            if (obj == null) {
                frameMap[identifier] = field
            } else if (obj is AbstractID3v2Frame) {
                val frames = mutableListOf<AbstractID3v2Frame>()
                frames.add(obj)
                mergeDuplicateFrames(field, frames)
            } else if (obj is MutableList<*>) {
                mergeDuplicateFrames(field, obj as MutableList<AbstractID3v2Frame>)
            }
        } else {
            frameMap[identifier] = field
        }
    }

    /**
     * Add frame taking into account existing frames of the same type
     *
     * @param newFrame
     * @param frames
     */
    fun mergeDuplicateFrames(
        newFrame: AbstractID3v2Frame,
        frames: MutableList<AbstractID3v2Frame>
    ) {
        val li = frames.listIterator()
        val identifier = newFrame.getIdentifier() ?: error("No identifier")
        while (li.hasNext()) {
            val nextFrame = li.next()
            if (newFrame.frameBody is FrameBodyTXXX) {
                // Value with matching key exists so replace
                if ((newFrame.frameBody as FrameBodyTXXX).getDescription().equals(
                        (nextFrame.frameBody as FrameBodyTXXX).getDescription()
                    )
                ) {
                    li.set(newFrame)
                    frameMap[identifier] = frames
                    return
                }
            } else if (newFrame.frameBody is FrameBodyWXXX) {
                // Value with matching key exists so replace
                if ((newFrame.frameBody as FrameBodyWXXX).getDescription().equals(
                        (nextFrame.frameBody as FrameBodyWXXX).getDescription()
                    )
                ) {
                    li.set(newFrame)
                    frameMap[identifier] = frames
                    return
                }
            } else if (newFrame.frameBody is FrameBodyCOMM) {
                if ((newFrame.frameBody as FrameBodyCOMM).getDescription().equals(
                        (nextFrame.frameBody as FrameBodyCOMM).getDescription()
                    )
                ) {
                    li.set(newFrame)
                    frameMap[identifier] = frames
                    return
                }
            } else if (newFrame.frameBody is FrameBodyUFID) {
                if ((newFrame.frameBody as FrameBodyUFID).getOwner().equals(
                        (nextFrame.frameBody as FrameBodyUFID).getOwner()
                    )
                ) {
                    li.set(newFrame)
                    frameMap[identifier] = frames
                    return
                }
            } else if (newFrame.frameBody is FrameBodyUSLT) {
                if ((newFrame.frameBody as FrameBodyUSLT).getDescription().equals(
                        (nextFrame.frameBody as FrameBodyUSLT).getDescription()
                    )
                ) {
                    li.set(newFrame)
                    frameMap[identifier] = frames
                    return
                }
            } else if (newFrame.frameBody is FrameBodyPOPM) {
                if ((newFrame.frameBody as FrameBodyPOPM).getEmailToUser().equals(
                        (nextFrame.frameBody as FrameBodyPOPM).getEmailToUser()
                    )
                ) {
                    li.set(newFrame)
                    frameMap[identifier] = frames
                    return
                }
            } else if (newFrame.frameBody is AbstractFrameBodyNumberTotal) {
                mergeNumberTotalFrames(newFrame, nextFrame)
                return
            } else if (newFrame.frameBody is AbstractFrameBodyPairs) {
                val frameBody = newFrame.frameBody as AbstractFrameBodyPairs
                val existingFrameBody = nextFrame.frameBody as AbstractFrameBodyPairs
                existingFrameBody.addPair(frameBody.getText())
                return
            }
        }

        if (!isMultipleAllowed(identifier)) {
            frameMap[identifier] = newFrame
        } else {
            // No match found so addField new one
            frames.add(newFrame)
            frameMap[identifier] = frames
        }
    }

    abstract fun isMultipleAllowed(identifier: String?): Boolean

    /**
     * All Number/Count frames  are treated the same (TCK, TPOS, MVNM)
     *
     * @param newFrame
     * @param nextFrame
     */
    fun mergeNumberTotalFrames(
        newFrame: AbstractID3v2Frame,
        nextFrame: AbstractID3v2Frame
    ) {
        val newBody =
            newFrame.frameBody as AbstractFrameBodyNumberTotal
        val oldBody =
            nextFrame.frameBody as AbstractFrameBodyNumberTotal

        if (newBody.getNumber() != null && (newBody.getNumber()?:0) > 0) {
            oldBody.setNumber(newBody.getNumberAsText())
        }

        if (newBody.getTotal() != null && (newBody.getTotal()?:0) > 0) {
            oldBody.setTotal(newBody.getTotalAsText())
        }
    }

    /**
     * Count number of frames/fields in this tag
     *
     * @return
     */
    fun getFieldCount(): Int {
        val toList = getFields().asSequence().toList()
        return toList.size
    }

    override fun toString(): String {
        return "Tag content [${this::class.simpleName}]:\n${
            getFields().asSequence().joinToString("\n") { field -> "\t${field?.getIdentifier() ?: "UNSET"}:$field" }
        }"
    }
}