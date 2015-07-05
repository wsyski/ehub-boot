package com.axiell.ehub.provider.alias;

import com.axiell.ehub.provider.ContentProvider;

import com.axiell.ehub.provider.IContentProviderAdminController;
import com.google.common.base.Function;
import com.google.common.collect.Lists;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.io.Serializable;
import java.util.List;

class ContentProviderNameDropDownChoice extends DropDownChoice<String> {

    @SpringBean(name = "contentProviderAdminController")
    private IContentProviderAdminController contentProviderAdminController;

    ContentProviderNameDropDownChoice(final String id, final AliasMapping aliasMapping) {
        super(id);
        setModel(aliasMapping);
        setChoices();
        setRequired(true);
    }

    private void setModel(final AliasMapping aliasMapping) {
        IModel<String> model = new PropertyModel<>(aliasMapping, "name");
        setModel(model);
    }

    private void setChoices() {
        final List<ContentProvider> contentProviders = contentProviderAdminController.getContentProviders();
        final List<String> choices = Lists.transform(contentProviders, new ContentProvider2ContentProviderNameFunction());
        setChoices(choices);
    }

    private static class ContentProvider2ContentProviderNameFunction implements Function<ContentProvider, String>, Serializable {
        public String apply(final ContentProvider item) {
            return item.getName();
        }
    }
}
