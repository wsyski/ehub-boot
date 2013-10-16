package com.axiell.ehub.consumer;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.DeleteLink;

final class EhubConsumerDeleteLink extends DeleteLink<EhubConsumer> {
    private static final String CONFIRMATION_TEXT = "Are you sure you want to delete this eHUB Consumer?";
    private final EhubConsumer ehubConsumer;
    private final ConsumersMediator consumersMediator;

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;
    
    EhubConsumerDeleteLink(final String id, final EhubConsumer ehubConsumer, final ConsumersMediator consumersMediator) {
        super(id, CONFIRMATION_TEXT);
        this.ehubConsumer = ehubConsumer;
        this.consumersMediator = consumersMediator;
    }

    @Override
    public void onClick() {
        consumerAdminController.delete(ehubConsumer);
        consumersMediator.afterDeleteEhubConsumer();
    } 
}