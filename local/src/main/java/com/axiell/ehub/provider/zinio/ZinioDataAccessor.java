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

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ZinioDataAccessor extends AbstractContentProviderDataAccessor {
    public static final String ZINIO_FORMAT_0_ID = "ZINIO.stream";

    @Autowired
    private IFormatFactory formatFactory;
    @Autowired
    private IZinioFacade zinioFacade;
    @Autowired
    private IExpirationDateFactory expirationDateFactory;

    @Override
    public List<Issue> getIssues(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String language = data.getLanguage();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        return getIssues(contentProviderConsumer, contentProviderRecordId, language);
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String issueId = data.getIssueId();
        final String language = data.getLanguage();
        final String loginUrl = zinioFacade.login(contentProviderConsumer, patron, language);
        zinioFacade.checkout(contentProviderConsumer, patron, issueId, language);
        final String contentUrl = zinioFacade.getContentUrl(loginUrl, issueId);
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
        final String contentUrl = zinioFacade.getContentUrl(loginUrl, loanMetadata.getIssueId());
        return makeContent(contentUrl);
    }

    private ContentProviderLoanMetadata makeContentProviderLoanMetadata(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String issueId = data.getIssueId();
        final String language = data.getLanguage();
        final Date expirationDate = expirationDateFactory.createExpirationDate(contentProviderConsumer.getContentProvider());
        final String issueTitle = getIssueTitle(contentProviderConsumer, contentProviderRecordId, issueId, language);
        return newContentProviderLoanMetadataBuilder(data, expirationDate).issueId(issueId).issueTitle(issueTitle).build();
    }

    private Content makeContent(final String contentLinkHref) {
        final ContentLinks contentLinks = createContentLinks(contentLinkHref);
        return new Content(contentLinks);
    }

    private List<Issue> getIssues(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId, final String language) {
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final Format format = formatFactory.create(contentProvider, ZINIO_FORMAT_0_ID, language);
        final List<IssueDTO> issuesDTO = zinioFacade.getIssues(contentProviderConsumer, contentProviderRecordId, language);
        return issuesDTO.stream().map(issueDTO -> issueDTO.toIssue(format)).collect(Collectors.toList());
    }

    private String getIssueTitle(final ContentProviderConsumer contentProviderConsumer, final String contentProviderRecordId, final String issueId,
                                 final String language) {
        if (issueId == null) {
            return null;
        }
        List<Issue> issues = getIssues(contentProviderConsumer, contentProviderRecordId, language);
        for (Issue issue : issues) {
            if (issueId.equals(issue.getId())) {
                return issue.getTitle();
            }
        }
        return null;
    }
}
