package net.tylerwade.registrationsystem.common;

public record APIResponse<T>(boolean isSuccess, String message, T data) {

}
