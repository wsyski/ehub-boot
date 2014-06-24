package com.axiell.ehub.provider.elib.library3;

import org.codehaus.jackson.annotate.JsonProperty;

public class LibraryProductResponse {
    @JsonProperty("Product")
    private LibraryProduct libraryProduct;

    public LibraryProduct getLibraryProduct() {
        return libraryProduct;
    }
}
