package com.axiell.ehub.provider.ep;

import com.axiell.ehub.checkout.ContentLinkDTO;
import com.axiell.ehub.checkout.SupplementLinkDTO;
import com.google.common.collect.Lists;

import java.util.List;

public class FormatMetadataDTOBuilder {
    public static final String CONTENT_HREF_0 = "contentHref0";
    public static final String CONTENT_HREF_1 = "contentHref1";
    public static final String NAME_0 = "name0";
    public static final String SUPPLEMENT_HREF_0 = "supplementHref0";
    public static final String NAME_1 = "name1";
    public static final String SUPPLEMENT_HREF_01 = "supplementHref1";

    public static FormatMetadataDTO defaultFormatMetadataDTO(final String contentProviderFormatId) {
        List<ContentLinkDTO> contentLinks = Lists.newArrayList(new ContentLinkDTO().href(contentProviderFormatId + "-" + CONTENT_HREF_0),
                new ContentLinkDTO().href(contentProviderFormatId + "-" + CONTENT_HREF_1));
        List<SupplementLinkDTO> supplementLinks = Lists.newArrayList(new SupplementLinkDTO().name(contentProviderFormatId + "-" + NAME_0)
                        .href(contentProviderFormatId + "-" + SUPPLEMENT_HREF_0),
                new SupplementLinkDTO().name(contentProviderFormatId + "-" + NAME_1).href(contentProviderFormatId + "-" + SUPPLEMENT_HREF_01));
        return new FormatMetadataDTO().contentLinks(contentLinks).supplementLinks(supplementLinks);
    }

}
