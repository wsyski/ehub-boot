package com.axiell.ehub.local.provider.ep.lpf;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import com.axiell.ehub.local.loan.ContentProviderLoan;
import com.axiell.ehub.local.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.local.provider.CommandData;
import com.axiell.ehub.local.provider.ep.AbstractEpDataAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component(value = "lpfEpDataAccessor")
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
