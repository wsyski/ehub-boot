package com.axiell.ehub.loan;

import com.axiell.ehub.provider.record.format.FormatDecoration;

public interface ILoanAdminController {

    /**
     * Counts the number of loans that have been created in a specific format.
     * 
     * @param formatDecoration the FormatDecoration to count the number of loans by
     * @return the number of loans that have been created in the specific format
     */
    long countLoansByFormatDecoration(FormatDecoration formatDecoration);

    void deleteByContentProviderId(long contentProviderId);
}
