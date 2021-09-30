package io.nuvalence.aamva;

import io.nuvalence.aamva.barcode.Barcode;
import io.nuvalence.aamva.barcode.Pdf417BarcodeDecoder;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.SoftAssertions.assertSoftly;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AamvaIdentityServiceTest {

    @Test
    void whenFindIdentityFromImage_givenValidAamvaEncodedBarcode_thenIdentityDeserialized() {
        String barcodeText =
                "@\n" +
                        "\u001E\n" +
                        "ANSI 636001030102DL00410258ZN02990070DLDCAD\n" +
                        "DCBB\n" +
                        "DCDNONE\n" +
                        "DBA12302023\n" +
                        "DCSLICENSE\n" +
                        "DCTSAMPLE\n" +
                        "DADANY\n" +
                        "DBD09212015\n" +
                        "DBB12301985\n" +
                        "DBC1\n" +
                        "DAYBLU\n" +
                        "DAU600\n" +
                        "DAG2345 ANYPLACE AVE\n" +
                        "DAIANYTOWN\n" +
                        "DAJNY\n" +
                        "DAK123450000\n" +
                        "DAQ123456789\n" +
                        "DCFXFEE38ADF\n" +
                        "DCGUSA\n" +
                        "ZNZNALICENSE@SAMPLE@A\n" +
                        "ZNB^^KPtTcM$V=6C#B(l$\\\\LFq\\dKbrd!X<==Q4[RCI";
        Pdf417BarcodeDecoder decoder = mock(Pdf417BarcodeDecoder.class);
        when(decoder.findPdf417BarcodeFromImage(any(BufferedImage.class))).thenReturn(new Barcode(barcodeText));

        AamvaIdentityService service = new AamvaIdentityService(Collections.singleton(decoder));
        Optional<AamvaIdentity> identity = service.findIdentityFromImage(mock(BufferedImage.class));

        assertThat(identity)
                .isNotEmpty()
                .hasValueSatisfying(i -> assertSoftly(softly -> {
                    softly.assertThat(i.getFirstName()).isEqualTo("SAMPLE");
                    softly.assertThat(i.getLastName()).isEqualTo("LICENSE");
                    softly.assertThat(i.getMiddleName()).isEqualTo("ANY");
                    softly.assertThat(i.getSex()).isEqualTo(AamvaIdentity.Sex.MALE);
                    softly.assertThat(i.getHeight()).isEqualTo("600");
                    softly.assertThat(i.getEyeColor()).isEqualTo("B");
                    softly.assertThat(i.getAddress()).isEqualTo("2345 ANYPLACE AVE");
                    softly.assertThat(i.getCity()).isEqualTo("ANYTOWN");
                    softly.assertThat(i.getState()).isEqualTo("NY");
                    softly.assertThat(i.getPostalCode()).isEqualTo(123450000);
                    softly.assertThat(i.getDateOfBirth()).isEqualTo(LocalDate.of(1985, 12, 30));
                    softly.assertThat(i.getDriverLicenseNumber()).isEqualTo(123456789);
                    softly.assertThat(i.getLicenseIssueDate()).isEqualTo(LocalDate.of(2015, 9, 21));
                    softly.assertThat(i.getLicenseExpirationDate()).isEqualTo(LocalDate.of(2023, 12, 30));
                }));
    }

}
