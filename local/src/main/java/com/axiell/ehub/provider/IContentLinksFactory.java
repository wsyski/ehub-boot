package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.provider.record.format.FormatDecoration;

import java.util.List;

public interface IContentLinksFactory {

    ContentLinks create(List<String> contentUrls, FormatDecoration formatDecoration);
}
