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
public class LinksDto {

    @JsonProperty("Telegram")
    @URL(message = "Telegram must be a valid URL")
    private String telegram;

    @JsonProperty("Website")
    @URL(message = "Website must be a valid URL")
    private String website;

    @JsonProperty("Whatsapp")
    @URL(message = "Whatsapp must be a valid URL")
    private String whatsapp;
}
