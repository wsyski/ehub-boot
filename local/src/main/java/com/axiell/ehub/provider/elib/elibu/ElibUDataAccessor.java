/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.elibu;

import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotFoundException;
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
import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.resteasy.client.ProxyFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIBU_SERVICE_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIBU_SERVICE_KEY;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.SUBSCRIPTION_ID;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.CONSUME_LICENSE_URL;
import static com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey.PRODUCT_URL;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * The ElibU integration.
 */
@Component
public class ElibUDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElibUDataAccessor.class);
    private static final String ELIBU_LOAN_ID_PATTERN = "{0}|{1}";
    private static final String ELIBU_LOAN_ID_SPLIT_PATTERN = "\\|";
    private static final int ELIBU_LOAN_ID_PARTS = 2;
    private static final int ELIBU_RECORD_ID_INDEX = 1;

    /**
     * @see com.axiell.ehub.provider.IContentProviderDataAccessor#getFormats(com.axiell.ehub.consumer.ContentProviderConsumer,
     * java.lang.String, java.lang.String)
     */
    @Override
    public Formats getFormats(ContentProviderConsumer contentProviderConsumer, String elibuRecordId, String language) {
        final String serviceId = contentProviderConsumer.getProperty(ELIBU_SERVICE_ID);
        final String serviceKey = contentProviderConsumer.getProperty(ELIBU_SERVICE_KEY);
        final String md5ServiceKey = md5Hex(serviceKey.getBytes());
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String productUrl = contentProvider.getProperty(PRODUCT_URL);

        final IElibUResource elibUResource = ProxyFactory.create(IElibUResource.class, productUrl);
        final Response response = elibUResource.getProduct(serviceId, md5ServiceKey, elibuRecordId);
        final Result result = response.getResult();
        final Status status = result.getStatus();

        if (status.hasRetrievedProduct()) {
            final Formats formats = new Formats();
            final Product product = result.getProduct();
            final List<AvailableFormat> availableFormats = product.getFormats();

            for (AvailableFormat availableFormat : availableFormats) {
                final String formatId = availableFormat.getId();
                
                if (formatId == null) {
                    LOGGER.warn("ElibU returned a format without ID - ignoring this one");
                } else {
                    final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
                    final FormatTextBundle formatTextBundle = formatDecoration.getTextBundle(language);
                    final String name = formatTextBundle.getName();
                    final String description = formatTextBundle.getDescription();
                    final Format format = new Format(formatId, name, description, null);
                    formats.addFormat(format);    
                }
            }

            return formats;
        } else {
            final ErrorCauseArgument argument = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, status.getCode());
            throw new InternalServerErrorException("Could not get formats", ErrorCause.CONTENT_PROVIDER_ERROR, argument);
        }
    }

    /**
     * @see com.axiell.ehub.provider.IContentProviderDataAccessor#createLoan(com.axiell.ehub.consumer.ContentProviderConsumer, String, String, com.axiell.ehub.loan.PendingLoan)
     */
    @Override
    public ContentProviderLoan createLoan(final ContentProviderConsumer contentProviderConsumer,
            final String libraryCard,
            final String pin,
            final PendingLoan pendingLoan) {
        final Integer licenseId = consumeLicense(contentProviderConsumer, libraryCard);
        final String elibuRecordId = pendingLoan.getContentProviderRecordId();
        final String formatId = pendingLoan.getContentProviderFormatId();
        
        if (formatId == null) {
            throw new BadRequestException(ErrorCause.MISSING_FORMAT);
        }
        
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
        final IContent content = consumeProduct(contentProviderConsumer, licenseId, elibuRecordId, formatDecoration);
        final String elibuLoanId = createElibULoanId(licenseId, elibuRecordId);
        // TODO: expiration date
        final Date expirationDate = new Date();
        final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata(elibuLoanId, contentProvider, expirationDate, formatDecoration);
        return new ContentProviderLoan(metadata, content);
    }

    /**
     * @see com.axiell.ehub.provider.IContentProviderDataAccessor#getContent(com.axiell.ehub.consumer.ContentProviderConsumer, String, String, com.axiell.ehub.loan.ContentProviderLoanMetadata)
     *
     */
    @Override
    public IContent getContent(final ContentProviderConsumer contentProviderConsumer,
            final String libraryCard,
            final String pin,
            final ContentProviderLoanMetadata contentProviderLoanMetadata) {

        final Integer licenseId = consumeLicense(contentProviderConsumer, libraryCard);
        final String elibuLoanId = contentProviderLoanMetadata.getId();
        final String elibuRecordId = getElibURecordId(elibuLoanId);
        final FormatDecoration formatDecoration = contentProviderLoanMetadata.getFormatDecoration();
        return consumeProduct(contentProviderConsumer, licenseId, elibuRecordId, formatDecoration);
    }

    /**
     * @see com.axiell.ehub.provider.IContentProviderDataAccessor#getContentProviderName()
     */
    @Override
    public ContentProviderName getContentProviderName() {
        return ContentProviderName.ELIBU;
    }
    
    /**
     * Consumes an ElibU license.
     * 
     * @param contentProviderConsumer the {@link ContentProviderConsumer}
     * @param libraryCard the library card
     * @return the ID of the ElibU license
     */
    protected final Integer consumeLicense(final ContentProviderConsumer contentProviderConsumer, final String libraryCard) {
        final String serviceId = contentProviderConsumer.getProperty(ELIBU_SERVICE_ID);
        final String serviceKey = contentProviderConsumer.getProperty(ELIBU_SERVICE_KEY);
        final String md5ServiceKey = md5Hex(serviceKey.getBytes());
        final String subscriptionId = contentProviderConsumer.getProperty(SUBSCRIPTION_ID);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String consumeLicenseUrl = contentProvider.getProperty(CONSUME_LICENSE_URL);

        final IElibUResource elibUResource = ProxyFactory.create(IElibUResource.class, consumeLicenseUrl);
        final Response response = elibUResource.consumeLicense(serviceId, md5ServiceKey, subscriptionId, libraryCard);
        final Result result = response.getResult();
        final Status status = result.getStatus();

        if (status.isConsumedLicense()) {
            final License license = result.getLicense();
            return license.getLicenseId();
        } else {
            final ErrorCauseArgument argument = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, status.getCode());
            throw new InternalServerErrorException("Could not consume license", ErrorCause.CONTENT_PROVIDER_ERROR, argument);
        }
    }

    /**
     * Consumes an ElibU product.
     * 
     * @param contentProviderConsumer the {@link ContentProviderConsumer}
     * @param licenseId the ID of the license
     * @param elibuRecordId the ID of the record at ElibU
     * @param formatDecoration
     * @return an {@link IContent} containing the URL to the content of the consumed product
     */
    protected final IContent consumeProduct(final ContentProviderConsumer contentProviderConsumer,
            final Integer licenseId,
            final String elibuRecordId,
            final FormatDecoration formatDecoration) {
        final String serviceId = contentProviderConsumer.getProperty(ELIBU_SERVICE_ID);
        final String serviceKey = contentProviderConsumer.getProperty(ELIBU_SERVICE_KEY);
        final String md5ServiceKey = DigestUtils.md5Hex(serviceKey.getBytes());
        final String checksum = new StringBuilder(serviceId).append(serviceKey).append(licenseId).append(elibuRecordId).toString();
        final String md5Checksum = DigestUtils.md5Hex(checksum.getBytes());

        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String consumeProductUrl = contentProvider.getProperty(PRODUCT_URL);

        final IElibUResource elibUResource = ProxyFactory.create(IElibUResource.class, consumeProductUrl);
        final Response response = elibUResource.consumeProduct(serviceId, md5ServiceKey, elibuRecordId, licenseId, md5Checksum);
        final Result result = response.getResult();
        final Status status = result.getStatus();

        if (status.isConsumedProduct()) {
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
            
            final ErrorCauseArgument argument1 = new ErrorCauseArgument(Type.CONTENT_PROVIDER_RECORD_ID, elibuRecordId);
            final ErrorCauseArgument argument2 = new ErrorCauseArgument(Type.FORMAT_ID, formatId);
            final ErrorCauseArgument argument3 = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.ELIBU);
            throw new NotFoundException(ErrorCause.CONTENT_PROVIDER_RECORD_NOT_FOUND, argument1, argument2, argument3);
        } else {
            final ErrorCauseArgument argument1 = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.ELIBU);
            final ErrorCauseArgument argument2 = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, status.getCode());
            throw new InternalServerErrorException("Could not consume product", ErrorCause.CONTENT_PROVIDER_ERROR, argument1,argument2);
        }
    }

    /**
     * Creates the ElibU loan ID according to the following pattern: <code>licenseId|elibuRecordId</code>.
     * 
     * @param licenseId the ID of the license
     * @param elibuRecordId the ID of the record at ElibU
     * @return an ElibU loan ID
     */
    protected final String createElibULoanId(final Integer licenseId, final String elibuRecordId) {
        return MessageFormat.format(ELIBU_LOAN_ID_PATTERN, licenseId, elibuRecordId);
    }

    /**
     * Extracts the ElibU record ID from the provided ElibU loan ID which is assumed to be in the following pattern:
     * <code>licenseId|elibuRecordId|formatId</code>..
     * 
     * @param elibuLoanId an ElibU loan ID
     * @return an ElibU record ID
     */
    protected final String getElibURecordId(final String elibuLoanId) {
        final String[] parts = elibuLoanId.split(ELIBU_LOAN_ID_SPLIT_PATTERN);
        if (parts != null && parts.length == ELIBU_LOAN_ID_PARTS) {
            return parts[ELIBU_RECORD_ID_INDEX];
        } else {
            throw new InternalServerErrorException("The provided ElibU loan ID '" + elibuLoanId + "' is in the wrong format, it should be in the format '"
                    + ELIBU_LOAN_ID_PATTERN + "'");
        }
    }

}
