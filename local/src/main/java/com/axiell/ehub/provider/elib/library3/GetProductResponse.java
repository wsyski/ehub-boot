package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonProperty;

public class GetProductResponse {
    @JsonProperty("Product")
    private Product product;

    public Product getProduct() {
        return product;
    }
}
