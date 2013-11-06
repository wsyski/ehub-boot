package com.axiell.ehub.consumer;

import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.model.IModel;

import com.axiell.ehub.provider.ContentProvider;

class ContentProviderDropDownChoice extends DropDownChoice<ContentProvider> {
    private final ConsumersMediator consumersMediator;
    private final IModel<ContentProviderConsumer> formModel;
    private final EhubConsumerHandler ehubConsumerHandler;
    

    ContentProviderDropDownChoice(final String id, final ConsumersMediator consumersMediator,
	    final IModel<ContentProviderConsumer> formModel, final EhubConsumerHandler ehubConsumerHandler) {
	super(id);
	this.consumersMediator = consumersMediator;
	this.formModel = formModel;
	this.ehubConsumerHandler = ehubConsumerHandler;
	setModel();
	setChoices();
	setChoiceRenderer();
	addOnChangeBehavior();
    }
    
    private void setModel() {
	final ContentProviderModel contentProviderModel = new ContentProviderModel(formModel);
	setModel(contentProviderModel);
    }
    
    private void setChoices() {
	final ContentProvidersModel contentProvidersModel = new ContentProvidersModel(ehubConsumerHandler);
	setChoices(contentProvidersModel);
    }
    
    private void setChoiceRenderer() {
	final ContentProviderChoiceRenderer contentProviderChoiceRenderer = new ContentProviderChoiceRenderer();
	setChoiceRenderer(contentProviderChoiceRenderer);
    }
    
    private void addOnChangeBehavior() {	
	final ContentProviderChoiceOnChangeBehavior contentProviderChoiceOnChangeBehavior = new ContentProviderChoiceOnChangeBehavior(formModel,
		consumersMediator);
	add(contentProviderChoiceOnChangeBehavior);
    }
}
