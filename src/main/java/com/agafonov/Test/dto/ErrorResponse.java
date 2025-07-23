package com.agafonov.Test.dto;

import lombok.Data;
import java.util.List;

@Data
public class ErrorResponse {
    private int status;
    private String message;
    private long timestamp;
    private List<String> errors;

    public ErrorResponse(int status, String message, long timestamp, List<String> errors) {
        this.status = status;
        this.message = message;
        this.timestamp = timestamp;
        this.errors = errors;
    }

    public ErrorResponse(int status, String message, long timestamp) {
        this(status, message, timestamp, null);
    }
}
