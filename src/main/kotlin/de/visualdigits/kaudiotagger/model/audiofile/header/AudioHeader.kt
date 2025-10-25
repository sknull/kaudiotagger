package de.visualdigits.kaudiotagger.model.audiofile.header

interface AudioHeader {

    /**
     * @return the audio file type
     */
    fun getEncodingType(): String?

    /**
     * @return the ByteRate of the Audio, this is the total average amount of bytes of data sampled per second
     */
    fun getByteRate(): Int?

    /**
     * @return the BitRate of the Audio, this is the amount of kilobits of data sampled per second
     */
    fun getBitRate(): String?

    /**
     * @return bitRate as a number, this is the amount of kilobits of data sampled per second
     */
    fun getBitRateAsNumber(): Long

    /**
     * @return length of the audio data in bytes, exactly what this means depends on the audio format
     *
     *
     * TODO currently only used by Wav/Aiff/Flac/Mp4
     */
    fun getAudioDataLength(): Long?

    /**
     * @return the Sampling rate, the number of samples taken per second
     */
    fun getSampleRate(): String?

    /**
     * @return he Sampling rate, the number of samples taken per second
     */
    fun getSampleRateAsNumber(): Int

    /**
     * @return the format
     */
    fun getFormat(): String?

    /**
     * @return the number of channels (i.e 1 = Mono, 2 = Stereo)
     */
    fun getChannels(): String?

    /**
     * @return if the sampling bitRate is variable or constant
     */
    fun isVariableBitRate(): Boolean

    /**
     * @return track length in seconds
     */
    fun getTrackLength(): Int

    /**
     * @return track length as float
     */
    fun getPreciseTrackLength(): Double

    /**
     * @return the number of bits in each sample
     */
    fun getBitsPerSample(): Int

    /**
     * @return if the audio codec is lossless or lossy
     */
    fun isLossless(): Boolean

    /**
     * @return the total number of samples, this can usually be used in conjunction with the
     * sample rate to determine the track duration
     */
 }