package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class BookAvailability {
    @JsonProperty("Products")
    private List<Product> products;

    @JsonProperty("Reason")
    private String reason;

    public List<Product> getProducts() {
        return products;
    }

    boolean isProductAvailable(final String productId) {
        if (products == null)
            return false;
        for (Product product : products) {
            if (productId.equals(product.getProductId()))
                return product.isAvailable();
        }
        return false;
    }

    public boolean isMaxNumberOfDownloadsForProductReached() {
        return "product".equals(reason);
    }

    public boolean isLibraryLimitReached() {
        return "library".equals(reason);
    }

    public boolean isBorrowerLimitReached() {
        return "borrower".equals(reason);
    }

    public static class Product {
        @JsonProperty("ProductID")
        private String productId;

        @JsonProperty("Available")
        private Boolean available;

        public String getProductId() {
            return productId;
        }

        public Boolean isAvailable() {
            return available;
        }
    }
}
