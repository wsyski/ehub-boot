package com.axiell.ehub.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class LibraryProduct {
    @JsonProperty("ProductID")
    private String productId;

    @JsonProperty("ModelAvailabilities")
    private List<ModelAvailability> models;

    public String getProductId() {
        return productId;
    }

    public boolean hasAvailableModel() {
        if (hasNoModels())
            return true;
        return hasAtLeastOneAvailableModel();
    }

    private boolean hasNoModels() {
        return models == null || models.isEmpty();
    }

    private boolean hasAtLeastOneAvailableModel() {
        for (ModelAvailability model : models) {
            if (model.isAvailable())
                return true;
        }
        return false;
    }

    public static class ModelAvailability {
        @JsonProperty("Model")
        private String model;
        @JsonProperty("Current")
        private Integer current;
        @JsonProperty("Max")
        private Integer max;

        public boolean isAvailable() {
            if (isAccessModel())
                return true;
            return hasAvailableLicenses();
        }

        private boolean isAccessModel() {
            return "access".equals(model);
        }

        private boolean hasAvailableLicenses() {
            return current < max;
        }
    }
}
