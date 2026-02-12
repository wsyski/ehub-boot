package com.axiell.ehub.common.checkout;

import com.axiell.ehub.common.controller.external.v5_0.checkout.dto.ContentLinkDTO;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.io.Serializable;

public class ContentLink implements Serializable {
    private final ContentLinkDTO contentLinkDTO;

    public ContentLink(String href) {
        contentLinkDTO = new ContentLinkDTO().href(href);
    }

    public ContentLink(ContentLinkDTO contentLinkDTO) {
        this.contentLinkDTO = contentLinkDTO;
    }

    public String href() {
        return contentLinkDTO.getHref();
    }

    public ContentLinkDTO toDTO() {
        return contentLinkDTO;
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
}
