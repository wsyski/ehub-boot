package com.axiell.ehub.provider.record.format;

import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.panel.Panel;

abstract class AbstractFormatDecorationFormPanel<M extends IContentDispositionChangedAwareMediator> extends Panel {
    protected final M mediator;
    protected final AbstractFormatDecorationForm<M> decorationForm;

    protected AbstractFormatDecorationFormPanel(final String panelId, final M mediator) {
        super(panelId);
        this.mediator = mediator;
        setOutputMarkupPlaceholderTag(true);
        addCancelLink();

        decorationForm = makeFormatDecorationForm("decorationForm");
        add(decorationForm);
    }

    protected abstract AbstractFormatDecorationForm<M> makeFormatDecorationForm(final String id);

    private void addCancelLink() {
        final Link<?> cancelLink = makeFormatDecorationCancelLink("cancelLink");
        add(cancelLink);
    }

    protected abstract Link<?> makeFormatDecorationCancelLink(final String id);

    public void setFormModelObject(FormatDecoration formatDecoration) {
        decorationForm.setModelObject(formatDecoration);
    }
}
