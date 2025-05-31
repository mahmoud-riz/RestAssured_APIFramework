package com.automation.exceptions;

/**
 * Custom exception for API-related errors
 */
public class APIException extends RuntimeException {
    
    private final int statusCode;
    private final String responseBody;
    
    public APIException(String message) {
        super(message);
        this.statusCode = 0;
        this.responseBody = null;
    }
    
    public APIException(String message, Throwable cause) {
        super(message, cause);
        this.statusCode = 0;
        this.responseBody = null;
    }
    
    public APIException(String message, int statusCode, String responseBody) {
        super(message);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
    
    public APIException(String message, int statusCode, String responseBody, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
        this.responseBody = responseBody;
    }
    
    public int getStatusCode() {
        return statusCode;
    }
    
    public String getResponseBody() {
        return responseBody;
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        if (statusCode > 0) {
            sb.append(" [Status Code: ").append(statusCode).append("]");
        }
        if (responseBody != null) {
            sb.append(" [Response Body: ").append(responseBody).append("]");
        }
        return sb.toString();
    }
} 