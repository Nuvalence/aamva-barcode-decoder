package io.nuvalence.aamva.barcode;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.pdf417.PDF417Reader;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;
import java.util.EnumMap;
import java.util.Map;

@Slf4j
public class PurePdf417BarcodeDecoder implements Pdf417BarcodeDecoder {

    private static final Map<DecodeHintType, Object> PURE_DECODE_HINTS;
    static {
        PURE_DECODE_HINTS = new EnumMap<>(BarcodeConstants.DECODE_HINTS);
        PURE_DECODE_HINTS.put(DecodeHintType.PURE_BARCODE, Boolean.TRUE);
    }

    @Override
    public Barcode findPdf417BarcodeFromImage(BufferedImage image) {
        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(new BufferedImageLuminanceSource(image)));
        Reader reader = new PDF417Reader();

        try {
            return new Barcode(reader.decode(bitmap, PURE_DECODE_HINTS).getText());
        } catch (ReaderException re) {
            log.warn("Failed to find PDF417 barcode using pure barcode decoder", re);
            return null;
        }
    }

}
