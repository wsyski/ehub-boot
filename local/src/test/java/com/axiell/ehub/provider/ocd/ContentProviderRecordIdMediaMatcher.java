package com.axiell.ehub.provider.ocd;

import com.axiell.ehub.util.IMatcher;

class ContentProviderRecordIdMediaMatcher implements IMatcher<MediaDTO> {
    private final String contentProviderRecordId;

    ContentProviderRecordIdMediaMatcher(String contentProviderRecordId) {
        this.contentProviderRecordId = contentProviderRecordId;
    }

    @Override
    public boolean matches(MediaDTO mediaDTO) {
        final String isbn = mediaDTO.getIsbn();
        return contentProviderRecordId.equals(isbn);
    }
}
