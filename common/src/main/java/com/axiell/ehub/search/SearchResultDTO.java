package com.axiell.ehub.search;

import java.util.ArrayList;
import java.util.List;

public class SearchResultDTO<O> {
    private List<O> items = new ArrayList<>();
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;

    public List<O> getItems() {
        return null;
    }

    public int getPageNumber() {
        return 0;
    }

    public int getPageSize() {
        return 0;
    }

    public long getTotalElements() {
        return 0;
    }

    public int getTotalPages() {
        return 0;
    }
}
