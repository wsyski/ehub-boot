package com.axiell.ehub.provider.ep;

import com.axiell.ehub.util.IMatcher;

class FormatIdFormatMatcher implements IMatcher<FormatDTO> {
    private final String contentProviderFormatId;

    FormatIdFormatMatcher(final String contentProviderFormatId) {
        this.contentProviderFormatId = contentProviderFormatId;
    }

    @Override
    public boolean matches(final FormatDTO format) {
        return contentProviderFormatId.equals(format.getId());
    }
}
