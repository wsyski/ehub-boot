package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.provider.ContentProvider;
import org.springframework.stereotype.Component;

@Component
class FormatFactory implements IFormatFactory {

    @Override
    public Format create(final ContentProvider contentProvider, final String formatId, final String language) {
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
        final FormatTextBundle textBundle = formatDecoration == null ? null : formatDecoration.getTextBundle(language);
        final String name = textBundle == null ? formatId : textBundle.getName();
        final String description = textBundle == null ? formatId : textBundle.getDescription();
        return new Format(formatId, name, description, null);
    }
}
