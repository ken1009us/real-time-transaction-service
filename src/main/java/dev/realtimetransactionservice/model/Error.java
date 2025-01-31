package dev.realtimetransactionservice.model;

/**
 * Error model class
 */
public class Error {
    private String message;
    private String code;

    public Error() {}

    public Error(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}