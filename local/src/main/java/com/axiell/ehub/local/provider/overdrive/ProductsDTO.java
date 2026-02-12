package com.axiell.ehub.local.provider.overdrive;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProductsDTO {
    private List<ProductDTO> products;
    private String id;
    private int totalItems;

    public String getId() {
        return id;
    }

    public List<ProductDTO> getProducts() {
        return products;
    }

    public int getTotalItems() {
        return totalItems;
    }
}
