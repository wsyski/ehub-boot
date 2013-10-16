package com.axiell.ehub.provider;

import java.io.Serializable;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;

import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecorationFormPanel;
import com.axiell.ehub.provider.record.format.FormatDecorationPanelFactory;

public class ContentProviderMediator implements Serializable {
    private ContentProviderPanel contentProviderPanel;
    private FormatDecorationFormPanel formatDecorationFormPanel;
    
    void registerContentProviderPanel(final ContentProviderPanel contentProviderPanel) {
	this.contentProviderPanel = contentProviderPanel;
    }
    
    void registerFormatDecorationFormPanel(final FormatDecorationFormPanel formatDecorationFormPanel) {
	this.formatDecorationFormPanel = formatDecorationFormPanel;
    }

    void afterEditContentProvider() {
	contentProviderPanel.activate(contentProviderPanel);
    }
    
    public void afterDeleteFormatDecoration() {
	contentProviderPanel.activate(contentProviderPanel);
    }

    public void afterClickOnFormatDecorationCreateLink(AjaxRequestTarget target) {
	formatDecorationFormPanel.setVisible(true);
	
	if (target != null) {
	    target.addComponent(formatDecorationFormPanel);
	}
    }

    public void afterSavedFormatDecoration(final FormatDecoration formatDecoration) {
	final IBreadCrumbPanelFactory factory = new FormatDecorationPanelFactory(formatDecoration, this);
        contentProviderPanel.activate(factory);	
    }
}
