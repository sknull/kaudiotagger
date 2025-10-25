package de.visualdigits.kaudiotagger.util

/*
 * For Formatting metadata contents of a file as simple text
 */
class PlainTextTagDisplayFormatter : AbstractTagDisplayFormatter() {
    var sb: StringBuffer = StringBuffer()
    var indent: StringBuffer = StringBuffer()

    override fun openHeadingElement(type: String, value: Boolean) {
        openHeadingElement(type, value.toString())
    }

    override fun openHeadingElement(type: String, value: String) {
        addElement(type, value)
        increaseLevel()
    }

    fun increaseLevel() {
        level++
        indent.append("  ")
    }

    override fun addElement(type: String, value: String) {
        sb.append(indent).append(type).append(":").append(value).append('\n')
    }

    override fun openHeadingElement(type: String, value: Int) {
        openHeadingElement(type, value.toString())
    }

    override fun closeHeadingElement(type: String) {
        decreaseLevel()
    }

    fun decreaseLevel() {
        level--
        indent = StringBuffer(indent.substring(0, indent.length - 2))
    }

    override fun addElement(type: String, value: Int) {
        addElement(type, value.toString())
    }

    override fun addElement(type: String, value: Boolean) {
        addElement(type, value.toString())
    }

    override fun toString(): String {
        return sb.toString()
    }

    companion object {
        var formatter: PlainTextTagDisplayFormatter? = null

        fun getInstanceOf(): AbstractTagDisplayFormatter? {
            if (formatter == null) {
                formatter = PlainTextTagDisplayFormatter()
            }
            return formatter
        }
    }
}
