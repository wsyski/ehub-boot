package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.provider.ContentProvider;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
class FormatFactory implements IFormatFactory {

    @Override
    public Format create(final ContentProvider contentProvider, final String formatId, final String language) {
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
        final FormatTextBundle textBundle = formatDecoration == null ? null : formatDecoration.getTextBundle(language);
        return makeFormat(formatId, formatDecoration, textBundle);
    }

    @Override
    public Format create(final FormatDecoration formatDecoration, final String language) {
        final FormatTextBundle textBundle = formatDecoration.getTextBundle(language);
        final String formatId = formatDecoration.getContentProviderFormatId();
        return makeFormat(formatId, formatDecoration, textBundle);
    }

    private Format makeFormat(final String formatId, final FormatDecoration formatDecoration, final FormatTextBundle textBundle) {
        final String name = textBundle == null ? formatId : textBundle.getName();
        final String description = textBundle == null ? formatId : textBundle.getDescription();
        final ContentDisposition contentDisposition = formatDecoration == null ? null : formatDecoration.getContentDisposition();
        final boolean isLocked = formatDecoration != null && formatDecoration.isLocked();
        final Set<String> platforms = formatDecoration == null ? new HashSet<>() : formatDecoration.getPlatformNames();
        return new Format(formatId, name, description, contentDisposition, platforms, isLocked);
    }
}
