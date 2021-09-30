package io.nuvalence.aamva.barcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.PDF417Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class Pdf417BarcodeDecoderTest {

    private static final Path IMAGE_PATH = Paths.get(System.getProperty("java.io.tmpdir"), UUID.randomUUID() + ".jpg");

    @AfterEach
    void afterEach() throws IOException {
        Files.deleteIfExists(IMAGE_PATH);
    }

    @ParameterizedTest(name = "when findPdf417BarcodeFromImage with {0}, given barcode in image, then barcode returned")
    @ArgumentsSource(BarcodeDecoderArgumentsProvider.class)
    void whenFindPdf417BarcodeFromImage_givenBarcodeInImage_thenBarcodeReturned(Pdf417BarcodeDecoder decoder)
            throws IOException, WriterException {
        final String barcodeText = UUID.randomUUID().toString();
        BitMatrix matrix = new PDF417Writer().encode(barcodeText, BarcodeFormat.PDF_417, 360, 180);
        MatrixToImageWriter.writeToPath(matrix, "jpeg", IMAGE_PATH);

        BufferedImage image = ImageIO.read(new FileInputStream(IMAGE_PATH.toFile()));
        assertThat(decoder.findPdf417BarcodeFromImage(image))
                .isNotNull()
                .extracting(Barcode::getText)
                .isEqualTo(barcodeText);
    }

    @ParameterizedTest(name = "when findPdf417BarcodeFromImage with {0}, given non-PDF417 barcode in image, then null")
    @ArgumentsSource(BarcodeDecoderArgumentsProvider.class)
    void whenFindPdf417BarcodeFromImage_givenNoPdf417Barcode_thenNull(Pdf417BarcodeDecoder decoder)
            throws IOException, WriterException {
        BitMatrix matrix = new QRCodeWriter().encode(UUID.randomUUID().toString(), BarcodeFormat.QR_CODE, 360, 360);
        MatrixToImageWriter.writeToPath(matrix, "jpeg", IMAGE_PATH);

        BufferedImage image = ImageIO.read(new FileInputStream(IMAGE_PATH.toFile()));
        assertThat(decoder.findPdf417BarcodeFromImage(image)).isNull();
    }

    public static class BarcodeDecoderArgumentsProvider implements ArgumentsProvider {
        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of(
                    Arguments.of(new DefaultPdf417BarcodeDecoder()),
                    Arguments.of(new PurePdf417BarcodeDecoder()),
                    Arguments.of(new HybridBinarizerPdf417BarcodeDecoder()));
        }
    }

}
