package com.axiell.ehub.provider.ocd;

class ContentProviderRecordIdMediaMatcher implements IMediaMatcher {
    private final String contentProviderRecordId;

    ContentProviderRecordIdMediaMatcher(String contentProviderRecordId) {
        this.contentProviderRecordId = contentProviderRecordId;
    }

    @Override
    public boolean matches(MediaDTO mediaDTO) {
        final String titleId = mediaDTO.getTitleId();
        return contentProviderRecordId.equals(titleId);
    }
}
