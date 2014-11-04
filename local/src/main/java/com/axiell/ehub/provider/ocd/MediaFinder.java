package com.axiell.ehub.provider.ocd;

import java.util.List;

class MediaFinder {

    private MediaFinder() {
    }

    static MediaDTO find(IMediaMatcher matcher, List<MediaDTO> allMedia) throws MediaNotFoundException {
        for (MediaDTO media : allMedia) {
            if (matcher.matches(media))
                return media;
        }
        throw new MediaNotFoundException();
    }
}
