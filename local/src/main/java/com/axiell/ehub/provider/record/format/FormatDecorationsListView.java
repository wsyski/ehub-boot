package com.axiell.ehub.provider.record.format;

import java.util.List;
import java.util.Locale;

import org.apache.wicket.extensions.breadcrumb.IBreadCrumbModel;
import org.apache.wicket.extensions.breadcrumb.panel.BreadCrumbPanelLink;
import org.apache.wicket.extensions.breadcrumb.panel.IBreadCrumbPanelFactory;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;

import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderMediator;
import com.axiell.ehub.provider.record.format.FormatDecoration;

public final class FormatDecorationsListView extends ListView<String> {
    private final IBreadCrumbModel breadCrumbModel;
    private final ContentProviderMediator contentProviderMediator;
    private ContentProvider contentProvider;

    public FormatDecorationsListView(final String id, final IBreadCrumbModel breadCrumbModel, final ContentProviderMediator contentProviderMediator) {
        super(id);
        this.breadCrumbModel = breadCrumbModel;
        this.contentProviderMediator = contentProviderMediator;
    }

    @Override
    protected void populateItem(ListItem<String> item) {
        final String formatId = item.getModelObject();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
        
        final BreadCrumbPanelLink formatDecorationLink = makeFormatDecorationLink(formatId, formatDecoration);
        item.add(formatDecorationLink);
        
        final FormatDecorationDeleteLink formatDecorationDeleteLink = new FormatDecorationDeleteLink("deleteLink", formatDecoration, contentProviderMediator);
        item.add(formatDecorationDeleteLink);
    }

    private BreadCrumbPanelLink makeFormatDecorationLink(final String formatId, final FormatDecoration formatDecoration) {	                                
        final IBreadCrumbPanelFactory factory = new FormatDecorationPanelFactory(formatDecoration, contentProviderMediator);	
	final BreadCrumbPanelLink link = new BreadCrumbPanelLink("decorationLink", breadCrumbModel, factory);        
        final Label linkLabel = new Label("decorationLinkLabel", formatId);
        link.add(linkLabel);
        return link;
    }
    
    public void setContentProvider(ContentProvider contentProvider) {
	this.contentProvider = contentProvider;	
	setFormatIds(contentProvider);
    }

    private void setFormatIds(final ContentProvider contentProvider) {
	final Locale locale = getLocale();
        final List<String> formatIds = contentProvider.getFormatIds(locale);
        setList(formatIds);
    }
}