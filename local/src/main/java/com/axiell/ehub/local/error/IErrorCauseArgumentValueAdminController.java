package com.axiell.ehub.local.error;

import com.axiell.ehub.common.ErrorCauseArgumentType;
import com.axiell.ehub.common.ErrorCauseArgumentValue;
import com.axiell.ehub.common.ErrorCauseArgumentValueTextBundle;
import com.axiell.ehub.common.language.Language;

import java.util.List;

public interface IErrorCauseArgumentValueAdminController {

    List<ErrorCauseArgumentType> getTypes();

    ErrorCauseArgumentValue findBy(ErrorCauseArgumentType type);

    ErrorCauseArgumentValueTextBundle save(ErrorCauseArgumentValueTextBundle textBundle);

    ErrorCauseArgumentValue save(ErrorCauseArgumentValue argumentValue);

    void deleteErrorCauseArgumentValueTextBundles(final Language language);
}
