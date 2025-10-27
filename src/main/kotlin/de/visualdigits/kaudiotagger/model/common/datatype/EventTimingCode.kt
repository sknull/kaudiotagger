package de.visualdigits.kaudiotagger.model.common.datatype

import de.visualdigits.kaudiotagger.model.common.types.EventTimingTypes
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody

/**
 * A single event timing code. Part of a list of timing codes ([EventTimingCodeList]), that are contained in
 * [org.jaudiotagger.tag.id3.framebody.FrameBodyETCO].
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

    constructor(copy: EventTimingCode) : super(copy) {
        this.type.setValue(copy.type.getValue())
        this.timestamp.setValue(copy.timestamp.getValue())
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

    override fun readByteArray(buffer: ByteArray, originalOffset: Int) {
        var localOffset = originalOffset
        val size = getSize()

        log.debug("offset:" + localOffset)

        //The read has extended further than the defined frame size (ok to extend upto
        //size because the next datatype may be of length 0.)
        if (originalOffset > buffer.size - size) {
            log.warn("Invalid size for FrameBody")
            throw InvalidDataTypeException("Invalid size for FrameBody")
        }

        this.type.readByteArray(buffer, localOffset)
        localOffset += this.type.getSize()
        this.timestamp.readByteArray(buffer, localOffset)
        localOffset += this.timestamp.getSize()
    }

    override fun getSize(): Int {
        return SIZE
    }

    override fun writeByteArray(): ByteArray {
        val typeData = this.type.writeByteArray()
        val timeData = this.timestamp.writeByteArray()
        val objectData = ByteArray(typeData.size + timeData.size)
        System.arraycopy(typeData, 0, objectData, 0, typeData.size)
        System.arraycopy(timeData, 0, objectData, typeData.size, timeData.size)

        return objectData
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) {
            return true
        }
        if (o == null || javaClass != o.javaClass) {
            return false
        }
        if (!super.equals(o)) {
            return false
        }

        val that = o as EventTimingCode
        return (this.getType() == that.getType() &&
                this.getTimestamp() == that.getTimestamp()
                )
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

    override fun hashCode(): Int {
        var result = if (type != null) type.hashCode() else 0
        result = 31 * result + (if (timestamp != null) timestamp.hashCode() else 0)
        return result
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
