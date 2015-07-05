/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import com.axiell.ehub.*;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;

import com.axiell.ehub.provider.elib.elibu.ConsumedProduct.Content;
import com.axiell.ehub.provider.elib.elibu.Product.AvailableFormat;
import com.axiell.ehub.provider.record.format.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

/**
 * The ElibU integration.
 */
@Component
public class ElibUDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElibUDataAccessor.class);

    @Autowired(required = true)
    private IElibUFacade elibUFacade;

    @Autowired
    private IFormatFactory formatFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String elibuRecordId = data.getContentProviderRecordId();
        final String language = data.getLanguage();
        final Response response = elibUFacade.getProduct(contentProviderConsumer, elibuRecordId);
        final Result result = response.getResult();
        final Status status = result.getStatus();

        if (status.hasRetrievedProduct()) {
            final Formats formats = new Formats();
            final List<AvailableFormat> availableFormats = getAvailableFormats(result);
            final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();

            for (AvailableFormat availableFormat : availableFormats) {
                final String formatId = availableFormat.getId();

                if (formatId == null) {
                    LOGGER.warn("ElibU returned a format without ID - ignoring this one");
                } else {
                    final Format format = formatFactory.create(contentProvider, formatId, language);
                    formats.addFormat(format);
                }
            }

            return formats;
        }

        throw makeInternalServerErrorException("Could not get formats", status);
    }

    private List<AvailableFormat> getAvailableFormats(final Result result) {
        final Product product = result.getProduct();
        return product.getFormats();
    }

    private InternalServerErrorException makeInternalServerErrorException(String message, Status status) {
        final ErrorCauseArgument argument1 = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProvider.CONTENT_PROVIDER_ELIBU);
        final ErrorCauseArgument argument2 = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, status.getCode());
        return new InternalServerErrorException(message, ErrorCause.CONTENT_PROVIDER_ERROR, argument1, argument2);
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String libraryCard = patron.getLibraryCard();
        final Integer licenseId = consumeLicense(contentProviderConsumer, libraryCard);
        final String recordId = data.getContentProviderRecordId();
        final String formatId = data.getContentProviderFormatId();

        if (formatId == null)
            throw new BadRequestException(ErrorCause.MISSING_FORMAT);

        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
        final ContentLink contentLink = consumeProduct(contentProviderConsumer, licenseId, recordId, formatDecoration);
        // TODO:
        final Date expirationDate = new Date();
        final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, recordId, formatDecoration).build();
        return new ContentProviderLoan(metadata, contentLink);
    }

    @Override
    public ContentLink getContent(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String libraryCard = patron.getLibraryCard();
        final ContentProviderLoanMetadata contentProviderLoanMetadata = data.getContentProviderLoanMetadata();
        final Integer licenseId = consumeLicense(contentProviderConsumer, libraryCard);
        final String recordId = contentProviderLoanMetadata.getRecordId();
        final FormatDecoration formatDecoration = contentProviderLoanMetadata.getFormatDecoration();
        return consumeProduct(contentProviderConsumer, licenseId, recordId, formatDecoration);
    }

    private Integer consumeLicense(final ContentProviderConsumer contentProviderConsumer, final String libraryCard) {
        final Response response = elibUFacade.consumeLicense(contentProviderConsumer, libraryCard);
        final Result result = response.getResult();
        final Status status = result.getStatus();

        if (status.isConsumedLicense()) {
            return getLicenseId(result);
        }

        throw makeInternalServerErrorException("Could not consume license", status);
    }

    private Integer getLicenseId(final Result result) {
        final License license = result.getLicense();
        return license.getLicenseId();
    }

    private ContentLink consumeProduct(final ContentProviderConsumer contentProviderConsumer, final Integer licenseId, final String recordId,
                                    final FormatDecoration formatDecoration) {
        final Response response = elibUFacade.consumeProduct(contentProviderConsumer, licenseId, recordId);
        final Result result = response.getResult();
        final Status status = result.getStatus();

        if (status.isConsumedProduct()) {
            return makeContent(recordId, formatDecoration, result);
        }

        throw makeInternalServerErrorException("Could not consume product", status);
    }

    private ContentLink makeContent(final String recordId, final FormatDecoration formatDecoration, final Result result) {
        final ConsumedProduct consumedProduct = result.getConsumedProduct();
        final List<ConsumedProduct.Format> formats = consumedProduct.getFormats();
        final String formatId = formatDecoration.getContentProviderFormatId();

        for (ConsumedProduct.Format format : formats) {
            if (format.isSameFormat(formatId)) {
                final Content content = format.getContent();
                final String contentUrl = content.getUrl();
                return createContent(contentUrl, formatDecoration);
            }
        }

        throw NotFoundExceptionFactory.create(ContentProvider.CONTENT_PROVIDER_ELIBU, recordId, formatId);
    }
}
