package com.axiell.ehub.v1.loan;

import com.axiell.ehub.Fields;
import com.axiell.ehub.loan.PendingLoan;

class PendingLoanConverter_v1 {

    private PendingLoanConverter_v1() {
    }

    public static Fields convert(PendingLoan_v1 pendingLoan_v1) {
        String lmsRecordId = pendingLoan_v1.getLmsRecordId();
        String contentProviderAlias = pendingLoan_v1.getContentProviderName();
        String contentProviderRecordId = pendingLoan_v1.getContentProviderRecordId();
        String contentProviderFormatId = pendingLoan_v1.getContentProviderFormatId();
        return new Fields().addValue("lmsRecordId", lmsRecordId).addValue("contentProviderAlias", contentProviderAlias).addValue("contentProviderRecordId", contentProviderRecordId).addValue("contentProviderFormatId", contentProviderFormatId);
    }
}
