package com.axiell.ehub.local.provider.elib.library3;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LibraryProductResponse {
    @JsonProperty("Product")
    private LibraryProduct libraryProduct;

    public LibraryProduct getLibraryProduct() {
        return libraryProduct;
    }
}
