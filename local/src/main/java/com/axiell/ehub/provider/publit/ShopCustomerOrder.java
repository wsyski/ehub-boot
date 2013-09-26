package com.axiell.ehub.provider.publit;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShopCustomerOrder {
    private Integer id;

    public Integer getId() {
        return id;
    }
}
