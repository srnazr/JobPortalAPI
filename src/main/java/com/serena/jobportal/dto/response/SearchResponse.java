package com.serena.jobportal.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse<T> {

    private List<T> results;

    private long totalElements;

    private int totalPages;

    private int currentPage;

    private int pageSize;

    private Map<String, Long> facets;
}