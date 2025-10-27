package de.visualdigits.kaudiotagger.util

import de.visualdigits.kaudiotagger.model.common.types.ID3V2Version
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.tag.AbstractID3v2Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v22Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v23Tag
import de.visualdigits.kaudiotagger.model.id3.tag.ID3v24Tag

object TagOptionSingleton {

    /**
     * Map of lyric ID's to Boolean objects if we should or should not save the
     * specific lyrics3 field. Defaults to true.
     */
    var lyrics3SaveFieldMap = HashMap<String, Boolean>()

    /**
     * parenthesis map stuff
     */
    var parenthesisMap = HashMap<String, String>()

    /**
     * `HashMap` listing words to be replaced if found
     */
    var replaceWordMap = HashMap<String, String>()

    /**
     * default language for any ID3v2 tags frames which require it. This string
     * is in the [ISO-639-2] ISO/FDIS 639-2 definition
     */
    var language: String = "eng"

    /**
     *
     */
    var filenameTagSave = false

    /**
     * if we should save any fields of the ID3v1 tag or not. Defaults to true.
     */
    var id3v1Save = true

    /**
     * if we should save the album field of the ID3v1 tag or not. Defaults to
     * true.
     */
    var id3v1SaveAlbum = true

    /**
     * if we should save the artist field of the ID3v1 tag or not. Defaults to
     * true.
     */
    var id3v1SaveArtist = true

    /**
     * if we should save the comment field of the ID3v1 tag or not. Defaults to
     * true.
     */
    var id3v1SaveComment = true

    /**
     * if we should save the genre field of the ID3v1 tag or not. Defaults to
     * true.
     */
    var id3v1SaveGenre = true

    /**
     * if we should save the title field of the ID3v1 tag or not. Defaults to
     * true.
     */
    var id3v1SaveTitle = true

    /**
     * if we should save the track field of the ID3v1 tag or not. Defaults to
     * true.
     */
    var id3v1SaveTrack = true

    /**
     * if we should save the year field of the ID3v1 tag or not. Defaults to
     * true.
     */
    var id3v1SaveYear = true

    /**
     * When adjusting the ID3v2 padding, if should we copy the current ID3v2
     * tag to the new MP3 file. Defaults to true.
     */
    var id3v2PaddingCopyTag = true

    /**
     * When adjusting the ID3v2 padding, if we should shorten the length of the
     * ID3v2 tag padding. Defaults to false.
     */
    var id3v2PaddingWillShorten = false

    /**
     * if we should save any fields of the ID3v2 tag or not. Defaults to true.
     */
    var id3v2Save = true

    /**
     * if we should keep an empty Lyrics3 field while we're reading. This is
     * different from a string of white space. Defaults to false.
     */
    var lyrics3KeepEmptyFieldIfRead = false

    /**
     * if we should save any fields of the Lyrics3 tag or not. Defaults to
     * true.
     */
    var lyrics3Save = true

    /**
     * if we should save empty Lyrics3 field or not. Defaults to false.
     *
     *
     * todo I don't think this is implemented yet.
     */
    var lyrics3SaveEmptyField = false

    /**
     *
     */
    var originalSavedAfterAdjustingID3v2Padding = true

    /**
     * default time stamp format for any ID3v2 tag frames which require it.
     */
    var timeStampFormat: Byte = 2

    /**
     * number of frames to sync when trying to find the start of the MP3 frame
     * data. The start of the MP3 frame data is the start of the music and is
     * different from the ID3v2 frame data.
     */
    var numberMP3SyncFrame = 3

    /**
     * Unsynchronize tags/frames this is rarely required these days and can cause more
     * problems than it solves
     */
    var unsyncTags = false

    /**
     * iTunes needlessly writes null terminators at the end for TextEncodedStringSizeTerminated values,
     * if this option is enabled these characters are removed
     */
    var removeTrailingTerminatorOnWrite = true

    /**
     * This is the default text encoding to use for new v23 frames, when unicode is required
     * UTF16 will always be used because that is the only valid option for v23.
     */
    var id3v23DefaultTextEncoding = TextEncoding.ISO_8859_1

    /**
     * This is the default text encoding to use for new v24 frames, it defaults to simple ISO8859
     * but by changing this value you could always used UTF8 for example whether you needed to or not
     */
    var id3v24DefaultTextEncoding = TextEncoding.ISO_8859_1

    /**
     * This is text encoding to use for new v24 frames when unicode is required, it defaults to UTF16 just
     * because this encoding is understand by all ID3 versions
     */
    var id3v24UnicodeTextEncoding = TextEncoding.UTF_16

    /**
     * When writing frames if this is set to true then the frame will be written
     * using the defaults disregarding the text encoding originally used to create
     * the frame.
     */
    var resetTextEncodingForExistingFrames = false

    /**
     * Some formats impose maxmimum lengths for fields , if the text provided is longer
     * than the formats allows it will truncate and write a warning, if this is not set
     * it will throw an exception
     */
    var truncateTextWithoutErrors = false

    /**
     * Frames such as TRCK and TPOS sometimes pad single digit numbers to aid sorting
     *
     *
     * Currently only applies to ID3 files
     */
    var padNumbers = false

    /**
     * Number of padding zeroes digits 1- 9, numbers larger than nine will be padded accordingly based on the value.
     * Only has any effect if padNumbers is set to true
     *
     *
     * Currently only applies to ID3 files
     */
    var padNumberTotalLength: PadNumberOption = PadNumberOption.PAD_ONE_ZERO

    /**
     * There are a couple of problems with the Java implementation on Google Android, enabling this value
     * switches on Google workarounds
     */
    var isAndroid = false
    var isAPICDescriptionITunesCompatible = false

    /**
     * When you specify a field should be stored as UTF16 in ID3 this means write with BOM indicating whether
     * written as Little Endian or Big Endian, its defaults to little Endian
     */
    var isEncodeUTF16BomAsLittleEndian = true

    /**
     * When this is set and using the generic interface jaudiotagger will make some adjustments
     * when saving field so they work best with the specified Tagger
     */
    //TODO Not Actually Used yet, originally intended for dealing with ratings and genres
    var playerCompatability = -1

    /**
     * max size of data to copy when copying audiodata from one file to , default to 4mb
     */
    var writeChunkSize = (4 * 1024 * 1024).toLong()
    var isWriteMp4GenresAsText = false
    var isWriteMp3GenresAsText = false

    /**
     * Whether Files.isWritable should be used to check if a file can be written. In some
     * cases, isWritable can return false negatives.
     */
    var checkIsWritable = false

    /**
     * Preserve file identity if possible
     */
    var preserveFileIdentity = true

    var id3v2Version = ID3V2Version.ID3_V23

    /**
     * Default based on user option
     *
     * @return
     */
    fun createDefaultID3Tag(): AbstractID3v2Tag {
        if (id3v2Version == ID3V2Version.ID3_V24) {
            return ID3v24Tag()
        } else if (id3v2Version == ID3V2Version.ID3_V23) {
            return ID3v23Tag()
        } else if (id3v2Version == ID3V2Version.ID3_V22) {
            return ID3v22Tag()
        }
        //Default in case not set somehow
        return ID3v23Tag()
    }
}