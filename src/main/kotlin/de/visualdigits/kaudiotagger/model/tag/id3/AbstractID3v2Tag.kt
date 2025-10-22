package de.visualdigits.kaudiotagger.model.tag.id3

import de.visualdigits.kaudiotagger.model.datatype.types.FileSystemMessage
import de.visualdigits.kaudiotagger.model.exceptions.UnableToCreateFileException
import de.visualdigits.kaudiotagger.model.exceptions.UnableToModifyFileException
import de.visualdigits.kaudiotagger.model.exceptions.UnableToRenameFileException
import de.visualdigits.kaudiotagger.model.frame.AggregatedFrame
import de.visualdigits.kaudiotagger.model.frame.TyerTdatAggregatedFrame
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyEncrypted
import de.visualdigits.kaudiotagger.model.frame.id3.AbstractID3v2Frame
import de.visualdigits.kaudiotagger.model.kframe.ID3v22KFrame
import de.visualdigits.kaudiotagger.model.tag.Tag
import de.visualdigits.kaudiotagger.util.ErrorMessage
import de.visualdigits.kaudiotagger.util.ID3SyncSafeInteger
import de.visualdigits.kaudiotagger.util.Utils
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.io.RandomAccessFile
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.channels.FileLock
import java.util.TreeSet

abstract class AbstractID3v2Tag(
    val buffer: ByteBuffer? = null
) : AbstractID3Tag(), Tag {

    companion object {
        //Tag ID as held in file
        val TAG_ID: ByteArray = byteArrayOf('I'.code.toByte(), 'D'.code.toByte(), '3'.code.toByte())
        const val TAGID: String = "ID3"

        //The tag header is the same for ID3v2 versions
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

        //The max size we try to write in one go to avoid out of memory errors (10mb)
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
            //So we have a tag
            val tagHeader = ByteArray(AbstractID3v2Tag.FIELD_TAG_SIZE_LENGTH)
            raf.seek(
                raf.getFilePointer() +
                        AbstractID3v2Tag.FIELD_TAGID_LENGTH +
                        AbstractID3v2Tag.FIELD_TAG_MAJOR_VERSION_LENGTH +
                        AbstractID3v2Tag.FIELD_TAG_MINOR_VERSION_LENGTH +
                        AbstractID3v2Tag.FIELD_TAG_FLAG_LENGTH
            )
            raf.read(tagHeader)
            val bb = ByteBuffer.wrap(tagHeader)

            val size = ID3SyncSafeInteger.bufferToValue(bb)
            raf.seek((size + AbstractID3v2Tag.TAG_HEADER_LENGTH).toLong())
            
            return true
        }

        /**
         * True if files has a ID3v2 header
         *
         * @param raf
         * @return
         * @throws IOException
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
            
            return s == AbstractID3v2Tag.TAGID
        }

        /**
         * Checks to see if the file contains an ID3tag and if so return its size as reported in
         * the tag header  and return the size of the tag (including header), if no such tag exists return
         * zero.
         *
         * @param file
         * @return the end of the tag in the file or zero if no tag exists.
         * @throws java.io.IOException
         */
        fun getV2TagSizeIfExists(file: File): Long {
            //Files
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
            //Read possible Tag header  Byte Buffer
            //ID3 identifier
            val tagIdentifier = ByteArray(FIELD_TAGID_LENGTH)
            bb.get(tagIdentifier, 0, FIELD_TAGID_LENGTH)
            if (!tagIdentifier.contentEquals(TAG_ID)) {
                return 0
            }

            //Is it valid Major Version
            val majorVersion = bb.get()
            if (
                (majorVersion != ID3v22Tag.MAJOR_VERSION) &&
                (majorVersion != ID3v23Tag.MAJOR_VERSION) &&
                (majorVersion != ID3v24Tag.MAJOR_VERSION)
            ) {
                return 0
            }

            //Skip Minor Version
            bb.get()

            //Skip Flags
            bb.get()

            //Get size as recorded in frame header
            var frameSize = ID3SyncSafeInteger.bufferToValue(bb).toLong()

            //addField header size to frame size
            frameSize += TAG_HEADER_LENGTH

            return frameSize
        }
    }

    /**
     * Map of all frames for this tag
     */
    var frameMap: MutableMap<String, Any> = mutableMapOf()

    /**
     * Map of all encrypted frames, these cannot be unencrypted by jaudiotagger
     */
    var encryptedFrameMap: MutableMap<String, Any> = mutableMapOf()
    var duplicateFrameId: String = ""
    var duplicateBytes: Int = 0
    var emptyFrameBytes: Int = 0
    var fileReadSize: Int = 0
    var invalidFrames: Int = 0

    //Start location of this chunk
    //TODO currently only used by ID3 embedded into Wav/Aiff but shoudl be extended to mp3s
    private var startLocationInFile: Long = 0

    //End location of this chunk
    private var endLocationInFile: Long = 0

    /**
     * Return tag size based upon the sizes of the tags rather than the physical
     * no of bytes between start of ID3Tag and start of Audio Data.Should be extended
     * by subclasses to include header.
     *
     * @return size of the tag
     */
    override fun getSize(): Int {
        var sum = 0
        frameMap.values.forEach { frame ->
            when (frame) {
                is AbstractID3v2Frame -> frame.getSize()
                is AggregatedFrame -> frame.frames.sumOf { f -> f.getSize() }
                is MutableList<*> -> (frame as? java.util.ArrayList<AbstractID3v2Frame>)?.let { f -> f.sumOf { e -> e.getSize() } }
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
        //Primitives type variables common to all IDv2 Tags
        this.duplicateFrameId = copyObject.duplicateFrameId
        this.duplicateBytes = copyObject.duplicateBytes
        this.emptyFrameBytes = copyObject.emptyFrameBytes
        this.fileReadSize = copyObject.fileReadSize
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
        if (frameMap.containsKey(newFrame.getIdentifier())) {
            val o: Any = frameMap[newFrame.getIdentifier()]!!
            when (o) {
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
                    log.error("Unknown frame class:discarding:" + o.javaClass)
                }
            }
        } else {
            frameMap[newFrame.getIdentifier()?:""] = newFrame
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
        val list: MutableList<AbstractID3v2Frame> = mutableListOf()
        list.add(existingFrame)
        list.add(newFrame)
        frameMap[newFrame.getIdentifier()?:""] = list
    }

    /**
     * Copy frames from another tag,
     *
     * @param copyObject
     */
    //TODO Copy Encrypted frames needs implementing
    fun copyFrames(copyObject: AbstractID3v2Tag) {
        frameMap = mutableMapOf()
        encryptedFrameMap = mutableMapOf()

        //Copy Frames that are a valid 2.4 type
        for (o1 in copyObject.frameMap.keys) {
            val id = o1
            val o = copyObject.frameMap[id]
            //SingleFrames
            when (o) {
                is AbstractID3v2Frame -> {
                    addFrame(o)
                }
                is TyerTdatAggregatedFrame -> {
                    for (next in o.getFrames()) {
                        addFrame(next)
                    }
                }
                is java.util.ArrayList<*> -> {
                    for (frame in o as java.util.ArrayList<AbstractID3v2Frame>) {
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
    //TODO:This method is problematic because sometimes it returns a list and sometimes a frame, we need to
    //replace with two separate methods as in the tag interface.
    fun getFrame(identifier: String): Any {
        return frameMap.get(identifier)!!
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
                val o = frameMap.get(key)
                o as? List<*> ?: listOf(o)
            } else null
        }.flatten().toSet()
    }

    /**
     * Decides what to with the frame that has just been read from file.
     * If the frame is an allowable duplicate frame and is a duplicate we add all
     * frames into an ArrayList and add the ArrayList to the HashMap. if not allowed
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
        if ((ID3v22KFrame.isMultipleAllowed(frameId)) ||
            (ID3v22KFrame.isMultipleAllowed(frameId)) ||
            (ID3v22KFrame.isMultipleAllowed(frameId))
        ) {
            //If a frame already exists of this type
            if (map.containsKey(frameId)) {
                val o = map[frameId]
                if (o is ArrayList<*>) {
                    val multiValues = o as ArrayList<AbstractID3v2Frame>
                    multiValues.add(next)
                    log.debug("Adding Multi Frame(1)$frameId")
                } else {
                    val multiValues = ArrayList<AbstractID3v2Frame>()
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
            //If we have multiple duplicate frames in a tag separate them with semicolons
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
     * @throws IOException
     */
    protected fun writeFramesToBuffer(): ByteArrayOutputStream {
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
    private fun writeFramesToBufferStream(
        map: MutableMap<String, Any>,
        bodyBuffer: ByteArrayOutputStream
    ) {
        //Sort keys into Preferred Order
        val sortedWriteOrder = TreeSet(
            getPreferredFrameOrderComparator()
        )
        sortedWriteOrder.addAll(map.keys)

        var frame: AbstractID3v2Frame
        for (id in sortedWriteOrder) {
            val o = map.get(id)
            if (o is AbstractID3v2Frame) {
                frame = o
                frame.write(bodyBuffer!!)
            } else if (o is AggregatedFrame) {
                for (next in o.getFrames()) {
                    next.write(bodyBuffer!!)
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
     * @throws IOException
     */
    abstract fun write(file: File, audioStartByte: Long): Long

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
    protected fun calculateTagSize(tagSize: Int, preferredSize: Int): Int {
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
     * @throws IOException
     */
    protected fun writeBufferToFile(
        file: File,
        headerBuffer: ByteBuffer?,
        bodyByteBuffer: ByteArray,
        padding: Int,
        sizeIncPadding: Int,
        audioStartLocation: Long
    ) {
        var fc: FileChannel? = null
        var fileLock: FileLock? = null

        //We need to adjust location of audio file if true
        if (sizeIncPadding > audioStartLocation) {
            log.debug("Adjusting Padding")
            adjustPadding(file, sizeIncPadding, audioStartLocation)
        }

        try {
            fc = RandomAccessFile(file, "rw").getChannel()
            fileLock = getFileLockForWriting(fc, file.getPath())
            fc.write(headerBuffer)
            fc.write(ByteBuffer.wrap(bodyByteBuffer))
            fc.write(ByteBuffer.wrap(ByteArray(padding)))
        } catch (fe: FileNotFoundException) {
            log.error(fe.message, fe)
            if (fe.message?.contains(FileSystemMessage.ACCESS_IS_DENIED.message)?:false ||
                fe.message?.contains(FileSystemMessage.PERMISSION_DENIED.message)?:false
            ) {
                log.error(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(
                        file.getPath()
                    )
                )
                throw UnableToModifyFileException(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(
                        file.getPath()
                    )
                )
            } else {
                log.error(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(
                        file.getPath()
                    )
                )
                throw UnableToCreateFileException(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(
                        file.getPath()
                    )
                )
            }
        } catch (ioe: IOException) {
            log.error(ioe.message, ioe)
            if (ioe.message == FileSystemMessage.ACCESS_IS_DENIED.message
            ) {
                log.error(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(
                        file.getParentFile().getPath()
                    )
                )
                throw UnableToModifyFileException(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(
                        file.getParentFile().getPath()
                    )
                )
            } else {
                log.error(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(
                        file.getParentFile().getPath()
                    )
                )
                throw UnableToCreateFileException(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_OPEN_FILE_FOR_EDITING.getMsg(
                        file.getParentFile().getPath()
                    )
                )
            }
        } finally {
            if (fc != null) {
                if (fileLock != null) {
                    fileLock.release()
                }
                fc.close()
            }
        }
    }

    /**
     * Get file lock for writing too file
     *
     *
     * TODO:this appears to have little effect on Windows Vista
     *
     * @param fileChannel
     * @param filePath
     * @return lock or null if locking is not supported
     * @throws IOException                                    if unable to get lock because already locked by another program
     * @throws java.nio.channels.OverlappingFileLockException if already locked by another thread in the same VM, we dont catch this
     * because indicates a programming error
     */
    @Throws(IOException::class)
    protected fun getFileLockForWriting(
        fileChannel: FileChannel,
        filePath: String?
    ): FileLock? {
        log.debug("locking fileChannel for " + filePath)
        val fileLock: FileLock
        try {
            fileLock = fileChannel.tryLock()
        } catch (exception: IOException) { //Assumes locking is not supported on this platform so just returns null
            return null
        } catch (error: Error) { //#129 Workaround for https://bugs.openjdk.java.net/browse/JDK-8025619
            return null
        }

        //Couldnt getFields lock because file is already locked by another application
        if (fileLock == null) {
            throw IOException(
                ErrorMessage.GENERAL_WRITE_FAILED_FILE_LOCKED.getMsg(filePath)
            )
        }
        return fileLock
    }

    /**
     * Adjust the length of the  padding at the beginning of the MP3 file, this is only called when there is currently
     * not enough space before the start of the audio to write the tag.
     *
     *
     * A new file will be created with enough size to fit the `ID3v2` tag.
     * The old file will be deleted, and the new file renamed.
     *
     * @param paddingSize This is total size required to store tag before audio
     * @param audioStart
     * @param file        The file to adjust the padding length of
     * @throws FileNotFoundException if the file exists but is a directory
     * rather than a regular file or cannot be opened for any other
     * reason
     * @throws IOException           on any I/O error
     */
    fun adjustPadding(file: File, paddingSize: Int, audioStart: Long) {
        log.debug("Need to move audio file to accommodate tag")
        var fcIn: FileChannel? = null
        val fcOut: FileChannel?

        //Create buffer holds the necessary padding
        val paddingBuffer = ByteBuffer.wrap(ByteArray(paddingSize))

        //Create Temporary File and write channel, make sure it is locked
        val paddedFile: File?

        try {
            paddedFile = File.createTempFile(
                Utils.getBaseFilenameForTempFile(file),
                ".new",
                file.getParentFile()
            )
            log.debug(
                "Created temp file:" + paddedFile.getName() + " for " + file.getName()
            )
        } catch (ioe: IOException) { //Vista:Can occur if have Write permission on folder this file would be created in Denied
            log.error(ioe.message, ioe)
            if (ioe.message == FileSystemMessage.ACCESS_IS_DENIED.message
            ) {
                log.error(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(
                        file.getName(),
                        file.getParentFile().getPath()
                    )
                )
                throw UnableToCreateFileException(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(
                        file.getName(),
                        file.getParentFile().getPath()
                    )
                )
            } else {
                log.error(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(
                        file.getName(),
                        file.getParentFile().getPath()
                    )
                )
                throw UnableToCreateFileException(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_CREATE_TEMPORARY_FILE_IN_FOLDER.getMsg(
                        file.getName(),
                        file.getParentFile().getPath()
                    )
                )
            }
        }

        try {
            fcOut = FileOutputStream(paddedFile).getChannel()
        } catch (ioe: FileNotFoundException) { //Vista:Can occur if have special permission Create Folder/Append Data denied
            log.error(ioe.message, ioe)
            log.error(
                ErrorMessage.GENERAL_WRITE_FAILED_TO_MODIFY_TEMPORARY_FILE_IN_FOLDER.getMsg(
                    file.getName(),
                    file.getParentFile().getPath()
                )
            )
            throw UnableToModifyFileException(
                ErrorMessage.GENERAL_WRITE_FAILED_TO_MODIFY_TEMPORARY_FILE_IN_FOLDER.getMsg(
                    file.getName(),
                    file.getParentFile().getPath()
                )
            )
        }

        try {
            //Create read channel from original file
            //TODO lock so cant be modified by anything else whilst reading from it ?
            fcIn = FileInputStream(file).getChannel()

            //Write padding to new file (this is where the tag will be written to later)
            val written = fcOut.write(paddingBuffer).toLong()

            //Write rest of file starting from audio
            log.debug("Copying:" + (file.length() - audioStart) + "bytes")

            //If the amount to be copied is very large we split into 10MB lumps to try and avoid
            //out of memory errors
            val audiolength = file.length() - audioStart
            if (audiolength <= MAXIMUM_WRITABLE_CHUNK_SIZE) {
                fcIn.position(audioStart)
                val written2 = fcOut.transferFrom(fcIn, paddingSize.toLong(), audiolength)
                log.debug("Written padding:" + written + " Data:" + written2)
                if (written2 != audiolength) {
                    throw RuntimeException(
                        ErrorMessage.MP3_UNABLE_TO_ADJUST_PADDING.getMsg(
                            audiolength,
                            written2
                        )
                    )
                }
            } else {
                val noOfChunks = audiolength / MAXIMUM_WRITABLE_CHUNK_SIZE
                val lastChunkSize = audiolength % MAXIMUM_WRITABLE_CHUNK_SIZE
                var written2: Long = 0
                for (i in 0..<noOfChunks) {
                    written2 += fcIn.transferTo(
                        audioStart + (i * MAXIMUM_WRITABLE_CHUNK_SIZE),
                        MAXIMUM_WRITABLE_CHUNK_SIZE,
                        fcOut
                    )
                }
                written2 += fcIn.transferTo(
                    audioStart + (noOfChunks * MAXIMUM_WRITABLE_CHUNK_SIZE),
                    lastChunkSize,
                    fcOut
                )
                log.debug("Written padding:" + written + " Data:" + written2)
                if (written2 != audiolength) {
                    throw RuntimeException(
                        ErrorMessage.MP3_UNABLE_TO_ADJUST_PADDING.getMsg(
                            audiolength,
                            written2
                        )
                    )
                }
            }

            //Store original modification time
            val lastModified = file.lastModified()

            //Close Channels and locks
            if (fcIn != null) {
                if (fcIn.isOpen()) {
                    fcIn.close()
                }
            }

            if (fcOut != null) {
                if (fcOut.isOpen()) {
                    fcOut.close()
                }
            }

            //Replace file with paddedFile
            replaceFile(file, paddedFile)

            //Update modification time
            //TODO is this the right file ?
            paddedFile.setLastModified(lastModified)
        } catch (ure: UnableToRenameFileException) {
            paddedFile.delete()
            throw ure
        } finally {
            try {
                //Whatever happens ensure all locks and channels are closed/released
                if (fcIn != null) {
                    if (fcIn.isOpen()) {
                        fcIn.close()
                    }
                }

                if (fcOut != null) {
                    if (fcOut.isOpen()) {
                        fcOut.close()
                    }
                }
            } catch (e: Exception) {
                log.warn("Problem closing channels and locks:" + e.message, e)
            }
        }
    }

    /**
     * Replace originalFile with the contents of newFile
     *
     *
     * Both files must exist in the same folder so that there are no problems with filesystem mount points
     *
     * @param newFile
     * @param originalFile
     * @throws IOException
     */
    private fun replaceFile(originalFile: File, newFile: File) {
        var renameOriginalResult: Boolean
        //Rename Original File to make a backup in case problem with new file
        var originalFileBackup = File(
            originalFile.getAbsoluteFile().getParentFile().getPath(),
            originalFile.nameWithoutExtension + ".old"
        )
        //If already exists modify the suffix
        var count = 1
        while (originalFileBackup.exists()) {
            originalFileBackup = File(
                originalFile.getAbsoluteFile().getParentFile().getPath(),
                originalFile.nameWithoutExtension + ".old" + count
            )
            count++
        }

        renameOriginalResult = originalFile.renameTo(originalFileBackup)
        if (!renameOriginalResult) {
            log.warn(
                ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_FILE_TO_BACKUP.getMsg(
                    originalFile.getAbsolutePath(),
                    originalFileBackup.getName()
                )
            )
            newFile.delete()
            throw UnableToRenameFileException(
                ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_FILE_TO_BACKUP.getMsg(
                    originalFile.getAbsolutePath(),
                    originalFileBackup.getName()
                )
            )
        }

        //Rename new Temporary file to the final file
        val renameResult = newFile.renameTo(originalFile)
        if (!renameResult) {
            //Renamed failed so lets do some checks rename the backup back to the original file
            //New File doesnt exist
            if (!newFile.exists()) {
                log.warn(
                    ErrorMessage.GENERAL_WRITE_FAILED_NEW_FILE_DOESNT_EXIST.getMsg(
                        newFile.getAbsolutePath()
                    )
                )
            }

            //Rename the backup back to the original
            renameOriginalResult = originalFileBackup.renameTo(originalFile)
            if (!renameOriginalResult) {
                //TODO now if this happens we are left with testfile.old instead of testfile.mp3
                log.warn(
                    ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_ORIGINAL_BACKUP_TO_ORIGINAL.getMsg(
                        originalFileBackup.getAbsolutePath(),
                        originalFile.getName()
                    )
                )
            }

            log.warn(
                ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE.getMsg(
                    originalFile.getAbsolutePath(),
                    newFile.getName()
                )
            )
            newFile.delete()
            throw UnableToRenameFileException(
                ErrorMessage.GENERAL_WRITE_FAILED_TO_RENAME_TO_ORIGINAL_FILE.getMsg(
                    originalFile.getAbsolutePath(),
                    newFile.getName()
                )
            )
        } else {
            //Rename was okay so we can now deleteField the backup of the original
            val deleteResult = originalFileBackup.delete()
            if (!deleteResult) {
                //Not a disaster but can't deleteField the backup so make a warning
                log.warn(
                    ErrorMessage.GENERAL_WRITE_WARNING_UNABLE_TO_DELETE_BACKUP_FILE.getMsg(
                        originalFileBackup.getAbsolutePath()
                    )
                )
            }
        }
    }

    /**
     * Does a tag of the correct version exist in this file.
     *
     * @param byteBuffer to search through
     * @return true if tag exists.
     */
    override fun seek(byteBuffer: ByteBuffer): Boolean {
        byteBuffer.rewind();
        log.debug(
                "ByteBuffer pos:" +
                        byteBuffer.position() +
                        ":limit" +
                        byteBuffer.limit() +
                        ":cap" +
                        byteBuffer.capacity()
        );

        val tagIdentifier = ByteArray(FIELD_TAGID_LENGTH)
        byteBuffer.get(tagIdentifier, 0, FIELD_TAGID_LENGTH);
        if (!(tagIdentifier.contentEquals(TAG_ID))) {
            return false;
        }
        //Major Version
        val major = byteBuffer.get();
        if (major != getMajorVersion()) {
            return false;
        }
        //Minor Version
        val minor = byteBuffer.get();
        return minor == getRevision();
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
    //TODO should clear all data and preferably recover lost space and go upto end of mp3s
    override fun delete(file: RandomAccessFile) {
        // this works by just erasing the "ID3" tag at the beginning
        // of the file
        val buffer = ByteArray(FIELD_TAGID_LENGTH)
        //Read into Byte Buffer
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
     * @throws IOException TODO should be abstract
     */
    override fun write(file: RandomAccessFile) {
    }
}