package com.axiell.ehub.provider.zinio;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IExpirationDateFactory;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import com.axiell.ehub.provider.record.issue.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ZinioDataAccessor extends AbstractContentProviderDataAccessor {
    public static final String ZINIO_STREAM_FORMAT_ID = "ZINIO.stream";

    @Autowired
    private IFormatFactory formatFactory;
    @Autowired
    private IZinioFacade zinioFacade;
    @Autowired
    private IExpirationDateFactory expirationDateFactory;

    @Override
    public List<Issue> getIssues(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String language = data.getLanguage();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final Format format = formatFactory.create(contentProvider, ZINIO_STREAM_FORMAT_ID, language);
        final List<IssueDTO> issuesDTO = zinioFacade.getIssues(contentProviderConsumer, contentProviderRecordId, language);
        return issuesDTO.stream().map(issueDTO -> issueDTO.toIssue(format)).collect(Collectors.toList());
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String contentProviderIssueId = data.getContentProviderIssueId();
        final String language = data.getLanguage();
        final String loginUrl = zinioFacade.login(contentProviderConsumer, patron, language);
        zinioFacade.checkout(contentProviderConsumer, patron, contentProviderIssueId, language);
        final String contentUrl = zinioFacade.getContentUrl(loginUrl, contentProviderIssueId);
        final ContentProviderLoanMetadata loanMetadata = makeContentProviderLoanMetadata(data);
        final Content content = makeContent(contentUrl);
        return new ContentProviderLoan(loanMetadata, content);
    }

    @Override
    public Content getContent(final CommandData data) {
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String language = data.getLanguage();
        final String loginUrl = zinioFacade.login(contentProviderConsumer, patron, language);
        final String contentUrl = zinioFacade.getContentUrl(loginUrl, loanMetadata.getContentProviderIssueId());
        return makeContent(contentUrl);
    }

    private ContentProviderLoanMetadata makeContentProviderLoanMetadata(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Date expirationDate = expirationDateFactory.createExpirationDate(contentProviderConsumer.getContentProvider());
        return newContentProviderLoanMetadataBuilder(data, expirationDate).build();
    }

    private Content makeContent(final String contentLinkHref) {
        final ContentLinks contentLinks = createContentLinks(contentLinkHref);
        return new Content(contentLinks);
    }
}
