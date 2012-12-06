/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.StringResourceModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.TranslatedKey;
import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;

/**
 * A {@link Panel} that displays all available {@link EhubConsumer}s in the eHUB. It also provides the possibility
 * create a new {@link EhubConsumer}.
 */
public class EhubConsumersPanel extends BreadCrumbPanel {
    private static final long serialVersionUID = 4447179018707775379L;
    private final WebMarkupContainer ehubConsumerFormContainer;
    private final ListView<EhubConsumer> ehubConsumersView;
    private final StatelessForm<EhubConsumer> ehubConsumerForm;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;

    /**
     * Constructs a new {@link EhubConsumersPanel}.
     * 
     * @param panelId the ID of this {@link Panel}
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     */
    public EhubConsumersPanel(final String panelId, final IBreadCrumbModel breadCrumbModel) {
        super(panelId, breadCrumbModel);
        this.ehubConsumersView = new ListView<EhubConsumer>("ehubConsumers") {
            private static final long serialVersionUID = -8141453866471467440L;

            /**
             * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
             */
            @Override
            protected void populateItem(ListItem<EhubConsumer> item) {
                final EhubConsumer ehubConsumer = item.getModelObject();
                final String description = ehubConsumer.getDescription();
                final Label linkLabel = new Label("ehubConsumerLinkLabel", description);
                final IBreadCrumbPanelFactory factory = createEhubConsumerPanelFactory(breadCrumbModel, ehubConsumer);
                final BreadCrumbPanelLink link = new BreadCrumbPanelLink("ehubConsumerLink", breadCrumbModel, factory);
                link.add(linkLabel);
                item.add(link);

                final Link<EhubConsumer> deleteLink = createDeleteLink("deleteLink", ehubConsumer);
                item.add(deleteLink);
            }
        };
        add(ehubConsumersView);

        this.ehubConsumerFormContainer = new WebMarkupContainer("ehubConsumerFormContainer");
        ehubConsumerFormContainer.setOutputMarkupPlaceholderTag(true);
        add(ehubConsumerFormContainer);

        this.ehubConsumerForm = createEhubConsumerForm(breadCrumbModel);
        ehubConsumerFormContainer.add(ehubConsumerForm);

        final IndicatingAjaxFallbackLink<Void> newEhubConsumerLink = new IndicatingAjaxFallbackLink<Void>("newEhubConsumerLink") {
            private static final long serialVersionUID = -2631861270065092510L;

            /**
             * @see org.apache.wicket.ajax.markup.html.AjaxFallbackLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
             */
            @Override
            public void onClick(AjaxRequestTarget target) {
                setVisible(false);
                ehubConsumerFormContainer.setVisible(true);

                if (target != null) {
                    target.addComponent(this);
                    target.addComponent(ehubConsumerFormContainer);
                }
            }
        };
        newEhubConsumerLink.setOutputMarkupPlaceholderTag(true);
        add(newEhubConsumerLink);

        final IndicatingAjaxFallbackLink<Void> cancelNewEhubConsumerLink = new IndicatingAjaxFallbackLink<Void>("cancelNewEhubConsumerLink") {
            private static final long serialVersionUID = 5006162719287884604L;

            /**
             * @see org.apache.wicket.ajax.markup.html.AjaxFallbackLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
             */
            @Override
            public void onClick(AjaxRequestTarget target) {
                ehubConsumerFormContainer.setVisible(false);
                newEhubConsumerLink.setVisible(true);

                if (target != null) {
                    target.addComponent(ehubConsumerFormContainer);
                    target.addComponent(newEhubConsumerLink);
                }
            }
        };
        ehubConsumerFormContainer.add(cancelNewEhubConsumerLink);
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant#getTitle()
     */
    @Override
    public String getTitle() {
        final StringResourceModel model = new StringResourceModel("txtBreadCrumbPanelTitle", this, new Model<>());
        return model.getString();
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel#onActivate(org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant)
     */
    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        final EhubConsumer ehubConsumer = new EhubConsumer();
        final IModel<EhubConsumer> ehubCosumerFormModel = new CompoundPropertyModel<>(ehubConsumer);
        ehubConsumerForm.setModel(ehubCosumerFormModel);
        ehubConsumerFormContainer.setVisible(false);

        final List<EhubConsumer> ehubConsumers = consumerAdminController.getEhubConsumers();
        ehubConsumersView.setList(ehubConsumers);
        super.onActivate(previous);
    }

    /**
     * Creates a new {@link IBreadCrumbPanelFactory} that provides the possibility to create {@link EhubConsumerPanel}s.
     * 
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     * @param ehubConsumer the {@link EhubConsumer} to be forwarded to the constructor of the {@link EhubConsumerPanel}
     * @return an {@link IBreadCrumbPanelFactory}
     */
    private IBreadCrumbPanelFactory createEhubConsumerPanelFactory(final IBreadCrumbModel breadCrumbModel, final EhubConsumer ehubConsumer) {
        return new IBreadCrumbPanelFactory() {
            private static final long serialVersionUID = 7353674966745648720L;

            /**
             * @see org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory#create(java.lang.String,
             * org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel)
             */
            @Override
            public BreadCrumbPanel create(final String id, final IBreadCrumbModel model) {
                return new EhubConsumerPanel(id, model, ehubConsumer);
            }
        };
    }

    /**
     * Creates a {@link Link} that deletes the provided {@link EhubConsumer} on click.
     * 
     * @param ehubConsumer the {@link EhubConsumer} to be deleted on click
     * @return a {@link Link}
     */
    private Link<EhubConsumer> createDeleteLink(final String linkId, final EhubConsumer ehubConsumer) {
        return new Link<EhubConsumer>(linkId) {
            private static final long serialVersionUID = -3735251678540234334L;

            /**
             * @see org.apache.wicket.markup.html.link.Link#onClick()
             */
            @Override
            public void onClick() {
                consumerAdminController.delete(ehubConsumer);
                activate(EhubConsumersPanel.this);
            }
        };
    }

    /**
     * Creates a {@link StatelessForm} that provides the possibility to create a new {@link EhubConsumer}.
     * 
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     * @return a {@link StatelessForm}
     */
    private StatelessForm<EhubConsumer> createEhubConsumerForm(final IBreadCrumbModel breadCrumbModel) {
        final StatelessForm<EhubConsumer> ehubConsumerForm = new StatelessForm<EhubConsumer>("ehubConsumerForm");

        final RequiredTextField<String> descriptionField = new RequiredTextField<>("description");

        final List<EhubConsumerPropertyKey> propertyKeys = Arrays.asList(EhubConsumer.EhubConsumerPropertyKey.values());
        final TranslatedKeys<EhubConsumerPropertyKey> translatedKeys = new TranslatedKeys<>(this, propertyKeys);
        final ListView<TranslatedKey<EhubConsumerPropertyKey>> propertiesView = new ListView<TranslatedKey<EhubConsumerPropertyKey>>("properties",
                translatedKeys) {
            private static final long serialVersionUID = 408792772105665766L;

            /**
             * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
             */
            @Override
            protected void populateItem(ListItem<TranslatedKey<EhubConsumerPropertyKey>> item) {
                final TranslatedKey<EhubConsumerPropertyKey> translatedKey = item.getModelObject();
                final EhubConsumerPropertyKey propertyKey = translatedKey.getKey();

                final IModel<String> propertyModel = new PropertyModel(propertyKey);
                final RequiredTextField<String> propertyField = new RequiredTextField<>("property", propertyModel);
                item.add(propertyField);

                final String title = translatedKey.getTitle();
                final AttributeModifier titleModifier = new AttributeModifier("title", true, new Model<>(title));
                propertyField.add(titleModifier);

                final String label = translatedKey.getLabel();
                final Label propertyFieldLabel = new Label("propertyLabel", label);
                item.add(propertyFieldLabel);
            };
        };

        final Button submitButton = new Button("submit") {
            private static final long serialVersionUID = 573189878150333446L;

            /**
             * @see org.apache.wicket.markup.html.form.Button#onSubmit()
             */
            @Override
            public void onSubmit() {
                final String secretKey = RandomStringUtils.random(10, true, true);
                EhubConsumer ehubConsumer = ehubConsumerForm.getModelObject();
                ehubConsumer.setSecretKey(secretKey);
                ehubConsumer = consumerAdminController.save(ehubConsumer);
                final IBreadCrumbPanelFactory factory = createEhubConsumerPanelFactory(breadCrumbModel, ehubConsumer);
                activate(factory);
            }
        };

        ehubConsumerForm.add(descriptionField);
        ehubConsumerForm.add(propertiesView);
        ehubConsumerForm.add(submitButton);
        return ehubConsumerForm;
    }

    /**
     * The {@link IModel} of a specific property of an {@link EhubConsumer}.
     */
    private class PropertyModel implements IModel<String> {
        private static final long serialVersionUID = 7418161395141615016L;
        private final EhubConsumerPropertyKey propertyKey;

        /**
         * Constructs a new {@link PropertyModel}
         * 
         * @param propertyKey the key of the property value to handle
         */
        private PropertyModel(final EhubConsumerPropertyKey propertyKey) {
            this.propertyKey = propertyKey;
        }

        /**
         * @see org.apache.wicket.model.IModel#getObject()
         */
        @Override
        public String getObject() {
            final EhubConsumer ehubConsumer = ehubConsumerForm.getModelObject();
            Map<EhubConsumerPropertyKey, String> properties = ehubConsumer.getProperties();

            if (properties == null) {
                properties = new HashMap<EhubConsumerPropertyKey, String>();
                ehubConsumer.setProperties(properties);
            }

            return properties.get(propertyKey);
        }

        /**
         * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
         */
        @Override
        public void setObject(String propertyValue) {
            final EhubConsumer ehubConsumer = ehubConsumerForm.getModelObject();
            Map<EhubConsumerPropertyKey, String> properties = ehubConsumer.getProperties();

            if (properties == null) {
                properties = new HashMap<EhubConsumerPropertyKey, String>();
                ehubConsumer.setProperties(properties);
            }

            properties.put(propertyKey, propertyValue);
        }

        /**
         * @see org.apache.wicket.model.IDetachable#detach()
         */
        @Override
        public void detach() {
            final IModel<EhubConsumer> formModel = ehubConsumerForm.getModel();
            formModel.detach();
        }
    }
}
