package com.axiell.ehub.support;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerAdminController;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.spring.injection.annot.SpringBean;

import java.util.List;

class EhubConsumerChoice extends DropDownChoice<EhubConsumer> {

    @SpringBean(name = "consumerAdminController")
    private IConsumerAdminController consumerAdminController;

    EhubConsumerChoice(final String id) {
        super(id);
        setChoices();
        setChoiceRenderer();
    }

    @Override
    public boolean isRequired() {
        return true;
    }

    @Override
    public boolean isNullValid() {
        return false;
    }

    private void setChoiceRenderer() {
        final EhubConsumerChoiceRenderer renderer = new EhubConsumerChoiceRenderer();
        setChoiceRenderer(renderer);
    }

    private void setChoices() {
        final List<EhubConsumer> ehubConsumers = consumerAdminController.getEhubConsumers();
        setChoices(ehubConsumers);
    }
}
