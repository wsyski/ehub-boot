package com.axiell.ehub;

import org.apache.wicket.model.IModel;

import java.util.Map;

public abstract class AbstractPropertyModel<F, K> implements IModel<String> {
    private final IModel<F> formModel;
    private final K propertyKey;

    public AbstractPropertyModel(final IModel<F> formModel, final K propertyKey) {
        this.formModel = formModel;
        this.propertyKey = propertyKey;
    }

    @Override
    public final String getObject() {
        final F formModelObject = formModel.getObject();
        Map<K, String> properties = getProperties(formModelObject);
        return properties.get(propertyKey);
    }

    @Override
    public final void setObject(String propertyValue) {
        final F formModelObject = formModel.getObject();
        final Map<K, String> properties = getProperties(formModelObject);
        properties.put(propertyKey, propertyValue);
    }

    @Override
    public final void detach() {
        formModel.detach();
    }

    protected abstract Map<K, String> getProperties(F formModelObject);
}
