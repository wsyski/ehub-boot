package com.axiell.ehub.provider;

import com.axiell.ehub.Fields;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.patron.Patron;

public class CommandDataMatcherHelper {
    private final CommandData data;

    public CommandDataMatcherHelper(final CommandData data) {
        this.data = data;
    }

    public boolean isExpectedContentProviderConsumer(ContentProviderConsumer expectedConsumer) {
        return expectedConsumer.equals(data.getContentProviderConsumer());
    }

    public boolean isExpectedPatron(Patron expectedPatron) {
        return expectedPatron.equals(data.getPatron());
    }

    public boolean isExpectedLanguage(String expectedLanguage) {
        return expectedLanguage.equals(data.getLanguage());
    }

    public boolean isExpectedContentProviderLoanMetadata(ContentProviderLoanMetadata expectedLoanMetadata) {
        return expectedLoanMetadata.equals(data.getContentProviderLoanMetadata());
    }

    public boolean isExpectedContentProviderRecordId(String expectedContentProviderRecordId) {
        return expectedContentProviderRecordId.equals(data.getContentProviderRecordId());
    }

    public boolean isExpectedPendingLoan(PendingLoan expectedPendingLoan) {
        PendingLoan actualPendingLoan = new PendingLoan(new Fields().addValue("contentProviderAlias", expectedPendingLoan.contentProviderAlias()).addValue("lmsRecordId", expectedPendingLoan.lmsRecordId()).addValue("contentProviderRecordId", data.getContentProviderRecordId())
                .addValue("contentProviderIssueId", data.getContentProviderIssueId()).addValue("contentProviderFormatId", data.getContentProviderFormatId()));
        return expectedPendingLoan.equals(actualPendingLoan);
    }
}
