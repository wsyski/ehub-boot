package com.axiell.ehub.provider.ocd;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;
import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutSummaryDTO extends AbstractCheckoutDTO {
    private static final String SUCCESS = "SUCCESS";
    private String output;

    public boolean isSuccessful() {
        return SUCCESS.equals(output);
    }
}
