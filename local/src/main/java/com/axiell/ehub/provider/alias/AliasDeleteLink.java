package com.axiell.ehub.provider.alias;

import com.axiell.ehub.ConfirmationLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

class AliasDeleteLink extends ConfirmationLink<AliasMapping> {
    private final AliasMediator mediator;

    @SpringBean(name = "aliasAdminController")
    private IAliasAdminController aliasAdminController;

    AliasDeleteLink(final String id, final AliasMapping aliasMapping, final AliasMediator mediator) {
        super(id);
        this.mediator = mediator;
        setModel(aliasMapping);
    }

    @Override
    public void onClick() {
        final AliasMapping aliasMapping = getModelObject();
        aliasAdminController.delete(aliasMapping);
        mediator.afterAliasWasDeleted();
    }

    private void setModel(AliasMapping aliasMapping) {
        setModel(new Model<>(aliasMapping));
    }
}
