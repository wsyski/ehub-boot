package com.axiell.ehub.provider.borrowbox;

import com.axiell.ehub.util.IMatcher;

class FormatIdFormatMatcher implements IMatcher<FormatsDTO.FormatDTO> {
    private final String contentProviderFormatId;

    FormatIdFormatMatcher(final String contentProviderFormatId) {
        this.contentProviderFormatId = contentProviderFormatId;
    }

    @Override
    public boolean matches(final FormatsDTO.FormatDTO format) {
        return contentProviderFormatId.equals(format.getFormatId());
    }
}
