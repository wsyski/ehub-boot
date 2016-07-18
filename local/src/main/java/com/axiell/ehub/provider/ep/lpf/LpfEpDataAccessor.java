package com.axiell.ehub.provider.ep.lpf;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ep.AbstractEpDataAccessor;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class LpfEpDataAccessor extends AbstractEpDataAccessor<ILpfEpFacade> {

    @Autowired
    private ILpfEpFacade epFacade;

    @Override
    protected ILpfEpFacade getEpFacade() {
        return epFacade;
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String contentProviderFormatId = data.getContentProviderFormatId();
        final LpfCheckoutDTO lpfCheckoutDTO = epFacade.checkout(contentProviderConsumer, patron, contentProviderRecordId, contentProviderFormatId);
        final ContentProviderLoanMetadata loanMetadata = makeContentProviderLoanMetadata(data, lpfCheckoutDTO);
        final Content contentLinks = makeContent(lpfCheckoutDTO.getFormatMetadata());
        return new ContentProviderLoan(loanMetadata, contentLinks);
    }

    @Override
    public Content getContent(final CommandData data) {
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final String contentProviderLoanId = loanMetadata.getId();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final LpfCheckoutDTO lpfCheckoutDTO = epFacade.getCheckout(contentProviderConsumer, patron, contentProviderLoanId);
        return makeContent(lpfCheckoutDTO.getFormatMetadata());
    }
}
