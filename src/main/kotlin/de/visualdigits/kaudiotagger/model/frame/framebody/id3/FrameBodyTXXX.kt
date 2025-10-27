package de.visualdigits.kaudiotagger.model.frame.framebody.id3

import de.visualdigits.kaudiotagger.model.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.datatype.TextEncodedStringNullTerminated
import de.visualdigits.kaudiotagger.model.datatype.TextEncodedStringSizeTerminated
import de.visualdigits.kaudiotagger.model.datatype.types.ID3v24Frames
import de.visualdigits.kaudiotagger.model.datatype.types.TextEncoding
import de.visualdigits.kaudiotagger.model.frame.framebody.AbstractFrameBodyTextInfo
import de.visualdigits.kaudiotagger.util.ID3TextEncodingConversion
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class FrameBodyTXXX: AbstractFrameBodyTextInfo, ID3v23FrameBody, ID3v24FrameBody {
    
    companion object {

        //Used by Picard and Jaikoz
        const val ACOUSTID_FINGERPRINT: String = "Acoustid Fingerprint"
        const val ACOUSTID_ID: String = "Acoustid Id"
        const val AMAZON_ASIN: String = "ASIN"
        const val ARRANGER_SORT: String = "ARRANGER_SORT"
        const val ARTISTS: String = "ARTISTS"
        const val ARTISTS_SORT: String = "ARTISTS_SORT"
        const val ALBUM_ARTISTS: String = "ALBUM_ARTISTS"
        const val ALBUM_ARTISTS_SORT: String = "ALBUM_ARTISTS_SORT"
        const val BARCODE: String = "BARCODE"
        const val CATALOG_NO: String = "CATALOGNUMBER"
        const val CHOIR: String = "CHOIR"
        const val CHOIR_SORT: String = "CHOIR_SORT"
        const val CLASSICAL_CATALOG: String = "CLASSICAL_CATALOG"
        const val CLASSICAL_NICKNAME: String = "CLASSICAL_NICKNAME"
        const val CONDUCTOR_SORT: String = "CONDUCTOR_SORT"
        const val COUNTRY: String = "Country"
        const val ENSEMBLE: String = "ENSEMBLE"
        const val ENSEMBLE_SORT: String = "ENSEMBLE_SORT"
        const val FBPM: String = "FBPM"
        const val IS_CLASSICAL: String = "IS_CLASSICAL"
        const val IS_SOUNDTRACK: String = "IS_SOUNDTRACK"
        const val MOOD: String = "MOOD" //ID3 v23 only
        const val MOOD_ACOUSTIC: String = "MOOD_ACOUSTIC"
        const val MOOD_AGGRESSIVE: String = "MOOD_AGGRESSIVE"
        const val MOOD_AROUSAL: String = "MOOD_AROUSAL"
        const val MOOD_DANCEABILITY: String = "MOOD_DANCEABILITY"
        const val MOOD_ELECTRONIC: String = "MOOD_ELECTRONIC"
        const val MOOD_HAPPY: String = "MOOD_HAPPY"
        const val MOOD_INSTRUMENTAL: String = "MOOD_INSTRUMENTAL"
        const val MOOD_PARTY: String = "MOOD_PARTY"
        const val MOOD_RELAXED: String = "MOOD_RELAXED"
        const val MOOD_SAD: String = "MOOD_SAD"
        const val MOOD_VALENCE: String = "MOOD_VALENCE"
        const val MUSICBRAINZ_ALBUMID: String = "MusicBrainz Album Id"
        const val MUSICBRAINZ_ALBUM_ARTISTID: String = "MusicBrainz Album Artist Id"
        const val MUSICBRAINZ_ALBUM_COUNTRY: String = "MusicBrainz Album Release Country"
        const val MUSICBRAINZ_ALBUM_STATUS: String = "MusicBrainz Album Status"
        const val MUSICBRAINZ_ALBUM_TYPE: String = "MusicBrainz Album Type"
        const val MUSICBRAINZ_ARTISTID: String = "MusicBrainz Artist Id"
        const val MUSICBRAINZ_DISCID: String = "MusicBrainz Disc Id"
        const val MUSICBRAINZ_ORIGINAL_ALBUMID: String = "MusicBrainz Original Album Id"
        const val MUSICBRAINZ_RELEASE_GROUPID: String = "MusicBrainz Release Group Id"
        const val MUSICBRAINZ_RELEASE_TRACKID: String = "MusicBrainz Release Track Id"
        const val MUSICBRAINZ_WORK_COMPOSITION: String = "MUSICBRAINZ_WORK_COMPOSITION"
        const val MUSICBRAINZ_WORK_COMPOSITION_ID: String = "MUSICBRAINZ_WORK_COMPOSITION_ID"
        const val MUSICBRAINZ_WORKID: String = "MusicBrainz Work Id"
        const val MUSICBRAINZ_WORK: String = "MUSICBRAINZ_WORK"
        const val MUSICBRAINZ_WORK_PART_LEVEL1: String = "MUSICBRAINZ_WORK_PART_LEVEL1"
        const val MUSICBRAINZ_WORK_PART_LEVEL1_ID: String = "MUSICBRAINZ_WORK_PART_LEVEL1_ID"
        const val MUSICBRAINZ_WORK_PART_LEVEL1_TYPE: String = "MUSICBRAINZ_WORK_PART_LEVEL1_TYPE"
        const val MUSICBRAINZ_WORK_PART_LEVEL2: String = "MUSICBRAINZ_WORK_PART_LEVEL2"
        const val MUSICBRAINZ_WORK_PART_LEVEL2_ID: String = "MUSICBRAINZ_WORK_PART_LEVEL2_ID"
        const val MUSICBRAINZ_WORK_PART_LEVEL2_TYPE: String = "MUSICBRAINZ_WORK_PART_LEVEL2_TYPE"
        const val MUSICBRAINZ_WORK_PART_LEVEL3: String = "MUSICBRAINZ_WORK_PART_LEVEL3"
        const val MUSICBRAINZ_WORK_PART_LEVEL3_ID: String = "MUSICBRAINZ_WORK_PART_LEVEL3_ID"
        const val MUSICBRAINZ_WORK_PART_LEVEL3_TYPE: String = "MUSICBRAINZ_WORK_PART_LEVEL3_TYPE"
        const val MUSICBRAINZ_WORK_PART_LEVEL4: String = "MUSICBRAINZ_WORK_PART_LEVEL4"
        const val MUSICBRAINZ_WORK_PART_LEVEL4_ID: String = "MUSICBRAINZ_WORK_PART_LEVEL4_ID"
        const val MUSICBRAINZ_WORK_PART_LEVEL4_TYPE: String = "MUSICBRAINZ_WORK_PART_LEVEL4_TYPE"
        const val MUSICBRAINZ_WORK_PART_LEVEL5: String = "MUSICBRAINZ_WORK_PART_LEVEL5"
        const val MUSICBRAINZ_WORK_PART_LEVEL5_ID: String = "MUSICBRAINZ_WORK_PART_LEVEL5_ID"
        const val MUSICBRAINZ_WORK_PART_LEVEL5_TYPE: String = "MUSICBRAINZ_WORK_PART_LEVEL5_TYPE"
        const val MUSICBRAINZ_WORK_PART_LEVEL6: String = "MUSICBRAINZ_WORK_PART_LEVEL6"
        const val MUSICBRAINZ_WORK_PART_LEVEL6_ID: String = "MUSICBRAINZ_WORK_PART_LEVEL6_ID"
        const val MUSICBRAINZ_WORK_PART_LEVEL6_TYPE: String = "MUSICBRAINZ_WORK_PART_LEVEL6_TYPE"
        const val MUSICIP_ID: String = "MusicIP PUID"
        const val OPUS: String = "OPUS"
        const val ORCHESTRA: String = "ORCHESTRA"
        const val ORCHESTRA_SORT: String = "ORCHESTRA_SORT"
        const val PART: String = "PART"
        const val PART_NUMBER: String = "PARTNUMBER"
        const val PART_TYPE: String = "PART_TYPE"
        const val PERFORMER_NAME: String = "PERFORMER_NAME"
        const val PERFORMER_NAME_SORT: String = "PERFORMER_NAME_SORT"
        const val PERIOD: String = "PERIOD"
        const val RANKING: String = "RANKING"
        const val SCRIPT: String = "Script"
        const val SINGLE_DISC_TRACK_NO: String = "SINGLE_DISC_TRACK_NO"
        const val TAGS: String = "TAGS"
        const val TIMBRE: String = "TIMBRE_BRIGHTNESS"
        const val TITLE_MOVEMENT: String = "TITLE_MOVEMENT"
        const val TONALITY: String = "TONALITY"
        const val WORK: String = "WORK"
        const val WORK_TYPE: String = "WORK_TYPE"

        //used by Foobar 20000
        const val ALBUM_ARTIST: String = "ALBUM ARTIST"
    }

    /**
     * Creates a new FrameBodyTXXX datatype.
     */
    constructor() {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, "")
        this.setObjectValue(DataTypes.OBJ_TEXT, "")
    }

    /**
     * Convert from V4 TMOO Frame to V3 Frame
     *
     * @param body
     */
    constructor(body: FrameBodyTMOO) {
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, body.getTextEncoding())
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1)
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, MOOD)
        this.setObjectValue(DataTypes.OBJ_TEXT, body.getText())
    }

    constructor(body: FrameBodyTXXX): super(body)

    /**
     * Creates a new FrameBodyTXXX datatype.
     *
     * @param textEncoding
     * @param description
     * @param text
     */
    constructor(textEncoding: Byte, description: String, text: String) {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        this.setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
        this.setObjectValue(DataTypes.OBJ_TEXT, text)
    }

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * @return the description field
     */
    fun getDescription(): String? {
        return getObjectValue(DataTypes.OBJ_DESCRIPTION) as? String
    }

    /**
     * Set the description field
     *
     * @param description
     */
    fun setDescription(description: String) {
        setObjectValue(DataTypes.OBJ_DESCRIPTION, description)
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24Frames.USER_DEFINED_INFO.id
    }

    /**
     * Because TXXX frames also have a text encoded description we need to check this as well.     *
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        //Ensure valid for type
        setTextEncoding(
            ID3TextEncodingConversion.getTextEncoding(header, getTextEncoding())
        )

        //Ensure valid for description
        if (!(getObject(
                DataTypes.OBJ_DESCRIPTION
            ) as TextEncodedStringNullTerminated).canBeEncoded()
        ) {
            this.setTextEncoding(
                ID3TextEncodingConversion.getUnicodeTextEncoding(header)
            )
        }
        super.write(tagBuffer)
    }

    /**
     * This is different to other text Frames
     */
    override fun setupObjectList() {
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_TEXT_ENCODING,
                this,
                TextEncoding.TEXT_ENCODING_FIELD_SIZE
            )
        )
        objectList.add(
            TextEncodedStringNullTerminated(DataTypes.OBJ_DESCRIPTION, this)
        )
        objectList.add(
            TextEncodedStringSizeTerminated(DataTypes.OBJ_TEXT, this)
        )
    }
}