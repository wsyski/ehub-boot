package com.axiell.ehub.provider;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecorationCreateFormPanel;
import com.axiell.ehub.provider.record.format.FormatDecorationCreateLink;
import com.axiell.ehub.provider.record.format.FormatDecorationPanelFactory;
import com.axiell.ehub.provider.record.format.IContentDispositionChangedAwareMediator;
import com.axiell.ehub.provider.record.format.PlayerContainer;

public class ContentProviderMediator implements Serializable, IContentDispositionChangedAwareMediator {
    private ContentProviderPanel contentProviderPanel;
    private FormatDecorationCreateFormPanel formatDecorationCreateFormPanel;
    private FormatDecorationCreateLink formatDecorationCreateLink;
    private PlayerContainer playerContainer;

    void registerContentProviderPanel(final ContentProviderPanel contentProviderPanel) {
	this.contentProviderPanel = contentProviderPanel;
    }

    void registerFormatDecorationCreateFormPanel(final FormatDecorationCreateFormPanel formatDecorationCreateFormPanel) {
	this.formatDecorationCreateFormPanel = formatDecorationCreateFormPanel;
    }

    public void registerFormatDecorationCreateLink(final FormatDecorationCreateLink formatDecorationCreateLink) {
	this.formatDecorationCreateLink = formatDecorationCreateLink;
    }
    
    @Override
    public void registerPlayerContainer(PlayerContainer playerContainer) {
	this.playerContainer = playerContainer;
    }

    void afterEditContentProvider() {
	contentProviderPanel.activate(contentProviderPanel);
    }

    public void afterDeleteFormatDecoration() {
	contentProviderPanel.activate(contentProviderPanel);
    }

    public void afterClickOnFormatDecorationCreateLink(final AjaxRequestTarget target) {
	formatDecorationCreateFormPanel.setVisible(true);

	if (target != null) {
	    target.addComponent(formatDecorationCreateFormPanel);
	}
    }

    public void afterNewFormatDecoration(final FormatDecoration formatDecoration) {
	final IBreadCrumbPanelFactory factory = new FormatDecorationPanelFactory(formatDecoration);
	contentProviderPanel.activate(factory);
    }

    public void afterCancelNewFormatDecoration(final AjaxRequestTarget target) {
	formatDecorationCreateFormPanel.setVisible(false);
	formatDecorationCreateLink.setVisible(true);

	if (target != null) {
	    target.addComponent(formatDecorationCreateFormPanel);
	    target.addComponent(formatDecorationCreateLink);
	}
    }

    @Override
    public void afterContentDispositionChanged(AjaxRequestTarget target) {
	if (target != null) {
	    target.addComponent(playerContainer);
	}
    }
}
