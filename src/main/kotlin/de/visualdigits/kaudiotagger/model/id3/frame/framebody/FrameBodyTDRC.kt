package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.types.ID3v23FrameId
import de.visualdigits.kaudiotagger.model.id3.types.ID3v24FrameId
import java.nio.ByteBuffer
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FrameBodyTDRC: AbstractFrameBodyTextInfo, ID3v24FrameBody {

    companion object {
        val formatYearIn = SimpleDateFormat("yyyy", Locale.UK)
        val formatDateIn = SimpleDateFormat("ddMM", Locale.UK)
        val formatTimeIn = SimpleDateFormat("HHmm", Locale.UK)

        // These are the separate components of the v24 format that the v23 formats map to
        val formatYearOut = SimpleDateFormat("yyyy", Locale.UK)
        val formatDateOut = SimpleDateFormat("-MM-dd", Locale.UK)
        val formatMonthOut = SimpleDateFormat("-MM", Locale.UK)
        val formatTimeOut = SimpleDateFormat("'T'HH:mm", Locale.UK)
        val formatHoursOut = SimpleDateFormat("'T'HH", Locale.UK)
        
        val formatters = listOf(
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.UK),
            SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.UK),
            SimpleDateFormat("yyyy-MM-dd'T'HH", Locale.UK),
            SimpleDateFormat("yyyy-MM-dd", Locale.UK),
            SimpleDateFormat("yyyy-MM", Locale.UK),
            SimpleDateFormat("yyyy", Locale.UK)
        )
        
        const val PRECISION_SECOND: Int = 0
        const val PRECISION_MINUTE: Int = 1
        const val PRECISION_HOUR: Int = 2
        const val PRECISION_DAY: Int = 3
        const val PRECISION_MONTH: Int = 4
        const val PRECISION_YEAR: Int = 5
    }

    /**
     * Used when converting from v3 tags , these fields should ALWAYS hold the v23 value
     */
    var originalID: String? = null
    var year: String? = null
    var time: String? = null
    var date: String? = null
    var monthOnly = false
    var hoursOnly = false

    constructor()

    constructor(copyObject: FrameBodyTDRC): super(copyObject)

    constructor(
        byteBuffer: ByteBuffer? = null,
        frameSize: Int = 0
    ): super(byteBuffer, frameSize)

    /**
     * When converting v3 TYER to v4 TDRC frame
     *
     * @param body
     */
    constructor(body: FrameBodyTYER) {
        originalID = ID3v23FrameId.TYER.id
        year = body.getText()
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1.id)
        setObjectValue(DataTypes.OBJ_TEXT, getFormattedText())
    }

    /**
     * When converting v3 TIME to v4 TDRC frame
     *
     * @param body
     */
    constructor(body: FrameBodyTIME) {
        originalID = ID3v23FrameId.TIME.id
        time = body.getText()
        hoursOnly = body.hoursOnly
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1.id)
        setObjectValue(DataTypes.OBJ_TEXT, getFormattedText())
    }

    /**
     * When converting v3 TDAT to v4 TDRC frame
     *
     * @param body
     */
    constructor(body: FrameBodyTDAT) {
        originalID = ID3v23FrameId.TDAT.id
        date = body.getText()
        monthOnly = body.isMonthOnly
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1.id)
        setObjectValue(DataTypes.OBJ_TEXT, getFormattedText())
    }

    /**
     * When converting v3 TDRA to v4 TDRC frame
     *
     * @param body
     */
    constructor(body: FrameBodyTRDA) {
        originalID = ID3v23FrameId.TRDA.id
        date = body.getText()
        setObjectValue(DataTypes.OBJ_TEXT_ENCODING, TextEncoding.ISO_8859_1.id)
        setObjectValue(DataTypes.OBJ_TEXT, getFormattedText())
    }

    /**
     * Creates a new FrameBodyTDRC dataType.
     *
     *
     * Tries to decode the text to find the v24 date mask being used, and store the v3 components of the mask
     *
     * @param textEncoding
     * @param text
     */
    constructor(textEncoding: Byte, text: String): super(textEncoding, text) {
        findMatchingMaskAndExtractV3Values()
    }

    fun findMatchingMaskAndExtractV3Values() {
        // Find the date format of the text
        for (i in formatters.indices) {
            try {
                val d: Date?
                synchronized(formatters[i]) {
                    d = formatters[i].parse(getText())
                }
                // If able to parse a date from the text
                if (d != null) {
                    extractID3v23Formats(d, i)
                    break
                }
            } catch (e: ParseException) { // Dont display will occur for each failed format
                // Do nothing;
            } catch (nfe: NumberFormatException) {
                // Do nothing except log warning because not really expecting this to happen
                log.warn("Date Formatter:${formatters[i].toPattern()}failed to parse:${getText()}with ${nfe.message}", nfe)
            }
        }
    }

    /**
     * When this has been generated as an amalgamation of v3 frames assumes
     * the v3 frames match the the format in specification and convert them
     * to their equivalent v4 format and return the generated String.
     * i.e if the v3 frames contain a valid value this will return a valid
     * v4 value, if not this won't.
     */
    fun getFormattedText(): String {
        val sb = StringBuilder()
        return if (originalID == null) {
            this.getText() ?: ""
        } else {
            if (year != null && year?.trim()?.isNotEmpty() == true) {
                sb.append(formatAndParse(formatYearOut, formatYearIn, year))
            }
            if (!date.equals("")) {
                if (monthOnly) {
                    sb.append(formatAndParse(formatMonthOut, formatDateIn, date))
                } else {
                    sb.append(formatAndParse(formatDateOut, formatDateIn, date))
                }
            }
            if (!time.equals("")) {
                if (hoursOnly) {
                    sb.append(formatAndParse(formatHoursOut, formatTimeIn, time))
                } else {
                    sb.append(formatAndParse(formatTimeOut, formatTimeIn, time))
                }
            }
            sb.toString()
        }
    }

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3v24FrameId.YEAR.id
    }

    /**
     * Synchronized because SimpleDatFormat aren't thread safe
     *
     * @param formatDate
     * @param parseDate
     * @param text
     * @return
     */
    @Synchronized
    private fun formatAndParse(
        formatDate: SimpleDateFormat,
        parseDate: SimpleDateFormat,
        text: String?
    ): String {
        try {
            return text
                ?.let { t -> parseDate.parse(t) }
                ?.let { d -> formatDate.format(d) }
                ?:""
        } catch (_: ParseException) {
            log.warn("Unable to parse:$text")
        }
        return ""
    }

    /**
     * Extract the components ans store the v23 version of the various values
     *
     * @param dateRecord
     * @param precision
     */
    private fun extractID3v23Formats(
        dateRecord: Date,
        precision: Int
    ) {
        log.debug(
            "Precision is:" + precision + "for date:" + dateRecord.toString()
        )
        val d = dateRecord

        // Precision Year
        when (precision) {
            PRECISION_YEAR -> {
                year = formatDateAsYear(d)
            }
            PRECISION_MONTH -> {
                year = formatDateAsYear(d)
                date = formatDateAsDate(d)
                monthOnly = true
            }
            PRECISION_DAY -> {
                year = formatDateAsYear(d)
                date = formatDateAsDate(d)
            }
            PRECISION_HOUR -> {
                year = formatDateAsYear(d)
                date = formatDateAsDate(d)
                time = formatDateAsTime(d)
                hoursOnly = true
            }
            PRECISION_MINUTE, PRECISION_SECOND -> {
                year = formatDateAsYear(d)
                date = formatDateAsDate(d)
                time = formatDateAsTime(d)
            }
        }
    }

    /**
     * Format Date
     *
     *
     * Synchronized because SimpleDateFormat is invalid
     *
     * @param d
     * @return
     */
    @Synchronized
    private fun formatDateAsYear(d: Date): String {
        return formatYearIn.format(d)
    }

    /**
     * Format Date
     *
     *
     * Synchronized because SimpleDateFormat is invalid
     *
     * @param d
     * @return
     */
    @Synchronized
    private fun formatDateAsDate(d: Date): String {
        return formatDateIn.format(d)
    }

    /**
     * Format Date
     *
     *
     * Synchronized because SimpleDateFormat is invalid
     *
     * @param d
     * @return
     */
    @Synchronized
    private fun formatDateAsTime(d: Date): String {
        return formatTimeIn.format(d)
    }
}