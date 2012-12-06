/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.IndicatingAjaxFallbackLink;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.StatelessForm;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.TranslatedKey;
import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecorationFormPanel;
import com.axiell.ehub.provider.record.format.FormatDecorationPanel;
import com.axiell.ehub.provider.record.format.IFormatAdminController;

/**
 * A {@link Panel} that displays a {@link ContentProvider}. It also provides the possibility to modify the properties of
 * the {@link ContentProvider} and to add new {@link FormatDecoration}s to the {@link ContentProvider}.
 */
final class ContentProviderPanel extends BreadCrumbPanel {
    private static final long serialVersionUID = 787652555249894737L;
    private final ContentProviderName contentProviderName;
    private final StatelessForm<ContentProvider> contentProviderForm;
    private ListView<TranslatedKey<ContentProviderPropertyKey>> propertiesView;
    private final ListView<String> decorationsView;
    private ContentProvider contentProvider;
    
    private final IndicatingAjaxFallbackLink<Void> newFormatDecorationLink;
    private final FormatDecorationFormPanel decorationFormPanel;

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;
    
    @SpringBean(name = "formatAdminController")
    private IFormatAdminController formatAdminController;
    
    /**
     * Constructs a new {@link ContentProviderPanel}.
     * 
     * @param panelId the ID of this {@link Panel}
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     * @param contentProviderName the name of the {@link ContentProvider} to be shown
     */
    ContentProviderPanel(final String panelId, final IBreadCrumbModel breadCrumbModel, final ContentProviderName contentProviderName) {
        super(panelId, breadCrumbModel);
        this.contentProviderName = contentProviderName;
        this.contentProviderForm = createContentProviderForm();
        add(contentProviderForm);
                
        this.decorationsView = new ListView<String>("decorations") {
            private static final long serialVersionUID = 5109529098752699187L;

            /**
             * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
             */
            @Override
            protected void populateItem(ListItem<String> item) {
                final String formatId = item.getModelObject();
                final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);                                
                final IBreadCrumbPanelFactory factory = createFormatDecorationPanelFactory(breadCrumbModel, formatDecoration);
                final Label linkLabel = new Label("decorationLinkLabel", formatId);
                final BreadCrumbPanelLink link = new BreadCrumbPanelLink("decorationLink", breadCrumbModel, factory);
                link.add(linkLabel);
                item.add(link);
                
                final Link<EhubConsumer> deleteLink = createDeleteLink("deleteLink", formatDecoration);
                item.add(deleteLink);
            }
        };
        add(decorationsView);
        
        this.decorationFormPanel = new FormatDecorationFormPanel("decorationFormPanel", true) {
            private static final long serialVersionUID = -2310720444445683944L;

            /**
             * @see com.axiell.ehub.provider.record.format.FormatDecorationFormPanel#afterSave(com.axiell.ehub.provider.record.format.FormatDecoration)
             */
            @Override
            protected void afterSave(FormatDecoration formatDecoration) {
                final IBreadCrumbPanelFactory factory = createFormatDecorationPanelFactory(breadCrumbModel, formatDecoration);
                activate(factory);
            }
        };
        add(decorationFormPanel);
        
        this.newFormatDecorationLink = new IndicatingAjaxFallbackLink<Void>("newFormatDecorationLink") {
            private static final long serialVersionUID = 2089552173606255904L;

            /**
             * @see org.apache.wicket.ajax.markup.html.AjaxFallbackLink#onClick(org.apache.wicket.ajax.AjaxRequestTarget)
             */
            @Override
            public void onClick(AjaxRequestTarget target) {
                setVisible(false);
                decorationFormPanel.setVisible(true);

                if (target != null) {
                    target.addComponent(this);
                    target.addComponent(decorationFormPanel);
                }
            }
        };
        newFormatDecorationLink.setOutputMarkupPlaceholderTag(true);
        add(newFormatDecorationLink);
        decorationFormPanel.setOnCancelVisibleLink(newFormatDecorationLink);
        
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant#getTitle()
     */
    @Override
    public String getTitle() {
        return contentProviderName.toString();
    }

    /**
     * @see org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanel#onActivate(org.apache.wicket.extensions.breadcrumb.IBreadCrumbParticipant)
     */
    @Override
    public void onActivate(IBreadCrumbParticipant previous) {
        contentProvider = contentProviderAdminController.getContentProvider(contentProviderName);        
        contentProviderForm.setModelObject(contentProvider);
        
        final List<ContentProviderPropertyKey> propertyKeys = contentProvider.getValidPropertyKeys();
        final TranslatedKeys<ContentProviderPropertyKey> translatedKeys = new TranslatedKeys<>(this, propertyKeys);
        propertiesView.setList(translatedKeys);
        
        final Locale locale = getLocale();
        final List<String> formatIds = contentProvider.getFormatIds(locale);
        decorationsView.setList(formatIds);
        
        final FormatDecoration formatDecoration = new FormatDecoration(contentProvider);
        decorationFormPanel.setFormModelObject(formatDecoration);
        decorationFormPanel.setVisible(false);
        newFormatDecorationLink.setVisible(true);
        
        super.onActivate(previous);
    }
    
    /**
     * Creates a {@link StatelessForm} that provides the possibility to modify the properties of the previously provided {@link ContentProvider}.
     * 
     * @return a {@link StatelessForm}
     */
    private StatelessForm<ContentProvider> createContentProviderForm() {
        final IModel<ContentProvider> formModel = new Model<>();
        final StatelessForm<ContentProvider> form = new StatelessForm<>("contentProviderForm", formModel);
        
        this.propertiesView = new ListView<TranslatedKey<ContentProviderPropertyKey>>("properties") {
            private static final long serialVersionUID = 5666157159534216838L;

            /**
             * @see org.apache.wicket.markup.html.list.ListView#populateItem(org.apache.wicket.markup.html.list.ListItem)
             */
            @Override
            protected void populateItem(ListItem<TranslatedKey<ContentProviderPropertyKey>> item) {
                final TranslatedKey<ContentProviderPropertyKey> translatedKey = item.getModelObject();
                final ContentProviderPropertyKey propertyKey = translatedKey.getKey();
                
                final IModel<String> propertyModel = new ContentProviderPropertyModel(propertyKey);
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
            private static final long serialVersionUID = -5998456086528056338L;

            /**
             * @see org.apache.wicket.markup.html.form.Button#onSubmit()
             */
            @Override
            public void onSubmit() {
                ContentProvider contentProvider = form.getModelObject();
                contentProviderAdminController.save(contentProvider);
                activate(ContentProviderPanel.this);
            }
        };
        form.add(submitButton);
        
        return form;
    }
    
    /**
     * Creates an {@link IBreadCrumbPanelFactory} the provides the possibility to create {@link FormatDecorationPanel}s.
     * 
     * @param breadCrumbModel the {@link IBreadCrumbModel} to be used
     * @param formatDecoration the {@link FormatDecoration} to be forwarded to the constructor of the {@link FormatDecorationPanel}
     * @return
     */
    private IBreadCrumbPanelFactory createFormatDecorationPanelFactory(final IBreadCrumbModel breadCrumbModel, final FormatDecoration formatDecoration) {
        return new IBreadCrumbPanelFactory() {
            private static final long serialVersionUID = -3381311577172628586L;

            /**
             * @see org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory#create(java.lang.String,
             * org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel)
             */
            @Override
            public BreadCrumbPanel create(final String id, final IBreadCrumbModel model) {
                return new FormatDecorationPanel(id, model, formatDecoration);
            }
        };
    }
    
    /**
     * Creates a {@link Link} that deletes the provides {@link FormatDecoration} on click.
     * 
     * @param formatDecoration the {@link FormatDecoration} to be deleted
     * @return a {@link Link}
     */
    private Link<EhubConsumer> createDeleteLink(final String linkId, final FormatDecoration formatDecoration) {
        return new Link<EhubConsumer>(linkId) {
            private static final long serialVersionUID = -3735251678540234334L;

            /**
             * @see org.apache.wicket.markup.html.link.Link#onClick()
             */
            @Override
            public void onClick() {
                formatAdminController.delete(formatDecoration);
                activate(ContentProviderPanel.this);
            }
        };
    }
    
    /**
     * The {@link IModel} for a specific property of the {@link ContentProvider}.
     */
    private class ContentProviderPropertyModel implements IModel<String> {
        private static final long serialVersionUID = -650434626998789730L;
        private final ContentProviderPropertyKey propertyKey;

        /**
         * Constructs a new {@link ContentProviderPropertyModel}
         * 
         * @param propertyKey the key of the property value to handle
         */
        private ContentProviderPropertyModel(ContentProviderPropertyKey propertyKey) {
            this.propertyKey = propertyKey;
        }

        /**
         * @see org.apache.wicket.model.IModel#getObject()
         */
        @Override
        public String getObject() {
            final ContentProvider contentProvider = contentProviderForm.getModelObject();
            Map<ContentProviderPropertyKey, String> properties = contentProvider.getProperties();

            if (properties == null) {
                properties = new HashMap<ContentProviderPropertyKey, String>();
                contentProvider.setProperties(properties);
            }

            return properties.get(propertyKey);
        }

        /**
         * @see org.apache.wicket.model.IModel#setObject(java.lang.Object)
         */
        @Override
        public void setObject(String propertyValue) {
            final ContentProvider contentProvider = contentProviderForm.getModelObject();
            Map<ContentProviderPropertyKey, String> properties = contentProvider.getProperties();

            if (properties == null) {
                properties = new HashMap<ContentProviderPropertyKey, String>();
                contentProvider.setProperties(properties);
            }

            properties.put(propertyKey, propertyValue);
        }

        /**
         * @see org.apache.wicket.model.IDetachable#detach()
         */
        @Override
        public void detach() {
            final IModel<ContentProvider> formModel = contentProviderForm.getModel();
            formModel.detach();
        }
    }
}
