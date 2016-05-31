package com.axiell.ehub.provider.ep;

import com.axiell.ehub.checkout.ContentLinkDTO;
import com.axiell.ehub.checkout.SupplementLinkDTO;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class FormatMetadataDTO {
    private List<ContentLinkDTO> contentLinks;
    private List<SupplementLinkDTO> supplementLinks;

    public List<ContentLinkDTO> getContentLinks() {
        return contentLinks;
    }

    public List<SupplementLinkDTO> getSupplementLinks() {
        return supplementLinks;
    }

    public FormatMetadataDTO contentLinks(final List<ContentLinkDTO> contentLinks) {
        this.contentLinks = contentLinks;
        return this;
    }

    public FormatMetadataDTO supplementLinks(final List<SupplementLinkDTO> supplementLinks) {
        this.supplementLinks = supplementLinks;
        return this;
    }
}
