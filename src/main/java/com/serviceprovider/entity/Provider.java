package com.serviceprovider.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Map;

@Document(collection = "providers")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Provider {

    @Id
    private String id;

    @Indexed(unique = true)
    private String code;

    private String name;
    private Colors colors;
    private Map<String, String> hosts;
    private String icon;
    private Links links;
    private Offers offers;
    private Map<String, String> ads;
}
