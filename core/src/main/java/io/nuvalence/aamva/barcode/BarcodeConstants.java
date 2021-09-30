package io.nuvalence.aamva.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.DecodeHintType;
import lombok.experimental.UtilityClass;

import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

@UtilityClass
public class BarcodeConstants {

    public static final Map<DecodeHintType,Object> DECODE_HINTS;
    static {
        DECODE_HINTS = new EnumMap<>(DecodeHintType.class);
        DECODE_HINTS.put(DecodeHintType.TRY_HARDER, Boolean.TRUE);
        DECODE_HINTS.put(DecodeHintType.POSSIBLE_FORMATS, Collections.singleton(BarcodeFormat.PDF_417));
        DECODE_HINTS.put(DecodeHintType.ALSO_INVERTED, Boolean.TRUE);
    }

}
