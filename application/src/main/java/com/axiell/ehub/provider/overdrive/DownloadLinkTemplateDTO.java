package com.axiell.ehub.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadLinkTemplateDTO {
    private String href;

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    private DownloadLinkTemplateDTO() {
    }

    public DownloadLinkTemplateDTO(final String href) {
        this.href = href;
    }
}
