/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents a consumed product.
 */
@JsonIgnoreProperties(value = {"ProductID", "Bookmarks", "RemovedByLicense", "Supplements"})
public class ConsumedProduct {
    @JsonProperty("LicenseProductFormats")
    private List<Format> formats; 
    
    /**
     * Returns the formats.
     *
     * @return the formats
     */
    public List<Format> getFormats() {
        return formats;
    }
    
    /**
     * Sets the formats.
     *
     * @param formats the formats to set
     */
    public void setFormats(List<Format> formats) {
        this.formats = formats;
    }
    
    /**
     * Represents the format of a {@link ConsumedProduct} for a certain license.
     */
    @JsonIgnoreProperties(value = {"Name"})
    public static class Format {
        @JsonProperty("FormatID")
        private String id;
        
        @JsonProperty("Content")
        private Content content;
        
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
        
        /**
         * Returns the contentLink of a {@link ConsumedProduct}.
         *
         * @return the contentLink of a {@link ConsumedProduct}
         */
        public Content getContent() {
            return content;
        }
        
        /**
         * Sets the contentLink of a {@link ConsumedProduct}.
         *
         * @param content the contentLink of a {@link ConsumedProduct} to set
         */
        public void setContent(Content content) {
            this.content = content;
        }
        
        /**
         * Indicates whether the provided format ID represents the same format as this format. 
         * 
         * @param formatId the ID of a format
         * @return <code>true</code> if and only if the provided format ID is the same as the ID of this format, <code>false</code> otherwise
         */
        public boolean isSameFormat(String formatId) {
            return formatId.equals(id);
        }
    }
    
    /**
     * Represents the contentLink of a {@link ConsumedProduct} for a certain license and format.
     */
    @JsonIgnoreProperties(value = {"Part"})
    public static class Content {
        @JsonProperty("Url")
        private String url;
        
        /**
         * Returns the URL to the contentLink.
         *
         * @return the URL to the contentLink
         */
        public String getUrl() {
            return url;
        }
        
        /**
         * Sets the URL to the contentLink.
         *
         * @param url the URL to the contentLink to set
         */
        public void setUrl(String url) {
            this.url = url;
        }
    }
}
