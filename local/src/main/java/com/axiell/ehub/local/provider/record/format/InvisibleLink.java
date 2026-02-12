package com.axiell.ehub.local.provider.record.format;

import org.apache.wicket.markup.html.link.Link;

class InvisibleLink extends Link<Void> {

    InvisibleLink(final String id) {
        super(id);
    }

    @Override
    public void onClick() {
        // Do nothing because this method will never be invoked
    }

    @Override
    public boolean isVisible() {
        return false;
    }
}
