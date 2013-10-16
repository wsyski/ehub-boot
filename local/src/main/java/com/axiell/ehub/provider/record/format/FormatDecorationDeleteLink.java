package com.axiell.ehub.provider.record.format;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.DeleteLink;
import com.axiell.ehub.provider.ContentProviderMediator;

public final class FormatDecorationDeleteLink extends DeleteLink<Void> {
    private static final String CONFIRMATION_TEXT = "Are you sure you want to delete this format?";
    private final FormatDecoration formatDecoration;
    private final ContentProviderMediator contentProviderMediator;
    
    @SpringBean(name = "formatAdminController") 
    private IFormatAdminController formatAdminController;

    public FormatDecorationDeleteLink(final String id, final FormatDecoration formatDecoration, final ContentProviderMediator contentProviderMediator) {
        super(id, CONFIRMATION_TEXT);
        this.formatDecoration = formatDecoration;
        this.contentProviderMediator = contentProviderMediator;
    }

    @Override
    public void onClick() {
        formatAdminController.delete(formatDecoration);
        contentProviderMediator.afterDeleteFormatDecoration();
    }
}