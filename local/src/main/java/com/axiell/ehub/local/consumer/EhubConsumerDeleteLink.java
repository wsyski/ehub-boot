package com.axiell.ehub.local.consumer;

import com.axiell.ehub.common.consumer.EhubConsumer;
import com.axiell.ehub.local.ConfirmationLink;
import org.apache.wicket.spring.injection.annot.SpringBean;

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
