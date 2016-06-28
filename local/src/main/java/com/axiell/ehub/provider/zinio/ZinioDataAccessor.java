package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.record.format.Formats;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ZinioDataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired
    private IFormatFactory formatFactory;
    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final Formats formats = new Formats();
        return formats;
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        return null;
    }

    @Override
    public Content getContent(final CommandData data) {
        return null;
    }
}
