package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.loan.*;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.record.format.Formats;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Elib3DataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired(required = true)
    private IElib3CommandChainFactory commandChainFactory;

    @Override
    public Formats getFormats(final CommandData commandData) {
        final Elib3CommandData data = Elib3CommandData.newInstance(commandData);
        final GetFormatsCommandChain commandChain = commandChainFactory.createGetFormatsCommandChain();
        return commandChain.execute(data);
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final CreateLoanCommandChain commandChain = commandChainFactory.createCreateLoanCommandChain();
        return commandChain.execute(data);
    }

    @Override
    public ContentLink getContent(final CommandData data) {
        final GetContentCommandChain commandChain = commandChainFactory.createGetContentCommandChain();
        return commandChain.execute(data);
    }
}
