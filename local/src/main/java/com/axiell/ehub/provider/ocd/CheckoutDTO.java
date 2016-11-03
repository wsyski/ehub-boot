package com.axiell.ehub.provider.ocd;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO extends AbstractCheckoutDTO {
    @JsonProperty(value = "expiration")
    private Date expirationDate;
    private String downloadUrl;
    private List<FileDTO> files;

    public Date getExpirationDate() {
        return expirationDate;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public List<FileDTO> getFiles() {
        return files;
    }

    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class FileDTO {
        private String downloadUrl;

        public String getDownloadUrl() {
            return downloadUrl;
        }
    }
}
