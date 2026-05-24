package com.serviceprovider.mapper;

import com.serviceprovider.dto.ColorsDto;
import com.serviceprovider.dto.LinksDto;
import com.serviceprovider.dto.OffersDto;
import com.serviceprovider.dto.ProviderDto;
import com.serviceprovider.entity.Colors;
import com.serviceprovider.entity.Links;
import com.serviceprovider.entity.Offers;
import com.serviceprovider.entity.Provider;
import org.springframework.stereotype.Component;

@Component
public class ProviderMapper {

    public ProviderDto toDto(Provider provider) {
        if (provider == null) return null;
        return ProviderDto.builder()
                .code(provider.getCode())
                .name(provider.getName())
                .colors(toColorsDto(provider.getColors()))
                .hosts(provider.getHosts())
                .icon(provider.getIcon())
                .links(toLinksDto(provider.getLinks()))
                .offers(toOffersDto(provider.getOffers()))
                .ads(provider.getAds())
                .build();
    }

    public Provider toEntity(ProviderDto dto) {
        if (dto == null) return null;
        return Provider.builder()
                .code(dto.getCode())
                .name(dto.getName())
                .colors(toColors(dto.getColors()))
                .hosts(dto.getHosts())
                .icon(dto.getIcon())
                .links(toLinks(dto.getLinks()))
                .offers(toOffers(dto.getOffers()))
                .ads(dto.getAds())
                .build();
    }

    public void updateEntity(Provider provider, ProviderDto dto) {
        provider.setName(dto.getName());
        provider.setColors(toColors(dto.getColors()));
        provider.setHosts(dto.getHosts());
        provider.setIcon(dto.getIcon());
        provider.setLinks(toLinks(dto.getLinks()));
        provider.setOffers(toOffers(dto.getOffers()));
        provider.setAds(dto.getAds());
    }

    // ── Nested mappers ──────────────────────────────────────────────────────

    private ColorsDto toColorsDto(Colors colors) {
        if (colors == null) return null;
        return ColorsDto.builder()
                .bright(colors.getBright())
                .dark(colors.getDark())
                .theme(colors.getTheme())
                .build();
    }

    private Colors toColors(ColorsDto dto) {
        if (dto == null) return null;
        return Colors.builder()
                .bright(dto.getBright())
                .dark(dto.getDark())
                .theme(dto.getTheme())
                .build();
    }

    private LinksDto toLinksDto(Links links) {
        if (links == null) return null;
        return LinksDto.builder()
                .telegram(links.getTelegram())
                .website(links.getWebsite())
                .whatsapp(links.getWhatsapp())
                .build();
    }

    private Links toLinks(LinksDto dto) {
        if (dto == null) return null;
        return Links.builder()
                .telegram(dto.getTelegram())
                .website(dto.getWebsite())
                .whatsapp(dto.getWhatsapp())
                .build();
    }

    private OffersDto toOffersDto(Offers offers) {
        if (offers == null) return null;
        return OffersDto.builder()
                .whatsapp(offers.getWhatsapp())
                .build();
    }

    private Offers toOffers(OffersDto dto) {
        if (dto == null) return null;
        return Offers.builder()
                .whatsapp(dto.getWhatsapp())
                .build();
    }
}
