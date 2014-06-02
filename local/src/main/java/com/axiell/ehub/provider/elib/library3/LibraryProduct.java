package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

public class LibraryProduct {
    @JsonProperty("ModelAvailabilities")
    private List<ModelAvailability> models;

    public boolean isAvailable() {
        if (hasNoModels())
            return true;
        return hasX();
    }

    private boolean hasNoModels() {
        return models == null || models.isEmpty();
    }

    private boolean hasX() {
        for (ModelAvailability model : models) {
            if (model.isProductAvailable())
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

        public boolean isProductAvailable() {
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
