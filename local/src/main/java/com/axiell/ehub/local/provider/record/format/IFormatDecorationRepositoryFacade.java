package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;

public interface IFormatDecorationRepositoryFacade {

    FormatDecoration find(ContentProvider contentProvider, String contentProviderFormatId);
}
