package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.id3.datatype.TCONString
import de.visualdigits.kaudiotagger.model.id3.types.GenreTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v2ExtendedGenreTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import java.nio.ByteBuffer

/**
 * Content type Text information frame.
 *
 * <p>The 'Content type', which previously was
 * stored as a one byte numeric value only, is now a numeric string. You
 * may use one or several of the types as ID3v1.1 did or, since the
 * category list would be impossible to maintain with accurate and up to
 * date categories, define your own.
 * <p>
 * ID3V23:References to the ID3v1 genres can be made by, as first byte, enter
 * "(" followed by a number from the genres list (appendix A) and
 * ended with a ")" character. This is optionally followed by a
 * refinement, e.g. "(21)" or "(4)Eurodisco". Several references can be
 * made in the same frame, e.g. "(51)(39)". If the refinement should
 * begin with a "(" character it should be replaced with "((", e.g. "((I
 * can figure out any genre)" or "(55)((I think...)". The following new
 * content types is defined in ID3v2 and is implemented in the same way
 * as the numeric content types, e.g. "(RX)".
 * <p><table border=0 width="70%">
 * <tr><td>RX</td><td width="100%">Remix</td></tr>
 * <tr><td>CR</td><td>Cover</td></tr>
 * </table>
 *
 * <p>For more details, please refer to the ID3 specifications:
 * <ul>
 * <li><a href="http://www.id3.org/id3v2.3.0.txt">ID3 v2.3.0 Spec</a>
 * </ul>
 * <p>
 * ID3V24:The 'Content type', which ID3v1 was stored as a one byte numeric
 * value only, is now a string. You may use one or several of the ID3v1
 * types as numerical strings, or, since the category list would be
 * impossible to maintain with accurate and up to date categories,
 * define your own. Example: "21" $00 "Eurodisco" $00
 * <p>
 * You may also use any of the following keywords:
 * <p><table border=0 width="70%">
 * <tr><td>RX</td><td width="100%">Remix</td></tr>
 * <tr><td>CR</td><td>Cover</td></tr>
 * </table>
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyTCON: AbstractFrameBodyTextInfo, ID3v23FrameBody, ID3v24FrameBody {

    companion object {

        fun convertGenericToID3v22Genre(value: String): String {
            return convertGenericToID3v23Genre(value)
        }

        /**
         * Convert value to internal genre value
         *
         * @param value
         * @return
         */
        fun convertGenericToID3v24Genre(value: String): String {
            try {
                //If passed id and known value use it
                val genreId = Integer.parseInt(value)
                if (genreId <= GenreTypes.MAX_GENRE_ID) {
                    return genreId.toString()
                } else {
                    return value
                }
            } catch (nfe: NumberFormatException) {
                // If passed String, use matching integral value if can
                val genreId = GenreTypes.fromName(value)
                // to preserve iTunes compatibility, don't write genre ids higher than getMaxStandardGenreId, rather use string
                return if (genreId != null && genreId.id <= GenreTypes.MAX_GENRE_ID) {
                    genreId.id.toString()
                } else if (value.equals(ID3v2ExtendedGenreTypes.RX.description, ignoreCase = true)) {
                    ID3v2ExtendedGenreTypes.RX.name
                } else if (value.equals(ID3v2ExtendedGenreTypes.CR.description, ignoreCase = true)) {
                    ID3v2ExtendedGenreTypes.CR.name
                } else if (value.equals(ID3v2ExtendedGenreTypes.RX.name, ignoreCase = true)) {
                    ID3v2ExtendedGenreTypes.RX.name
                } else if (value.equals(ID3v2ExtendedGenreTypes.CR.name, ignoreCase = true)) {
                    ID3v2ExtendedGenreTypes.CR.name
                } else {
                    ""
                }
            }
        }

        /**
         * Convert value to internal genre value
         *
         * @param value
         * @return
         */
        fun convertGenericToID3v23Genre(value: String): String {
            try {
                //If passed integer and in list use numeric form else use original value
                val genreId = value.toInt()
                if (genreId <= GenreTypes.MAX_GENRE_ID) {
                    return bracketWrap(genreId.toString())
                } else {
                    return value
                }
            } catch (nfe: NumberFormatException) {
                //if passed text try and find integral value otherwise use text
                val genreId = GenreTypes.fromName(value)
                // to preserve iTunes compatibility, don't write genre ids higher than getMaxStandardGenreId, rather use string
                return if (genreId != null && genreId.id <= GenreTypes.MAX_GENRE_ID) {
                    return bracketWrap(genreId.toString())
                } else if (value.equals(ID3v2ExtendedGenreTypes.RX.description, ignoreCase = true)) {
                    bracketWrap(ID3v2ExtendedGenreTypes.RX.name)
                } else if (value.equals(ID3v2ExtendedGenreTypes.CR.description, ignoreCase = true)) {
                    bracketWrap(ID3v2ExtendedGenreTypes.CR.name)
                } else if (value.equals(ID3v2ExtendedGenreTypes.RX.name, ignoreCase = true)) {
                    bracketWrap(ID3v2ExtendedGenreTypes.RX.name)
                } else if (value.equals(ID3v2ExtendedGenreTypes.CR.name, ignoreCase = true)) {
                    bracketWrap(ID3v2ExtendedGenreTypes.CR.name)
                } else {
                    ""
                }
            }
        }

    fun convertID3v22GenreToGeneric(value: String): String? {
        return convertID3v23GenreToGeneric(value)
    }

    /**
     * Convert V23 format to Generic
     * <p>
     * i.e.
     * <p>
     * (2)         -> Country
     * (RX)        -> Remix
     * Shoegaze    -> Shoegaze
     * (2)Shoegaze -> Country Shoegaze
     * <p>
     * Note only handles one field so if the frame stored (2)(3) this would be two separate fields
     * and would manifest itself as two different calls to this method once for (2) and once for (3)
     *
     * @param value
     * @return
     */
    fun convertID3v23GenreToGeneric(value: String): String? {
        if (value.contains(")") && value.lastIndexOf(')') < value.length - 1) {
            return (
                    checkBracketed(value.substring(0, value.lastIndexOf(')'))) +
                            ' ' +
                            value.substring(value.lastIndexOf(')') + 1)
            )
        } else {
            return checkBracketed(value)
        }
    }

    private fun checkBracketed(value: String): String? {
        var value1 = value
            .replace("(", "")
            .replace(")", "")
        return try {
            val genreId = value1.toInt()
            if (genreId <= GenreTypes.MAX_GENRE_ID) {
                GenreTypes.fromId(genreId)?.name
            } else {
                value1
            }
        } catch (nfe: NumberFormatException) {
            if (value1.equals(ID3v2ExtendedGenreTypes.RX.name, ignoreCase = true)) {
                ID3v2ExtendedGenreTypes.RX.description
            } else if (value1.equals(ID3v2ExtendedGenreTypes.CR.name, ignoreCase = true)) {
                ID3v2ExtendedGenreTypes.CR.description
            } else {
                value1
            }
        }
    }

        private fun bracketWrap(value: Any?): String {
            return "(" + value + ')'
        }
    }
    
    /**
     * Creates a new FrameBodyTCON datatype.
     */
    constructor()

    constructor(body: FrameBodyTCON): super(body)

    /**
     * Creates a new FrameBodyTCON datatype.
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text)

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24FrameId.GENRE.id
    }

    fun setV23Format() {
        val text: TCONString = getObject(DataTypes.OBJ_TEXT) as TCONString
        text.isNullSeperateMultipleValues = false
    }

    override fun setupObjectList() {
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_TEXT_ENCODING,
                this,
                TextEncoding.TEXT_ENCODING_FIELD_SIZE
            )
        )
        objectList.add(TCONString(DataTypes.OBJ_TEXT, this))
    }
}