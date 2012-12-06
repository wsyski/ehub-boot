/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.consumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.TranslatedKey;
import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import com.axiell.ehub.consumer.EhubConsumer.EhubConsumerPropertyKey;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.IContentProviderAdminController;

/**
 * A {@link Panel} that displays a {@link EhubConsumer}. It also provides the possibility to modify the properties of
 * the {@link EhubConsumer} and to add new {@link ContentProviderConsumer}s to the {@link EhubConsumer}.
 */
final class EhubConsumerPanel extends BreadCrumbPanel {
    private static final long serialVersionUID = 1048472863645177254L;
    private EhubConsumer ehubConsumer;
    private List<ContentProvider> remainingProviders;
    private final IndicatingAjaxFallbackLink<Void> newCpcLink;
    private final StatelessForm<EhubConsumer> ecForm;
    private ListView<TranslatedKey<EhubConsumerPropertyKey>> ecPropertiesView;

    private final ListView<ContentProviderConsumer> cpcView;
    private final WebMarkupContainer cpcFormContainer;
    private final StatelessForm<ContentProviderConsumer> cpcForm;
    private ListView<TranslatedKey<ContentProviderConsumerPropertyKey>> cpcPropertiesView;
    private WebMarkupContainer cpcPropertiesContainer;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    /**
     * Constructs a new {@link EhubConsumerPanel}.
     * 
     * @param panelId the ID of this {@link Panel}
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     * @param ehubConsumer the {@link EhubConsumer} to be shown
     */
    EhubConsumerPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final EhubConsumer ehubConsumer) {
        super(panelId, breadCrumbModel);
        this.ehubConsumer = ehubConsumer;
        this.ecForm = createEhubConsumerForm();
        add(ecForm);

        this.cpcView = new ListView<ContentProviderConsumer>("contentProviderConsumers") {
            private static final long serialVersionUID = -3631959029511603461L;

            /**
             * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
             */
            @Override
            protected void populateItem(ListItem<ContentProviderConsumer> item) {
                final ContentProviderConsumer contentProviderConsumer = item.getModelObject();
                final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
                final String contentProviderName = contentProvider.getName().toString();
                final IBreadCrumbPanelFactory factory = createContentProviderConsumerPanelFactory(breadCrumbModel, contentProviderConsumer);
                final Label linkLabel = new Label("contentProviderConsumerLinkLabel", contentProviderName);
                final BreadCrumbPanelLink link = new BreadCrumbPanelLink("contentProviderConsumerLink", breadCrumbModel, factory);
                link.add(linkLabel);
                item.add(link);

                final Link<ContentProviderConsumer> deleteLink = createDeleteLink("deleteLink", contentProviderConsumer);
                item.add(deleteLink);
            }
        };
        add(cpcView);

        this.cpcFormContainer = new WebMarkupContainer("contentProviderConsumerFormContainer");
        cpcFormContainer.setOutputMarkupPlaceholderTag(true);
        add(cpcFormContainer);

        this.cpcForm = createContentProviderConsumerForm(breadCrumbModel);
        cpcFormContainer.add(cpcForm);

        this.newCpcLink = new IndicatingAjaxFallbackLink<Void>("newCpcLink") {
            private static final long serialVersionUID = -3811238275876928739L;

            /**
             * @see org.apache.wicket.ajax.markup.html.AjaxFallbackLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
             */
            @Override
            public void onClick(AjaxRequestTarget target) {
                setVisible(false);
                cpcFormContainer.setVisible(true);

                if (target != null) {
                    target.addComponent(this);
                    target.addComponent(cpcFormContainer);
                }
            }
        };
        newCpcLink.setOutputMarkupPlaceholderTag(true);
        add(newCpcLink);

        final IndicatingAjaxFallbackLink<Void> cancelNewCpcLink = new IndicatingAjaxFallbackLink<Void>("cancelNewCpcLink") {
            private static final long serialVersionUID = -5017508691698711168L;

            /**
             * @see org.apache.wicket.ajax.markup.html.AjaxFallbackLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
             */
            @Override
            public void onClick(AjaxRequestTarget target) {
                cpcFormContainer.setVisible(false);
                newCpcLink.setVisible(true);

                if (target != null) {
                    target.addComponent(cpcFormContainer);
                    target.addComponent(newCpcLink);
                }
            }
        };
        cpcFormContainer.add(cancelNewCpcLink);
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant#getTitle()
     */
    @Override
    public String getTitle() {
        return ehubConsumer.getDescription();
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel#onActivate(org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant)
     */
    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        ehubConsumer = consumerAdminController.getEhubConsumer(ehubConsumer.getId());
        List<ContentProvider> allContentProviders = contentProviderAdminController.getContentProviders();
        remainingProviders = ehubConsumer.getRemainingContentProviders(allContentProviders);
        newCpcLink.setVisible(!remainingProviders.isEmpty());

        ecForm.setModelObject(ehubConsumer);
        TranslatedKeys<EhubConsumerPropertyKey> ecPropKeys = new TranslatedKeys<>(this, ehubConsumer.getProperties());
        ecPropertiesView.setList(ecPropKeys);

        final ContentProviderConsumer contentProviderConsumer = new ContentProviderConsumer();
        cpcForm.setModelObject(contentProviderConsumer);
        cpcFormContainer.setVisible(false);
        final List<TranslatedKey<ContentProviderConsumerPropertyKey>> cpcPropKeys = Collections.emptyList();
        cpcPropertiesView.setList(cpcPropKeys);

        final List<ContentProviderConsumer> contentProviderConsumers = ehubConsumer.getContentProviderConsumersAsList();
        cpcView.setList(contentProviderConsumers);
        super.onActivate(previous);
    }

    /**
     * Creates a {@link StatelessForm} that provides the possibility to modify the previously provided
     * {@link EhubConsumer}, except for its {@link ContentProviderConsumer}s which is handled in a separate
     * {@link StatelessForm}.
     * 
     * @return a {@link StatelessForm}
     */
    private StatelessForm<EhubConsumer> createEhubConsumerForm() {
        final IModel<EhubConsumer> ecModel = new Model<>();
        final StatelessForm<EhubConsumer> form = new StatelessForm<>("ehubConsumerForm", ecModel);

        final EhubConsumerDescriptionModel descriptionModel = new EhubConsumerDescriptionModel();
        final RequiredTextField<String> descriptionField = new RequiredTextField<>("description", descriptionModel);
        form.add(descriptionField);

        final TextField<String> idField = createDisabledEhubConsumerTextField("id", "id");
        form.add(idField);

        final TextField<String> secretKeyField = createDisabledEhubConsumerTextField("secretKey", "secretKey");
        form.add(secretKeyField);

        this.ecPropertiesView = new ListView<TranslatedKey<EhubConsumerPropertyKey>>("ecProperties") {
            private static final long serialVersionUID = 5666157159534216838L;

            /**
             * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
             */
            @Override
            protected void populateItem(ListItem<TranslatedKey<EhubConsumerPropertyKey>> item) {
                final TranslatedKey<EhubConsumerPropertyKey> translatedKey = item.getModelObject();
                final EhubConsumerPropertyKey propertyKey = translatedKey.getKey();

                final IModel<String> propertyModel = new EhubConsumerPropertyModel(propertyKey);
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
        form.add(ecPropertiesView);

        final Button submitButton = new Button("submit") {
            private static final long serialVersionUID = -3775651638786232172L;

            /**
             * @see org.apache.wicket.markup.html.form.Button#onSubmit()
             */
            @Override
            public void onSubmit() {
                EhubConsumer ehubConsumer = form.getModelObject();
                consumerAdminController.save(ehubConsumer);
                activate(EhubConsumerPanel.this);
            }
        };
        form.add(submitButton);

        return form;
    }

    /**
     * Creates a {@link RequiredTextField} which is disabled.
     * 
     * @param id the ID of the field
     * @param modelExpression the expression to be used in the {@link PropertyModel} of the {@link RequiredTextField} to
     * be created
     * @return a {@link RequiredTextField}
     */
    private TextField<String> createDisabledEhubConsumerTextField(final String id, final String modelExpression) {
        final PropertyModel<String> model = new PropertyModel<>(ehubConsumer, modelExpression);
        final RequiredTextField<String> field = new RequiredTextField<>(id, model);
        field.setEnabled(false);
        return field;
    }

    /**
     * Creates a {@link IBreadCrumbPanelFactory} that provides the possibility to create
     * {@link ContentProviderConsumerPanel}s.
     * 
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     * @param contentProviderConsumer the {@link ContentProviderConsumer} to be forwarded to the constructor of the
     * {@link ContentProviderConsumerPanel}
     * @return a {@link IBreadCrumbPanelFactory}
     */
    private IBreadCrumbPanelFactory createContentProviderConsumerPanelFactory(final IBreadCrumbModel breadCrumbModel,
            final ContentProviderConsumer contentProviderConsumer) {
        return new IBreadCrumbPanelFactory() {
            private static final long serialVersionUID = 7353674966745648720L;

            /**
             * @see org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory#create(java.lang.String,
             * org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel)
             */
            @Override
            public BreadCrumbPanel create(final String id, final IBreadCrumbModel model) {
                return new ContentProviderConsumerPanel(id, model, contentProviderConsumer);
            }
        };
    }

    /**
     * Creates a {@link Link} that deletes provide {@link ContentProviderConsumer} on click.
     * 
     * @param contentProviderConsumer the {@link ContentProviderConsumer} to be deleted on click
     * @return a {@link Link}
     */
    private Link<ContentProviderConsumer> createDeleteLink(final String linkId, final ContentProviderConsumer contentProviderConsumer) {
        return new Link<ContentProviderConsumer>(linkId) {
            private static final long serialVersionUID = 5012256164456200762L;

            /**
             * @see org.apache.wicket.markup.html.link.Link#onClick()
             */
            @Override
            public void onClick() {
                consumerAdminController.delete(contentProviderConsumer);
                activate(EhubConsumerPanel.this);
            }
        };
    }

    /**
     * Creates a {@link StatelessForm} that provides the possibility to create a new {@link ContentProviderConsumer}
     * which will be added to the previously provided {@link EhubConsumer}.
     * 
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     * @return a {@link StatelessForm}
     */
    private StatelessForm<ContentProviderConsumer> createContentProviderConsumerForm(final IBreadCrumbModel breadCrumbModel) {
        final IModel<ContentProviderConsumer> cpcModel = new Model<>();
        final StatelessForm<ContentProviderConsumer> form = new StatelessForm<ContentProviderConsumer>("contentProviderConsumerForm", cpcModel);

        final IModel<ContentProvider> contentProviderModel = new ContentProviderModel();
        final IModel<List<ContentProvider>> contentProvidersModel = new LoadableDetachableModel<List<ContentProvider>>() {
            private static final long serialVersionUID = -6564443201990746713L;

            /**
             * @see org.apache.wicket.model.LoadableDetachableModel#load()
             */
            @Override
            protected List<ContentProvider> load() {
                return remainingProviders;
            }
        };
        final ContentProviderChoiceRenderer contentProviderChoiceRenderer = new ContentProviderChoiceRenderer();
        final ContentProviderChoiceOnChangeBehavior contentProviderChoiceOnChangeBehavior = new ContentProviderChoiceOnChangeBehavior();
        final DropDownChoice<ContentProvider> contentProviderChoice = new DropDownChoice<>("contentProvider", contentProviderModel, contentProvidersModel,
                contentProviderChoiceRenderer);
        contentProviderChoice.add(contentProviderChoiceOnChangeBehavior);

        this.cpcPropertiesView = new ListView<TranslatedKey<ContentProviderConsumerPropertyKey>>("cpcProperties") {
            private static final long serialVersionUID = -2196673718609562113L;

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
            };
        };

        this.cpcPropertiesContainer = new WebMarkupContainer("cpcPropertiesContainer");
        cpcPropertiesContainer.setOutputMarkupId(true);
        cpcPropertiesContainer.add(cpcPropertiesView);

        final Button submitButton = new Button("submit") {
            private static final long serialVersionUID = 2890193713568376966L;

            /**
             * @see org.apache.wicket.markup.html.form.Button#onSubmit()
             */
            @Override
            public void onSubmit() {
                ContentProviderConsumer contentProviderConsumer = form.getModelObject();
                contentProviderConsumer.setEhubConsumer(ehubConsumer);
                contentProviderConsumer = consumerAdminController.save(contentProviderConsumer);

                Set<ContentProviderConsumer> contentProviderConsumers = ehubConsumer.getContentProviderConsumers();
                if (contentProviderConsumers == null) {
                    contentProviderConsumers = new HashSet<>();
                    ehubConsumer.setContentProviderConsumers(contentProviderConsumers);
                }
                contentProviderConsumers.add(contentProviderConsumer);
                ehubConsumer = consumerAdminController.save(ehubConsumer);

                final IBreadCrumbPanelFactory factory = createContentProviderConsumerPanelFactory(breadCrumbModel, contentProviderConsumer);
                activate(factory);
            }
        };

        form.add(contentProviderChoice);
        form.add(cpcPropertiesContainer);
        form.add(submitButton);
        return form;
    }

    /**
     * The {@link IModel} of a {@link ContentProvider} {@link DropDownChoice} in the {@link ContentProviderConsumer}
     * {@link StatelessForm}.
     */
    private class ContentProviderModel implements IModel<ContentProvider> {
        private static final long serialVersionUID = -1899921125773400463L;

        /**
         * @see org.apache.wicket.model.IModel#getObject()
         */
        @Override
        public ContentProvider getObject() {
            final ContentProviderConsumer contentProviderConsumer = cpcForm.getModelObject();
            return contentProviderConsumer.getContentProvider();
        }

        /**
         * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
         */
        @Override
        public void setObject(ContentProvider contentProvider) {
            final ContentProviderConsumer contentProviderConsumer = cpcForm.getModelObject();
            contentProviderConsumer.setContentProvider(contentProvider);
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

    /**
     * Provides the possibility to render a {@link ContentProvider}.
     */
    private class ContentProviderChoiceRenderer extends ChoiceRenderer<ContentProvider> {
        private static final long serialVersionUID = 1331907787902356770L;

        /**
         * @see org.apache.wicket.markup.html.form.ChoiceRenderer#getIdValue(java.lang.Object, int)
         */
        @Override
        public String getIdValue(ContentProvider object, int index) {
            return object.getId().toString();
        }

        /**
         * @see org.apache.wicket.markup.html.form.ChoiceRenderer#getDisplayValue(java.lang.Object)
         */
        @Override
        public Object getDisplayValue(ContentProvider object) {
            ContentProviderName contentProviderName = object.getName();
            return contentProviderName.toString();
        }
    }

    /**
     * This {@link AjaxFormComponentUpdatingBehavior} updates the {@link ListView} of {@link ContentProviderConsumer}
     * properties when the {@link Component} this behavior is added to is changed.
     */
    private class ContentProviderChoiceOnChangeBehavior extends AjaxFormComponentUpdatingBehavior {
        private static final long serialVersionUID = 450874678091338768L;

        /**
         * Constructs a new {@link ContentProviderChoiceOnChangeBehavior}.
         */
        private ContentProviderChoiceOnChangeBehavior() {
            super("onchange");
        }

        /**
         * @see org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior#onUpdate(org.apache.wicket.ajax.AjaxRequestTarget)
         */
        @Override
        protected void onUpdate(AjaxRequestTarget target) {
            final ContentProviderConsumer contentProviderConsumer = cpcForm.getModelObject();
            final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
            final List<ContentProviderConsumerPropertyKey> keys = contentProvider == null ? new ArrayList<ContentProviderConsumerPropertyKey>()
                    : contentProviderConsumer.getValidPropertyKeys();
            final TranslatedKeys<ContentProviderConsumerPropertyKey> translatedKeys = new TranslatedKeys<>(EhubConsumerPanel.this, keys);
            cpcPropertiesView.setList(translatedKeys);

            if (target != null) {
                target.addComponent(cpcPropertiesContainer);
            }
        }
    }

    /**
     * The {@link IModel} for the description of an {@link EhubConsumer}.
     */
    private class EhubConsumerDescriptionModel implements IModel<String> {
        private static final long serialVersionUID = -2261772571252584477L;

        /**
         * @see org.apache.wicket.model.IModel#getObject()
         */
        @Override
        public String getObject() {
            final EhubConsumer ehubConsumer = ecForm.getModelObject();
            return ehubConsumer.getDescription();
        }

        /**
         * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
         */
        @Override
        public void setObject(String value) {
            final EhubConsumer ehubConsumer = ecForm.getModelObject();
            ehubConsumer.setDescription(value);
        }

        /**
         * @see org.apache.wicket.model.IDetachable#detach()
         */
        @Override
        public void detach() {
            final IModel<EhubConsumer> formModel = ecForm.getModel();
            formModel.detach();
        }
    }

    /**
     * The {@link IModel} for a specific property of the {@link EhubConsumer}.
     */
    private class EhubConsumerPropertyModel implements IModel<String> {
        private static final long serialVersionUID = 7124261273843037637L;
        private final EhubConsumerPropertyKey propertyKey;

        /**
         * 
         * @param propertyKey
         */
        private EhubConsumerPropertyModel(EhubConsumerPropertyKey propertyKey) {
            this.propertyKey = propertyKey;
        }

        /**
         * @see org.apache.wicket.model.IModel#getObject()
         */
        @Override
        public String getObject() {
            final EhubConsumer ehubConsumer = ecForm.getModelObject();
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
            final EhubConsumer ehubConsumer = ecForm.getModelObject();
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
            final IModel<EhubConsumer> formModel = ecForm.getModel();
            formModel.detach();
        }
    }

    /**
     * The {@link IModel} for a specific property of the {@link ContentProviderConsumer}.
     */
    private class ContentProviderConsumerPropertyModel implements IModel<String> {
        private static final long serialVersionUID = 7418161395141615016L;
        private final ContentProviderConsumerPropertyKey propertyKey;

        /**
         * Constructs a new {@link ContentProviderConsumerPropertyModel}.
         * 
         * @param propertyKey the key of the property value to be handled
         */
        private ContentProviderConsumerPropertyModel(ContentProviderConsumerPropertyKey propertyKey) {
            this.propertyKey = propertyKey;
        }

        /**
         * @see org.apache.wicket.model.IModel#getObject()
         */
        @Override
        public String getObject() {
            final ContentProviderConsumer contentProviderConsumer = cpcForm.getModelObject();
            Map<ContentProviderConsumerPropertyKey, String> properties = contentProviderConsumer.getProperties();

            if (properties == null) {
                properties = new HashMap<ContentProviderConsumerPropertyKey, String>();
                contentProviderConsumer.setProperties(properties);
            }

            return properties.get(propertyKey);
        }

        /**
         * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
         */
        @Override
        public void setObject(String propertyValue) {
            final ContentProviderConsumer contentProviderConsumer = cpcForm.getModelObject();
            Map<ContentProviderConsumerPropertyKey, String> properties = contentProviderConsumer.getProperties();

            if (properties == null) {
                properties = new HashMap<ContentProviderConsumerPropertyKey, String>();
                contentProviderConsumer.setProperties(properties);
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
