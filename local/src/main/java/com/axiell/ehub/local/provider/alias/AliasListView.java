package com.axiell.ehub.local.provider.alias;

import com.axiell.ehub.common.provider.alias.AliasMapping;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

public class AliasListView extends ListView<AliasMapping> {
    private final AliasMediator mediator;

    @SpringBean(name = "aliasAdminController")
    private IAliasAdminController aliasAdminController;

    AliasListView(final String id, final AliasMediator mediator) {
        super(id);
        this.mediator = mediator;
        setRoutingRuleList();
    }

    @Override
    protected void populateItem(final ListItem<AliasMapping> item) {
        final AliasMapping aliasMapping = item.getModelObject();
        final Label aliasLabel = new Label("alias", aliasMapping.getAlias().getValue());
        final Label contentProviderLabel = new Label("contentProvider", aliasMapping.getName());
        final AliasDeleteLink deleteLink = new AliasDeleteLink("deleteLink", aliasMapping, mediator);
        item.add(deleteLink);
        item.add(aliasLabel);
        item.add(contentProviderLabel);
    }

    private void setRoutingRuleList() {
        final List<AliasMapping> aliasMappings = aliasAdminController.getRoutingRules();
        setList(aliasMappings);
    }
}
