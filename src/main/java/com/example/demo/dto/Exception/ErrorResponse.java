package com.example.demo.dto.Exception;

import java.time.LocalDateTime;

public class ErrorResponse {

    private int status;
    private String error;      // 예: BAD_REQUEST, NOT_FOUND
    private String message;    // 예: "결제가 존재하지 않습니다."
    private String path;       // 요청 URL
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String error, String message, String path) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus() { return status; }
    public String getError() { return error; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
    public LocalDateTime getTimestamp() { return timestamp; }
}
