package com.axiell.ehub.provider.alias;

import com.axiell.ehub.provider.ContentProviderName;
import com.google.common.collect.Lists;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;

import java.util.List;

class ContentProviderNameDropDownChoice extends DropDownChoice<ContentProviderName> {

    ContentProviderNameDropDownChoice(final String id, final AliasMapping aliasMapping) {
        super(id);
        setModel(aliasMapping);
        setChoices();
        setRequired(true);
    }

    private void setModel(final AliasMapping aliasMapping) {
        IModel<ContentProviderName> model = new PropertyModel<>(aliasMapping, "name");
        setModel(model);
    }

    private void setChoices() {
        final List<ContentProviderName> choices = Lists.newArrayList(ContentProviderName.values());
        setChoices(choices);
    }
}
