package com.challange.stocksync.errorhandler;


import com.challange.stocksync.api.dto.ApiResponse;
import com.challange.stocksync.enums.ResponseStatus;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    private ApiResponse<Object> getExceptionResponse(String errMsg) {
        Map<String, Object> response = new HashMap<>();
        response.put("timestamp", Instant.now());
        response.put("status", HttpStatus.ALREADY_REPORTED.value());
        response.put("error", HttpStatus.ALREADY_REPORTED.getReasonPhrase());
        response.put("message", errMsg);
        return new ApiResponse<>(null, response);
    }

    // Handle generic exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        log.error("handleGlobalException() => Exception error", ex);
        return new ResponseEntity<>(getExceptionResponse("Internal Server Error. Contact support."),
                HttpStatus.ALREADY_REPORTED);
    }


    // Handle validation errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        log.error("handleMethodArgumentNotValidException() => Validation error: ", ex);
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(field -> field.getField() + ": " + field.getDefaultMessage())
                .collect(Collectors.joining(", "));
        ErrorResponseDto response = getErrorResponse(ErrorCode.VALIDATION_ERROR.getCode(),
                errors);
        ApiResponse<Object> apiResponse = ApiResponse
                .builder()
                .status(ResponseStatus.ERROR)
                .errorDetails(response)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    // Handle missing entity (404 Not Found)
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleEntityNotFoundException(EntityNotFoundException ex) {
        log.error("handleEntityNotFoundException() => Entity not found: ", ex);
        return new ResponseEntity<>(getExceptionResponse(ex.getMessage()), HttpStatus.NOT_FOUND);
    }


    @ExceptionHandler(UnsupportedOperationException.class)
    public ResponseEntity<ApiResponse<Object>> handleUnsupportedOperationException(UnsupportedOperationException exp) {
        log.error("Unsupported Type Exception", exp);
        ErrorResponseDto response = getErrorResponse(ErrorCode.UNSUPPORTED_ERROR.getCode(),
                ErrorCode.UNSUPPORTED_ERROR.getMessage());
        ApiResponse<Object> apiResponse = ApiResponse
                .builder()
                .status(ResponseStatus.ERROR)
                .errorDetails(response)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiResponse);
    }

    @ExceptionHandler(CommonException.class)
    public ResponseEntity<ApiResponse<Object>> handlePixieException(HttpServletRequest request, CommonException exp) {
        log.error("handlePixieException() => Resource Not Found", exp);
        ErrorResponseDto response = getErrorResponse(exp.getCode(), exp.getMessage());
        ApiResponse<Object> apiResponse = ApiResponse
                .builder()
                .status(ResponseStatus.ERROR)
                .errorDetails(response)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiResponse);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Object>> handleConstraintViolations(ConstraintViolationException ex) {
        String errorMessage = ex.getConstraintViolations().stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining(", "));

        return new ResponseEntity<>(getExceptionResponse(errorMessage), HttpStatus.BAD_REQUEST);
    }

    private ErrorResponseDto getErrorResponse(String code, String message) {
        return ErrorResponseDto
                .builder()
                .code(code)
                .message(message)
                .build();
    }
}
