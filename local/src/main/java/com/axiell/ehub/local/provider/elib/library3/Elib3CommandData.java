package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.local.provider.CommandData;

import java.util.List;

public class Elib3CommandData extends CommandData {
    private List<Product.AvailableFormat> availableFormats;

    private Elib3CommandData(ContentProviderConsumer contentProviderConsumer, Patron patron, String language) {
        super(contentProviderConsumer, patron, language);
    }

    public static Elib3CommandData newInstance(final CommandData commandData) {
        final ContentProviderConsumer contentProviderConsumer = commandData.getContentProviderConsumer();
        final Patron patron = commandData.getPatron();
        final String language = commandData.getLanguage();
        final String contentProviderRecordId = commandData.getContentProviderRecordId();
        final Elib3CommandData data = new Elib3CommandData(contentProviderConsumer, patron, language);
        data.setContentProviderRecordId(contentProviderRecordId);
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
