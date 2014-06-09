package com.axiell.ehub.consumer;

import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.markup.html.form.Button;
import org.apache.wicket.model.IModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;

class EhubConsumerEditButton extends Button {
    private final ConsumersMediator consumersMediator;
    private final IModel<EhubConsumer> formModel;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;
    
    EhubConsumerEditButton(final String id, final ConsumersMediator consumersMediator, final IModel<EhubConsumer> formModel) {
        super(id);
        InjectorHolder.getInjector().inject(this);
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