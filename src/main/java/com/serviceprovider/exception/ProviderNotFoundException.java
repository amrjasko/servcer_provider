package com.serviceprovider.exception;

public class ProviderNotFoundException extends RuntimeException {

    public ProviderNotFoundException(String code) {
        super("Provider not found with code: " + code);
    }
}
