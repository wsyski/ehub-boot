package com.axiell.ehub.local.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CheckoutsDTO {
    private List<CheckoutDTO> checkouts;

    public List<CheckoutDTO> getCheckouts() {
        return checkouts == null ? Lists.newArrayList() : checkouts;
    }
}
