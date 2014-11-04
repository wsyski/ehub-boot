package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.consumer.ContentProviderConsumer;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OCD_BASIC_TOKEN;

class BasicToken {
    private final String value;

    BasicToken(ContentProviderConsumer contentProviderConsumer) {
        this.value = contentProviderConsumer.getProperty(OCD_BASIC_TOKEN);
    }

    @Override
    public String toString() {
        return "basic " + value;
    }
}
