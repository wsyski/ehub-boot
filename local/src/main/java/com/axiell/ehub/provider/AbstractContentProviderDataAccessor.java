/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.loan.DownloadableContent;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.StreamingContent;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition;

/**
 * Skeletal implementation of an {@link IContentProviderDataAccessor}. It should be sub-classed to provide the data
 * accessor for the specific Content Provider.
 */
public abstract class AbstractContentProviderDataAccessor implements IContentProviderDataAccessor {

    /**
     * Creates an {@link IContent}. The type of the {@link IContent} depends on the {@link ContentDisposition} retrieved
     * from the provided {@link ContentProviderFormatDecoration}.
     * 
     * @param contentUrl the URL of the content
     * @param formatDecoration the {@link ContentProviderFormatDecoration}
     * @return a concrete instance of {@link IContent}
     */
    protected final IContent createContent(final String contentUrl, final FormatDecoration formatDecoration) {
        final ContentDisposition contentDisposition = formatDecoration.getContentDisposition();

        switch (contentDisposition) {
            case DOWNLOADABLE:
                return new DownloadableContent(contentUrl);
            case STREAMING:
                final int width = formatDecoration.getPlayerWidth();
                final int height = formatDecoration.getPlayerHeight();
                return new StreamingContent(contentUrl, width, height);
            default:
                throw new NotImplementedException("Create content with content disposition '" + contentDisposition + "' has not been implemented");
        }
    }
}
