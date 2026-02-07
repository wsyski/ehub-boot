package com.axiell.ehub.consumer;

import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

class ContentProviderConsumerCreateButton extends Button {
    private final IModel<ContentProviderConsumer> formModel;
    private final ConsumersMediator consumersMediator;
    private final EhubConsumer ehubConsumer;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;

    ContentProviderConsumerCreateButton(final String id, final IModel<ContentProviderConsumer> formModel, final ConsumersMediator consumersMediator, final EhubConsumer ehubConsumer) {
	super(id);
	this.formModel = formModel;
	this.consumersMediator = consumersMediator;
	this.ehubConsumer = ehubConsumer;
    }

    @Override
    public void onSubmit() {
	final Long ehubConsumerId = ehubConsumer.getId();
	ContentProviderConsumer contentProviderConsumer = formModel.getObject();
	contentProviderConsumer = consumerAdminController.add(ehubConsumerId, contentProviderConsumer);
	consumersMediator.afterNewContentProviderConsumer(contentProviderConsumer);
    }
}