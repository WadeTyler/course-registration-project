package net.tylerwade.registrationsystem.common;

import lombok.Getter;

import java.util.List;

public record PageResponse<T>(
        @Getter
        List<T> content,
        @Getter
        int pageNumber,
        @Getter
        int pageSize,
        @Getter
        long totalElements,
        @Getter
        int totalPages
        ) {
}
