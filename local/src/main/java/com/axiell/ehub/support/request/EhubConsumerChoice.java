package com.axiell.ehub.support.request;

import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerAdminController;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
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

    private static class EhubConsumerChoiceRenderer extends ChoiceRenderer<EhubConsumer> {

        @Override
        public String getIdValue(final EhubConsumer ehubConsumer, final int index) {
            return ehubConsumer.getId().toString();
        }

        @Override
        public Object getDisplayValue(final EhubConsumer ehubConsumer) {
            return ehubConsumer.getDescription();
        }
    }
}
