package de.visualdigits.kaudiotagger.model.datatype

import de.visualdigits.kaudiotagger.model.frame.framebody.id3.FrameBodyETCO

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
    constructor(copy: EventTimingCodeList) : super(copy)

    constructor(body: FrameBodyETCO) : super(DataTypes.OBJ_TIMED_EVENT_LIST, body)

    protected override fun createListElement(): EventTimingCode? {
        return EventTimingCode(DataTypes.OBJ_TIMED_EVENT, getBody())
    }
}
