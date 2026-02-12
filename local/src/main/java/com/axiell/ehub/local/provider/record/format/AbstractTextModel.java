package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.language.Language;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import com.axiell.ehub.common.provider.record.format.FormatTextBundle;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.HashMap;
import java.util.Map;

abstract class AbstractTextModel implements IModel<String> {
    private final IModel<FormatDecoration> formModel;
    private final Language language;

    @SpringBean(name = "formatAdminController")
    private IFormatAdminController formatAdminController;

    protected AbstractTextModel(final IModel<FormatDecoration> formModel, final Language language) {
        Injector.get().inject(this);
        this.formModel = formModel;
        this.language = language;
    }

    abstract String getText(FormatTextBundle textBundle);

    abstract void setText(FormatTextBundle textBundle, String text);

    @Override
    public final String getObject() {
        final FormatDecoration formatDecoration = formModel.getObject();
        final Map<Language, FormatTextBundle> textBundles = formatDecoration.getTextBundles();
        final FormatTextBundle textBundle = textBundles == null ? null : textBundles.get(language);
        return textBundle == null ? null : getText(textBundle);
    }

    @Override
    public final void setObject(String value) {
        final FormatDecoration formatDecoration = formModel.getObject();
        Map<Language, FormatTextBundle> textBundles = formatDecoration.getTextBundles();

        if (textBundles == null) {
            textBundles = new HashMap<>();
            formatDecoration.setTextBundles(textBundles);
        }

        FormatTextBundle textBundle = textBundles.get(language);

        if (textBundle == null) {
            // Temporarily add a default name and description. These will be overridden shortly.
            textBundle = new FormatTextBundle(formatDecoration, language, "name", "desc");
            textBundle = formatAdminController.save(textBundle);
            textBundles.put(language, textBundle);
        }

        setText(textBundle, value);
    }

    @Override
    public void detach() {
        formModel.detach();
    }
}
