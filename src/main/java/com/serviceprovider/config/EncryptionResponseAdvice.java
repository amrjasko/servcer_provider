package com.serviceprovider.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.serviceprovider.dto.EncryptedResponse;
import com.serviceprovider.service.EncryptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import static com.serviceprovider.util.Constants.AUTH_PATH_PREFIX;
import static com.serviceprovider.util.Constants.API_DOCS_PATH;
import static com.serviceprovider.util.Constants.SWAGGER_UI_PATH;
import static com.serviceprovider.util.Constants.PROVIDERS_BY_CODE_PATH;
import static com.serviceprovider.util.Constants.DECRYPT_PATH;

/**
 * Intercepts all JSON responses from @RestController methods and encrypts the body using AES-256-CBC.
 * Auth endpoints (/auth/**) and API documentation paths are excluded.
 *
 * Response format: { "encryptedData": "<base64(iv + ciphertext)>" }
 */
@RestControllerAdvice
@RequiredArgsConstructor
@Slf4j
public class EncryptionResponseAdvice implements ResponseBodyAdvice<Object> {

    private final EncryptionService encryptionService;
    private final ObjectMapper objectMapper;

    @Override
    public boolean supports(MethodParameter returnType,
                            Class<? extends HttpMessageConverter<?>> converterType) {
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType)
                && !returnType.getDeclaringClass().getSimpleName().equals("BasicErrorController");
    }

    @Override
    public Object beforeBodyWrite(Object body,
                                  MethodParameter returnType,
                                  MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        if (body == null) return null;
        if (body instanceof EncryptedResponse) return body;

        String path = request.getURI().getPath();
        if (isExcludedPath(path)) {
            return body;
        }

        try {
            String json = objectMapper.writeValueAsString(body);
            String encryptedData = encryptionService.encrypt(json);
            log.debug("Response encrypted for path: {}", path);
            return new EncryptedResponse(encryptedData);
        } catch (Exception e) {
            log.error("Response encryption failed for path '{}': {}", path, e.getMessage());
            return body;
        }
    }

    private boolean isExcludedPath(String path) {
        return path.startsWith(AUTH_PATH_PREFIX)
                || path.startsWith(SWAGGER_UI_PATH)
                || path.startsWith(API_DOCS_PATH)
                || path.startsWith(PROVIDERS_BY_CODE_PATH)
                || path.startsWith(DECRYPT_PATH);
    }
}
