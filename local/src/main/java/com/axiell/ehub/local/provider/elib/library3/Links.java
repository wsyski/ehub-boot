package com.axiell.ehub.local.provider.elib.library3;

import java.util.ArrayList;
import java.util.List;

class Links extends ArrayList<Link> {

    List<String> getContentUrlsFor(final String formatId) {
        final Link link = getLinkFor(formatId);
        return link == null ? null : link.getContentUrls();
    }

    private Link getLinkFor(final String formatId) {
        for (Link link : this) {
            if (link.isLinkForFormat(formatId))
                return link;
        }
        return null;
    }
}
