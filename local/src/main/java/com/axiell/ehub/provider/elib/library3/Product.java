/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(value = {"BookType", "Categories", "PublishedDate", "Contributors", "CoverImage", "CreatedDate", "Description", "DistributionRegions", "Language",
        "OrderableFormatGroups", "ProductId", "PublicIdentifiers", "PublishedDate", "Publisher", "Relations", "Teaser", "Title", "UpdatedDate"})
public class Product {
    @JsonProperty("AvailableFormats")
    private List<AvailableFormat> formats;

    @JsonProperty("Statuses")
    private List<Status> statuses;

    public List<AvailableFormat> getFormats() {
        return formats;
    }

    public boolean isActive() {
        for (Status status : statuses) {
            if (status.isActive())
                return true;
        }
        return  false;
    }

    @JsonIgnoreProperties(value = {"SizeInBytes", "Name"})
    public static class AvailableFormat {
        @JsonProperty("FormatId")
        private String id;

        public String getId() {
            return id;
        }
    }

    @JsonIgnoreProperties(value = {"ValidFrom", "ValidUntil"})
    public static class Status {
        @JsonProperty("Name")
        private String name;

        public boolean isActive() {
            return "Active".equals(name);
        }
    }
}
