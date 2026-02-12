package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.common.language.Language;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;

class EhubConsumerLanguageModel extends Model<Language> {
    private final IModel<EhubConsumer> formModel;

    EhubConsumerLanguageModel(final IModel<EhubConsumer> formModel) {
        this.formModel = formModel;
    }

    @Override
    public Language getObject() {
        final EhubConsumer ehubConsumer = formModel.getObject();
        return ehubConsumer.getDefaultLanguage();
    }

    @Override
    public void setObject(final Language language) {
        final EhubConsumer ehubConsumer = formModel.getObject();
        ehubConsumer.setDefaultLanguage(language);
    }

    @Override
    public void detach() {
        formModel.detach();
    }
}
