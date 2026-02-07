package com.axiell.ehub.provider.record.format;

import com.axiell.ehub.provider.ContentProvider;

public interface IFormatFactory {

    Format create(ContentProvider contentProvider, String formatId, String language);

    Format create(FormatDecoration formatDecoration, String language);
}
