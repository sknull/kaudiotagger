package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.util.TagOptionSingleton
import java.util.regex.Pattern

class PartOfSetValue {

    companion object {

        val trackNoPatternWithTotalCount: Pattern = Pattern.compile("([0-9]+)/([0-9]+)(.*)", Pattern.CASE_INSENSITIVE)
        val trackNoPattern: Pattern = Pattern.compile("([0-9]+)(.*)", Pattern.CASE_INSENSITIVE)
        const val SEPARATOR: String = "/"
    }

    private var count: Int? = null
    private var total: Int? = null
    var extra: String? = null //Any extraneous info such as null chars
    var rawCount: String? = null //count value as provided
    var rawTotal: String? = null //total value as provided
    var rawText: String? = null // raw text representation used to actually save the data IF !TagOptionSingleton.isPadNumbers()

    constructor()

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
    constructor(count: Int, total: Int) : this() {
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
    fun getCountAsText(): String {
        return if (!TagOptionSingleton.padNumbers) {
            rawCount?.substringBefore(0.toChar()) ?: "0"
        } else {
            count.toString().padStart(TagOptionSingleton.padNumberTotalLength.length, '0')
        }
    }

    /**
     * Get Total padded
     *
     * @return
     */
    fun getTotalAsText(): String {
        //Don't Pad
        return if (!TagOptionSingleton.padNumbers) {
            rawTotal?.substringBefore(0.toChar()) ?: "0"
        } else {
            total.toString().padStart(TagOptionSingleton.padNumberTotalLength.length, '0')
        }
    }

    fun getCount(): Int? = count

    fun setCount(count: Int) {
        this.count = count;
        this.rawCount = count.toString();
        resetValueFromCounts();
    }

    fun setCount(count: String) {
        this.count = count.toInt()
        this.rawCount = count;
        resetValueFromCounts();
    }

    fun getTotal(): Int? = total

    fun setTotal(total: Int) {
        this.total = total;
        this.rawTotal = total.toString();
        resetValueFromCounts();
    }

    fun setTotal(total: String) {
        this.total = total.toInt()
        this.rawTotal = total;
        resetValueFromCounts();
    }

    override fun toString(): String {
        //Don't Pad
        val sb = StringBuilder()
        if (!TagOptionSingleton.padNumbers) {
            rawText?.substringBefore(0.toChar())?.also { t -> sb.append(t) }
        } else {
            count?.toString()?.padStart(TagOptionSingleton.padNumberTotalLength.length, '0')?.also { t -> sb.append(t) }
            sb.append(SEPARATOR)
            total?.toString()?.padStart(TagOptionSingleton.padNumberTotalLength.length, '0')?.also { t -> sb.append(t) }
            if (extra != null) {
                extra?.also { t -> sb.append(t) }
            }
        }
        return sb.toString()
    }
}