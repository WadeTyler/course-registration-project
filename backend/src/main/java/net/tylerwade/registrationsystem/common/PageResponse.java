package net.tylerwade.registrationsystem.common;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

/**
 * Represents a paginated response for API endpoints.
 * Provides details about the content, pagination, and total elements.
 *
 * @param <T> The type of the content in the paginated response.
 */
public record PageResponse<T>(
        /*
         * The content of the current page.
         */
        @Getter
        List<T> content,

        /*
         * The current page number (zero-based index).
         */
        @Getter
        int pageNumber,

        /*
         * The size of the page (number of elements per page).
         */
        @Getter
        int pageSize,

        /*
         * The total number of elements across all pages.
         */
        @Getter
        long totalElements,

        /*
         * The total number of pages available.
         */
        @Getter
        int totalPages
) {

        /**
         * Converts a Spring Data Page object to a PageResponse object.
         *
         * @param page The Page object to convert.
         * @param <T> The type of the content in the Page object.
         * @return A PageResponse object containing the paginated data.
         */
        public static <T> PageResponse<T> convertPage(Page<T> page) {
                return new PageResponse<>(page.getContent(),
                        page.getNumber(),
                        page.getSize(),
                        page.getTotalElements(),
                        page.getTotalPages());
        }
}