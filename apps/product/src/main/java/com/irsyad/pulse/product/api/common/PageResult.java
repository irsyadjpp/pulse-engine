package com.irsyad.pulse.product.api.common;

import java.util.List;

/**
 * Standard pagination response envelope (TSD_04 Section 12).
 *
 * <pre>
 * {
 *   "content": [],
 *   "page": 0,
 *   "size": 20,
 *   "totalElements": 100,
 *   "totalPages": 5
 * }
 * </pre>
 */
public record PageResult<T>(
        List<T> content,
        int page,
        int size,
        long totalElements,
        int totalPages) {

    public static <T> PageResult<T> of(List<T> content, int page, int size, long totalElements) {
        int totalPages = size == 0 ? 0 : (int) Math.ceil((double) totalElements / size);
        return new PageResult<>(content, page, size, totalElements, totalPages);
    }
}