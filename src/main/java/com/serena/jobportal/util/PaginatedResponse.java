package com.serena.jobportal.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PaginatedResponse<T> {
    private Date timestamp;
    private int status;
    private String message;
    private List<T> data;
    private int page;
    private int size;
    private long totalItems;
    private int totalPages;

    public static <T> PaginatedResponse<T> of(List<T> data, int page, int size, long totalItems) {
        return PaginatedResponse.<T>builder()
                .timestamp(new Date())
                .status(200)
                .message("Success")
                .data(data)
                .page(page)
                .size(size)
                .totalItems(totalItems)
                .totalPages((int) Math.ceil((double) totalItems / size))
                .build();
    }
}