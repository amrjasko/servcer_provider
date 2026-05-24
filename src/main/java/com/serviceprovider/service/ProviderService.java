package com.serviceprovider.service;

import com.serviceprovider.dto.ProviderDto;
import com.serviceprovider.entity.Provider;
import com.serviceprovider.exception.DuplicateCodeException;
import com.serviceprovider.exception.ProviderNotFoundException;
import com.serviceprovider.mapper.ProviderMapper;
import com.serviceprovider.repository.ProviderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProviderService {

    private final ProviderRepository providerRepository;
    private final ProviderMapper providerMapper;

    public ProviderDto createProvider(ProviderDto dto) {
        log.info("Creating provider with code: {}", dto.getCode());

        if (providerRepository.existsByCode(dto.getCode())) {
            throw new DuplicateCodeException(dto.getCode());
        }

        Provider saved = providerRepository.save(providerMapper.toEntity(dto));
        log.info("Provider created: {}", saved.getCode());
        return providerMapper.toDto(saved);
    }

    public List<ProviderDto> getAllProviders() {
        log.info("Fetching all providers");
        return providerRepository.findAll()
                .stream()
                .map(providerMapper::toDto)
                .toList();
    }

    public ProviderDto getProviderByCode(String code) {
        log.info("Fetching provider: {}", code);
        Provider provider = providerRepository.findByCode(code)
                .orElseThrow(() -> new ProviderNotFoundException(code));
        return providerMapper.toDto(provider);
    }

    public ProviderDto updateProvider(String code, ProviderDto dto) {
        log.info("Updating provider: {}", code);
        Provider provider = providerRepository.findByCode(code)
                .orElseThrow(() -> new ProviderNotFoundException(code));
        providerMapper.updateEntity(provider, dto);
        Provider updated = providerRepository.save(provider);
        log.info("Provider updated: {}", code);
        return providerMapper.toDto(updated);
    }

    public void deleteProvider(String code) {
        log.info("Deleting provider: {}", code);
        Provider provider = providerRepository.findByCode(code)
                .orElseThrow(() -> new ProviderNotFoundException(code));
        providerRepository.delete(provider);
        log.info("Provider deleted: {}", code);
    }
}
