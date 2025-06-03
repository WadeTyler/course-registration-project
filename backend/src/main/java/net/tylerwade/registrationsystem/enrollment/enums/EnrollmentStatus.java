package net.tylerwade.registrationsystem.enrollment.enums;

import lombok.Getter;

@Getter
public enum EnrollmentStatus {
    NOT_STARTED("NOT_STARTED"),
    STARTED("STARTED"),
    COMPLETED("COMPLETED"),
    DROPPED("DROPPED"),
    FAILED("FAILED");

    private final String value;

    EnrollmentStatus(String value) {
        this.value = value;
    }
}
