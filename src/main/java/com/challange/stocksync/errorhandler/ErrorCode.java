package com.challange.stocksync.errorhandler;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    INTERNAL_SERVER_ERROR("500", "Internal server error."),
    UNSUPPORTED_ERROR("501", "Unsupported Error."),
    VALIDATION_ERROR("704", "Validation error"),
    RESOURCE_NOT_FOUND("705", "Resource not found"),;
    private final String code;
    private final String message;

}
