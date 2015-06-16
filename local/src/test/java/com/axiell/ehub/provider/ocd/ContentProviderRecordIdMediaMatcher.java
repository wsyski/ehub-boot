package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.util.IMatcher;

class ContentProviderRecordIdMediaMatcher implements IMatcher<MediaDTO> {
    private final String contentProviderRecordId;

    ContentProviderRecordIdMediaMatcher(String contentProviderRecordId) {
        this.contentProviderRecordId = contentProviderRecordId;
    }

    @Override
    public boolean matches(final MediaDTO mediaDTO) {
        return contentProviderRecordId.equals(mediaDTO.getIsbn());
    }
}
