package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.util.SHA512Function;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_SERVICE_KEY;

class ChecksumBuilder {
    private final StringBuilder builder;
    private final String serviceKey;

    ChecksumBuilder(final String serviceId, final ContentProviderConsumer contentProviderConsumer) {
        builder = new StringBuilder(serviceId);
        serviceKey = contentProviderConsumer.getProperty(ELIB_SERVICE_KEY);
    }

    ChecksumBuilder appendParameter(final String param) {
        builder.append(param);
        return this;
    }

    String build() {
        final String input = builder.append(serviceKey).toString();
        return SHA512Function.sha512Hex(input);
    }
}
