package de.visualdigits.kaudiotagger.util

/**
 * Abstract class that provides structure to use for displaying a files metadata content
 */
abstract class AbstractTagDisplayFormatter {

    var level: Int = 0

    abstract fun openHeadingElement(type: String, value: String)

    abstract fun openHeadingElement(type: String, value: Boolean)

    abstract fun openHeadingElement(type: String, value: Int)

    abstract fun closeHeadingElement(type: String)

    abstract fun addElement(type: String, value: String)

    abstract fun addElement(type: String, value: Int)

    abstract fun addElement(type: String, value: Boolean)

    abstract override fun toString(): String

    companion object {

        val hexBinaryMap = HashMap<String, String>()

        init {
            hexBinaryMap.put("0", "0000")
            hexBinaryMap.put("1", "0001")
            hexBinaryMap.put("2", "0010")
            hexBinaryMap.put("3", "0011")
            hexBinaryMap.put("4", "0100")
            hexBinaryMap.put("5", "0101")
            hexBinaryMap.put("6", "0110")
            hexBinaryMap.put("7", "0111")
            hexBinaryMap.put("8", "1000")
            hexBinaryMap.put("9", "1001")
            hexBinaryMap.put("a", "1010")
            hexBinaryMap.put("b", "1011")
            hexBinaryMap.put("c", "1100")
            hexBinaryMap.put("d", "1101")
            hexBinaryMap.put("e", "1110")
            hexBinaryMap.put("f", "1111")
        }

        /**
         * Use to display headers as their binary representation
         *
         * @param buffer
         * @return
         */
        fun displayAsBinary(buffer: Byte): String {
            //Convert buffer to hex representation
            val hexValue = Integer.toHexString(buffer.toInt())
            var char1 = ""
            var char2 = ""
            try {
                if (hexValue.length == 8) {
                    char1 = hexValue.substring(6, 7)
                    char2 = hexValue.substring(7, 8)
                } else if (hexValue.length == 2) {
                    char1 = hexValue.substring(0, 1)
                    char2 = hexValue.substring(1, 2)
                } else if (hexValue.length == 1) {
                    char1 = "0"
                    char2 = hexValue.substring(0, 1)
                }
            } catch (se: StringIndexOutOfBoundsException) {
                return ""
            }
            return hexBinaryMap.get(char1) + hexBinaryMap.get(char2)
        }
    }
}
