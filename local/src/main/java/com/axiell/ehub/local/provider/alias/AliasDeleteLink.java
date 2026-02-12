package com.axiell.ehub.local.provider.alias;

import com.axiell.ehub.common.provider.alias.AliasMapping;
import com.axiell.ehub.local.ConfirmationLink;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;

class AliasDeleteLink extends ConfirmationLink<AliasMapping> implements Serializable {
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
