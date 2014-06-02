package com.axiell.ehub.provider;

import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.provider.record.format.FormatDecoration;

public interface IContentFactory {

    IContent create(String contentUrl, FormatDecoration formatDecoration);
}
