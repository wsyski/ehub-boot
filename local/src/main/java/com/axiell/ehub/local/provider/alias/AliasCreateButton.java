package com.axiell.ehub.local.provider.alias;

import com.axiell.ehub.common.provider.alias.AliasMapping;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.spring.injection.annot.SpringBean;

class AliasCreateButton extends AjaxButton {
    private final AliasMediator mediator;

    @SpringBean(name = "aliasAdminController")
    private IAliasAdminController aliasAdminController;

    AliasCreateButton(final String id, final AliasMediator mediator) {
        super(id);
        this.mediator = mediator;
    }

    @Override
    protected void onSubmit(final AjaxRequestTarget target) {
        final AliasMapping aliasMapping = (AliasMapping) getForm().getModelObject();
        aliasAdminController.save(aliasMapping);
        mediator.afterAliasWasCreated(target);
    }

    @Override
    protected void onError(final AjaxRequestTarget target) {
        mediator.onCreateError(target);
    }
}
