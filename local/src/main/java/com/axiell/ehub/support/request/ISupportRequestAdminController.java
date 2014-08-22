package com.axiell.ehub.support.request;

public interface ISupportRequestAdminController {

    SupportResponse getFormats(RequestArguments arguments);

    SupportResponse createLoan(RequestArguments arguments);

    SupportResponse getLoan(RequestArguments arguments);
}
