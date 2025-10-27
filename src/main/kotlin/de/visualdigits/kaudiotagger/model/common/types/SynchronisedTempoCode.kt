package de.visualdigits.kaudiotagger.model.common.types

import de.visualdigits.kaudiotagger.model.common.datatype.AbstractDataType
import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.common.datatype.NumberFixedLength
import de.visualdigits.kaudiotagger.model.common.exceptions.InvalidDataTypeException
import de.visualdigits.kaudiotagger.model.common.frame.framebody.AbstractTagFrameBody

/**
 * A single synchronized tempo code. Part of a list of temnpo codes ([SynchronisedTempoCodeList]), that are contained in
 * [org.jaudiotagger.tag.id3.framebody.FrameBodySYTC]
 *
 * @author [Hendrik Schreiber](mailto:hs@tagtraum.com)
 * @version $Id:$
 */
class SynchronisedTempoCode: AbstractDataType, Cloneable {

    private val tempo: TempoCode = TempoCode(
        DataTypes.OBJ_SYNCHRONISED_TEMPO_DATA,
        null,
        1
    )
    private val timestamp: NumberFixedLength? = NumberFixedLength(
        DataTypes.OBJ_DATETIME,
        null,
        4
    )

    constructor(copy: SynchronisedTempoCode) : super(copy) {
        this.tempo.setValue(copy.tempo.getValue())
        this.timestamp!!.setValue(copy.timestamp!!.getValue())
    }

    constructor(
        identifier: String?,
        frameBody: AbstractTagFrameBody?,
        tempo: Int = 0x00,
        timestamp: Long = 0L
    ) : super(identifier, frameBody) {
        setBody(frameBody)
        this.tempo!!.setValue(tempo)
        this.timestamp!!.setValue(timestamp)
    }

    override fun setBody(frameBody: AbstractTagFrameBody?) {
        super.setBody(frameBody)
        this.tempo!!.setBody(frameBody)
        this.timestamp!!.setBody(frameBody)
    }

    override fun readByteArray(arr: ByteArray, offset: Int) {
        var localOffset = offset
        val size = getSize()

        log.debug("offset:$localOffset")

        //The read has extended further than the defined frame size (ok to extend upto
        //size because the next datatype may be of length 0.)
        if (offset > arr.size - size) {
            log.warn("Invalid size for FrameBody")
            throw InvalidDataTypeException("Invalid size for FrameBody")
        }

        this.tempo!!.readByteArray(arr, localOffset)
        localOffset += this.tempo.getSize()
        this.timestamp!!.readByteArray(arr, localOffset)
        localOffset += this.timestamp.getSize()
    }

    override fun getSize(): Int {
        return this.tempo!!.getSize() + this.timestamp!!.getSize()
    }

    override fun writeByteArray(): ByteArray {
        val typeData = this.tempo!!.writeByteArray()
        val timeData = this.timestamp!!.writeByteArray()

        val objectData = ByteArray(typeData.size + timeData.size)
        System.arraycopy(typeData, 0, objectData, 0, typeData.size)
        System.arraycopy(timeData, 0, objectData, typeData.size, timeData.size)
        return objectData
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) {
            return true
        }
        if (other == null || javaClass != other.javaClass) {
            return false
        }
        if (!super.equals(other)) {
            return false
        }

        val that = other as SynchronisedTempoCode
        return (this.getTempo() == that.getTempo() &&
                this.getTimestamp() == that.getTimestamp()
                )
    }

    fun getTimestamp(): Long {
        return (timestamp!!.getValue() as Number).toLong()
    }

    fun setTimestamp(timestamp: Long) {
        this.timestamp!!.setValue(timestamp)
    }

    fun getTempo(): Int {
        return (tempo!!.getValue() as Number).toInt()
    }

    fun setTempo(tempo: Int) {
        require(!(tempo !in 0..510)) { "Tempo must be a positive value less than 511: $tempo" }
        this.tempo!!.setValue(tempo)
    }

    override fun hashCode(): Int {
        var result = if (tempo != null) tempo.hashCode() else 0
        result = 31 * result + (if (timestamp != null) timestamp.hashCode() else 0)
        return result
    }

    override fun toString(): String {
        return ("${getTempo()} (\"${EventTimingTypes.fromId(getTempo())}\"), ${getTimestamp()}")
    }

    public override fun clone(): Any {
        return SynchronisedTempoCode(this)
    }
}
