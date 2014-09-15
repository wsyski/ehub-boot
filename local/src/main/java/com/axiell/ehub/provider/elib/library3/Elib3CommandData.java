package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.provider.CommandData;

import java.util.List;

public class Elib3CommandData extends CommandData {
    private List<Product.AvailableFormat> availableFormats;

    private Elib3CommandData(ContentProviderConsumer contentProviderConsumer, String libraryCard) {
        super(contentProviderConsumer, libraryCard);
    }

    public static Elib3CommandData newInstance(final CommandData commandData) {
        final ContentProviderConsumer contentProviderConsumer = commandData.getContentProviderConsumer();
        final String libraryCard = commandData.getLibraryCard();
        final String language = commandData.getLanguage();
        final String contentProviderRecordId = commandData.getContentProviderRecordId();
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
