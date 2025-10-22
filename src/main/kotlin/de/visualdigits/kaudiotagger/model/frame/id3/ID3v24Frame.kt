package de.visualdigits.kaudiotagger.model.frame.id3

import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.exceptions.EmptyFrameException
import de.visualdigits.kaudiotagger.model.exceptions.InvalidFrameException
import de.visualdigits.kaudiotagger.model.exceptions.InvalidFrameIdentifierException
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyDeprecated
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTMOO
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyTXXX
import de.visualdigits.kaudiotagger.model.frame.framebody.FrameBodyUnsupported
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.AbstractID3v2FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24EncodingFlags
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24FrameBody
import de.visualdigits.kaudiotagger.model.frame.framebody.id3.ID3v24StatusFlags
import de.visualdigits.kaudiotagger.model.kframe.ID3v23KFrame
import de.visualdigits.kaudiotagger.model.kframe.ID3v24KFrame
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
        val FRAME_HEADER_SIZE: Int = FRAME_ID_SIZE + FRAME_SIZE_SIZE + FRAME_FLAGS_SIZE

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
        
        statusFlags = ID3v24StatusFlags(this, frame.statusFlags?.originalFlags?:0.toByte())
        encodingFlags = ID3v24EncodingFlags(this, frame.encodingFlags?.flags?:0.toByte())
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
    constructor(frame: ID3v23Frame, identifier: String) {
        this.identifier = identifier
        statusFlags = ID3v24StatusFlags(
            this,
            frame.statusFlags
        )
        encodingFlags = ID3v24EncodingFlags(this, frame.encodingFlags?.flags?:0.toByte())
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
            encodingFlags = ID3v24EncodingFlags(this, frame.encodingFlags?.flags?:0.toByte())
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

    private fun createV24FrameFromV23Frame(frame: ID3v23Frame) {
        // Is it a straight conversion e.g TALB - TALB
        identifier = ID3Tags.convertFrameID23To24(frame.identifier)
        log.debug(
            "Creating V24frame from v23:" + frame.identifier + ":" + identifier
        )

        //We cant convert unsupported bodies properly
        if (frame.frameBody is FrameBodyUnsupported) {
            this.frameBody = FrameBodyUnsupported(
                frame.frameBody as FrameBodyUnsupported
            )
            this.frameBody?.header = this
            identifier = frame.identifier
            log.debug(
                "V3:UnsupportedBody:Orig id is:${frame.identifier}:New id is:$identifier"
            )
        } else if (identifier != null) {
            //Special Case
            if ((frame.identifier == ID3v23KFrame.USER_DEFINED_INFO.id) &&
                ((frame.frameBody as FrameBodyTXXX).getDescription() == FrameBodyTXXX.MOOD)
            ) {
                this.frameBody = FrameBodyTMOO(frame.frameBody as FrameBodyTXXX)
                this.frameBody?.header = this
                identifier = frameBody?.getIdentifier()
            } else {
                log.debug(
                    "V3:Orig id is:${frame.identifier}:New id is:$identifier"
                )
                this.frameBody = ID3Tags.copyObject(
                    frame.frameBody
                ) as? AbstractTagFrameBody
                this.frameBody?.header = this
            }
        } else if (ID3Tags.isID3v23FrameIdentifier(frame.identifier)) {
            identifier = ID3Tags.forceFrameID23To24(frame.identifier)
            if (identifier != null) {
                log.debug(
                    "V3:Orig id is:${frame.identifier}:New id is:$identifier"
                )
                this.frameBody = this.readBody(
                    identifier,
                    frame.frameBody as AbstractID3v2FrameBody
                )
                this.frameBody?.header = this
            } else {
                this.frameBody = FrameBodyDeprecated(
                    frame.frameBody as AbstractID3v2FrameBody
                )
                this.frameBody?.header = this
                identifier = frame.identifier
                log.debug(
                    "V3:Deprecated:Orig id is:${frame.identifier}:New id is:$identifier"
                )
            }
        } else {
            this.frameBody = FrameBodyUnsupported(
                frame.frameBody as  FrameBodyUnsupported
            )
            this.frameBody?.header = this
            identifier = frame.identifier
            log.debug(
                "V3:Unknown:Orig id is:${frame.identifier}:New id is:$identifier"
            )
        }
    }

    /**
     * Read the frame from the specified file.
     * Read the frame header then delegate reading of data to frame body.
     *
     * @param byteBuffer to read the frame from
     */
    override fun read(byteBuffer: ByteBuffer) {
        val identifier = readIdentifier(byteBuffer)!!

        //Is this a valid identifier?
        if (!isValidID3v2FrameIdentifier(identifier)) {
            //If not valid move file pointer back to one byte after
            //the original check so can try again.
            log.debug(
                "Invalid identifier:$identifier"
            )
            byteBuffer.position(byteBuffer.position() - (getFrameIdSize() - 1))
            throw InvalidFrameIdentifierException(
                "$identifier:is not a valid ID3v2.30 frame"
            )
        }

        //Get the frame size, adjusted as necessary
        getFrameSize(byteBuffer)

        //Read the flag bytes
        statusFlags = ID3v24StatusFlags(this, byteBuffer.get())
        encodingFlags = ID3v24EncodingFlags(this, byteBuffer.get())

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
                    "Converted frame body with:$identifier to deprecated framebody"
                )
                frameBody = FrameBodyDeprecated((frameBody as AbstractID3v2FrameBody?)!!)
            }
        } finally {
            //Update position of main buffer, so no attempt is made to reread these bytes
            byteBuffer.position(byteBuffer.position() + realFrameSize)
        }
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
    fun isValidID3v2FrameIdentifier(identifier: String): Boolean {
        val m = validFrameIdentifier.matcher(identifier)
        return m.matches()
    }

    /**
     * Return size of frame
     *
     * @return val frame size
     */
    override fun getSize(): Int {
        return frameBody!!.getSize() + FRAME_HEADER_SIZE
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
                "Invalid Frame size:$identifier"
            )
            throw InvalidFrameException("$identifier is invalid frame")
        } else if (frameSize == 0) {
            log.warn("Empty Frame:$identifier")
            //We dont process this frame or add to framemap becuase contains no useful information
            //Skip the two flag bytes so in correct position for subsequent frames
            byteBuffer.get()
            byteBuffer.get()
            throw EmptyFrameException("$identifier is empty frame")
        } else if (frameSize > (byteBuffer.remaining() - FRAME_FLAGS_SIZE)) {
            log.warn(
                "Invalid Frame size larger than size before mp3 audio:$identifier"
            )
            throw InvalidFrameException("$identifier is invalid frame")
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
            val currentPosition = byteBuffer.position();

            //Read as nonsync safe integer
            byteBuffer.position(currentPosition - getFrameIdSize());
            val nonSyncSafeFrameSize = byteBuffer.getInt();

            //Is the frame size syncsafe, should always be BUT some encoders such as Itunes do not do it properly
            //so do an easy check now.
            byteBuffer.position(currentPosition - getFrameIdSize());
            val isNotSyncSafe = ID3SyncSafeInteger.isBufferNotSyncSafe(
                    byteBuffer
            );

            //not relative so need to move position
            byteBuffer.position(currentPosition);

            if (isNotSyncSafe) {
                log.warn(
                    "Frame size is NOT stored as a sync safe integer:$identifier"
                );

                //This will return a larger frame size so need to check against buffer size if too large then we are
                //buggered , give up
                if (
                        nonSyncSafeFrameSize > (byteBuffer.remaining() - -getFrameFlagsSize())
                ) {
                    log.warn(
                        "Invalid Frame size larger than size before mp3 audio:$identifier"
                    );
                    throw InvalidFrameException("$identifier is invalid frame");
                } else {
                    frameSize = nonSyncSafeFrameSize;
                }
            } else {
                //appears to be sync safe but lets look at the bytes just after the reported end of this
                //frame to see if find a valid frame header

                //Read the Frame Identifier
                var readAheadbuffer = ByteArray(getFrameIdSize())
                byteBuffer.position(currentPosition + frameSize + getFrameFlagsSize());

                if (byteBuffer.remaining() < getFrameIdSize()) {
                    //There is no padding or framedata we are at end so assume syncsafe
                    //reset position to just after framesize
                    byteBuffer.position(currentPosition);
                } else {
                    byteBuffer.get(readAheadbuffer, 0, getFrameIdSize());

                    //reset position to just after framesize
                    byteBuffer.position(currentPosition);

                    var readAheadIdentifier = String(readAheadbuffer);
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
                            byteBuffer.position(currentPosition);
                        } else {
                            readAheadbuffer = ByteArray(getFrameIdSize())
                            byteBuffer.position(
                                    currentPosition + nonSyncSafeFrameSize + getFrameFlagsSize()
                            );

                            if (byteBuffer.remaining() >= getFrameIdSize()) {
                                byteBuffer.get(readAheadbuffer, 0, getFrameIdSize());
                                readAheadIdentifier = String(readAheadbuffer);

                                //reset position to just after framesize
                                byteBuffer.position(currentPosition);

                                //ok found a valid identifier using non-syncsafe so assume non-syncsafe size
                                //and continue
                                if (isValidID3v2FrameIdentifier(readAheadIdentifier)) {
                                    frameSize = nonSyncSafeFrameSize;
                                    log.warn(
                                        "Assuming frame size is NOT stored as a sync safe integer:$identifier"
                                    );
                                }
                                //no data found so assume entered padding in which case assume it is last
                                //frame and we are ok whereas we didn't hit padding when using syncsafe integer
                                //or we wouldn't have got to this point. So assume syncsafe integer ended within
                                //the frame data whereas this has reached end of frames.
                                else if (ID3SyncSafeInteger.isBufferEmpty(readAheadbuffer)) {
                                    frameSize = nonSyncSafeFrameSize;
                                    log.warn(
                                        "Assuming frame size is NOT stored as a sync safe integer:$identifier"
                                    );
                                }
                                //invalid so assume syncsafe as that is is the standard
                                else {
                                }
                            } else {
                                //reset position to just after framesize
                                byteBuffer.position(currentPosition);

                                //If the unsync framesize matches exactly the remaining bytes then assume it has the
                                //correct size for the last frame
                                if (byteBuffer.remaining() == 0) {
                                    frameSize = nonSyncSafeFrameSize;
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

        log.debug("Writing frame to file:" + getIdentifier())

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
        if (getIdentifier()?.length == 3) {
            identifier = identifier + ' '
        }
        headerBuffer.put(
            getIdentifier()?.toByteArray(StandardCharsets.ISO_8859_1),
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
        headerBuffer.put(statusFlags?.writeFlags?:0.toByte())

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
        headerBuffer.put(encodingFlags?.flags?:0.toByte())

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
        return ID3v24KFrame.isCommon(getId())
    }

    /**
     * @return true if considered a common frame
     */
    override fun isBinary(): Boolean {
        return ID3v24KFrame.isBinary(getId())
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
}