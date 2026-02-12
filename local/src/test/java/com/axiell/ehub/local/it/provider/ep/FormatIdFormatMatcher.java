package com.axiell.ehub.local.it.provider.ep;

import com.axiell.ehub.common.util.IMatcher;
import com.axiell.ehub.local.provider.ep.FormatDTO;

public class FormatIdFormatMatcher implements IMatcher<FormatDTO> {
    private final String contentProviderFormatId;

    public FormatIdFormatMatcher(final String contentProviderFormatId) {
        this.contentProviderFormatId = contentProviderFormatId;
    }

    @Override
    public boolean matches(final FormatDTO format) {
        return contentProviderFormatId.equals(format.getId());
    }
}
