package com.community.web.exception;

public class NotOwnerException extends RuntimeException {

    public NotOwnerException(String msg) {
        super(msg);
    }
}
