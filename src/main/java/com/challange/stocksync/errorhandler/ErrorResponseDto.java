package com.challange.stocksync.errorhandler;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponseDto {
    private String message;
    private String code;
}
