package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.util.JacksonTimestampDeserializer;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonDeserialize;

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

