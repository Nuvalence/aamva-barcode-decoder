package io.nuvalence.aamva.barcode;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.Reader;
import com.google.zxing.ReaderException;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.GlobalHistogramBinarizer;
import com.google.zxing.pdf417.PDF417Reader;
import lombok.extern.slf4j.Slf4j;

import java.awt.image.BufferedImage;

@Slf4j
public class DefaultPdf417BarcodeDecoder implements Pdf417BarcodeDecoder {

    @Override
    public Barcode findPdf417BarcodeFromImage(BufferedImage image) {
        BinaryBitmap bitmap = new BinaryBitmap(new GlobalHistogramBinarizer(new BufferedImageLuminanceSource(image)));
        Reader reader = new PDF417Reader();

        try {
            return new Barcode(reader.decode(bitmap, BarcodeConstants.DECODE_HINTS).getText());
        } catch (ReaderException re) {
            log.warn("Failed to find PDF417 barcode using default decoder", re);
            return null;
        }
    }

}
