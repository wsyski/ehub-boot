package com.axiell.ehub.lms.palma;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.util.Validate;

import static com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey.ARENA_PALMA_URL;

class PalmaUrlBuilder {
    private String url;

    PalmaUrlBuilder(EhubConsumer ehubConsumer) {
        url = ehubConsumer.getProperties().get(ARENA_PALMA_URL);
    }

    PalmaUrlBuilder appendPath(String path) {
        url += path;
        return this;
    }

    PalmaWsdlUrl build() {
        return new PalmaWsdlUrl(url);
    }
}
