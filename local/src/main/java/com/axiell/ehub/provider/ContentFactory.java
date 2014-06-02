package com.axiell.ehub.provider;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.loan.DownloadableContent;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.StreamingContent;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.springframework.stereotype.Component;

@Component
public class ContentFactory implements IContentFactory {

    @Override
    public IContent create(final String contentUrl, final FormatDecoration formatDecoration) {
        final FormatDecoration.ContentDisposition contentDisposition = formatDecoration.getContentDisposition();

        switch (contentDisposition) {
            case DOWNLOADABLE:
                return new DownloadableContent(contentUrl);
            case STREAMING:
                final int width = formatDecoration.getPlayerWidth();
                final int height = formatDecoration.getPlayerHeight();
                return new StreamingContent(contentUrl, width, height);
            default:
                throw new NotImplementedException("Create content with content disposition '" + contentDisposition + "' has not been implemented");
        }
    }
}

