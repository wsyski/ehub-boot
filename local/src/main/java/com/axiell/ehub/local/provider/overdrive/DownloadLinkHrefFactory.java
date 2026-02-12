package com.axiell.ehub.local.provider.overdrive;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;

import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_ERROR_PAGE_URL;
import static com.axiell.ehub.common.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.OVERDRIVE_READ_AUTH_URL;

final class DownloadLinkHrefFactory {
    private static final String ERROR_PAGE_URL = "{errorpageurl}";
    private static final String OD_READ_AUTH_URL = "{odreadauthurl}";

    private DownloadLinkHrefFactory() {
    }

    static String create(final ContentProviderConsumer contentProviderConsumer, final DownloadLinkTemplateDTO downloadLinkTemplate) {
        final String errorPageUrl = contentProviderConsumer.getProperty(OVERDRIVE_ERROR_PAGE_URL);
        final String odReadAuthUrl = contentProviderConsumer.getProperty(OVERDRIVE_READ_AUTH_URL);
        final String hrefTemplate = downloadLinkTemplate.getHref();
        return hrefTemplate.replace(ERROR_PAGE_URL, errorPageUrl).replace(OD_READ_AUTH_URL, odReadAuthUrl);
    }
}
