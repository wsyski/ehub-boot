package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.provider.ContentProvider;
import org.springframework.beans.factory.annotation.Autowired;

public class FormatDecorationRepositoryFacade implements IFormatDecorationRepositoryFacade {

    @Autowired
    private IFormatDecorationRepository formatDecorationRepository;

    @Override
    public FormatDecoration find(final ContentProvider contentProvider, final String contentProviderFormatId) {
        FormatDecoration formatDecoration =
                formatDecorationRepository.findByContentProviderIdAndContentProviderFormatId(contentProvider.getId(), contentProviderFormatId);
        if (formatDecoration == null) {
            final ErrorCauseArgument argument0 = new ErrorCauseArgument(ErrorCauseArgument.Type.FORMAT_ID, contentProviderFormatId);
            final ErrorCauseArgument argument1 = new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, contentProvider.getName());
            throw new NotFoundException(ErrorCause.FORMAT_NOT_FOUND, argument0, argument1);
        }
        return formatDecoration;
    }
}
