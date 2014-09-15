package com.axiell.ehub.provider.f1;

import com.axiell.ehub.provider.CommandData;

public interface IF1Facade {

    GetFormatResponse getFormats(CommandData data);

    CreateLoanResponse createLoan(CommandData data);

    GetLoanContentResponse getLoanContent(CommandData data);
}
