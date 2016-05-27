package com.axiell.ehub.provider.f1;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.forlagett.api.F1ServiceSoap;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.*;

@Component
class F1Facade implements IF1Facade {
    private static final String CONTINUE = "false";

    @Autowired
    private IF1ServiceSoapFactory f1ServiceSoapFactory;

    @Autowired
    private IF1SoapServiceParameterHelper f1SoapServiceParameterHelper;

    @Override
    public GetFormatResponse getFormats(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String language = data.getLanguage();
        final F1ServiceSoap f1ServiceSoap = f1ServiceSoapFactory.getInstance(contentProviderConsumer);
        final int mediaId = f1SoapServiceParameterHelper.getMediaId(contentProviderConsumer, contentProviderRecordId, language);
        final String typeId = f1ServiceSoap.getFormats(mediaId);
        return new GetFormatResponse(typeId);
    }

    @Override
    public CreateLoanResponse createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String language = data.getLanguage();
        final Patron patron = data.getPatron();
        final String libraryCard = patron.getLibraryCard();
        final int mediaId = f1SoapServiceParameterHelper.getMediaId(contentProviderConsumer, contentProviderRecordId, language);
        final String username = contentProviderConsumer.getProperty(F1_USERNAME);
        final String password = contentProviderConsumer.getProperty(F1_PASSWORD);
        final int regionId = f1SoapServiceParameterHelper.getRegionId(contentProviderConsumer, language);
        final F1ServiceSoap f1ServiceSoap = f1ServiceSoapFactory.getInstance(contentProviderConsumer);
        final String loanId = f1ServiceSoap.createLoan(mediaId, libraryCard, username, password, regionId, CONTINUE);
        return new CreateLoanResponse(loanId);
    }

    @Override
    public GetLoanContentResponse getLoanContent(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final FormatDecoration formatDecoration = data.getFormatDecoration();
        final String contentProviderRecordId = loanMetadata.getRecordId();
        final String formatId = formatDecoration.getContentProviderFormatId();
        final String language = data.getLanguage();
        final Patron patron = data.getPatron();
        final String libraryCard = patron.getLibraryCard();
        final int mediaId = f1SoapServiceParameterHelper.getMediaId(contentProviderConsumer, contentProviderRecordId, language);
        final String username = contentProviderConsumer.getProperty(F1_USERNAME);
        final String password = contentProviderConsumer.getProperty(F1_PASSWORD);
        final int regionId = f1SoapServiceParameterHelper.getRegionId(contentProviderConsumer, language);
        final int typeId = f1SoapServiceParameterHelper.getTypeId(contentProviderConsumer, formatId, language);
        final String contentProviderLoanId = loanMetadata.getId();
        final int contentProviderLoanIdValue = f1SoapServiceParameterHelper.getLoanId(contentProviderConsumer, contentProviderLoanId, language);
        final F1ServiceSoap f1ServiceSoap = f1ServiceSoapFactory.getInstance(contentProviderConsumer);
        final String content = f1ServiceSoap.getLoanContent(mediaId, username, password, regionId, libraryCard, typeId, CONTINUE, contentProviderLoanIdValue);
        return new GetLoanContentResponse(content);
    }
}
