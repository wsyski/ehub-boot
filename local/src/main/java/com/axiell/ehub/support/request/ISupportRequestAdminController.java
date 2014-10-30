package com.axiell.ehub.support.request;

public interface ISupportRequestAdminController {

    DefaultSupportResponse getFormats(RequestArguments arguments);

    DefaultSupportResponse createLoan(RequestArguments arguments);

    DefaultSupportResponse getLoan(RequestArguments arguments);
}
