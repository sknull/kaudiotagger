package de.visualdigits.kaudiotagger.model.id3.frame

import de.visualdigits.kaudiotagger.model.audiofile.mp3.MP3File
import de.visualdigits.kaudiotagger.model.common.exceptions.EmptyFrameException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidFrameIdentifierException
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidTagException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyCOMM
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyDeprecated
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodySYLT
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTALB
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTCOM
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTIT2
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTMOO
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTPE1
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyTXXX
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyUSLT
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyUnsupported
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.ID3v24FrameBody
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24EncodingFlags
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24StatusFlags
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23Frames
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.lyrics3.Lyrics3v2Field
import de.visualdigits.kaudiotagger.model.lyrics3.datatype.Lyrics3Line
import de.visualdigits.kaudiotagger.model.lyrics3.frame.framebody.FieldFrameBodyAUT
import de.visualdigits.kaudiotagger.model.lyrics3.frame.framebody.FieldFrameBodyEAL
import de.visualdigits.kaudiotagger.model.lyrics3.frame.framebody.FieldFrameBodyEAR
import de.visualdigits.kaudiotagger.model.lyrics3.frame.framebody.FieldFrameBodyETT
import de.visualdigits.kaudiotagger.model.lyrics3.frame.framebody.FieldFrameBodyINF
import de.visualdigits.kaudiotagger.model.lyrics3.frame.framebody.FieldFrameBodyLYR
import de.visualdigits.kaudiotagger.util.ID3Compression
import de.visualdigits.kaudiotagger.util.ID3SyncSafeInteger
import de.visualdigits.kaudiotagger.util.ID3Tags
import de.visualdigits.kaudiotagger.util.ID3Unsynchronization
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.nio.ByteBuffer
import java.nio.charset.Charset
import java.nio.charset.StandardCharsets
import java.util.regex.Pattern

class ID3v24Frame: AbstractID3v2Frame {
    
    companion object {
        
        const val FRAME_DATA_LENGTH_SIZE: Int = 4
        const val FRAME_ID_SIZE: Int = 4
        const val FRAME_FLAGS_SIZE: Int = 2
        const val FRAME_SIZE_SIZE: Int = 4
        const val FRAME_ENCRYPTION_INDICATOR_SIZE: Int = 1
        const val FRAME_GROUPING_INDICATOR_SIZE: Int = 1
        const val FRAME_HEADER_SIZE: Int = FRAME_ID_SIZE + FRAME_SIZE_SIZE + FRAME_FLAGS_SIZE

        val validFrameIdentifier: Pattern = Pattern.compile("[A-Z][0-9A-Z]{3}")


    }

    /**
     * If the frame is encrypted then the encryption method is stored in this byte
     */
    var encryptionMethod = 0

    /**
     * If the frame belongs in a group with other frames then the group identifier byte is stored
     */
    var groupIdentifier = 0

    constructor()

    /**
     * Creates a ID3v2_4Frame of type identifier. An empty
     * body of the correct type will be automatically created.
     * This constructor should be used when wish to create a new
     * frame from scratch using user input
     *
     * @param identifier defines the type of body to be created
     */
    constructor(identifier: String): super(identifier) {
        statusFlags = ID3v24StatusFlags(this)
        encodingFlags = ID3v24EncodingFlags(this)
    }

    /**
     * Copy Constructor:Creates a ID3v24 frame datatype based on another frame.
     *
     * @param frame
     */
    constructor(frame: ID3v24Frame): super(frame) {

        statusFlags = ID3v24StatusFlags(this, frame.statusFlags?.originalFlags?:0)
        encodingFlags = ID3v24EncodingFlags(this, frame.encodingFlags?.flags?:0)
    }

    /**
     * Partially construct ID3v24 Frame form an IS3v23Frame
     *
     *
     * Used for Special Cases
     *
     * @param frame
     * @param identifier
     */
    constructor(frame: ID3v23Frame, identifier: String): super(identifier) {
        statusFlags = ID3v24StatusFlags(
            this,
            frame.statusFlags
        )
        encodingFlags = ID3v24EncodingFlags(this, frame.encodingFlags?.flags?:0)
    }

    /**
     * Creates a ID3v24 frame datatype based on another frame of different version
     * Converts the framebody to the equivalent v24 framebody or to UnsupportedFrameBody if identifier
     * is unknown.
     *
     * @param frame to construct a frame from
     * @throws InvalidFrameException
     */
    constructor(frame: AbstractID3v2Frame?) {
        //Should not be called
        if ((frame is ID3v24Frame)) {
            throw UnsupportedOperationException(
                "Copy Constructor not called. Please type cast the argument"
            )
        } else if (frame is ID3v23Frame) {
            statusFlags = ID3v24StatusFlags(
                this,
                frame.statusFlags
            )
            encodingFlags = ID3v24EncodingFlags(this, frame.encodingFlags?.flags?:0)
        } else if (frame is ID3v22Frame) {
            statusFlags = ID3v24StatusFlags(this)
            encodingFlags = ID3v24EncodingFlags(this)
        }

        // Convert Identifier. If the id was a known id for the original
        // version we should be able to convert it to an v24 frame, although it may mean minor
        // modification to the data. If it was not recognised originally it should remain
        // unknown.
        if (frame is ID3v23Frame) {
            createV24FrameFromV23Frame(frame)
        } else if (frame is ID3v22Frame) {
            val v23Frame = ID3v23Frame(frame)
            createV24FrameFromV23Frame(v23Frame)
        }
        this.frameBody?.header = this
    }

    /**
     * Creates a ID3v24Frame datatype by reading from byteBuffer.
     *
     * @param byteBuffer      to read from
     * @throws org.jaudiotagger.tag.InvalidFrameException
     */
    constructor(byteBuffer: ByteBuffer) {
        read(byteBuffer)
    }

    /**
     * Creates a new ID3v2_4Frame datatype based on Lyrics3.
     *
     * @param field
     * @throws InvalidTagException
     */
    constructor(field: Lyrics3v2Field) {
        val id = field.getIdentifier()
        val value: String?
        if (id == "IND") {
            throw InvalidTagException(
                "Cannot create ID3v2.40 frame from Lyrics3 indications field."
            )
        } else if (id == "LYR") {
            val lyric = field.frameBody as FieldFrameBodyLYR
            var line: Lyrics3Line
            val sync: FrameBodySYLT?
            val unsync: FrameBodyUSLT?
            val hasTimeStamp = lyric.hasTimeStamp()
            // we'll create only one frame here.
            // if there is any timestamp at all, we will create a sync'ed frame.
            sync = FrameBodySYLT(
                0.toInt(),
                "ENG",
                2.toByte().toInt(),
                1.toByte().toInt(),
                "",
                ByteArray(0)
            )
            unsync = FrameBodyUSLT(0, "ENG", "", "")
            lyric.lines.forEach { line ->
                if (hasTimeStamp) {
                    // sync.addLyric(line);
                } else {
                    unsync.addLyric(line)
                }
            }
            if (hasTimeStamp) {
                this.frameBody = sync
                frameBody?.header = this
            } else {
                this.frameBody = unsync
                frameBody?.header = this
            }
        } else if (id == "INF") {
            value = (field.frameBody as FieldFrameBodyINF).getAdditionalInformation()
            this.frameBody = FrameBodyCOMM(0, "ENG", "", value)
            frameBody?.header = this
        } else if (id == "AUT") {
            value = (field.frameBody as FieldFrameBodyAUT).getAuthor()
            this.frameBody = FrameBodyTCOM(0, value)
            frameBody?.header = this
        } else if (id == "EAL") {
            value = (field.frameBody as FieldFrameBodyEAL).getAlbum()
            this.frameBody = FrameBodyTALB(0, value)
            frameBody?.header = this
        } else if (id == "EAR") {
            value = (field.frameBody as FieldFrameBodyEAR).getArtist()
            this.frameBody = FrameBodyTPE1(0, value)
            frameBody?.header = this
        } else if (id == "ETT") {
            value = (field.frameBody as FieldFrameBodyETT).getTitle()
            this.frameBody = FrameBodyTIT2(0, value)
            frameBody?.header = this
        } else if (id == "IMG") {
            throw InvalidTagException(
                "Cannot create ID3v2.40 frame from Lyrics3 image field."
            )
        } else {
            throw InvalidTagException(
                "Cannot caret ID3v2.40 frame from " + id + " Lyrics3 field"
            )
        }
    }

    private fun createV24FrameFromV23Frame(frame: ID3v23Frame) {
        // Is it a straight conversion e.g TALB - TALB
        setIdentifier(ID3Tags.convertFrameID23To24(frame.getIdentifier()))
        log.debug(
            "Creating V24frame from v23:" + frame.getIdentifier() + ":" + this@ID3v24Frame.getIdentifier()
        )

        //We cant convert unsupported bodies properly
        if (frame.frameBody is FrameBodyUnsupported) {
            this.frameBody = FrameBodyUnsupported(
                frame.frameBody as FrameBodyUnsupported
            )
            this.frameBody?.header = this
            setIdentifier(frame.getIdentifier())
            log.debug(
                "V3:UnsupportedBody:Orig id is:${frame.getIdentifier()}:New id is:${this@ID3v24Frame.getIdentifier()}"
            )
        } else if (this@ID3v24Frame.getIdentifier() != null) {
            //Special Case
            if ((frame.getIdentifier() == ID3v23Frames.USER_DEFINED_INFO.id) &&
                ((frame.frameBody as FrameBodyTXXX).getDescription() == FrameBodyTXXX.MOOD)
            ) {
                this.frameBody = FrameBodyTMOO(frame.frameBody as FrameBodyTXXX)
                this.frameBody?.header = this
                setIdentifier(frameBody?.getIdentifier())
            } else {
                log.debug(
                    "V3:Orig id is:${frame.getIdentifier()}:New id is:${this@ID3v24Frame.getIdentifier()}"
                )
                this.frameBody = ID3Tags.copyObject(
                    frame.frameBody
                ) as? AbstractTagFrameBody
                this.frameBody?.header = this
            }
        } else if (ID3Tags.isID3v23FrameIdentifier(frame.getIdentifier())) {
            setIdentifier(ID3Tags.forceFrameID23To24(frame.getIdentifier()))
            if (this@ID3v24Frame.getIdentifier() != null) {
                log.debug(
                    "V3:Orig id is:${frame.getIdentifier()}:New id is:${this@ID3v24Frame.getIdentifier()}"
                )
                this.frameBody = this.readBody(
                    this@ID3v24Frame.getIdentifier(),
                    frame.frameBody as AbstractID3v2FrameBody
                )
                this.frameBody?.header = this
            } else {
                this.frameBody = FrameBodyDeprecated(
                    frame.frameBody as AbstractID3v2FrameBody
                )
                this.frameBody?.header = this
                setIdentifier(frame.getIdentifier())
                log.debug(
                    "V3:Deprecated:Orig id is:${frame.getIdentifier()}:New id is:${this@ID3v24Frame.getIdentifier()}"
                )
            }
        } else {
            this.frameBody = FrameBodyUnsupported(
                frame.frameBody as  FrameBodyUnsupported
            )
            this.frameBody?.header = this
            setIdentifier(frame.getIdentifier())
            log.debug(
                "V3:Unknown:Orig id is:${frame.getIdentifier()}:New id is:${this@ID3v24Frame.getIdentifier()}"
            )
        }
    }

    /**
     * Read the frame from the specified file.
     * Read the frame header then delegate reading of data to frame body.
     *
     * @param byteBuffer to read the frame from
     */
    override fun read(byteBuffer: ByteBuffer?): Boolean {
        if (byteBuffer == null) {
            return false
        }
        val identifier = readIdentifier(byteBuffer)

        //Is this a valid identifier?
        if (!isValidID3v2FrameIdentifier(identifier)) {
            //If not valid move file pointer back to one byte after
            //the original check so can try again.
            log.debug(
                "Invalid identifier:${this@ID3v24Frame.getIdentifier()}"
            )
            byteBuffer.position(byteBuffer.position() - (getFrameIdSize() - 1))
            throw InvalidFrameIdentifierException(
                "${this@ID3v24Frame.getIdentifier()}:is not a valid ID3v2.30 frame"
            )
        }

        //Get the frame size, adjusted as necessary
        getFrameSize(byteBuffer)

        //Read the flag bytes
        statusFlags = ID3v24StatusFlags(this, byteBuffer.get().toInt())
        encodingFlags = ID3v24EncodingFlags(this, byteBuffer.get().toInt())

        //Read extra bits appended to frame header for various encodings
        //These are not included in header size but are included in frame size but wont be read when we actually
        //try to read the frame body data
        var extraHeaderBytesCount = 0
        var dataLengthSize = -1
        if ((encodingFlags as ID3v24EncodingFlags).isGrouping()) {
            extraHeaderBytesCount = FRAME_GROUPING_INDICATOR_SIZE
            groupIdentifier = byteBuffer.get().toInt()
        }

        if ((encodingFlags as ID3v24EncodingFlags).isEncryption()) {
            //Read the Encryption byte, but do nothing with it
            extraHeaderBytesCount += FRAME_ENCRYPTION_INDICATOR_SIZE
            encryptionMethod = byteBuffer.get().toInt()
        }

        if ((encodingFlags as ID3v24EncodingFlags).isDataLengthIndicator()) {
            //Read the sync safe size field
            dataLengthSize = ID3SyncSafeInteger.bufferToValue(byteBuffer)
            extraHeaderBytesCount += FRAME_DATA_LENGTH_SIZE
            log.debug(
                "Frame Size Is:$frameSize Data Length Size:$dataLengthSize"
            )
        }

        //Work out the real size of the frameBody data
        val realFrameSize = frameSize - extraHeaderBytesCount

        //Create Buffer that only contains the body of this frame rather than the remainder of tag
        var frameBodyBuffer = byteBuffer.slice()
        frameBodyBuffer.limit(realFrameSize)

        //Do we need to synchronize the frame body
        var syncSize = realFrameSize
        if ((encodingFlags as ID3v24EncodingFlags).isUnsynchronised()) {
            //We only want to synchronize the buffer up to the end of this frame (remember this
            //buffer contains the remainder of this tag not just this frame), and we cannot just
            //create a buffer because when this method returns the position of the buffer is used
            //to look for the next frame, so we need to modify the buffer. The action of synchronizing causes
            //bytes to be dropped so the existing buffer is large enough to hold the modifications
            frameBodyBuffer = ID3Unsynchronization.synchronize(frameBodyBuffer)
            syncSize = frameBodyBuffer.limit()
            log.debug(
                "Frame Size After Syncing is:$syncSize"
            )
        }

        //Read the body data
        try {
            if ((encodingFlags as ID3v24EncodingFlags).isCompression()) {
                frameBodyBuffer = ID3Compression.uncompress(
                    identifier,
                    byteBuffer,
                    dataLengthSize,
                    realFrameSize
                )
                if ((encodingFlags as ID3v24EncodingFlags).isEncryption()) {
                    frameBody = readEncryptedBody(
                        identifier,
                        frameBodyBuffer,
                        dataLengthSize
                    )
                } else {
                    frameBody = readBody(identifier, frameBodyBuffer, dataLengthSize)
                }
            } else if ((encodingFlags as ID3v24EncodingFlags).isEncryption()) {
                frameBodyBuffer = byteBuffer.slice()
                frameBodyBuffer.limit(realFrameSize)
                frameBody = readEncryptedBody(identifier, byteBuffer, frameSize)
            } else {
                frameBody = readBody(identifier, frameBodyBuffer, syncSize)
            }
            if (frameBody !is ID3v24FrameBody) {
                log.debug(
                    "Converted frame body with:${this@ID3v24Frame.getIdentifier()} to deprecated framebody"
                )
                frameBody = FrameBodyDeprecated((frameBody as AbstractID3v2FrameBody))
            }
        } finally {
            //Update position of main buffer, so no attempt is made to reread these bytes
            byteBuffer.position(byteBuffer.position() + realFrameSize)
        }

        return true
    }

    override fun getFrameIdSize(): Int {
        return FRAME_ID_SIZE
    }

    fun getFrameFlagsSize(): Int {
        return FRAME_FLAGS_SIZE
    }

    override fun getFrameSizeSize(): Int {
        return FRAME_SIZE_SIZE
    }

    override fun getFrameHeaderSize(): Int {
        return FRAME_HEADER_SIZE
    }

    /**
     * Does the frame identifier meet the syntax for a idv3v2 frame identifier.
     * must start with a capital letter and only contain capital letters and numbers
     *
     * @param identifier to be checked
     * @return whether the identifier is valid
     */
    fun isValidID3v2FrameIdentifier(identifier: String?): Boolean {
        return identifier?.let { id -> validFrameIdentifier.matcher(id).matches() }?:false
    }

    /**
     * Return size of frame
     *
     * @return val frame size
     */
    override fun getSize(): Int {
        return (frameBody?.getSize()?:0) + FRAME_HEADER_SIZE
    }

    /**
     * Read the frame size form the header, check okay , if not try to fix
     * or just throw exception
     *
     * @param byteBuffer
     * @throws InvalidFrameException
     */
    private fun getFrameSize(byteBuffer: ByteBuffer) {
        //Read frame size as syncsafe integer
        frameSize = ID3SyncSafeInteger.bufferToValue(byteBuffer)

        if (frameSize < 0) {
            log.warn(
                "Invalid Frame size:${this@ID3v24Frame.getIdentifier()}"
            )
            throw InvalidFrameException("${this@ID3v24Frame.getIdentifier()} is invalid frame")
        } else if (frameSize == 0) {
            log.warn("Empty Frame:${this@ID3v24Frame.getIdentifier()}")
            //We dont process this frame or add to framemap becuase contains no useful information
            //Skip the two flag bytes so in correct position for subsequent frames
            byteBuffer.get()
            byteBuffer.get()
            throw EmptyFrameException("${this@ID3v24Frame.getIdentifier()} is empty frame")
        } else if (frameSize > (byteBuffer.remaining() - FRAME_FLAGS_SIZE)) {
            log.warn(
                "Invalid Frame size larger than size before mp3 audio:${this@ID3v24Frame.getIdentifier()}"
            )
            throw InvalidFrameException("${this@ID3v24Frame.getIdentifier()} is invalid frame")
        }

        checkIfFrameSizeThatIsNotSyncSafe(byteBuffer)
    }

    /**
     * If frame is greater than certain size it will be decoded differently if unsynchronized to if synchronized
     * Frames with certain byte sequences should be unsynchronized but sometimes editors do not
     * unsynchronize them so this method checks both cases and goes with the option that fits best with the data
     *
     * @param byteBuffer
     * @throws InvalidFrameException
     */
    private fun checkIfFrameSizeThatIsNotSyncSafe(byteBuffer: ByteBuffer) {
        if (frameSize > ID3SyncSafeInteger.MAX_SAFE_SIZE) {
            //Set Just after size field this is where we want to be when we leave this if statement
            val currentPosition = byteBuffer.position()

            //Read as nonsync safe integer
            byteBuffer.position(currentPosition - getFrameIdSize())
            val nonSyncSafeFrameSize = byteBuffer.getInt()

            //Is the frame size syncsafe, should always be BUT some encoders such as Itunes do not do it properly
            //so do an easy check now.
            byteBuffer.position(currentPosition - getFrameIdSize())
            val isNotSyncSafe = ID3SyncSafeInteger.isBufferNotSyncSafe(
                    byteBuffer
            )

            //not relative so need to move position
            byteBuffer.position(currentPosition)

            if (isNotSyncSafe) {
                log.warn(
                    "Frame size is NOT stored as a sync safe integer:${this@ID3v24Frame.getIdentifier()}"
                )

                //This will return a larger frame size so need to check against buffer size if too large then we are
                //buggered , give up
                if (
                        nonSyncSafeFrameSize > (byteBuffer.remaining() - -getFrameFlagsSize())
                ) {
                    log.warn(
                        "Invalid Frame size larger than size before mp3 audio:${this@ID3v24Frame.getIdentifier()}"
                    )
                    throw InvalidFrameException("${this@ID3v24Frame.getIdentifier()} is invalid frame")
                } else {
                    frameSize = nonSyncSafeFrameSize
                }
            } else {
                //appears to be sync safe but lets look at the bytes just after the reported end of this
                //frame to see if find a valid frame header

                //Read the Frame Identifier
                var readAheadbuffer = ByteArray(getFrameIdSize())
                byteBuffer.position(currentPosition + frameSize + getFrameFlagsSize())

                if (byteBuffer.remaining() < getFrameIdSize()) {
                    //There is no padding or framedata we are at end so assume syncsafe
                    //reset position to just after framesize
                    byteBuffer.position(currentPosition)
                } else {
                    byteBuffer.get(readAheadbuffer, 0, getFrameIdSize())

                    //reset position to just after framesize
                    byteBuffer.position(currentPosition)

                    var readAheadIdentifier = String(readAheadbuffer)
                    if (isValidID3v2FrameIdentifier(readAheadIdentifier)) {
                        //Everything ok, so continue
                    } else if (ID3SyncSafeInteger.isBufferEmpty(readAheadbuffer)) {
                        //no data found so assume entered padding in which case assume it is last
                        //frame and we are ok
                    }
                    //haven't found identifier so maybe not syncsafe or maybe there are no more frames, just padding
                    else {
                        //Ok lets try using a non-syncsafe integer

                        //size returned will be larger so is it valid
                        if (
                                nonSyncSafeFrameSize >
                                        byteBuffer.remaining() - getFrameFlagsSize()
                        ) {
                            //invalid so assume syncsafe
                            byteBuffer.position(currentPosition)
                        } else {
                            readAheadbuffer = ByteArray(getFrameIdSize())
                            byteBuffer.position(
                                    currentPosition + nonSyncSafeFrameSize + getFrameFlagsSize()
                            )

                            if (byteBuffer.remaining() >= getFrameIdSize()) {
                                byteBuffer.get(readAheadbuffer, 0, getFrameIdSize())
                                readAheadIdentifier = String(readAheadbuffer)

                                //reset position to just after framesize
                                byteBuffer.position(currentPosition)

                                //ok found a valid identifier using non-syncsafe so assume non-syncsafe size
                                //and continue
                                if (isValidID3v2FrameIdentifier(readAheadIdentifier)) {
                                    frameSize = nonSyncSafeFrameSize
                                    log.warn(
                                        "Assuming frame size is NOT stored as a sync safe integer:${this@ID3v24Frame.getIdentifier()}"
                                    )
                                }
                                //no data found so assume entered padding in which case assume it is last
                                //frame and we are ok whereas we didn't hit padding when using syncsafe integer
                                //or we wouldn't have got to this point. So assume syncsafe integer ended within
                                //the frame data whereas this has reached end of frames.
                                else if (ID3SyncSafeInteger.isBufferEmpty(readAheadbuffer)) {
                                    frameSize = nonSyncSafeFrameSize
                                    log.warn(
                                        "Assuming frame size is NOT stored as a sync safe integer:${this@ID3v24Frame.getIdentifier()}"
                                    )
                                }
                                //invalid so assume syncsafe as that is is the standard
                                else {
                                }
                            } else {
                                //reset position to just after framesize
                                byteBuffer.position(currentPosition)

                                //If the unsync framesize matches exactly the remaining bytes then assume it has the
                                //correct size for the last frame
                                if (byteBuffer.remaining() == 0) {
                                    frameSize = nonSyncSafeFrameSize
                                }
                                //Inconclusive stick with syncsafe
                                else {
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * Write the frame. Writes the frame header but writing the data is delegated to the
     * frame body.
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        val unsynchronization: Boolean

        log.debug("Writing frame to file:" + this@ID3v24Frame.getIdentifier())

        //This is where we will write header, move position to where we can
        //write bodybuffer
        val headerBuffer = ByteBuffer.allocate(FRAME_HEADER_SIZE)

        //Write Frame Body Data to a stream
        val bodyOutputStream = ByteArrayOutputStream()
        (frameBody as AbstractID3v2FrameBody).write(bodyOutputStream)

        //Does it need unsynchronizing, and are we allowing unsychronizing
        var bodyBuffer = bodyOutputStream.toByteArray()
        unsynchronization =
            TagOptionSingleton.unsyncTags &&
                    ID3Unsynchronization.requiresUnsynchronization(bodyBuffer)
        if (unsynchronization) {
            bodyBuffer = ID3Unsynchronization.unsynchronize(bodyBuffer)
            log.debug(
                "bodybytebuffer:sizeafterunsynchronisation:${bodyBuffer.size}"
            )
        }

        //Write Frame Header
        //Write Frame ID, the identifier must be 4 bytes bytes long it may not be
        //because converted an unknown v2.2 id (only 3 bytes long)
        if (this@ID3v24Frame.getIdentifier()?.length == 3) {
            setIdentifier(this@ID3v24Frame.getIdentifier() + ' ')
        }
        headerBuffer.put(
            this@ID3v24Frame.getIdentifier()?.toByteArray(StandardCharsets.ISO_8859_1),
            0,
            FRAME_ID_SIZE
        )

        //Write Frame Size based on size of body buffer (if it has been unsynced then it size
        //will have increased accordingly
        val size = bodyBuffer.size
        log.debug("Frame Size Is:" + size)
        headerBuffer.put(ID3SyncSafeInteger.valueToBuffer(size))

        //Write the Flags
        //Status Flags:leave as they were when we read
        headerBuffer.put((statusFlags?.writeFlags?:0).toByte())

        //Remove any non standard flags
        (encodingFlags as ID3v24EncodingFlags).unsetNonStandardFlags()

        //Encoding we only support unsynchronization
        if (unsynchronization) {
            (encodingFlags as ID3v24EncodingFlags).setUnsynchronised()
        } else {
            (encodingFlags as ID3v24EncodingFlags).unsetUnsynchronised()
        }
        //These are not currently supported on write
        (encodingFlags as ID3v24EncodingFlags).unsetCompression()
        (encodingFlags as ID3v24EncodingFlags).unsetDataLengthIndicator()
        headerBuffer.put((encodingFlags?.flags?:0).toByte())

        try {
            //Add header to the Byte Array Output Stream
            tagBuffer.write(headerBuffer.array())

            if ((encodingFlags as ID3v24EncodingFlags).isEncryption()) {
                tagBuffer.write(encryptionMethod)
            }

            if ((encodingFlags as ID3v24EncodingFlags).isGrouping()) {
                tagBuffer.write(groupIdentifier)
            }

            //Add bodybuffer to the Byte Array Output Stream
            tagBuffer.write(bodyBuffer)
        } catch (ioe: IOException) {
            //This could never happen coz not writing to file, so convert to RuntimeException
            throw RuntimeException(ioe)
        }
    }

    /**
     * @return true if considered a common frame
     */
    override fun isCommon(): Boolean {
        return ID3v24Frames.isCommon(getIdentifier())
    }

    /**
     * @return true if considered a common frame
     */
    override fun isBinary(): Boolean {
        return ID3v24Frames.isBinary(getIdentifier())
    }

    /**
     * Sets the charset encoding used by the field.
     *
     * @param encoding charset.
     */
    override fun setEncoding(encoding: Charset) {
        val encodingId = TextEncoding.fromCharset(encoding)?.id
        if (encodingId != null) {
            if (encodingId < 4) {
                this.frameBody?.setTextEncoding(encodingId.toByte())
            }
        }
    }

    /**
     * Return String Representation of body
     */
    override fun createStructure() {
        MP3File.tagFormatter?.openHeadingElement(
            TYPE_FRAME,
            this@ID3v24Frame.getIdentifier() ?:""
        )
        MP3File.tagFormatter?.addElement(TYPE_FRAME_SIZE, frameSize)
        statusFlags?.createStructure()
        encodingFlags?.createStructure()
        frameBody?.createStructure()
        MP3File.tagFormatter?.closeHeadingElement(TYPE_FRAME)
    }
}