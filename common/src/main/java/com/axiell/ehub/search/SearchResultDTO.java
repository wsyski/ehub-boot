package com.axiell.ehub.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonIgnoreProperties(ignoreUnknown = true)
public class SearchResultDTO<O> {
    private List<O> items;
    private int offset;
    private int limit;
    private long totalItems;

    public List<O> getItems() {
        return items;
    }

    public SearchResultDTO<O> items(List<O> items) {
        this.items = items;
        return this;
    }

    public int getOffset() {
        return offset;
    }

    public SearchResultDTO<O> offset(int offset) {
        this.offset = offset;
        return this;
    }

    public int getLimit() {
        return limit;
    }

    public SearchResultDTO<O> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public SearchResultDTO<O> totalItems(long totalItems) {
        this.totalItems = totalItems;
        return this;
    }
}
