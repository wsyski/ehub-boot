/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

/**
 * Represents an ElibU product.
 */
@JsonIgnoreProperties(value = {"BookType", "Categories", "PublishedDate",  "Contributors", "CoverImage", "CreatedDate", "Description", "DistributionRegions", "Language",
    "OrderableFormatGroups", "ProductID", "PublicIdentifiers", "Publisher", "Relations", "Statuses", "Teaser", "Title", "UpdatedDate"})
public class Product {
    @JsonProperty("AvailableFormats")
    private List<AvailableFormat> formats;

    /**
     * Returns the {@link AvailableFormat}s of this product.
     * 
     * @return the {@link AvailableFormat}s of this product
     */
    public List<AvailableFormat> getFormats() {
        return formats;
    }

    /**
     * Sets the {@link AvailableFormat}s of this product.
     * 
     * @param formats the {@link AvailableFormat}s of this product to set
     */
    public void setFormats(List<AvailableFormat> formats) {
        this.formats = formats;
    }
    
    /**
     * Represents an available format of a {@link Product}.
     */
    @JsonIgnoreProperties(value = {"SizeInBytes", "Name"})
    public static class AvailableFormat {
        @JsonProperty("FormatID")
        private String id;
        
        /**
         * Returns the ID of the format.
         *
         * @return the ID of the format
         */
        public String getId() {
            return id;
        }
        
        /**
         * Sets the ID of the format.
         *
         * @param id the ID of the format to set
         */
        public void setId(String id) {
            this.id = id;
        }
    }
}
