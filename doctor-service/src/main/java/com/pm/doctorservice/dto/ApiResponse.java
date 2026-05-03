package com.pm.doctorservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Generic API Response wrapper")
public class ApiResponse<T> {

    @Schema(description = "Response status code", example = "200")
    private int statusCode;

    @Schema(description = "Response message", example = "Operation successful")
    private String message;

    @Schema(description = "Response data")
    private T data;

    @Schema(description = "Error details if any")
    private String error;

    @Schema(description = "Timestamp of the response")
    private LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(int statusCode, String message, T data) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(int statusCode, String message, String error) {
        return ApiResponse.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .error(error)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
