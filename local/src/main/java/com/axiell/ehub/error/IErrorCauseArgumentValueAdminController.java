package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.ErrorCauseArgumentValueTextBundle;
import com.axiell.ehub.language.Language;

import java.util.List;

import static com.axiell.ehub.ErrorCauseArgumentValue.Type;

public interface IErrorCauseArgumentValueAdminController {

    List<Type> getTypes();

    ErrorCauseArgumentValue findBy(Type type);

    ErrorCauseArgumentValueTextBundle save(ErrorCauseArgumentValueTextBundle textBundle);

    ErrorCauseArgumentValue save(ErrorCauseArgumentValue argumentValue);

    void deleteErrorCauseArgumentValueTextBundles(final Language language);
}
