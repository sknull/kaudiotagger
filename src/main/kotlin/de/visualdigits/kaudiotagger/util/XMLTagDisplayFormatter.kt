package de.visualdigits.kaudiotagger.util

import java.text.CharacterIterator
import java.text.StringCharacterIterator

/*
 * For Formatting the metadata contents of a file in an XML format
 *
 * This could provide the basis of a representation of a files metadata, which can then be manipulated to
 * to create technical reports.
 */
class XMLTagDisplayFormatter : AbstractTagDisplayFormatter() {
    
    var sb: StringBuffer = StringBuffer()

    override fun openHeadingElement(type: String, value: Boolean) {
        openHeadingElement(type, value.toString())
    }

    override fun openHeadingElement(type: String, value: String) {
        if (value.length == 0) {
            sb.append(xmlOpen(type))
        } else {
            sb.append(xmlOpenHeading(type, replaceXMLCharacters(value)))
        }
    }

    override fun openHeadingElement(type: String, value: Int) {
        openHeadingElement(type, value.toString())
    }

    override fun closeHeadingElement(type: String) {
        sb.append(xmlClose(type))
    }

    override fun addElement(type: String, value: Int) {
        addElement(type, value.toString())
    }

    override fun addElement(type: String, value: String) {
        sb.append(xmlFullTag(type, replaceXMLCharacters(value)))
    }

    override fun addElement(type: String, value: Boolean) {
        addElement(type, value.toString())
    }

    override fun toString(): String {
        return sb.toString()
    }

    companion object {

        const val xmlOpenStart: String = "<"
        const val xmlOpenEnd: String = ">"
        const val xmlCloseStart: String = "</"
        const val xmlCloseEnd: String = ">"
        const val xmlSingleTagClose: String = " />"
        const val xmlCDataTagOpen: String = "<![CDATA["
        const val xmlCDataTagClose: String = "]]>"

        val formatter: XMLTagDisplayFormatter? = null

        fun xmlSingleTag(data: String): String {
            return xmlOpenStart + data + xmlSingleTagClose
        }

        /**
         * Return xml open tag round a string e.g <tag>
         *
         * @param xmlName
         * @return
        </tag> */
        fun xmlOpen(xmlName: String): String {
            return xmlOpenStart + xmlName + xmlOpenEnd
        }

        fun xmlOpenHeading(name: String?, data: String?): String {
            return (xmlOpen(name + " id=\"" + data + "\""))
        }

        /**
         * Replace any special xml characters with the appropiate escape sequences
         * required to be done for the actual element names
         *
         * @param xmlData
         * @return
         */
        fun replaceXMLCharacters(xmlData: String): String {
            val sb = StringBuffer()
            val sCI = StringCharacterIterator(xmlData)
            var c = sCI.first()
            while (c != CharacterIterator.DONE) {
                when (c) {
                    '&' -> sb.append("&amp;")
                    '<' -> sb.append("&lt;")
                    '>' -> sb.append("&gt;")
                    '"' -> sb.append("&quot;")
                    '\'' -> sb.append("&apos;")
                    else -> sb.append(c)
                }
                c = sCI.next()
            }
            return sb.toString()
        }

        /**
         * Return xml close tag around a string e.g
         *
         * @param xmlName
         * @return
         */
        fun xmlClose(xmlName: String): String {
            return xmlCloseStart + xmlName + xmlCloseEnd
        }

        fun xmlFullTag(xmlName: String, data: String): String {
            return xmlOpen(xmlName) + xmlCData(data) + xmlClose(xmlName)
        }

        /**
         * Return CDATA tag around xml data e.g <![CDATA[xmlData]]>
         * We also need to deal with special chars
         *
         * @param xmlData
         * @return
         */
        fun xmlCData(xmlData: String): String {
            var tempChar: Char
            val replacedString = StringBuffer()
            for (i in 0..<xmlData.length) {
                tempChar = xmlData.get(i)
                if ((Character.isLetterOrDigit(tempChar)) ||
                    (Character.isSpaceChar(tempChar))
                ) {
                    replacedString.append(tempChar)
                } else {
                    replacedString
                        .append("&#x")
                        .append(Character.codePointAt(xmlData, i).toString(16))
                }
            }
            return xmlCDataTagOpen + replacedString + xmlCDataTagClose
        }
    }
}
