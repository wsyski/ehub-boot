package com.axiell.ehub.consumer;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.ConfirmationLink;

final class EhubConsumerDeleteLink extends ConfirmationLink<EhubConsumer> {   
    private final EhubConsumer ehubConsumer;
    private final ConsumersMediator consumersMediator;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;
    
    EhubConsumerDeleteLink(final String id, final EhubConsumer ehubConsumer, final ConsumersMediator consumersMediator) {
        super(id);
        this.ehubConsumer = ehubConsumer;
        this.consumersMediator = consumersMediator;
    }

    @Override
    public void onClick() {
        consumerAdminController.delete(ehubConsumer);
        consumersMediator.afterDeleteEhubConsumer();
    } 
}