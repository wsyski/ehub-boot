package com.axiell.ehub.provider.record.format;

public interface IFormatDecorationRepositoryFacade {

    FormatDecoration find(long contentProviderId, String contentProviderFormatId);

    FormatDecoration find(long id);
}