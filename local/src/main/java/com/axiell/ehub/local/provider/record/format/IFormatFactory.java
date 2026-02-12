package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.provider.ContentProvider;
import com.axiell.ehub.common.provider.record.format.Format;
import com.axiell.ehub.common.provider.record.format.FormatDecoration;

public interface IFormatFactory {

    Format create(ContentProvider contentProvider, String formatId, String language);

    Format create(FormatDecoration formatDecoration, String language);
}
