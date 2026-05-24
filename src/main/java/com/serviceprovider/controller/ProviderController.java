package com.serviceprovider.controller;

import com.serviceprovider.dto.ApiResponse;
import com.serviceprovider.dto.ProviderDto;
import com.serviceprovider.service.ProviderService;
import com.serviceprovider.util.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/providers")
@RequiredArgsConstructor
@Tag(name = "Providers", description = "Provider CRUD operations (responses are AES-encrypted)")
public class ProviderController {

    private final ProviderService providerService;

    @PostMapping
    @Operation(
            summary = "Create provider",
            description = "Create a new provider. Requires ADMIN role.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<ProviderDto>> createProvider(@Valid @RequestBody ProviderDto dto) {
        ProviderDto created = providerService.createProvider(dto);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, Constants.PROVIDER_CREATED, HttpStatus.CREATED));
    }

    @GetMapping
    @Operation(
            summary = "Get all providers",
            description = "Retrieve a list of all providers. Requires ADMIN role.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<List<ProviderDto>>> getAllProviders() {
        List<ProviderDto> providers = providerService.getAllProviders();
        return ResponseEntity.ok(ApiResponse.success(providers, Constants.PROVIDERS_FETCHED));
    }

    @GetMapping("/{code}")
    @Operation(
            summary = "Get provider by code",
            description = "Retrieve a provider by its unique code. This endpoint is public (no auth required)."
    )
    public ResponseEntity<ApiResponse<ProviderDto>> getProviderByCode(@PathVariable String code) {
        ProviderDto provider = providerService.getProviderByCode(code);
        return ResponseEntity.ok(ApiResponse.success(provider, Constants.PROVIDER_FETCHED));
    }

    @PutMapping("/{code}")
    @Operation(
            summary = "Update provider",
            description = "Update an existing provider by code. Requires ADMIN role.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<ProviderDto>> updateProvider(
            @PathVariable String code,
            @Valid @RequestBody ProviderDto dto) {
        ProviderDto updated = providerService.updateProvider(code, dto);
        return ResponseEntity.ok(ApiResponse.success(updated, Constants.PROVIDER_UPDATED));
    }

    @DeleteMapping("/{code}")
    @Operation(
            summary = "Delete provider",
            description = "Delete a provider by code. Requires ADMIN role.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    public ResponseEntity<ApiResponse<Void>> deleteProvider(@PathVariable String code) {
        providerService.deleteProvider(code);
        return ResponseEntity.ok(ApiResponse.success(null, Constants.PROVIDER_DELETED));
    }
}
