package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.provider.record.format.FormatDecoration;

public interface IContentFactory {

    ContentLink create(String contentUrl, FormatDecoration formatDecoration);
}
