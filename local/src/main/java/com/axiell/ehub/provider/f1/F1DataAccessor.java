package com.axiell.ehub.provider.f1;

import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.IExpirationDateFactory;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.IFormatFactory;
import com.axiell.ehub.provider.record.issue.Issue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static com.axiell.ehub.ErrorCauseArgumentType.CREATE_LOAN_FAILED;
import static com.axiell.ehub.ErrorCauseArgumentType.MISSING_CONTENT_IN_LOAN;

@Component
public class F1DataAccessor extends AbstractContentProviderDataAccessor {

    @Autowired
    private IF1Facade f1Facade;

    @Autowired
    private IFormatFactory formatFactory;

    @Autowired
    private IExpirationDateFactory expirationDateFactory;

    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Override
    public List<Issue> getIssues(final CommandData data) {
        final GetFormatResponse getFormatResponse = f1Facade.getFormats(data);
        final List<Format> formats = new ArrayList<>();

        if (getFormatResponse.isValidFormat()) {
            final Format format = makeFormat(data, getFormatResponse);
            formats.add(format);
        }
        return Collections.singletonList(new Issue(formats));
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final CreateLoanResponse createLoanResponse = f1Facade.createLoan(data);

        if (createLoanResponse.isValidLoan())
            return makeContentProviderLoan(data, createLoanResponse);
        throw makeCreateLoanFailedException(data, createLoanResponse);
    }

    @Override
    public Content getContent(final CommandData data) {
        final GetLoanContentResponse getLoanContentResponse = f1Facade.getLoanContent(data);

        if (getLoanContentResponse.isValidContent())
            return makeContentForActiveLoan(data, getLoanContentResponse);
        throw makeMissingContentException(data, getLoanContentResponse);
    }

    private Format makeFormat(CommandData data, GetFormatResponse getFormatResponse) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String language = data.getLanguage();
        final String formatId = getFormatResponse.getValue();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        return formatFactory.create(contentProvider, formatId, language);
    }

    private ContentProviderLoan makeContentProviderLoan(final CommandData data, final CreateLoanResponse createLoanResponse) {
        ContentProviderLoanMetadata loanMetadata = populateContentProviderLoanMetadataInCommandData(data, createLoanResponse);
        final GetLoanContentResponse getLoanContentResponse = f1Facade.getLoanContent(data);

        if (getLoanContentResponse.isValidContent())
            return makeContentForNewLoan(loanMetadata, getLoanContentResponse);
        throw makeMissingContentException(data, getLoanContentResponse);
    }

    private ContentProviderLoanMetadata populateContentProviderLoanMetadataInCommandData(CommandData data, CreateLoanResponse createLoanResponse) {
        final String loanId = createLoanResponse.getValue();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final Date expirationDate = expirationDateFactory.createExpirationDate(contentProvider);

        final ContentProviderLoanMetadata metadata = newContentProviderLoanMetadataBuilder(data, expirationDate).contentProviderLoanId(loanId).build();
        data.setContentProviderLoanMetadata(metadata);
        return metadata;
    }

    private Content makeContentForActiveLoan(CommandData data, GetLoanContentResponse getLoanContentResponse) {
        final String contentUrl = getLoanContentResponse.getValue();
        final ContentLinks contentLinks = createContentLinks(contentUrl);
        return new Content(contentLinks);
    }

    private ContentProviderLoan makeContentForNewLoan(final ContentProviderLoanMetadata loanMetadata, final GetLoanContentResponse getLoanContentResponse) {
        final String contentUrl = getLoanContentResponse.getValue();
        final ContentLinks contentLinks = createContentLinks(contentUrl);
        final Content content = new Content(contentLinks);
        return new ContentProviderLoan(loanMetadata, content);
    }

    private InternalServerErrorException makeCreateLoanFailedException(final CommandData data, final CreateLoanResponse createLoanResponse) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String language = data.getLanguage();
        final String responseValue = createLoanResponse.getValue();
        return ehubExceptionFactory
                .createInternalServerErrorExceptionWithContentProviderNameAndStatus(responseValue, contentProviderConsumer, CREATE_LOAN_FAILED, language);
    }

    private InternalServerErrorException makeMissingContentException(final CommandData data, final GetLoanContentResponse getLoanContentResponse) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String language = data.getLanguage();
        final String responseValue = getLoanContentResponse.getValue();
        return ehubExceptionFactory
                .createInternalServerErrorExceptionWithContentProviderNameAndStatus(responseValue, contentProviderConsumer, MISSING_CONTENT_IN_LOAN, language);
    }
}
