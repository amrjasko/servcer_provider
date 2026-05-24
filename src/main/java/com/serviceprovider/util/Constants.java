package com.serviceprovider.util;

public final class Constants {

    private Constants() {
    }

    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";

    public static final String AUTH_PATH_PREFIX = "/auth/";
    public static final String SWAGGER_UI_PATH = "/swagger-ui";
    public static final String API_DOCS_PATH = "/v3/api-docs";
    public static final String PROVIDERS_BY_CODE_PATH = "/providers/";
    public static final String DECRYPT_PATH = "/decrypt";

    public static final String PROVIDER_CREATED = "Provider created successfully";
    public static final String PROVIDER_UPDATED = "Provider updated successfully";
    public static final String PROVIDER_DELETED = "Provider deleted successfully";
    public static final String PROVIDER_FETCHED = "Provider fetched successfully";
    public static final String PROVIDERS_FETCHED = "Providers fetched successfully";
}
