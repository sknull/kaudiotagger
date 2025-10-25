package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.util.PadNumberOption
import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.util.regex.Pattern

class PartOfSetValue {

    companion object {

        val trackNoPatternWithTotalCount: Pattern = Pattern.compile("([0-9]+)/([0-9]+)(.*)", Pattern.CASE_INSENSITIVE)
        val trackNoPattern: Pattern = Pattern.compile("([0-9]+)(.*)", Pattern.CASE_INSENSITIVE)
        const val SEPARATOR: String = "/"
    }

    var count: Int = 0
    var total: Int = 0
    var extra: String? = null //Any extraneous info such as null chars
    var rawCount: String? = null //count value as provided
    var rawTotal: String? = null //total value as provided
    var rawText: String = "" // raw text representation used to actually save the data IF !TagOptionSingleton.getInstance().isPadNumbers()

    constructor() {
        rawText = ""
    }

    constructor(value: String) {
        rawText = value
        initFromValue(value)
    }

    /**
     * Newly created
     *
     * @param count
     * @param total
     */
    constructor(count: Int, total: Int): this() {
        this.count = count
        this.rawCount = count.toString()
        this.total = total
        this.rawTotal = total.toString()
        resetValueFromCounts()
    }

    /**
     * Given a raw value that could contain both a count and total and extra stuff (but needdnt contain
     * anything tries to parse it)
     *
     * @param value
     */
    private fun initFromValue(value: String) {
        try {
            var m = trackNoPatternWithTotalCount.matcher(value)
            if (m.matches()) {
                this.extra = m.group(3)
                this.count = Integer.parseInt(m.group(1))
                this.rawCount = m.group(1)
                this.total = Integer.parseInt(m.group(2))
                this.rawTotal = m.group(2)
                return
            }

            m = trackNoPattern.matcher(value)
            if (m.matches()) {
                this.extra = m.group(2)
                this.count = Integer.parseInt(m.group(1))
                this.rawCount = m.group(1)
            }
        } catch (_: NumberFormatException) {
            //#JAUDIOTAGGER-366 Could occur if actually value is a long not an int
            this.count = 0
        }
    }

    private fun resetValueFromCounts() {
        val sb = StringBuffer()
        if (rawCount != null) {
            sb.append(rawCount)
        } else {
            sb.append("0")
        }
        if (rawTotal != null) {
            sb.append(SEPARATOR + rawTotal)
        }
        if (extra != null) {
            sb.append(extra)
        }
        this.rawText = sb.toString()
    }

    /**
     * Get Count including padded if padding is enabled
     *
     * @return
     */
    fun getCountAsText(): String? {
        //Don't Pad
        val sb = StringBuffer()
        if (!TagOptionSingleton.padNumbers) {
            return rawCount
        } else {
            padNumber(
                sb,
                count,
                TagOptionSingleton.padNumberTotalLength
            )
        }
        return sb.toString()
    }

    /**
     * Pad number so number is defined as long as length
     *
     * @param sb
     * @param count
     * @param padNumberLength
     */
    private fun padNumber(
        sb: StringBuffer,
        count: Int?,
        padNumberLength: PadNumberOption
    ) {
        if (count != null) {
            if (padNumberLength === PadNumberOption.PAD_ONE_ZERO) {
                if (count in 1..<10) {
                    sb.append("0").append(count)
                } else {
                    sb.append(count)
                }
            } else if (padNumberLength === PadNumberOption.PAD_TWO_ZERO) {
                if (count in 1..<10) {
                    sb.append("00").append(count)
                } else if (count in 10..<100) {
                    sb.append("0").append(count)
                } else {
                    sb.append(count)
                }
            } else if (padNumberLength === PadNumberOption.PAD_THREE_ZERO) {
                if (count in 1..<10) {
                    sb.append("000").append(count)
                } else if (count in 10..<100) {
                    sb.append("00").append(count)
                } else if (count in 100..<1000) {
                    sb.append("0").append(count)
                } else {
                    sb.append(count)
                }
            }
        }
    }

    /**
     * Get Total padded
     *
     * @return
     */
    fun getTotalAsText(): String {
        //Don't Pad
        val sb = StringBuffer()
        if (!TagOptionSingleton.padNumbers) {
            return rawTotal?:"0"
        } else {
            padNumber(
                sb,
                total,
                TagOptionSingleton.padNumberTotalLength
            )
        }
        return sb.toString()
    }
}