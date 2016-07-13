package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.record.issue.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class Elib3DataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired
    private IElib3CommandChainFactory commandChainFactory;

    @Override
    public List<Issue> getIssues(final CommandData commandData) {
        final Elib3CommandData data = Elib3CommandData.newInstance(commandData);
        final GetFormatsCommandChain commandChain = commandChainFactory.createGetFormatsCommandChain();
        return Collections.singletonList(new Issue(commandChain.execute(data)));
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final CreateLoanCommandChain commandChain = commandChainFactory.createCreateLoanCommandChain();
        return commandChain.execute(data);
    }

    @Override
    public Content getContent(final CommandData data) {
        final GetContentCommandChain commandChain = commandChainFactory.createGetContentCommandChain();
        return commandChain.execute(data);
    }
}
