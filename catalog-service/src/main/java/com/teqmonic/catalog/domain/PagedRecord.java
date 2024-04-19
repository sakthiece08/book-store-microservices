package com.teqmonic.catalog.domain;

import java.util.List;

public record PagedRecord<T>(
        List<T> data,
        long totalElements,
        int pageNumber,
        int totalPages,
        boolean isFirst,
        boolean isLast,
        boolean hasNext,
        boolean hasPrevious) {}
