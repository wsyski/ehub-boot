package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.EhubConsumer;
import org.apache.wicket.injection.Injector;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

class EhubConsumerEditButton extends Button {
    private final ConsumersMediator consumersMediator;
    private final IModel<EhubConsumer> formModel;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;

    EhubConsumerEditButton(final String id, final ConsumersMediator consumersMediator, final IModel<EhubConsumer> formModel) {
        super(id);
        Injector.get().inject(this);
        this.consumersMediator = consumersMediator;
        this.formModel = formModel;
    }

    @Override
    public void onSubmit() {
        EhubConsumer ehubConsumer = formModel.getObject();
        consumerAdminController.save(ehubConsumer);
        consumersMediator.afterEditEhubConsumer();
    }
}
