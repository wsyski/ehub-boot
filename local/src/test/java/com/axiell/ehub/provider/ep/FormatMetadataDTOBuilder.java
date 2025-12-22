package com.axiell.ehub.provider.ep;

import com.axiell.ehub.checkout.ContentLinkBuilder;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.ContentLinkDTO;
import com.axiell.ehub.controller.external.v5_0.checkout.dto.SupplementLinkDTO;
import com.google.common.collect.Lists;

import java.util.List;

public class FormatMetadataDTOBuilder {
    public static final String CONTENT_HREF_0 = ContentLinkBuilder.HREF;
    public static final String NAME_0 = "name0";
    public static final String SUPPLEMENT_HREF_0 = "supplementHref0";
    public static final String NAME_1 = "name1";
    public static final String SUPPLEMENT_HREF_01 = "supplementHref1";

    public static FormatMetadataDTO defaultFormatMetadataDTO() {
        List<ContentLinkDTO> contentLinks = Lists.newArrayList(new ContentLinkDTO().href(CONTENT_HREF_0));
        List<SupplementLinkDTO> supplementLinks = Lists.newArrayList(new SupplementLinkDTO().name(NAME_0).href(SUPPLEMENT_HREF_0),
                new SupplementLinkDTO().name(NAME_1).href(SUPPLEMENT_HREF_01));
        return new FormatMetadataDTO().contentLinks(contentLinks).supplementLinks(supplementLinks);
    }

}
