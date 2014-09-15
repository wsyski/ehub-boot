package com.axiell.ehub.provider.f1;

import org.apache.commons.lang3.StringUtils;

class CreateLoanResponse extends AbstractF1Response {

    CreateLoanResponse(final String value) {
        super(value);
    }

    boolean isValidLoan() {
        return StringUtils.isNumeric(value);
    }
}
