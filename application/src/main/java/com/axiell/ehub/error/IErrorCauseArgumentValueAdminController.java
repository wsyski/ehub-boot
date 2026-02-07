package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.ErrorCauseArgumentValueTextBundle;
import com.axiell.ehub.language.Language;

import java.util.List;

import com.axiell.ehub.ErrorCauseArgumentType;

public interface IErrorCauseArgumentValueAdminController {

    List<ErrorCauseArgumentType> getTypes();

    ErrorCauseArgumentValue findBy(ErrorCauseArgumentType type);

    ErrorCauseArgumentValueTextBundle save(ErrorCauseArgumentValueTextBundle textBundle);

    ErrorCauseArgumentValue save(ErrorCauseArgumentValue argumentValue);

    void deleteErrorCauseArgumentValueTextBundles(final Language language);
}
