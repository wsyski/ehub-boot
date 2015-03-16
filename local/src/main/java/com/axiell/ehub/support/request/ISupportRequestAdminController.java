package com.axiell.ehub.support.request;

public interface ISupportRequestAdminController {

    DefaultSupportResponse getRecord(RequestArguments arguments);

    DefaultSupportResponse getFormats(RequestArguments arguments);

    DefaultSupportResponse createLoan(RequestArguments arguments);

    DefaultSupportResponse getLoan(RequestArguments arguments);
}
