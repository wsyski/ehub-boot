package com.axiell.ehub;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

public abstract class AbstractPropertiesListView<M, T> extends ListView<TranslatedKey<T>> {
    protected final IModel<M> formModel;

    public AbstractPropertiesListView(String id, IModel<M> formModel) {
        super(id);
        this.formModel = formModel;
    }

    @Override
    protected void populateItem(ListItem<TranslatedKey<T>> item) {
        final TranslatedKey<T> translatedKey = item.getModelObject();

        final RequiredTextField<String> field = makePropertyField(translatedKey);
        item.add(field);

        final Label fieldLabel = makePropertyFieldLabel(translatedKey);
        item.add(fieldLabel);
    }

    private RequiredTextField<String> makePropertyField(final TranslatedKey<T> translatedKey) {
        final AttributeModifier title = makeTitle(translatedKey);
        final T key = translatedKey.getKey();
        final IModel<String> model = makePropertyModel(key);
        final RequiredTextField<String> field = new RequiredTextField<>("property", model);
        field.add(title);
        field.setOutputMarkupPlaceholderTag(true);
        return field;
    }

    private AttributeModifier makeTitle(final TranslatedKey<T> translatedKey) {
        final String title = translatedKey.getTitle();
        return new AttributeModifier("title", true, new Model<>(title));
    }

    private Label makePropertyFieldLabel(final TranslatedKey<T> translatedKey) {
        final String label = translatedKey.getLabel();
        return new Label("propertyLabel", label);
    }

    protected abstract IModel<String> makePropertyModel(final T propertyKey);
}
