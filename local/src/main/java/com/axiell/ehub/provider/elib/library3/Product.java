/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.util.HashCodeBuilderFactory;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(value = {"BookLength", "BookType", "Categories", "PublishedDate", "Contributors", "CoverImage", "CreatedDate", "Description", "DistributionRegions", "Language",
        "OrderableFormatGroups", "PublicIdentifiers", "PublishedDate", "Publisher", "Relations", "Teaser", "Title", "Toc", "UpdatedDate"})
public class Product {
    @JsonProperty("ProductId")
    private String productId;
    @JsonProperty("AvailableFormats")
    private List<AvailableFormat> formats;
    private FormatFilter formatFilter = new FormatFilter();

    public String getProductId() {
        return productId;
    }

    @JsonProperty("Statuses")
    private List<Status> statuses;

    public List<AvailableFormat> getFormats() {
        return formatFilter.applyFilter(formats);
    }

    public boolean isActive() {
        for (Status status : statuses) {
            if (status.isActive())
                return true;
        }
        return false;
    }

    @JsonIgnoreProperties(value = {"SizeInBytes", "Name"})
    public static class AvailableFormat {
        @JsonProperty("FormatId")
        private String id;

        public AvailableFormat() {
        }

        public AvailableFormat(String id) {
            this.id = id;
        }

        public String getId() {
            return id;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this)
                return true;
            if (!(obj instanceof AvailableFormat))
                return false;
            AvailableFormat rhs = (AvailableFormat) obj;
            return new EqualsBuilder().append(id, rhs.getId()).isEquals();
        }

        @Override
        public int hashCode() {
            return HashCodeBuilderFactory.create().append(id).toHashCode();
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
