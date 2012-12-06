/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import org.codehaus.jackson.annotate.JsonProperty;

/**
 * Represents the result of an ElibU response.
 */
public class Result {
    @JsonProperty("Status")
    private Status status;

    @JsonProperty("License")
    private License license;

    @JsonProperty("ConsumedProduct")
    private ConsumedProduct consumedProduct;

    @JsonProperty("Product")
    private Product product;

    /**
     * Returns the status.
     * 
     * @return the status
     */
    public Status getStatus() {
        return status;
    }

    /**
     * Sets the status.
     * 
     * @param status the status to set
     */
    public void setStatus(Status status) {
        this.status = status;
    }

    /**
     * Returns the license.
     * 
     * @return the license
     */
    public License getLicense() {
        return license;
    }

    /**
     * Sets the license.
     * 
     * @param license the license to set
     */
    public void setLicense(License license) {
        this.license = license;
    }

    /**
     * Returns the consumedProduct.
     * 
     * @return the consumedProduct
     */
    public ConsumedProduct getConsumedProduct() {
        return consumedProduct;
    }

    /**
     * Sets the consumedProduct.
     * 
     * @param consumedProduct the consumedProduct to set
     */
    public void setConsumedProduct(ConsumedProduct consumedProduct) {
        this.consumedProduct = consumedProduct;
    }
    
    /**
     * Returns the product.
     *
     * @return the product
     */
    public Product getProduct() {
        return product;
    }
    
    /**
     * Sets the product.
     *
     * @param product the product to set
     */
    public void setProduct(Product product) {
        this.product = product;
    }
}
