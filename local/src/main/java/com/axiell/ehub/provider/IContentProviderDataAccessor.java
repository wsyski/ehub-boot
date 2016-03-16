package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.provider.record.format.Formats;

/**
 * Defines the data accessor interface for the {@link ContentProvider}s.
 */
public interface IContentProviderDataAccessor {

    Formats getFormats(CommandData data);

    ContentProviderLoan createLoan(CommandData data);

    Content getContent(CommandData data);
}
