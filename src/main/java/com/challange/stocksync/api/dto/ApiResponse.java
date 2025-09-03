package com.challange.stocksync.api.dto;

import com.challange.stocksync.enums.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Exclude null fields from the response
public class ApiResponse<T> {
    private ResponseStatus status;
    private String message;
    private Long totalCount;
    private Integer pageNumber;
    private Integer pageSize;
    private Boolean isLastPage;
    private Boolean isFirstPage;
    private T data;
    private T errorDetails;

    // Constructor for success response
    public ApiResponse(long totalCount,
                       int pageNumber,
                       int pageSize,
                       boolean isFirstPage,
                       boolean isLastPage,
                       T data) {
        this.status = ResponseStatus.SUCCESS;
        this.totalCount = totalCount;
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.isLastPage = isLastPage;
        this.isFirstPage = isFirstPage;
        this.data = data;
        this.errorDetails = null;
    }

    // Constructor for success response or error handling with msg only
    public ApiResponse(ResponseStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    // Constructor for success response or error handling
    public ApiResponse(ResponseStatus status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    // Constructor for error response
    public ApiResponse(String message, T errorDetails) {
        this.status = ResponseStatus.ERROR;
        this.message = message;
        this.errorDetails = errorDetails;
        this.data = null; // No data for error response
    }
}
