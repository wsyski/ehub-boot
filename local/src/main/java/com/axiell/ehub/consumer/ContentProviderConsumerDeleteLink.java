package com.axiell.ehub.consumer;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.axiell.ehub.DeleteLink;

class ContentProviderConsumerDeleteLink extends DeleteLink<ContentProviderConsumer> {
    private static final String CONFIRMATION_TEXT = "Are you sure you want to delete this Content Provider Consumer?";
    private final ContentProviderConsumer contentProviderConsumer;
    private final ConsumersMediator consumersMediator;
    
    @SpringBean(name = "consumerAdminController") 
    private IConsumerAdminController consumerAdminController;

    ContentProviderConsumerDeleteLink(final String id, final ContentProviderConsumer contentProviderConsumer, final ConsumersMediator consumersMediator) {
        super(id, CONFIRMATION_TEXT);        
        this.contentProviderConsumer = contentProviderConsumer;
        this.consumersMediator = consumersMediator;
    }

    @Override
    public void onClick() {
        consumerAdminController.delete(contentProviderConsumer);
        consumersMediator.afterDeleteContentProviderConsumer();
    }
}