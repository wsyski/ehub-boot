package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.util.JacksonTimestampDeserializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.Date;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutDTO {
    private String loanId;
    private String contentUrl;
    @JsonDeserialize(using = JacksonTimestampDeserializer.class)
    @JsonProperty(value = "endDate")
    private Date expirationDate;

    public String getLoanId() {
        return loanId;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }
}

