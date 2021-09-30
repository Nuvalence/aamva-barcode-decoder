package io.nuvalence.aamva;

import io.nuvalence.aamva.annotation.AamvaElementId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Setter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Data
public class AamvaIdentity {

    private static final int ELEMENT_ID_LENGTH = 3;
    private static final DateTimeFormatter US_DATE_FORMATTER = DateTimeFormatter.ofPattern("MMddyyyy");

    @AamvaElementId({"DCT", "DAC"})
    private String firstName;
    @AamvaElementId({"DCS", "DAB"})
    private String lastName;
    @AamvaElementId("DAD")
    private String middleName;

    @AamvaElementId("DBC")
    private Sex sex;
    @AamvaElementId("DAU")
    private String height;
    @AamvaElementId("DCB")
    private String eyeColor;

    @AamvaElementId("DAG")
    private String address;
    @AamvaElementId("DAI")
    private String city;
    @AamvaElementId("DAJ")
    private String state;
    @AamvaElementId("DAK")
    private Integer postalCode;
    @AamvaElementId("DCG")
    private String country;

    @AamvaElementId("DBB")
    private LocalDate dateOfBirth;
    @AamvaElementId("DAQ")
    private Integer driverLicenseNumber;
    @AamvaElementId("DBD")
    private LocalDate licenseIssueDate;
    @AamvaElementId("DBA")
    private LocalDate licenseExpirationDate;

    @AllArgsConstructor
    public enum Sex {
        MALE(1), FEMALE(2), NOT_SPECIFIED(9);

        @Setter
        private int code;

        public static Sex ofCode(int code) {
            return Arrays.stream(Sex.values())
                    .filter(s -> s.code == code)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("Invalid code %d", code)));
        }

    }

}
