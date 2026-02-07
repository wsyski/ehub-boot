package com.axiell.ehub.consumer;

import com.axiell.ehub.TranslatedKeys;
import com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.WebMarkupContainer;

import java.io.Serializable;
import java.util.Optional;

final class ConsumersMediator implements Serializable {
    private EhubConsumerPanel ehubConsumerPanel;
    private ContentProviderConsumerCreateLink contentProviderConsumerCreateLink;
    private WebMarkupContainer contentProviderConsumerCreateFormContainer;
    private ContentProviderConsumerPropertiesListView contentProviderConsumerPropertiesListView;
    private WebMarkupContainer contentProviderConsumerPropertiesContainer;
    private ContentProviderConsumerPanel contentProviderConsumerPanel;
    private EhubConsumersPanel ehubConsumersPanel;
    private EhubConsumerCreateLink ehubConsumerCreateLink;
    private WebMarkupContainer ehubConsumerFormContainer;

    void registerEhubConsumerPanel(final EhubConsumerPanel ehubConsumerPanel) {
        this.ehubConsumerPanel = ehubConsumerPanel;
    }

    void registerContentProviderConsumerCreateLink(final ContentProviderConsumerCreateLink contentProviderConsumerCreateLink) {
        this.contentProviderConsumerCreateLink = contentProviderConsumerCreateLink;
    }

    void registerContentProviderConsumerCreateFormContainer(final WebMarkupContainer contentProviderConsumerCreateFormContainer) {
        this.contentProviderConsumerCreateFormContainer = contentProviderConsumerCreateFormContainer;
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

    void registerEhubConsumerCreateLink(final EhubConsumerCreateLink ehubConsumerCreateLink) {
        this.ehubConsumerCreateLink = ehubConsumerCreateLink;
    }

    void registerEhubConsumerFormContainer(final WebMarkupContainer ehubConsumerFormContainer) {
        this.ehubConsumerFormContainer = ehubConsumerFormContainer;
    }

    void afterDeleteContentProviderConsumer() {
        ehubConsumerPanel.activate(ehubConsumerPanel);
    }

    void afterNewContentProviderConsumerLinkClick(final Optional<AjaxRequestTarget> targetOptional) {
        contentProviderConsumerCreateFormContainer.setVisible(true);

        targetOptional.ifPresent(
                target -> {
                    target.add(contentProviderConsumerCreateFormContainer);
                });
    }

    void afterCancelNewContentProviderConsumer(final Optional<AjaxRequestTarget> targetOptional) {
        contentProviderConsumerCreateFormContainer.setVisible(false);
        contentProviderConsumerCreateLink.setVisible(true);

        targetOptional.ifPresent(
                target -> {
                    target.add(contentProviderConsumerCreateFormContainer);
                    target.add(contentProviderConsumerCreateLink);
                });
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
            target.add(contentProviderConsumerPropertiesContainer);
        }
    }

    void afterEditContentProviderConsumer() {
        contentProviderConsumerPanel.activate(contentProviderConsumerPanel);
    }

    void afterDeleteEhubConsumer() {
        ehubConsumersPanel.activate(ehubConsumersPanel);
    }

    void afterCancelNewEhubConsumerConsumer(final Optional<AjaxRequestTarget> targetOptional) {
        ehubConsumerFormContainer.setVisible(false);
        ehubConsumerCreateLink.setVisible(true);

        targetOptional.ifPresent(
                target -> {
                    target.add(ehubConsumerFormContainer);
                    target.add(ehubConsumerCreateLink);
                });
    }

    void afterClickOnEhubConsumerCreateLink(final Optional<AjaxRequestTarget> targetOptional) {
        ehubConsumerFormContainer.setVisible(true);

        targetOptional.ifPresent(
                target -> {
                    target.add(ehubConsumerFormContainer);
                });
    }

    void afterNewEhubConsumer(final EhubConsumer ehubConsumer) {
        final IBreadCrumbPanelFactory factory = new EhubConsumerPanelFactory(ehubConsumer);
        ehubConsumersPanel.activate(factory);
    }
}
