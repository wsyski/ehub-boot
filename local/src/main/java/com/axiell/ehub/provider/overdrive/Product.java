package com.axiell.ehub.provider.overdrive;

import java.util.List;

public class Product {
    private ProductDTO productDTO;
    private AvailabilityDTO availabilityDTO;

    public Product(final ProductDTO productDTO, final AvailabilityDTO availabilityDTO) {
        this.productDTO = productDTO;
        this.availabilityDTO = availabilityDTO;
    }

    public String getId() {
        return productDTO.getId();
    }

    public List<DiscoveryFormat> getFormats() {
        return productDTO.getFormats();
    }

    public boolean isAvailable() {
        return availabilityDTO.isAvailable();
    }
}
