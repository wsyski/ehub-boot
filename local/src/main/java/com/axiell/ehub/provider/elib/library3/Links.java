package com.axiell.ehub.provider.elib.library3;

import java.util.ArrayList;

class Links extends ArrayList<Link> {

    String getContentUrlFor(final String formatId) {
        final Link link = getLinkFor(formatId);
        return link == null ? null : link.getFirstContentUrl();
    }

    private Link getLinkFor(final String formatId) {
        for (Link link : this) {
            if (link.isLinkForFormat(formatId))
                return link;
        }
        return null;
    }
}
