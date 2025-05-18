// PaginationRequest.java
package com.serena.jobportal.dto.request;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationRequest {

    @Min(value = 0, message = "Page must be greater than or equal to 0")
    @Builder.Default
    private int page = 0;

    @Min(value = 1, message = "Size must be greater than 0")
    @Builder.Default
    private int size = 10;
}