/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotFoundExceptionFactory;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.elib.elibu.ConsumedProduct.Content;
import com.axiell.ehub.provider.elib.elibu.Product.AvailableFormat;
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import com.axiell.ehub.provider.record.format.Formats;

/**
 * The ElibU integration.
 */
@Component
public class ElibUDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElibUDataAccessor.class);

    @Autowired(required = true)
    private IElibUFacade elibUFacade;

    @Override
    public Formats getFormats(ContentProviderConsumer contentProviderConsumer, String elibuRecordId, String language) {
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
		    final Format format = makeFormat(language, contentProvider, formatId);
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

    private Format makeFormat(String language, final ContentProvider contentProvider, final String formatId) {
	final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
	final FormatTextBundle formatTextBundle = formatDecoration.getTextBundle(language);
	final String name = formatTextBundle.getName();
	final String description = formatTextBundle.getDescription();
	return new Format(formatId, name, description, null);
    }

    private InternalServerErrorException makeInternalServerErrorException(String message, Status status) {
	final ErrorCauseArgument argument1 = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.ELIBU);
	final ErrorCauseArgument argument2 = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, status.getCode());
	return new InternalServerErrorException(message, ErrorCause.CONTENT_PROVIDER_ERROR, argument1, argument2);
    }

    @Override
    public ContentProviderLoan createLoan(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String pin,
	    final PendingLoan pendingLoan) {	
	final Integer licenseId = consumeLicense(contentProviderConsumer, libraryCard);
	final String recordId = pendingLoan.getContentProviderRecordId();
	final String formatId = pendingLoan.getContentProviderFormatId();

	if (formatId == null)
	    throw new BadRequestException(ErrorCause.MISSING_FORMAT);

	final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
	final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
	final IContent content = consumeProduct(contentProviderConsumer, licenseId, recordId, formatDecoration);
	// TODO:
	final Date expirationDate = new Date();
	final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, expirationDate, recordId, formatDecoration).build();
	return new ContentProviderLoan(metadata, content);
    }

    @Override
    public IContent getContent(final ContentProviderConsumer contentProviderConsumer, final String libraryCard, final String pin,
	    final ContentProviderLoanMetadata contentProviderLoanMetadata) {
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

    private IContent consumeProduct(final ContentProviderConsumer contentProviderConsumer, final Integer licenseId, final String recordId,
	    final FormatDecoration formatDecoration) {
	final Response response = elibUFacade.consumeProduct(contentProviderConsumer, licenseId, recordId);
	final Result result = response.getResult();
	final Status status = result.getStatus();

	if (status.isConsumedProduct()) {
	    return makeContent(recordId, formatDecoration, result);
	}

	throw makeInternalServerErrorException("Could not consume product", status);
    }

    private IContent makeContent(final String recordId, final FormatDecoration formatDecoration, final Result result) {
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

	throw NotFoundExceptionFactory.create(ContentProviderName.ELIBU, recordId, formatId);
    }
}
