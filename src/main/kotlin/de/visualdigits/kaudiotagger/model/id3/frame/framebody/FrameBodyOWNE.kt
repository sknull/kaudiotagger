package de.visualdigits.kaudiotagger.model.id3.frame.framebody

import de.visualdigits.kaudiotagger.model.id3.datatype.AbstractString
import de.visualdigits.kaudiotagger.model.id3.datatype.DataTypes
import de.visualdigits.kaudiotagger.model.id3.datatype.NumberHashMap
import de.visualdigits.kaudiotagger.model.id3.datatype.StringDate
import de.visualdigits.kaudiotagger.model.id3.datatype.StringNullTerminated
import de.visualdigits.kaudiotagger.model.id3.datatype.TextEncodedStringSizeTerminated
import de.visualdigits.kaudiotagger.model.id3.types.ID3V24Frame
import de.visualdigits.kaudiotagger.model.common.types.TextEncoding
import de.visualdigits.kaudiotagger.util.ID3TextEncodingConversion
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

/**
 * Ownership frame.
 *
 *
 *
 *
 * The ownership frame might be used as a reminder of a made transaction
 * or, if signed, as proof. Note that the "USER" and "TOWN" frames are
 * good to use in conjunction with this one. The frame begins, after the
 * frame ID, size and encoding fields, with a 'price payed' field. The
 * first three characters of this field contains the currency used for
 * the transaction, encoded according to ISO-4217 alphabetic
 * currency code. Concatenated to this is the actual price payed, as a
 * numerical string using "." as the decimal separator. Next is an 8
 * character date string (YYYYMMDD) followed by a string with the name
 * of the seller as the last field in the frame. There may only be one
 * "OWNE" frame in a tag.
 *
 * <table border=0 width="70%">
 * <tr><td>&lt;Header for 'Ownership frame', ID: "OWNE"&gt;</td></tr>
 * <tr><td>Text encoding  </td><td>$xx                     </td></tr>
 * <tr><td>Price payed    </td><td>&lt;text string&gt; $00 </td></tr>
 * <tr><td>Date of purch. </td><td>&lt;text string&gt;     </td></tr>
 * <tr><td>Seller</td><td>&lt;text string according to encoding&gt;</td></tr>
</table> *
 *
 *
 * For more details, please refer to the ID3 specifications:
 *
 *  * [ID3 v2.3.0 Spec](http://www.id3.org/id3v2.3.0.txt)
 *
 *
 * @author : Paul Taylor
 * @author : Eric Farng
 * @version $Id$
 */
class FrameBodyOWNE

    : AbstractID3v2FrameBody, ID3v24FrameBody, ID3v23FrameBody {
    /**
     * Creates a new FrameBodyOWNE datatype.
     */
    constructor()

    constructor(body: FrameBodyOWNE) : super(body)

    /**
     * Creates a new FrameBodyOWNE datatype.
     *
     * @param textEncoding
     * @param pricePaid
     * @param dateOfPurchase
     * @param seller
     */
    constructor(
        textEncoding: Byte,
        pricePaid: String?,
        dateOfPurchase: String?,
        seller: String?
    ) {
        this.setObjectValue(DataTypes.OBJ_TEXT_ENCODING, textEncoding)
        this.setObjectValue(DataTypes.OBJ_PRICE_PAID, pricePaid)
        this.setObjectValue(DataTypes.OBJ_PURCHASE_DATE, dateOfPurchase)
        this.setObjectValue(DataTypes.OBJ_SELLER_NAME, seller)
    }

    /**
     * Creates a new FrameBodyOWNE datatype.
     *
     * @param byteBuffer
     * @param frameSize
     */
    constructor(byteBuffer: ByteBuffer?, frameSize: Int) : super(byteBuffer, frameSize)

    /**
     * The ID3v2 frame identifier
     *
     * @return the ID3v2 frame identifier  for this frame type
     */
    override fun getIdentifier(): String {
        return ID3V24Frame.OWNERSHIP.id
    }

    /**
     * If the seller name cannot be encoded using current encoder, change the encoder
     */
    override fun write(tagBuffer: ByteArrayOutputStream) {
        //Ensure valid for type
        setTextEncoding(
            ID3TextEncodingConversion.getTextEncoding(header, getTextEncoding())
        )

        //Ensure valid for data
        if (!(getObject(DataTypes.OBJ_SELLER_NAME) as AbstractString).canBeEncoded()
        ) {
            this.setTextEncoding(
                ID3TextEncodingConversion.getUnicodeTextEncoding(header)
            )
        }
        super.write(tagBuffer!!)
    }

    /**
     *
     */
    override fun setupObjectList() {
        objectList.add(
            NumberHashMap(
                DataTypes.OBJ_TEXT_ENCODING,
                this,
                TextEncoding.TEXT_ENCODING_FIELD_SIZE
            )
        )
        objectList.add(StringNullTerminated(DataTypes.OBJ_PRICE_PAID, this))
        objectList.add(StringDate(DataTypes.OBJ_PURCHASE_DATE, this))
        objectList.add(
            TextEncodedStringSizeTerminated(DataTypes.OBJ_SELLER_NAME, this)
        )
    }
}
