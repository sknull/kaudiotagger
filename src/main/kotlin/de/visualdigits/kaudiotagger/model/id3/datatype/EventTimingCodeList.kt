package de.visualdigits.kaudiotagger.model.id3.datatype

import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodyETCO

/**
 * List of [EventTimingCode]s.
 *
 * @author [Hendrik Schreiber](mailto:hs@tagtraum.com)
 * @version $Id:$
 */
class EventTimingCodeList : de.visualdigits.kaudiotagger.model.common.datatype.AbstractDataTypeList<EventTimingCode> {
    /**
     * Mandatory, concretely-typed copy constructor, as required by
     * [de.visualdigits.kaudiotagger.model.common.datatype.AbstractDataTypeList.AbstractDataTypeList].
     *
     * @param copy instance to copy
     */
    constructor(copy: EventTimingCodeList) : super(copy)

    constructor(body: FrameBodyETCO) : super(_root_ide_package_.de.visualdigits.kaudiotagger.model.common.datatype.DataTypes.OBJ_TIMED_EVENT_LIST, body)

    protected override fun createListElement(): EventTimingCode? {
        return EventTimingCode(_root_ide_package_.de.visualdigits.kaudiotagger.model.common.datatype.DataTypes.OBJ_TIMED_EVENT, getBody())
    }
}
