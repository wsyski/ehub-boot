package com.axiell.ehub.local.provider.overdrive;

import com.axiell.ehub.common.util.DateFactory;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.Map;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private String reserveId;

    private String crossRefId;

    @JsonProperty(value = "expires")
    private Date expirationDate;

    @JsonProperty(value = "isFormatLockedIn")
    private boolean isFormatLocked;

    private Map<String, DownloadLinkTemplateDTO> links;

    public String getReserveId() {
        return reserveId;
    }

    public Date getExpirationDate() {
        return DateFactory.create(expirationDate);
    }

    public Map<String, DownloadLinkTemplateDTO> getLinks() {
        return links;
    }

    public boolean isFormatLocked() {
        return isFormatLocked;
    }
}
