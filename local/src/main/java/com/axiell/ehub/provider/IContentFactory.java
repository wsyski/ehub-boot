package com.axiell.ehub.provider;

import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.provider.record.format.FormatDecoration;

import java.util.List;

public interface IContentFactory {

    ContentLinks create(List<String> contentUrls, FormatDecoration formatDecoration);
}
