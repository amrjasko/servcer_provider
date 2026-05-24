package com.serviceprovider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderDto {

    @NotBlank(message = "Code is required")
    private String code;

    @JsonProperty("Name")
    @NotBlank(message = "Name is required")
    private String name;

    @JsonProperty("Colors")
    @Valid
    private ColorsDto colors;

    @JsonProperty("Hosts")
    private Map<String, String> hosts;

    @JsonProperty("Icon")
    @URL(message = "Icon must be a valid URL")
    private String icon;

    @JsonProperty("Links")
    @Valid
    private LinksDto links;

    @JsonProperty("Offers")
    @Valid
    private OffersDto offers;

    @JsonProperty("Ads")
    private Map<String, String> ads;
}
