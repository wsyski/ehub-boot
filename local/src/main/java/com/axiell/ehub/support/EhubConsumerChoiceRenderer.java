package com.axiell.ehub.support;

import com.axiell.ehub.consumer.EhubConsumer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;

class EhubConsumerChoiceRenderer extends ChoiceRenderer<EhubConsumer> {

    @Override
    public String getIdValue(EhubConsumer ehubConsumer, int index) {
        return ehubConsumer.getId().toString();
    }

    @Override
    public Object getDisplayValue(EhubConsumer ehubConsumer) {
        return ehubConsumer.getDescription();
    }
}
