package com.axiell.ehub.checkout;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO implements Serializable {
    private CheckoutMetadataDTO metadata;

    private List<ContentLinkDTO> contentLinks;
    private List<SupplementLinkDTO> supplementLinks;

    public CheckoutMetadataDTO getMetadata() {
        return metadata;
    }

    public CheckoutDTO metadata(final CheckoutMetadataDTO metadataDTO) {
        this.metadata = metadataDTO;
        return this;
    }

    public List<ContentLinkDTO> getContentLinks() {
        return contentLinks == null ? new ArrayList<>() : contentLinks;
    }

    public List<SupplementLinkDTO> getSupplementLinks() {
        return supplementLinks == null ? new ArrayList<>() : supplementLinks;
    }

    public CheckoutDTO contentLinks(final List<ContentLinkDTO> contentLinks) {
        this.contentLinks = contentLinks;
        return this;
    }

    public CheckoutDTO supplementLinks(final List<SupplementLinkDTO> supplementLinks) {
        this.supplementLinks = supplementLinks;
        return this;
    }

    public CheckoutDTO contentLink(final ContentLinkDTO contentLinkDTO) {
        this.contentLinks = Collections.singletonList(contentLinkDTO);
        return this;
    }
}
