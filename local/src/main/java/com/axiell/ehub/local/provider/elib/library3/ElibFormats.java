package com.axiell.ehub.local.provider.elib.library3;

enum ElibFormats {
    FLASH("4002"), PDF("4103"), EPUB_ONLINE("4101"), EPUB_OFFLINE("4104");

    private String id;

    private ElibFormats(String id) {
        this.id = id;
    }

    String getId() {
        return id;
    }

    Product.AvailableFormat toAvailableFormat() {
        return new Product.AvailableFormat(id);
    }
}
