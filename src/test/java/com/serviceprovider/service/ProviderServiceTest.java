package com.serviceprovider.service;

import com.serviceprovider.dto.ColorsDto;
import com.serviceprovider.dto.LinksDto;
import com.serviceprovider.dto.OffersDto;
import com.serviceprovider.dto.ProviderDto;
import com.serviceprovider.entity.Colors;
import com.serviceprovider.entity.Links;
import com.serviceprovider.entity.Offers;
import com.serviceprovider.entity.Provider;
import com.serviceprovider.exception.DuplicateCodeException;
import com.serviceprovider.exception.ProviderNotFoundException;
import com.serviceprovider.mapper.ProviderMapper;
import com.serviceprovider.repository.ProviderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("ProviderService Unit Tests")
class ProviderServiceTest {

    @Mock
    private ProviderRepository providerRepository;

    @Mock
    private ProviderMapper providerMapper;

    @InjectMocks
    private ProviderService providerService;

    private ProviderDto providerDto;
    private Provider provider;

    @BeforeEach
    void setUp() {
        providerDto = ProviderDto.builder()
                .code("leo-tv")
                .name("Leo Tv")
                .colors(ColorsDto.builder()
                        .bright("#F2C858")
                        .dark("#201E1F")
                        .theme("#F7C236")
                        .build())
                .hosts(Map.of(
                        "Falcon", "7aeed.store:80",
                        "HULK", "hulascw.space:8080"
                ))
                .icon("https://example.com/icon.png")
                .links(LinksDto.builder()
                        .telegram("https://t.me/leo_store11")
                        .website("https://leos20.com/")
                        .whatsapp("https://wa.me/966501070573")
                        .build())
                .offers(OffersDto.builder()
                        .whatsapp("https://example.com/offers.png")
                        .build())
                .ads(Map.of("ad1", "https://example.com/ad1.png"))
                .build();

        provider = Provider.builder()
                .id("64abc123def456")
                .code("leo-tv")
                .name("Leo Tv")
                .colors(Colors.builder()
                        .bright("#F2C858")
                        .dark("#201E1F")
                        .theme("#F7C236")
                        .build())
                .hosts(Map.of("Falcon", "7aeed.store:80"))
                .icon("https://example.com/icon.png")
                .links(Links.builder()
                        .telegram("https://t.me/leo_store11")
                        .build())
                .offers(Offers.builder()
                        .whatsapp("https://example.com/offers.png")
                        .build())
                .ads(Map.of("ad1", "https://example.com/ad1.png"))
                .build();
    }

    @Test
    @DisplayName("createProvider - success")
    void createProvider_WhenCodeIsUnique_ReturnsDto() {
        when(providerRepository.existsByCode("leo-tv")).thenReturn(false);
        when(providerMapper.toEntity(providerDto)).thenReturn(provider);
        when(providerRepository.save(provider)).thenReturn(provider);
        when(providerMapper.toDto(provider)).thenReturn(providerDto);

        ProviderDto result = providerService.createProvider(providerDto);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("leo-tv");
        assertThat(result.getName()).isEqualTo("Leo Tv");
        verify(providerRepository).save(provider);
    }

    @Test
    @DisplayName("createProvider - throws DuplicateCodeException when code exists")
    void createProvider_WhenCodeExists_ThrowsDuplicateCodeException() {
        when(providerRepository.existsByCode("leo-tv")).thenReturn(true);

        assertThatThrownBy(() -> providerService.createProvider(providerDto))
                .isInstanceOf(DuplicateCodeException.class)
                .hasMessageContaining("leo-tv");

        verify(providerRepository, never()).save(any());
    }

    @Test
    @DisplayName("getProviderByCode - success")
    void getProviderByCode_WhenExists_ReturnsDto() {
        when(providerRepository.findByCode("leo-tv")).thenReturn(Optional.of(provider));
        when(providerMapper.toDto(provider)).thenReturn(providerDto);

        ProviderDto result = providerService.getProviderByCode("leo-tv");

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("leo-tv");
        assertThat(result.getColors().getBright()).isEqualTo("#F2C858");
    }

    @Test
    @DisplayName("getProviderByCode - throws ProviderNotFoundException when not found")
    void getProviderByCode_WhenNotFound_ThrowsNotFoundException() {
        when(providerRepository.findByCode("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> providerService.getProviderByCode("unknown"))
                .isInstanceOf(ProviderNotFoundException.class)
                .hasMessageContaining("unknown");
    }

    @Test
    @DisplayName("getAllProviders - returns mapped list")
    void getAllProviders_ReturnsMappedList() {
        when(providerRepository.findAll()).thenReturn(List.of(provider));
        when(providerMapper.toDto(provider)).thenReturn(providerDto);

        List<ProviderDto> result = providerService.getAllProviders();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Leo Tv");
    }

    @Test
    @DisplayName("deleteProvider - success")
    void deleteProvider_WhenExists_DeletesProvider() {
        when(providerRepository.findByCode("leo-tv")).thenReturn(Optional.of(provider));

        providerService.deleteProvider("leo-tv");

        verify(providerRepository).delete(provider);
    }

    @Test
    @DisplayName("deleteProvider - throws ProviderNotFoundException when not found")
    void deleteProvider_WhenNotFound_ThrowsNotFoundException() {
        when(providerRepository.findByCode("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> providerService.deleteProvider("unknown"))
                .isInstanceOf(ProviderNotFoundException.class);

        verify(providerRepository, never()).delete(any());
    }

    @Test
    @DisplayName("updateProvider - throws ProviderNotFoundException when not found")
    void updateProvider_WhenNotFound_ThrowsNotFoundException() {
        when(providerRepository.findByCode("unknown")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> providerService.updateProvider("unknown", providerDto))
                .isInstanceOf(ProviderNotFoundException.class);
    }
}
