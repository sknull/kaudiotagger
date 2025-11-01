package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyETCO

/**
 * List of [EventTimingCode]s.
 *
 * @author [Hendrik Schreiber](mailto:hs@tagtraum.com)
 * @version $Id:$
 */
class EventTimingCodeList : AbstractDataTypeList<EventTimingCode> {

    /**
     * Mandatory, concretely-typed copy constructor, as required by
     * [AbstractDataTypeList.AbstractDataTypeList].
     *
     * @param copy instance to copy
     */
    constructor(copyObject: EventTimingCodeList): super(copyObject)

    constructor(body: FrameBodyETCO) : super(DataTypes.OBJ_TIMED_EVENT_LIST, body)

    override fun createListElement(): EventTimingCode {
        return EventTimingCode(DataTypes.OBJ_TIMED_EVENT, getBody())
    }
}
