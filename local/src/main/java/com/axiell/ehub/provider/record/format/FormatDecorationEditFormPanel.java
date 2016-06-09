package com.axiell.ehub.provider.record.format;

import org.apache.wicket.markup.html.link.Link;

class FormatDecorationEditFormPanel extends AbstractFormatDecorationFormPanel<FormatDecorationMediator> {

    FormatDecorationEditFormPanel(final String panelId, final FormatDecorationMediator mediator, final FormatDecoration formatDecoration) {
        super(panelId, mediator);
        setFormModelObject(formatDecoration);
    }

    @Override
    protected Link<?> makeFormatDecorationCancelLink(String id) {
        return new InvisibleLink(id);
    }

    @Override
    protected AbstractFormatDecorationForm<FormatDecorationMediator> makeFormatDecorationForm(String id) {
        return new FormatDecorationEditForm(id, mediator);
    }
}
