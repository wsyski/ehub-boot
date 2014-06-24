package com.axiell.ehub.provider.routing;

import com.axiell.ehub.provider.ContentProviderName;
import com.google.common.collect.Lists;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

class TargetDropDownChoice extends DropDownChoice<ContentProviderName> {

    TargetDropDownChoice(final String id, final RoutingRule routingRule) {
        super(id);
        setModel(routingRule);
        setChoices();
        setRequired(true);
    }

    private void setModel(final RoutingRule routingRule) {
        IModel<ContentProviderName> model = new PropertyModel<>(routingRule, "target");
        setModel(model);
    }

    private void setChoices() {
        List<ContentProviderName> choices = Lists.newArrayList(ContentProviderName.values());
        setChoices(choices);
    }
}
