package com.axiell.ehub.provider.ep.lpp;

import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.authinfo.Patron;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ep.AbstractEpDataAccessor;
import com.axiell.ehub.provider.ep.FormatMetadataDTO;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.axiell.ehub.ErrorCauseArgumentType.CREATE_LOAN_FAILED;

@Component
public class LppEpDataAccessor extends AbstractEpDataAccessor<ILppEpFacade> {

    @Autowired
    private ILppEpFacade epFacade;

    @Override
    protected ILppEpFacade getEpFacade() {
        return epFacade;
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final LppCheckoutDTO lppCheckoutDTO = epFacade.checkout(contentProviderConsumer, patron, contentProviderRecordId);
        final ContentProviderLoanMetadata loanMetadata = makeContentProviderLoanMetadata(data, lppCheckoutDTO);
        final String contentProviderFormatId = data.getContentProviderFormatId();
        final String language = data.getLanguage();
        final FormatMetadataDTO formatMetadataDTO =
                getFormatMetadataDTO(contentProviderFormatId, lppCheckoutDTO, contentProviderConsumer, language);
        final Content contentLinks = makeContent(formatMetadataDTO);
        return new ContentProviderLoan(loanMetadata, contentLinks);
    }

    @Override
    public Content getContent(final CommandData data) {
        final ContentProviderLoanMetadata loanMetadata = data.getContentProviderLoanMetadata();
        final FormatDecoration formatDecoration = data.getFormatDecoration();
        final String contentProviderLoanId = loanMetadata.getId();
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final LppCheckoutDTO lppCheckoutDTO = epFacade.getCheckout(contentProviderConsumer, patron, contentProviderLoanId);
        final String language = data.getLanguage();
        final String contentProviderFormatId = formatDecoration.getContentProviderFormatId();
        final FormatMetadataDTO formatMetadataDTO =
                getFormatMetadataDTO(contentProviderFormatId, lppCheckoutDTO, contentProviderConsumer, language);
        return makeContent(formatMetadataDTO);
    }

    private FormatMetadataDTO getFormatMetadataDTO(final String contentProviderFormatId, final LppCheckoutDTO lppCheckoutDTO,
                                                  final ContentProviderConsumer contentProviderConsumer, final String language) {
        FormatMetadataDTO formatMetadataDTO = lppCheckoutDTO.getFormatMetadatas().get(contentProviderFormatId);
        if (formatMetadataDTO == null) {
            throw getEhubExceptionFactory()
                    .createInternalServerErrorExceptionWithContentProviderNameAndStatus(contentProviderConsumer, CREATE_LOAN_FAILED, language);
        }
        return formatMetadataDTO;
    }
}
