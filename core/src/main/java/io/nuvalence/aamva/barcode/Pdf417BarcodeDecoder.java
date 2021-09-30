package io.nuvalence.aamva.barcode;

import java.awt.image.BufferedImage;

public interface Pdf417BarcodeDecoder {

    /**
     * Find a PDF417 barcode in an image.
     *
     * @param image the image to search
     * @return the decoded barcode or <code>null</code> if not found
     */
    Barcode findPdf417BarcodeFromImage(BufferedImage image);

}
