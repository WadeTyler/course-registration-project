package net.tylerwade.registrationsystem.common;

import lombok.Getter;
import org.springframework.data.domain.Page;

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

        public static <T> PageResponse<T> convertPage(Page<T> page) {
               return new PageResponse<>(page.getContent(),
                       page.getNumber(),
                       page.getSize(),
                       page.getTotalElements(),
                       page.getTotalPages());
        }
}
