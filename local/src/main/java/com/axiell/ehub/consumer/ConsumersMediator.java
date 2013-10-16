package com.axiell.ehub.consumer;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;

import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;

final class ConsumersMediator implements Serializable {
    private EhubConsumerPanel ehubConsumerPanel;
    private NewContentProviderConsumerLink newContentProviderConsumerLink;
    private WebMarkupContainer contentProviderConsumerFormContainer;
    private ContentProviderConsumerPropertiesListView contentProviderConsumerPropertiesListView;
    private WebMarkupContainer contentProviderConsumerPropertiesContainer;
    private ContentProviderConsumerPanel contentProviderConsumerPanel;
    private EhubConsumersPanel ehubConsumersPanel;
    private NewEhubConsumerLink newEhubConsumerLink;
    private WebMarkupContainer ehubConsumerFormContainer;
    
    void registerEhubConsumerPanel(final EhubConsumerPanel ehubConsumerPanel) {
	this.ehubConsumerPanel = ehubConsumerPanel;
    }
    
    void registerNewContentProviderConsumerLink(final NewContentProviderConsumerLink newContentProviderConsumerLink) {
	this.newContentProviderConsumerLink = newContentProviderConsumerLink;
    }
    
    void registerNewContentProviderConsumerFormContainer(final WebMarkupContainer contentProviderConsumerFormContainer) {
	this.contentProviderConsumerFormContainer = contentProviderConsumerFormContainer;
    }
    
    void registerContentProviderConsumerPropertiesListView(final ContentProviderConsumerPropertiesListView contentProviderConsumerPropertiesListView) {
	this.contentProviderConsumerPropertiesListView = contentProviderConsumerPropertiesListView;
    }
    
    void registerContentProviderConsumerPropertiesContainer(final WebMarkupContainer contentProviderConsumerPropertiesContainer) {
	this.contentProviderConsumerPropertiesContainer = contentProviderConsumerPropertiesContainer;
    }
    
    void registerContentProviderConsumerPanel(final ContentProviderConsumerPanel contentProviderConsumerPanel) {
	this.contentProviderConsumerPanel = contentProviderConsumerPanel;
    }
    
    void registerEhubConsumersPanel(final EhubConsumersPanel ehubConsumersPanel) {
	this.ehubConsumersPanel = ehubConsumersPanel;
    }
    
    void registerNewEhubConsumerLink(final NewEhubConsumerLink newEhubConsumerLink) {
	this.newEhubConsumerLink = newEhubConsumerLink;
    }
    
    void registerEhubConsumerFormContainer(final WebMarkupContainer ehubConsumerFormContainer) {
	this.ehubConsumerFormContainer = ehubConsumerFormContainer;
    }
    
    void afterDeleteContentProviderConsumer() {
	ehubConsumerPanel.activate(ehubConsumerPanel);
    }
    
    void afterNewContentProviderConsumerLinkClick(final AjaxRequestTarget target) {
	contentProviderConsumerFormContainer.setVisible(true);
	
	if (target != null) {
	    target.addComponent(contentProviderConsumerFormContainer);
	}
    }
    
    void afterCancelNewContentProviderConsumer(final AjaxRequestTarget target) {
	contentProviderConsumerFormContainer.setVisible(false);
	newContentProviderConsumerLink.setVisible(true);

        if (target != null) {
            target.addComponent(contentProviderConsumerFormContainer);
            target.addComponent(newContentProviderConsumerLink);
        }
    }
    
    void afterEditEhubConsumer() {
	ehubConsumerPanel.activate(ehubConsumerPanel);
    }
    
    void afterNewContentProviderConsumer(final ContentProviderConsumer contentProviderConsumer) {
	final IBreadCrumbPanelFactory factory = new ContentProviderConsumerPanelFactory(contentProviderConsumer);
	ehubConsumerPanel.activate(factory);
    }
    
    void onSelectedContentProviderConsumerChanged(final TranslatedKeys<ContentProviderConsumerPropertyKey> translatedKeys, final AjaxRequestTarget target) {
        contentProviderConsumerPropertiesListView.setList(translatedKeys);
        
        if (target != null) {
            target.addComponent(contentProviderConsumerPropertiesContainer);
        }
    }

    void afterEditContentProviderConsumer() {
	contentProviderConsumerPanel.activate(contentProviderConsumerPanel);	
    }
    
    void afterDeleteEhubConsumer() {
	ehubConsumersPanel.activate(ehubConsumersPanel);
    }
    
    void afterCancelNewEhubConsumerConsumer(final AjaxRequestTarget target) {
	ehubConsumerFormContainer.setVisible(false);
	newEhubConsumerLink.setVisible(true);

        if (target != null) {
            target.addComponent(ehubConsumerFormContainer);
            target.addComponent(newEhubConsumerLink);
        }
    }
    
    void afterNewEhubConsumerLinkClick(final AjaxRequestTarget target) {
	ehubConsumerFormContainer.setVisible(true);
	
	if (target != null) {
	    target.addComponent(ehubConsumerFormContainer);
	}
    }
    
    void afterNewEhubConsumer(final EhubConsumer ehubConsumer) {
	final IBreadCrumbPanelFactory factory = new EhubConsumerPanelFactory(ehubConsumer);
	ehubConsumersPanel.activate(factory);
    }
}
