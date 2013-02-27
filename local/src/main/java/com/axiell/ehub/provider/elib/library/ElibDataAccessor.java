/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.library;

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
import com.axiell.ehub.provider.record.format.Format;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import com.axiell.ehub.provider.record.format.Formats;
import org.apache.commons.codec.digest.DigestUtils;
import org.jboss.resteasy.client.ProxyFactory;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import se.elib.library.orderlist.Response.Data.Orderitem;
import se.elib.library.orderlist.Response.Data.Orderitem.Book;
import se.elib.library.product.Response.Data.Product;

import javax.xml.bind.JAXBElement;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_ID;
import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_RETAILER_KEY;
import static com.axiell.ehub.provider.elib.library.ElibUtils.generateIsbn13or10;
import static org.apache.commons.codec.digest.DigestUtils.md5Hex;

/**
 * The Elib integration.
 */
@Component
public class ElibDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElibDataAccessor.class);
    private static final String ENGLISH = Locale.ENGLISH.getLanguage();
    private static final String CREATE_LOAN_MOBI_POCKET_ID = "X";
    private static final int ELIB_STATUS_CODE_OK = 101;
    private static final DateTimeFormatter ELIB_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * @see com.axiell.ehub.provider.IContentProviderDataAccessor#getFormats(com.axiell.ehub.consumer.ContentProviderConsumer,
     *      java.lang.String, java.lang.String)
     */
    @Override
    public Formats getFormats(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId, String language) {
        final String retailerId = contentProviderConsumer.getProperty(ELIB_RETAILER_ID);
        final String retailerKeyCode = contentProviderConsumer.getProperty(ELIB_RETAILER_KEY);
        final String md5RetailerKeyCode = md5Hex(retailerKeyCode.getBytes());
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String productUrl = contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.PRODUCT_URL);

        final IElibProductResource elibProductResource = ProxyFactory.create(IElibProductResource.class, productUrl);
        final se.elib.library.product.Response elibResponse = elibProductResource.getProduct(retailerId, md5RetailerKeyCode, contentProviderRecordId, language);
        final se.elib.library.product.Response.Status elibStatus = elibResponse.getStatus();
        String errorMessage = "Could not get formats";
        if (elibStatus.getCode() == ELIB_STATUS_CODE_OK) {
            final se.elib.library.product.Response.Data data = elibResponse.getData();
            if (data != null) {
                final Product product = data.getProduct();
                final Product.Formats elibFormats = product.getFormats();
                final Formats formats = new Formats();

                for (Product.Formats.Format elibFormat : elibFormats.getFormat()) {
                    final String formatId = String.valueOf(elibFormat.getFormatId());
                /*
                 * First try to get the name and description from the eHUB database. If there exist no FormatTextBundle
                 * for this format in this language use the Elib translations instead.
                 */
                    final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
                    final FormatTextBundle textBundle = formatDecoration == null ? null : formatDecoration.getTextBundle(language);

                    final String name;
                    final String description;

                    if (textBundle == null) {
                        name = elibFormat.getName();
                        description = elibFormat.getDescription();
                    } else {
                        name = textBundle.getName() == null ? elibFormat.getName() : textBundle.getName();
                        description = textBundle.getDescription() == null ? elibFormat.getDescription() : textBundle.getDescription();
                    }

                    final String iconUrl = elibFormat.getIcon();
                    final Format format = new Format(formatId, name, description, iconUrl);
                    formats.addFormat(format);
                }
                return formats;
            }
            else {
                errorMessage = "Null data received from the response";
            }
        }
        final ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.ELIB);
        final ErrorCauseArgument argContentProviderStatus = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, String.valueOf(elibStatus.getCode()));
        throw new InternalServerErrorException(errorMessage, ErrorCause.CONTENT_PROVIDER_ERROR, argContentProviderName, argContentProviderStatus);
    }

    /**
     * @see com.axiell.ehub.provider.IContentProviderDataAccessor#createLoan(com.axiell.ehub.consumer.ContentProviderConsumer, String, String, com.axiell.ehub.loan.PendingLoan)
     */
    @Override
    public ContentProviderLoan createLoan(final ContentProviderConsumer contentProviderConsumer,
                                          final String libraryCard,
                                          final String pin,
                                          final PendingLoan pendingLoan) {
        final String retailerId = contentProviderConsumer.getProperty(ELIB_RETAILER_ID);
        final String retailerKeyCode = contentProviderConsumer.getProperty(ELIB_RETAILER_KEY);
        final String md5RetailerKeyCode = md5Hex(retailerKeyCode.getBytes());
        final String elibRecordId = pendingLoan.getContentProviderRecordId();
        final String formatId = pendingLoan.getContentProviderFormatId();
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String createLoanUrl = contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.CREATE_LOAN_URL);

        final IElibLoanResource elibLoanResource = ProxyFactory.create(IElibLoanResource.class, createLoanUrl);
        final se.elib.library.loan.Response elibResponse = elibLoanResource.createLoan(retailerId, md5RetailerKeyCode, elibRecordId, formatId, libraryCard,
                pin, ENGLISH, CREATE_LOAN_MOBI_POCKET_ID);
        se.elib.library.loan.Response.Status elibStatus = elibResponse.getStatus();

        if (elibStatus.getCode() == ELIB_STATUS_CODE_OK) {
            final List<Orderitem> orderItems = getOrderItems(contentProviderConsumer, libraryCard);
            final ElibLoan elibLoan = getElibLoan(orderItems, elibRecordId, formatId);
            final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
            final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata(elibLoan.id, contentProvider, elibLoan.expirationDate,
                    formatDecoration);
            se.elib.library.loan.Response.Data data = elibResponse.getData();
            final String contentUrl = data.getDownloadurl();
            final IContent content = createContent(contentUrl, formatDecoration);
            return new ContentProviderLoan(metadata, content);
        } else {
            final ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.ELIB);
            final ErrorCauseArgument argContentProviderStatus = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, String.valueOf(elibStatus.getCode()));
            throw new InternalServerErrorException("Could not create loan", ErrorCause.CONTENT_PROVIDER_ERROR, argContentProviderName, argContentProviderStatus);
        }
    }

    /**
     * @see com.axiell.ehub.provider.IContentProviderDataAccessor#getContent(com.axiell.ehub.consumer.ContentProviderConsumer, String, String, com.axiell.ehub.loan.ContentProviderLoanMetadata)
     */
    @Override
    public IContent getContent(final ContentProviderConsumer contentProviderConsumer,
                               final String libraryCard,
                               final String pin,
                               final ContentProviderLoanMetadata contentProviderLoanMetadata) {

        final String contentProviderLoanId = contentProviderLoanMetadata.getId();
        final List<Orderitem> orderItems = getOrderItems(contentProviderConsumer, libraryCard);

        for (Orderitem orderItem : orderItems) {
            final String orderNumber = String.valueOf(orderItem.getRetailerordernumber());

            if (contentProviderLoanId.equals(orderNumber)) {
                final Book book = orderItem.getBook();
                final List<Serializable> serializables = book.getData().getData().getContent();
                String contentUrl = null;
                for (Serializable serializable : serializables) {
                    if (serializable instanceof JAXBElement) {
                        contentUrl = String.class.cast(JAXBElement.class.cast(serializable).getValue());
                        break;
                    }
                }
                if (contentUrl == null && serializables.size() == 1) {
                    contentUrl = String.class.cast(serializables.get(0));
                    final FormatDecoration formatDecorations = contentProviderLoanMetadata.getFormatDecoration();
                    return createContent(contentUrl, formatDecorations);
                } else if (contentUrl == null) {
                    final ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.ELIB);
                    final ErrorCauseArgument argContentProviderStatus = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, String.valueOf(ELIB_STATUS_CODE_OK));
                    throw new InternalServerErrorException("Can not determine the content url", ErrorCause.CONTENT_PROVIDER_ERROR, argContentProviderName, argContentProviderStatus);
                }
            }
        }
        final ErrorCauseArgument argContentProviederLoanId = new ErrorCauseArgument(Type.CONTENT_PROVIDER_LOAN_ID, contentProviderLoanId);
        final ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.ELIB);
        throw new NotFoundException(ErrorCause.CONTENT_PROVIDER_LOAN_NOT_FOUND, argContentProviederLoanId, argContentProviderName);
    }

    /**
     * @see com.axiell.ehub.provider.IContentProviderDataAccessor#getContentProviderName()
     */
    @Override
    public ContentProviderName getContentProviderName() {
        return ContentProviderName.ELIB;
    }

    /**
     * Gets a list of {@link Orderitem}s, i.e. a list of Elib loans for a specific user.
     *
     * @param contentProviderConsumer the {@link ContentProviderConsumer}
     * @param libraryCard             the library card
     * @return a list of {@link Orderitem}s
     */
    protected List<Orderitem> getOrderItems(final ContentProviderConsumer contentProviderConsumer, final String libraryCard) {
        final String retailerId = contentProviderConsumer.getProperty(ELIB_RETAILER_ID);
        final String retailerKeyCode = contentProviderConsumer.getProperty(ELIB_RETAILER_KEY);
        final String md5RetailerKeyCode = md5Hex(retailerKeyCode.getBytes());
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final String orderListUrl = contentProvider.getProperty(ContentProvider.ContentProviderPropertyKey.ORDER_LIST_URL);

        final IElibOrderListResource elibOrderListResource = ProxyFactory.create(IElibOrderListResource.class, orderListUrl);
        final se.elib.library.orderlist.Response elibResponse = elibOrderListResource.getOrderList(retailerId, md5RetailerKeyCode, libraryCard, ENGLISH);
        final se.elib.library.orderlist.Response.Status elibStatus = elibResponse.getStatus();

        if (elibStatus.getCode() == ELIB_STATUS_CODE_OK) {
            se.elib.library.orderlist.Response.Data data = elibResponse.getData();
            return data.getOrderitem();
        }
        final ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.ELIB);
        final ErrorCauseArgument argContentProviderStatus = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, String.valueOf(ELIB_STATUS_CODE_OK));
        throw new InternalServerErrorException("Could not get order items", ErrorCause.CONTENT_PROVIDER_ERROR, argContentProviderName, argContentProviderStatus);
    }

    /**
     * Gets an {@link ElibLoan} by comparing the IDs and formats of the books in the the provided list of
     * {@link Orderitem} with the given Elib record ID and Elib format ID.
     *
     * @param orderItems   a list of {@link Orderitem}
     * @param elibRecordId the ID of the record at Elib
     * @param elibFormatId the ID of the format at Elib
     * @return an instance of {@link ElibLoan}
     * @throws
     */
    private ElibLoan getElibLoan(List<Orderitem> orderItems, String elibRecordId, String elibFormatId) {
        final long elibRecordIdValue = Long.valueOf(elibRecordId);
        //The id elib uses is the isbn, when we make a loan with ISBN13, they return the loan as ISBN10, so let's compare both ISBN10 and ISBN13 to make sure we don't miss it
        final long elibRecordIdSecondaryValue = Long.valueOf(generateIsbn13or10(elibRecordId));
        final short elibFormatIdValue = Short.valueOf(elibFormatId);

        for (Orderitem orderItem : orderItems) {
            final Book book = orderItem.getBook();
            final BigInteger bookId = book.getId();
            final long bookIdValue = bookId.longValue();
            final se.elib.library.orderlist.Response.Data.Orderitem.Book.Format bookFormat = book.getFormat();
            final short bookFormatId = bookFormat.getId();

            if ((elibRecordIdValue == bookIdValue || elibRecordIdSecondaryValue == bookIdValue) && elibFormatIdValue == bookFormatId) {
                final long orderNumber = orderItem.getRetailerordernumber();
                final String expirationDate = orderItem.getLoanexpiredate();
                return new ElibLoan(orderNumber, expirationDate);
            }
        }

        throw new InternalServerErrorException("Could not find an order number in the list of order items matching the record ID '" + elibRecordId
                + "' and the format '" + elibFormatId + "'", ErrorCause.MISSING_CONTENT_PROVIDER_LOAN_ID);
    }

    /**
     * Represents an Elib loan.
     */
    private static class ElibLoan {
        private final String id;
        private Date expirationDate;

        /**
         * Constructs a new {@link ElibLoan}.
         *
         * @param orderNumber the Elib order number
         * @param date        the expiration date as a String
         */
        private ElibLoan(long orderNumber, String date) {
            this.id = String.valueOf(orderNumber);
            final DateTime dateTime = ELIB_DATE_FORMAT.parseDateTime(date);
            expirationDate = dateTime.toDate();
        }
    }
}
