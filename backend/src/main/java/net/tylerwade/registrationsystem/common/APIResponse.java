package net.tylerwade.registrationsystem.common;


import lombok.Getter;

public record APIResponse<T>(
        boolean isSuccess,
        @Getter String message,
        @Getter T data) {
}
