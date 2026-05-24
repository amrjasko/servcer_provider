package com.serviceprovider.controller;

import com.serviceprovider.service.EncryptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/decrypt")
@RequiredArgsConstructor
@Tag(name = "Decrypt", description = "Utility endpoint for decrypting encrypted API responses")
public class DecryptController {

    private final EncryptionService encryptionService;

    @PostMapping(consumes = MediaType.TEXT_PLAIN_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @Operation(
            summary = "Decrypt encrypted response",
            description = "Paste the raw base64 encryptedData value as plain text body. No authentication required."
    )
    public ResponseEntity<String> decrypt(@RequestBody String encryptedData) {
        if (encryptedData == null || encryptedData.isBlank()) {
            return ResponseEntity.badRequest().body("Request body is required");
        }
        return ResponseEntity.ok(encryptionService.decrypt(encryptedData.trim()));
    }
}
