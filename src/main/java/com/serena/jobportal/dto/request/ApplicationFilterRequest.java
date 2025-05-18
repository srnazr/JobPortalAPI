// ApplicationFilterRequest.java
package com.serena.jobportal.dto.request;

import com.serena.jobportal.model.Application;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationFilterRequest {

    private String jobId;

    private String candidateId;

    private Application.Status status;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private PaginationRequest pagination;

    private SortRequest sort;
}