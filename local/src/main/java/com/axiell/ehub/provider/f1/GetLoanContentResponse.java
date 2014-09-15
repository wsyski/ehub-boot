package com.axiell.ehub.provider.f1;

import org.apache.commons.lang3.StringUtils;

class GetLoanContentResponse extends AbstractF1Response {

    GetLoanContentResponse(final String value) {
        super(value);
    }

    boolean isValidContent() {
        return !StringUtils.isBlank(value);
    }
}
