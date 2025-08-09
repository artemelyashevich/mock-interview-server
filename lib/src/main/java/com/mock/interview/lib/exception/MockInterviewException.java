package com.mock.interview.lib.exception;

public class MockInterviewException extends RuntimeException {

    private final Integer statusCode;

    public MockInterviewException(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public MockInterviewException(String message, Integer statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public MockInterviewException(String message, Throwable cause, Integer statusCode) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public MockInterviewException(Throwable cause, Integer statusCode) {
        super(cause);
        this.statusCode = statusCode;
    }

    public MockInterviewException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace, Integer statusCode) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.statusCode = statusCode;
    }
}
