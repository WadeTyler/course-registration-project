package net.tylerwade.registrationsystem.common;


import lombok.Getter;

public record APIResponse<T>(
        boolean isSuccess,
        @Getter String message,
        @Getter T data) {

    public static <T> APIResponse<T>  success(String message, T data) {
        return new APIResponse<>(true, message, data);
    }

    public static <T> APIResponse<T>  success(String message) {
        return new APIResponse<>(true, message, null);
    }

    public static <T> APIResponse<T>  failed(String message, T data) {
        return new APIResponse<>(false, message, data);
    }

    public static <T> APIResponse<T>  failed(String message) {
        return new APIResponse<>(true, message, null);
    }
}
