package com.axiell.ehub.consumer;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

final class NewEhubConsumerSubmitButton extends Button {
    private final ConsumersMediator consumersMediator;
    private final IModel<EhubConsumer> formModel;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;
    
    NewEhubConsumerSubmitButton(final String id, final ConsumersMediator consumersMediator, final IModel<EhubConsumer> formModel) {
        super(id);
        this.consumersMediator = consumersMediator;
        this.formModel = formModel;
    }

    @Override
    public void onSubmit() {
        final String secretKey = RandomStringUtils.random(10, true, true);
        EhubConsumer ehubConsumer = formModel.getObject();
        ehubConsumer.setSecretKey(secretKey);
        ehubConsumer = consumerAdminController.save(ehubConsumer);
        consumersMediator.afterNewEhubConsumer(ehubConsumer);
    }
}