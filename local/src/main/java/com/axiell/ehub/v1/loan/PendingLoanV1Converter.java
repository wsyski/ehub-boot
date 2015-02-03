package com.axiell.ehub.v1.loan;

import com.axiell.ehub.loan.PendingLoan;

class PendingLoanV1Converter {

    private PendingLoanV1Converter() {
    }

    public static PendingLoan convert(PendingLoan_v1 pendingLoan_v1) {
        String lmsRecordId = pendingLoan_v1.getLmsRecordId();
        String contentProviderName = pendingLoan_v1.getContentProviderName();
        String contentProviderRecordId = pendingLoan_v1.getContentProviderRecordId();
        String contentProviderFormatId = pendingLoan_v1.getContentProviderFormatId();
        return new PendingLoan(lmsRecordId, contentProviderName, contentProviderRecordId, contentProviderFormatId);
    }
}
