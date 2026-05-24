package com.serviceprovider.exception;

public class DuplicateCodeException extends RuntimeException {

    public DuplicateCodeException(String code) {
        super("Provider with code '" + code + "' already exists");
    }
}
