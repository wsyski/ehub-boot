package com.axiell.ehub.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class BookAvailability {
    @JsonProperty("Products")
    private List<Product> products;

    public List<Product> getProducts() {
        return products;
    }

    boolean isProductAvailable(final String productId) {
        return new ProductMatcher(products) {
            @Override
            boolean condition(Product product) {
                return product.isAvailable();
            }
        }.matches(productId);
    }

    public boolean isMaxNumberOfDownloadsForProductReached(final String productId) {
        return new ProductMatcher(products) {
            @Override
            boolean condition(Product product) {
                return "product".equals(product.getReason());
            }
        }.matches(productId);
    }

    public boolean isLibraryLimitReached(final String productId) {
        return new ProductMatcher(products) {
            @Override
            boolean condition(Product product) {
                return "library".equals(product.getReason());
            }
        }.matches(productId);
    }

    public boolean isBorrowerLimitReached(final String productId) {
        return new ProductMatcher(products) {
            @Override
            boolean condition(Product product) {
                return "borrower".equals(product.getReason());
            }
        }.matches(productId);
    }

    public static class Product {
        @JsonProperty("ProductID")
        private String productId;

        @JsonProperty("Available")
        private Boolean available;

        @JsonProperty("Reason")
        private String reason;

        public String getProductId() {
            return productId;
        }

        public Boolean isAvailable() {
            return available;
        }

        public String getReason() {
            return reason;
        }
    }
}
