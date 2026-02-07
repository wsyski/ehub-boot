package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.provider.ContentProvider;

public interface IFormatDecorationRepositoryFacade {

    FormatDecoration find(ContentProvider contentProvider, String contentProviderFormatId);
}