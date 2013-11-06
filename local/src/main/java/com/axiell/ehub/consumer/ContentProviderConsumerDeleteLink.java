package com.axiell.ehub.consumer;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.ConfirmationLink;

class ContentProviderConsumerDeleteLink extends ConfirmationLink<ContentProviderConsumer> {
    private final ContentProviderConsumer contentProviderConsumer;
    private final ConsumersMediator consumersMediator;
    
    @SpringBean(name = "consumerAdminController") 
    private IConsumerAdminController consumerAdminController;

    ContentProviderConsumerDeleteLink(final String id, final ContentProviderConsumer contentProviderConsumer, final ConsumersMediator consumersMediator) {
        super(id);        
        this.contentProviderConsumer = contentProviderConsumer;
        this.consumersMediator = consumersMediator;
    }

    @Override
    public void onClick() {
        consumerAdminController.delete(contentProviderConsumer);
        consumersMediator.afterDeleteContentProviderConsumer();
    }
}