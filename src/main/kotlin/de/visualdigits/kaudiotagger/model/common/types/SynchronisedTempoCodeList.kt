package de.visualdigits.kaudiotagger.model.common.types

import de.visualdigits.kaudiotagger.model.common.datatype.AbstractDataTypeList
import de.visualdigits.kaudiotagger.model.common.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.frame.framebody.FrameBodySYTC

/**
 * List of [org.jaudiotagger.tag.datatype.SynchronisedTempoCode]s.
 *
 * @author [Hendrik Schreiber](mailto:hs@tagtraum.com)
 * @version $Id:$
 */
class SynchronisedTempoCodeList: AbstractDataTypeList<SynchronisedTempoCode> {
    /**
     * Mandatory, concretely-typed copy constructor, as required by
     * [org.jaudiotagger.tag.datatype.AbstractDataTypeList.AbstractDataTypeList].
     *
     * @param copy instance to copy
     */
    constructor(copy: SynchronisedTempoCodeList) : super(copy)

    constructor(body: FrameBodySYTC) : super(DataTypes.OBJ_SYNCHRONISED_TEMPO_LIST, body)

    override fun createListElement(): SynchronisedTempoCode {
        return SynchronisedTempoCode(
            DataTypes.OBJ_SYNCHRONISED_TEMPO,
            getBody()
        )
    }
}
