package com.serviceprovider.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.URL;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OffersDto {

    @JsonProperty("Whatsapp")
    @URL(message = "Offers Whatsapp must be a valid URL")
    private String whatsapp;
}
