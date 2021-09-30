package io.nuvalence.aamva;

import io.nuvalence.aamva.annotation.AamvaElementId;
import io.nuvalence.aamva.barcode.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.reflect.FieldUtils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@NoArgsConstructor
@AllArgsConstructor
public class AamvaIdentityService {

    private static final int ELEMENT_ID_LENGTH = 3;
    private static final DateTimeFormatter US_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMddyyyy");

    private Collection<Pdf417BarcodeDecoder> decoders = Arrays.asList(
            new DefaultPdf417BarcodeDecoder(),
            new PurePdf417BarcodeDecoder(),
            new HybridBinarizerPdf417BarcodeDecoder());

    /**
     * Finds an AAMVA identity in an image encoded within a PDF417 barcode, usually of a driver license/non-driver id.
     *
     * @param image the image containing a PDF417 barcode to search
     * @return the identity
     * @see <a href="https://www.aamva.org/DL-ID-Card-Design-Standard/">AAMVA DL/ID Standards</a>
     */
    public Optional<AamvaIdentity> findIdentityFromImage(final BufferedImage image) {
        return decoders.stream().map(d -> d.findPdf417BarcodeFromImage(image))
                .filter(Objects::nonNull)
                .findFirst()
                .map(this::deserializeAamvaIdentity);
    }

    private AamvaIdentity deserializeAamvaIdentity(Barcode barcode) {
        BufferedReader reader = new BufferedReader(new StringReader(barcode.getText()));
        Map<String, String> subElements = reader.lines()
                .skip(3)
                .collect(Collectors.toMap(l -> l.substring(0, ELEMENT_ID_LENGTH), l -> l.substring(ELEMENT_ID_LENGTH).trim()));
        AamvaIdentity id = new AamvaIdentity();
        FieldUtils.getFieldsListWithAnnotation(AamvaIdentity.class, AamvaElementId.class)
                .forEach(field -> {
                    AamvaElementId element = field.getAnnotation(AamvaElementId.class);
                    Arrays.stream(element.value())
                            .map(subElements::get)
                            .filter(Objects::nonNull)
                            .findFirst()
                            .ifPresent(value -> setField(id, field, value));
                });
        return id;
    }

    private void setField(Object bean, Field field, String value) {
        try {
            Object valueObject = value;
            if (field.getType().isAssignableFrom(Integer.class)) {
                valueObject = Integer.valueOf(value);
            } else if (field.getType().isAssignableFrom(LocalDate.class)) {
                valueObject = LocalDate.parse(value, US_DATE_FORMATTER);
            } else if (field.getType().isAssignableFrom(AamvaIdentity.Sex.class)) {
                valueObject = AamvaIdentity.Sex.ofCode(Integer.parseInt(value));
            }
            PropertyUtils.setProperty(bean, field.getName(), valueObject);
        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            log.warn("Failed to set field {}", field.getName(), e);
        }
    }

}
