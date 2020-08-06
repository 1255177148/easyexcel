package com.example.easyexcel.exception;

/**
 * @Author Zhanzhan
 * @Date 2020/8/6 16:59
 */
public class CustomException extends RuntimeException {
    public CustomException() {
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }
}
