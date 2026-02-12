package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.ErrorCause;
import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.common.NotFoundException;
import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FormatDecorationRepositoryFacade implements IFormatDecorationRepositoryFacade {

    @Autowired
    private IFormatDecorationRepository formatDecorationRepository;

    @Override
    public FormatDecoration find(final ContentProvider contentProvider, final String contentProviderFormatId) {
        FormatDecoration formatDecoration =
                formatDecorationRepository.findOneByContentProviderIdAndContentProviderFormatId(contentProvider.getId(), contentProviderFormatId);
        if (formatDecoration == null) {
            final ErrorCauseArgument argument0 = new ErrorCauseArgument(ErrorCauseArgument.Type.FORMAT_ID, contentProviderFormatId);
            final ErrorCauseArgument argument1 = new ErrorCauseArgument(ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME, contentProvider.getName());
            throw new NotFoundException(ErrorCause.FORMAT_NOT_FOUND, argument0, argument1);
        }
        return formatDecoration;
    }
}
