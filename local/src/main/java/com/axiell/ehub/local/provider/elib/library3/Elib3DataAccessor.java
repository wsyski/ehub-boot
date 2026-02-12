package com.axiell.ehub.local.provider.elib.library3;

import com.axiell.ehub.common.checkout.Content;
import com.axiell.ehub.common.provider.record.issue.Issue;
import com.axiell.ehub.local.loan.ContentProviderLoan;
import com.axiell.ehub.local.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.local.provider.CommandData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component(value = "elib3DataAccessor")
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
