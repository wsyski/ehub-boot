package com.axiell.ehub.common.checkout;

import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.SupplementLinkDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SupplementLinks {
    private List<SupplementLinkDTO> supplementLinksDTO = new ArrayList<>();

    public SupplementLinks(final List<SupplementLink> supplementLinks) {
        if (supplementLinks != null) {
            this.supplementLinksDTO = supplementLinks.stream()
                    .map(supplementLink -> new SupplementLinkDTO().name(supplementLink.name()).href(supplementLink.href())).collect(Collectors.toList());
        }
    }

    public List<SupplementLink> getSupplementLinks() {
        return supplementLinksDTO.stream().map(SupplementLink::new).collect(Collectors.toList());
    }

    public List<SupplementLinkDTO> toDTO() {
        return supplementLinksDTO;
    }

    private SupplementLinks() {
    }

    private SupplementLinks supplementLinksDTO(final List<SupplementLinkDTO> supplementLinksDTO) {
        this.supplementLinksDTO = supplementLinksDTO;
        return this;
    }

    public static SupplementLinks fromDTO(final List<SupplementLinkDTO> supplementLinksDTO) {
        return new SupplementLinks().supplementLinksDTO(supplementLinksDTO);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}
