package com.axiell.ehub.provider.record.format;

import org.apache.wicket.markup.html.link.Link;

class InvisibleLink extends Link<Void> {

    InvisibleLink(final String id) {
	super(id);
    }
    
    @Override
    public void onClick() {   
    }
    
    @Override
    public boolean isVisible() {
        return false;
    }
}
