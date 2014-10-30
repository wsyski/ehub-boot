package com.axiell.ehub.support.request.patron;

import com.axiell.ehub.support.request.DefaultSupportResponse;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.Panel;

class GeneratedPatronIdPanel extends Panel {

    GeneratedPatronIdPanel(String id, GeneratedPatronResponse response) {
        super(id);
        setOutputMarkupId(true);
        addPatronId(response);
    }

    private void addPatronId(GeneratedPatronResponse response) {
        Label patronIdLabel = new Label("patronId", response.getPatronId());
        add(patronIdLabel);
    }
}
