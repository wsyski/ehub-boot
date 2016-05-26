package com.axiell.ehub.provider.record.format;

import org.springframework.beans.factory.annotation.Autowired;

public class FormatDecorationRepositoryFacade implements IFormatDecorationRepositoryFacade {

    @Autowired
    private IFormatDecorationRepository formatDecorationRepository;

    @Override
    public FormatDecoration find(final long contentProviderId, final String contentProviderFormatId) {
        return formatDecorationRepository.findByContentProviderIdAndContentProviderFormatId(contentProviderId, contentProviderFormatId);
    }

    @Override
    public FormatDecoration find(long id) {
        return formatDecorationRepository.findOne(id);
    }
}
