package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.EhubConsumer;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

final class EhubConsumerCreateButton extends Button {
    private final ConsumersMediator consumersMediator;
    private final IModel<EhubConsumer> formModel;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;

    EhubConsumerCreateButton(final String id, final ConsumersMediator consumersMediator, final IModel<EhubConsumer> formModel) {
        super(id);
        this.consumersMediator = consumersMediator;
        this.formModel = formModel;
    }

    @Override
    public void onSubmit() {
        final String secretKey = Base64.getEncoder().encodeToString(RandomStringUtils.random(8, true, true).getBytes(StandardCharsets.UTF_8));
        EhubConsumer ehubConsumer = formModel.getObject();
        ehubConsumer.setSecretKey(secretKey);
        ehubConsumer = consumerAdminController.save(ehubConsumer);
        consumersMediator.afterNewEhubConsumer(ehubConsumer);
    }
}
