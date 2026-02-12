package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.ContentProviderConsumer;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class ContentProviderConsumerEditButton extends Button {
    private final ConsumersMediator consumersMediator;
    private final IModel<ContentProviderConsumer> formModel;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;

    ContentProviderConsumerEditButton(final String id, final ConsumersMediator consumersMediator, final IModel<ContentProviderConsumer> formModel) {
        super(id);
        this.consumersMediator = consumersMediator;
        this.formModel = formModel;
    }

    @Override
    public void onSubmit() {
        ContentProviderConsumer contentProviderConsumer = formModel.getObject();
        consumerAdminController.save(contentProviderConsumer);
        consumersMediator.afterEditContentProviderConsumer();
    }
}
