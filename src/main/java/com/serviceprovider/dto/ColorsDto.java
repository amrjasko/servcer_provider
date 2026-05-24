package com.serviceprovider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ColorsDto {

    @JsonProperty("Bright")
    @NotBlank(message = "Bright color is required")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Bright must be a valid hex color (e.g. #F2C858)")
    private String bright;

    @JsonProperty("Dark")
    @NotBlank(message = "Dark color is required")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Dark must be a valid hex color (e.g. #201E1F)")
    private String dark;

    @JsonProperty("Theme")
    @NotBlank(message = "Theme color is required")
    @Pattern(regexp = "^#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})$", message = "Theme must be a valid hex color (e.g. #F7C236)")
    private String theme;
}
