/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import java.util.HashMap;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.TranslatedKey;
import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.provider.ContentProvider;

/**
 * A {@link Panel} that displays a {@link ContentProviderConsumer}. It also provides the possibility to modify the
 * properties of the {@link ContentProviderConsumer}.
 */
final class ContentProviderConsumerPanel extends BreadCrumbPanel {
    private static final long serialVersionUID = 1048472863645177254L;
    private ContentProviderConsumer contentProviderConsumer;
    private final StatelessForm<ContentProviderConsumer> cpcForm;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;

    /**
     * Constructs a new {@link ContentProviderConsumerPanel}.
     * 
     * @param panelId the ID of this {@link Panel}
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     * @param contentProviderConsumer the {@link ContentProviderConsumer} to be shown
     */
    ContentProviderConsumerPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final ContentProviderConsumer contentProviderConsumer) {
        super(panelId, breadCrumbModel);
        this.contentProviderConsumer = contentProviderConsumer;
        this.cpcForm = createCpcForm();
        add(cpcForm);
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant#getTitle()
     */
    @Override
    public String getTitle() {
        ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        return contentProvider.getName().toString();
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel#onActivate(org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant)
     */
    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        cpcForm.setModelObject(contentProviderConsumer);
        super.onActivate(previous);
    }

    /**
     * Constructs the {@link StatelessForm} that provides the possibility to modify the properties of the
     * {@link ContentProviderConsumer}.
     * 
     * @return a {@link StatelessForm}
     */
    private StatelessForm<ContentProviderConsumer> createCpcForm() {
        final IModel<ContentProviderConsumer> formModel = new Model<>();
        final StatelessForm<ContentProviderConsumer> form = new StatelessForm<>("cpcForm", formModel);
        final TranslatedKeys<ContentProviderConsumerPropertyKey> keys = new TranslatedKeys<>(this, contentProviderConsumer.getProperties());
        final ListView<TranslatedKey<ContentProviderConsumerPropertyKey>> propertiesView = new ListView<TranslatedKey<ContentProviderConsumerPropertyKey>>(
                "properties", keys) {
            private static final long serialVersionUID = 5666157159534216838L;

            /**
             * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
             */
            @Override
            protected void populateItem(ListItem<TranslatedKey<ContentProviderConsumerPropertyKey>> item) {
                final TranslatedKey<ContentProviderConsumerPropertyKey> translatedKey = item.getModelObject();
                final ContentProviderConsumerPropertyKey propertyKey = translatedKey.getKey();

                final IModel<String> propertyModel = new ContentProviderConsumerPropertyModel(propertyKey);
                final RequiredTextField<String> propertyField = new RequiredTextField<>("property", propertyModel);
                item.add(propertyField);

                final String title = translatedKey.getTitle();
                final AttributeModifier titleModifier = new AttributeModifier("title", true, new Model<>(title));
                propertyField.add(titleModifier);

                final String label = translatedKey.getLabel();
                final Label propertyFieldLabel = new Label("propertyLabel", label);
                item.add(propertyFieldLabel);
            }
        };
        form.add(propertiesView);

        final Button submitButton = new Button("submit") {
            private static final long serialVersionUID = -7748063288269298743L;

            /**
             * @see org.apache.wicket.markup.html.form.Button#onSubmit()
             */
            @Override
            public void onSubmit() {
                ContentProviderConsumer consumer = form.getModelObject();
                contentProviderConsumer = consumerAdminController.save(consumer);
                activate(ContentProviderConsumerPanel.this);
            }
        };
        form.add(submitButton);

        return form;
    }

    /**
     * An {@link IModel} that sets and gets the value of a specific {@link ContentProviderConsumerPropertyKey}.
     */
    private class ContentProviderConsumerPropertyModel implements IModel<String> {
        private static final long serialVersionUID = -1253220211059247879L;
        private final ContentProviderConsumerPropertyKey propertyKey;

        /**
         * Constructs a new {@link ContentProviderConsumerPropertyModel}.
         * 
         * @param propertyKey the {@link ContentProviderConsumerPropertyKey}
         */
        private ContentProviderConsumerPropertyModel(ContentProviderConsumerPropertyKey propertyKey) {
            this.propertyKey = propertyKey;
        }

        /**
         * @see org.apache.wicket.model.IModel#getObject()
         */
        @Override
        public String getObject() {
            final ContentProviderConsumer consumer = cpcForm.getModelObject();
            Map<ContentProviderConsumerPropertyKey, String> properties = consumer.getProperties();

            if (properties == null) {
                properties = new HashMap<ContentProviderConsumerPropertyKey, String>();
                consumer.setProperties(properties);
            }

            return properties.get(propertyKey);
        }

        /**
         * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
         */
        @Override
        public void setObject(String propertyValue) {
            final ContentProviderConsumer consumer = cpcForm.getModelObject();
            Map<ContentProviderConsumerPropertyKey, String> properties = consumer.getProperties();

            if (properties == null) {
                properties = new HashMap<ContentProviderConsumerPropertyKey, String>();
                consumer.setProperties(properties);
            }

            properties.put(propertyKey, propertyValue);
        }

        /**
         * @see org.apache.wicket.model.IDetachable#detach()
         */
        @Override
        public void detach() {
            final IModel<ContentProviderConsumer> formModel = cpcForm.getModel();
            formModel.detach();
        }
    }
}
