package com.axiell.ehub.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonProperty;

public class GetProductResponse {
    @JsonProperty("Product")
    private Product product;

    public Product getProduct() {
        return product;
    }
}
