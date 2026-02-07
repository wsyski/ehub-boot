package com.axiell.ehub.error;

import com.axiell.ehub.ErrorCauseArgumentValue;
import com.axiell.ehub.ErrorCauseArgumentValueTextBundle;
import com.axiell.ehub.language.Language;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashMap;
import java.util.Map;

class ErrorCauseArgumentValueTextModel implements IModel<String> {
    private final IModel<ErrorCauseArgumentValue> formModel;
    private final Language language;

    @SpringBean(name = "errorCauseArgumentValueAdminController")
    private IErrorCauseArgumentValueAdminController argumentValueAdminController;

    ErrorCauseArgumentValueTextModel(final IModel<ErrorCauseArgumentValue> formModel, final Language language) {
        Injector.get().inject(this);
        this.formModel = formModel;
        this.language = language;
    }

    @Override
    public String getObject() {
        final ErrorCauseArgumentValue argumentValue = formModel.getObject();
        final Map<Language, ErrorCauseArgumentValueTextBundle> textBundles = argumentValue.getTextBundles();
        final ErrorCauseArgumentValueTextBundle textBundle = textBundles == null ? null : textBundles.get(language);
        return textBundle == null ? null : textBundle.getText();
    }

    @Override
    public void setObject(final String text) {
        final ErrorCauseArgumentValue argumentValue = formModel.getObject();
        final Map<Language, ErrorCauseArgumentValueTextBundle> textBundles = getTextBundles(argumentValue);
        final ErrorCauseArgumentValueTextBundle textBundle = getTextBundle(argumentValue, textBundles);
        textBundle.setText(text);
    }

    private ErrorCauseArgumentValueTextBundle getTextBundle(final ErrorCauseArgumentValue argumentValue, final Map<Language, ErrorCauseArgumentValueTextBundle> textBundles) {
        ErrorCauseArgumentValueTextBundle textBundle = textBundles.get(language);
        if (textBundle == null) {
            // Temporarily add a default text. This will be overridden shortly.
            textBundle = new ErrorCauseArgumentValueTextBundle(argumentValue, language, "tmp");
            textBundle = argumentValueAdminController.save(textBundle);
            textBundles.put(language, textBundle);
        }
        return textBundle;
    }

    private Map<Language, ErrorCauseArgumentValueTextBundle> getTextBundles(final ErrorCauseArgumentValue argumentValue) {
        Map<Language, ErrorCauseArgumentValueTextBundle> textBundles = argumentValue.getTextBundles();
        if (textBundles == null) {
            textBundles = new HashMap<>();
            argumentValue.setTextBundles(textBundles);
        }
        return textBundles;
    }

    @Override
    public void detach() {
        formModel.detach();
    }
}
