package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody
import de.visualdigits.kaudiotagger.model.id3.types.EventTimingTypes

/**
 * A single event timing code. Part of a list of timing codes (EventTimingCodeList), that are contained in
 * FrameBodyETCO.
 *
 * @author [Hendrik Schreiber](mailto:hs@tagtraum.com)
 * @version $Id:$
 */
class EventTimingCode : AbstractDataType, Cloneable {

    private val type = NumberHashMap(
        DataTypes.OBJ_TYPE_OF_EVENT,
        null,
        1
    )
    private val timestamp = NumberFixedLength(
        DataTypes.OBJ_DATETIME,
        null,
        4
    )

    constructor(copyObject: EventTimingCode): super(copyObject) {
        this.type.setValue(copyObject.type.getValue())
        this.timestamp.setValue(copyObject.timestamp.getValue())
    }

    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody?
    ) : this(identifier, frameBody, 0x00, 0L)

    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody?,
        type: Int,
        timestamp: Long
    ) : super(identifier, frameBody) {
        setBody(frameBody)
        this.type.setValue(type)
        this.timestamp.setValue(timestamp)
    }

    override fun setBody(frameBody: AbstractTagFrameBody?) {
        super.setBody(frameBody)
        this.type.setBody(frameBody)
        this.timestamp.setBody(frameBody)
    }

    override fun readByteArray(byteArray: ByteArray, offset: Int) {
        var localOffset = offset
        val size = getSize()

        log.debug("offset:$localOffset")

        // The read has extended further than the defined frame size (ok to extend upto
        // size because the next datatype may be of length 0.)
        if (offset > byteArray.size - size) {
            log.warn("Invalid size for FrameBody")
            throw InvalidDataTypeException("Invalid size for FrameBody")
        }

        this.type.readByteArray(byteArray, localOffset)
        localOffset += this.type.getSize()
        this.timestamp.readByteArray(byteArray, localOffset)
    }

    override fun getSize(): Int {
        return SIZE
    }

    override fun writeByteArray(): ByteArray {
        val typeData = this.type.writeByteArray()?:error("Coulkd not write data")
        val timeData = this.timestamp.writeByteArray()?:error("Coulkd not write data")
        val objectData = ByteArray(typeData.size + timeData.size)
        System.arraycopy(typeData, 0, objectData, 0, typeData.size)
        System.arraycopy(timeData, 0, objectData, typeData.size, timeData.size)

        return objectData
    }

    fun getTimestamp(): Long {
        return (timestamp.getValue() as Number).toLong()
    }

    fun setTimestamp(timestamp: Long) {
        this.timestamp.setValue(timestamp)
    }

    fun getType(): Int {
        return (type.getValue() as Number).toInt()
    }

    fun setType(type: Int) {
        this.type.setValue(type)
    }

    override fun toString(): String {
        return ("${getType()} (\"${EventTimingTypes.fromId(getType())}\"), ${getTimestamp()}")
    }

    override fun clone(): Any {
        return EventTimingCode(this)
    }

    companion object {
        private const val SIZE = 5
    }
}
