package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.CommandData;

import java.util.List;

public class Elib3CommandData extends CommandData {
    private List<Product.AvailableFormat> availableFormats;

    private Elib3CommandData(ContentProviderConsumer contentProviderConsumer, String libraryCard) {
        super(contentProviderConsumer, libraryCard);
    }

    public static Elib3CommandData newInstance(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String contentProviderRecordId, final String language) {
        final Elib3CommandData data = new Elib3CommandData(contentProviderConsumer, libraryCard);
        data.setContentProviderRecordId(contentProviderRecordId).setLanguage(language);
        return data;
    }

    public List<Product.AvailableFormat> getAvailableFormats() {
        return availableFormats;
    }

    public Elib3CommandData setAvailableFormats(List<Product.AvailableFormat> availableFormats) {
        this.availableFormats = availableFormats;
        return this;
    }
}
