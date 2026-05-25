package com.serviceprovider.mapper;

import com.serviceprovider.dto.ProviderDto;
import com.serviceprovider.entity.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {

    public ProviderDto toDto(Provider provider) {
        if (provider == null) return null;
        return ProviderDto.builder()
                .code(provider.getCode())
                .data(provider.getData())
                .build();
    }

    public Provider toEntity(ProviderDto dto) {
        if (dto == null) return null;
        return Provider.builder()
                .code(dto.getCode())
                .data(dto.getData())
                .build();
    }

    public void updateEntity(Provider provider, ProviderDto dto) {
        provider.setData(dto.getData());
    }
}
