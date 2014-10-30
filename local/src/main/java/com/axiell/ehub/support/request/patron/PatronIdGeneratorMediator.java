package com.axiell.ehub.support.request.patron;

import com.axiell.ehub.support.request.AbstractRequestsGeneratorMediator;
import com.axiell.ehub.support.request.DefaultSupportResponse;
import org.apache.wicket.markup.html.panel.Panel;

class PatronIdGeneratorMediator extends AbstractRequestsGeneratorMediator<GeneratedPatronResponse> {

    @Override
    protected Panel makeResponsePanel(String panelId, GeneratedPatronResponse response) {
        return new GeneratedPatronIdPanel(panelId, response);
    }
}
