/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.*;
import com.axiell.ehub.loan.DownloadableContent;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.StreamingContent;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition;
import org.springframework.beans.factory.annotation.Autowired;

import static com.axiell.ehub.ErrorCause.CONTENT_PROVIDER_ERROR;
import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME;
import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_STATUS;
import static com.axiell.ehub.ErrorCauseArgumentValue.Type.*;

/**
 * Skeletal implementation of an {@link IContentProviderDataAccessor}. It should be sub-classed to provide the data
 * accessor for the specific Content Provider.
 */
public abstract class AbstractContentProviderDataAccessor implements IContentProviderDataAccessor {
    @Autowired(required = true)
    private IContentFactory contentFactory;

    protected final IContent createContent(final String contentUrl, final FormatDecoration formatDecoration) {
        return contentFactory.create(contentUrl, formatDecoration);
    }
}
