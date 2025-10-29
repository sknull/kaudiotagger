package de.visualdigits.kaudiotagger.model.id3.datatype

object DataTypes {

    /**
     * Represents a text encoding, now only IDv2Frames not Lyrics3 tags use
     * text encoding objects but both use Object Strings and these check
     * for a text encoding. The method below returns a default if one not set.
     */
    const val OBJ_TEXT_ENCODING: String = "TextEncoding"
    //Reference to datatype holding the main textual data
    const val OBJ_TEXT: String = "Text"
    //Reference to datatype holding non textual textual data
    const val OBJ_DATA: String = "Data"
    //Reference to datatype holding a description of the textual data
    const val OBJ_DESCRIPTION: String = "Description"
    //Reference to datatype holding reference to owner of frame.
    const val OBJ_OWNER: String = "Owner"
    //Reference to datatype holding a number
    const val OBJ_NUMBER: String = "Number"
    //Reference to timestamps
    const val OBJ_DATETIME: String = "DateTime"
    /**
     *
     */
    const val OBJ_GENRE: String = "Genre"
    /**
     *
     */
    const val OBJ_ID3V2_FRAME_DESCRIPTION: String = "ID3v2FrameDescription"

    //ETCO Frame
    const val OBJ_TYPE_OF_EVENT: String = "TypeOfEvent"
    const val OBJ_TIMED_EVENT: String = "TimedEvent"
    const val OBJ_TIMED_EVENT_LIST: String = "TimedEventList"
    //SYTC Frame
    const val OBJ_SYNCHRONISED_TEMPO_DATA: String = "SynchronisedTempoData"
    const val OBJ_SYNCHRONISED_TEMPO: String = "SynchronisedTempo"
    const val OBJ_SYNCHRONISED_TEMPO_LIST: String = "SynchronisedTempoList"
    /**
     *
     */
    const val OBJ_TIME_STAMP_FORMAT: String = "TimeStampFormat"
    /**
     *
     */
    const val OBJ_TYPE_OF_CHANNEL: String = "TypeOfChannel"
    /**
     *
     */
    const val OBJ_RECIEVED_AS: String = "RecievedAs"

    //APIC Frame
    const val OBJ_PICTURE_TYPE: String = "PictureType"
    const val OBJ_PICTURE_DATA: String = "PictureData"
    const val OBJ_MIME_TYPE: String = "MIMEType"
    const val OBJ_IMAGE_FORMAT: String = "ImageType"

    //AENC Frame
    const val OBJ_PREVIEW_START: String = "PreviewStart"
    const val OBJ_PREVIEW_LENGTH: String = "PreviewLength"
    const val OBJ_ENCRYPTION_INFO: String = "EncryptionInfo"

    //COMR Frame
    const val OBJ_PRICE_STRING: String = "PriceString"
    const val OBJ_VALID_UNTIL: String = "ValidUntil"
    const val OBJ_CONTACT_URL: String = "ContactURL"
    const val OBJ_SELLER_NAME: String = "SellerName"
    const val OBJ_SELLER_LOGO: String = "SellerLogo"

    //CRM Frame
    const val OBJ_ENCRYPTED_DATABLOCK: String = "EncryptedDataBlock"

    //ENCR Frame
    const val OBJ_METHOD_SYMBOL: String = "MethodSymbol"

    //EQU2 Frame
    const val OBJ_FREQUENCY: String = "Frequency"
    const val OBJ_VOLUME_ADJUSTMENT: String = "Volume Adjustment"
    const val OBJ_INTERPOLATION_METHOD: String = "InterpolationMethod"

    const val OBJ_FILENAME: String = "Filename"

    //GRID Frame
    const val OBJ_GROUP_SYMBOL: String = "GroupSymbol"
    const val OBJ_GROUP_DATA: String = "GroupData"

    //LINK Frame
    const val OBJ_URL: String = "URL"
    const val OBJ_ID: String = "ID"

    //OWNE Frame
    const val OBJ_PRICE_PAID: String = "PricePaid"
    const val OBJ_PURCHASE_DATE: String = "PurchaseDate"

    //POPM Frame
    const val OBJ_EMAIL: String = "Email"
    const val OBJ_RATING: String = "Rating"
    const val OBJ_COUNTER: String = "Counter"

    //POSS Frame
    const val OBJ_POSITION: String = "Position"

    //RBUF Frame
    const val OBJ_BUFFER_SIZE: String = "BufferSize"
    const val OBJ_EMBED_FLAG: String = "EmbedFlag"
    const val OBJ_OFFSET: String = "Offset"

    //RVRB Frame
    const val OBJ_REVERB_LEFT: String = "ReverbLeft"
    const val OBJ_REVERB_RIGHT: String = "ReverbRight"
    const val OBJ_REVERB_BOUNCE_LEFT: String = "ReverbBounceLeft"
    const val OBJ_REVERB_BOUNCE_RIGHT: String = "ReverbBounceRight"
    const val OBJ_REVERB_FEEDBACK_LEFT_TO_LEFT: String = "ReverbFeedbackLeftToLeft"
    const val OBJ_REVERB_FEEDBACK_LEFT_TO_RIGHT: String = "ReverbFeedbackLeftToRight"
    const val OBJ_REVERB_FEEDBACK_RIGHT_TO_RIGHT: String = "ReverbFeedbackRightToRight"
    const val OBJ_REVERB_FEEDBACK_RIGHT_TO_LEFT: String = "ReverbFeedbackRightToLeft"
    const val OBJ_PREMIX_LEFT_TO_RIGHT: String = "PremixLeftToRight"
    const val OBJ_PREMIX_RIGHT_TO_LEFT: String = "PremixRightToLeft"

    //SIGN Frame
    const val OBJ_SIGNATURE: String = "Signature"

    //SYLT Frame
    const val OBJ_CONTENT_TYPE: String = "contentType"

    //ULST Frame
    const val OBJ_LANGUAGE: String = "Language"
    const val OBJ_LYRICS: String = "Lyrics"
    const val OBJ_URLLINK: String = "URLLink"

    //CHAP Frame
    const val OBJ_ELEMENT_ID: String = "ElementID"
    const val OBJ_START_TIME: String = "StartTime"
    const val OBJ_END_TIME: String = "EndTime"
    const val OBJ_START_OFFSET: String = "StartOffset"
    const val OBJ_END_OFFSET: String = "EndOffset" //CTOC Frame


}