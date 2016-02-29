/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.provider.record.format.Formats;

/**
 * Defines the data accessor interface for the {@link ContentProvider}s.
 */
public interface IContentProviderDataAccessor {

    Formats getFormats(CommandData data);

    ContentProviderLoan createLoan(CommandData data);

    ContentLinks getContent(CommandData data);
}
