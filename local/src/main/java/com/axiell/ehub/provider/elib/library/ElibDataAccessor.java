/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider.elib.library;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.patron.Patron;
import com.axiell.ehub.provider.AbstractContentProviderDataAccessor;
import com.axiell.ehub.provider.CommandData;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.record.format.*;
import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.elib.library.orderlist.Response.Data.Orderitem;
import se.elib.library.orderlist.Response.Data.Orderitem.Book;
import se.elib.library.orderlist.Response.Data.Orderitem.Book.BookData;
import se.elib.library.orderlist.Response.Data.Orderitem.Book.BookData.UrlData;
import se.elib.library.product.Response;
import se.elib.library.product.Response.Data;

import javax.xml.bind.JAXBElement;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.axiell.ehub.provider.elib.library.ElibUtils.*;

/**
 * The Elib integration.
 */
@Component
public class ElibDataAccessor extends AbstractContentProviderDataAccessor {
    private static final Logger LOGGER = LoggerFactory.getLogger(ElibDataAccessor.class);

    @Autowired(required = true)
    private IElibFacade elibFacade;

    @Autowired
    private IFormatFactory formatFactory;

    @Override
    public Formats getFormats(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final String contentProviderRecordId = data.getContentProviderRecordId();
        final String language = data.getLanguage();
        final se.elib.library.product.Response response = elibFacade.getProduct(contentProviderConsumer, contentProviderRecordId, language);
        final se.elib.library.product.Response.Status status = response.getStatus();
        final short statusCode = status.getCode();

        if (statusCode == ELIB_STATUS_CODE_OK) {
            return makeFormats(contentProviderConsumer, contentProviderRecordId, language, response);
        }
        throw makeInternalServerErrorException("Could not get formats", String.valueOf(statusCode));
    }

    private boolean productStatusIsActive(final Response response, final String contentProviderRecordId) {
        final Data data = response.getData();
        if (data != null) {
            return data.getProduct().getStatus().getId() == ELIB_PRODUCT_OK_ID;
        } else {
            LOGGER.warn("No data received in the get formats response where content provider record ID = '" + contentProviderRecordId + "'");
            return false;
        }
    }

    private Formats makeFormats(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId, String language,
                                final se.elib.library.product.Response response) {
        final Formats formats = new Formats();
        if (productStatusIsActive(response, contentProviderRecordId)) {
            final se.elib.library.product.Response.Data data = response.getData();
            final se.elib.library.product.Response.Data.Product product = data.getProduct();
            final se.elib.library.product.Response.Data.Product.Formats elibFormats = product.getFormats();
            final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();

            for (se.elib.library.product.Response.Data.Product.Formats.Format elibFormat : elibFormats.getFormat()) {
                final Format format = makeFormat(language, contentProvider, elibFormat);
                formats.addFormat(format);
            }
        }

        return formats;
    }

    private Format makeFormat(final String language, final ContentProvider contentProvider,
                              se.elib.library.product.Response.Data.Product.Formats.Format elibFormat) {
        final String formatId = String.valueOf(elibFormat.getFormatId());
        return formatFactory.create(contentProvider, formatId, language);
    }

    private InternalServerErrorException makeInternalServerErrorException(String message, String statusCode) {
        final ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.ELIB);
        final ErrorCauseArgument argContentProviderStatus = new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, statusCode);
        return new InternalServerErrorException(message, ErrorCause.CONTENT_PROVIDER_ERROR, argContentProviderName, argContentProviderStatus);
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final Patron patron = data.getPatron();
        final String libraryCard = patron.getLibraryCard();
        final String pin = patron.getPin();
        final String elibRecordId = data.getContentProviderRecordId();
        final String formatId = data.getContentProviderFormatId();

        final se.elib.library.loan.Response response = elibFacade.createLoan(contentProviderConsumer, elibRecordId, formatId, libraryCard, pin);
        final se.elib.library.loan.Response.Status status = response.getStatus();
        final int statusCode = status.getCode();

        if (statusCode == ELIB_STATUS_CODE_OK) {
            return makeContentProviderLoan(contentProviderConsumer, libraryCard, elibRecordId, formatId, response);
        }

        throw makeInternalServerErrorException("Could not getInstance loan", String.valueOf(statusCode));
    }

    private ContentProviderLoan makeContentProviderLoan(final ContentProviderConsumer contentProviderConsumer, final String libraryCard,
                                                        final String elibRecordId, final String formatId, final se.elib.library.loan.Response loanResponse) {
        final List<Orderitem> orderItems = getOrderItems(contentProviderConsumer, libraryCard);
        final ElibLoan elibLoan = makeElibLoan(orderItems, elibRecordId, formatId);
        final ContentProvider contentProvider = contentProviderConsumer.getContentProvider();
        final FormatDecoration formatDecoration = contentProvider.getFormatDecoration(formatId);
        final ContentProviderLoanMetadata metadata = new ContentProviderLoanMetadata.Builder(contentProvider, elibLoan.expirationDate, elibRecordId,
                formatDecoration).contentProviderLoanId(elibLoan.id).build();
        final se.elib.library.loan.Response.Data data = loanResponse.getData();
        final String contentUrl = data.getDownloadurl();
        final ContentLink contentLink = createContent(contentUrl, formatDecoration);
        return new ContentProviderLoan(metadata, contentLink);
    }

    protected List<Orderitem> getOrderItems(final ContentProviderConsumer contentProviderConsumer, final String libraryCard) {
        final se.elib.library.orderlist.Response response = elibFacade.getOrderList(contentProviderConsumer, libraryCard);
        final se.elib.library.orderlist.Response.Status status = response.getStatus();
        final short statusCode = status.getCode();

        if (statusCode == ELIB_STATUS_CODE_OK) {
            se.elib.library.orderlist.Response.Data data = response.getData();
            return data.getOrderitem();
        }

        throw makeInternalServerErrorException("Could not get order items", String.valueOf(statusCode));
    }

    private ElibLoan makeElibLoan(List<Orderitem> orderItems, String reqRecordId, String reqFormatId) {
        final short reqFormatIdValue = Short.valueOf(reqFormatId);

        for (Orderitem orderItem : orderItems) {
            final Book book = orderItem.getBook();
            final String orderRecordId = book.getId();
            final se.elib.library.orderlist.Response.Data.Orderitem.Book.Format bookFormat = book.getFormat();
            final short orderFormatId = bookFormat.getId();

            if (requestRecordIdEqualsOrderRecordId(reqRecordId, orderRecordId) && requestFormatIdEqualsOrderFormatId(reqFormatIdValue, orderFormatId)) {
                final long orderNumber = orderItem.getRetailerordernumber();
                final String expirationDate = orderItem.getLoanexpiredate();
                return new ElibLoan(orderNumber, expirationDate);
            }
        }

        throw new InternalServerErrorException("Could not find an order number in the list of order items matching the record ID '" + reqRecordId
                + "' and the format '" + reqFormatId + "'", ErrorCause.MISSING_CONTENT_PROVIDER_LOAN_ID);
    }

    private boolean requestRecordIdEqualsOrderRecordId(String reqRecordId, final String orderRecordId) {
    /*
	 * The id elib uses is the isbn, when we make a loan with ISBN13, they
	 * return the loan as ISBN10, so let's compare both ISBN10 and ISBN13 to
	 * make sure we don't miss it
	 */
        final String secondaryReqRecordId = generateIsbn13or10(reqRecordId);
        return reqRecordId.equals(orderRecordId) || secondaryReqRecordId.equals(orderRecordId);
    }

    private boolean requestFormatIdEqualsOrderFormatId(final short reqFormatIdValue, final short orderFormatId) {
        return reqFormatIdValue == orderFormatId;
    }

    @Override
    public ContentLink getContent(final CommandData data) {
        final ContentProviderConsumer contentProviderConsumer = data.getContentProviderConsumer();
        final ContentProviderLoanMetadata contentProviderLoanMetadata = data.getContentProviderLoanMetadata();
        final Patron patron = data.getPatron();
        final String libraryCard = patron.getLibraryCard();
        final String contentProviderLoanId = contentProviderLoanMetadata.getId();
        final List<Orderitem> orderItems = getOrderItems(contentProviderConsumer, libraryCard);

        for (Orderitem orderItem : orderItems) {
            if (contentProviderLoanIdEqualsOrderNumber(contentProviderLoanId, orderItem)) {
                final String contentUrl = getContentUrl(orderItem);

                if (contentUrl == null) {
                    throw makeInternalServerErrorException("Can not determine the content url", String.valueOf(ELIB_STATUS_CODE_OK));
                } else {
                    final FormatDecoration formatDecorations = contentProviderLoanMetadata.getFormatDecoration();
                    return createContent(contentUrl, formatDecorations);
                }
            }
        }

        throw makeNotFoundException(contentProviderLoanId);
    }

    private boolean contentProviderLoanIdEqualsOrderNumber(String contentProviderLoanId, Orderitem orderItem) {
        final String orderNumber = String.valueOf(orderItem.getRetailerordernumber());
        return contentProviderLoanId.equals(orderNumber);
    }

    private String getContentUrl(final Orderitem orderItem) {
        final Book book = orderItem.getBook();
        final BookData bookData = book.getData();
        final UrlData urlData = bookData.getData();
        final List<Serializable> content = urlData.getContent();
        return getContentUrl(content);
    }

    private String getContentUrl(final List<Serializable> serializables) {
        for (Serializable serializable : serializables) {
            if (serializable instanceof JAXBElement) {
                String value = String.class.cast(JAXBElement.class.cast(serializable).getValue());
                if (!StringUtils.isBlank(value)) {
                    return value;
                }
            }
        }

        for (Serializable serializable : serializables) {
            String value = String.class.cast(serializable);
            if (!StringUtils.isBlank(value)) {
                return value;
            }
        }

        return null;
    }

    private NotFoundException makeNotFoundException(final String contentProviderLoanId) {
        final ErrorCauseArgument argContentProviederLoanId = new ErrorCauseArgument(Type.CONTENT_PROVIDER_LOAN_ID, contentProviderLoanId);
        final ErrorCauseArgument argContentProviderName = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.ELIB);
        return new NotFoundException(ErrorCause.CONTENT_PROVIDER_LOAN_NOT_FOUND, argContentProviederLoanId, argContentProviderName);
    }

    /**
     * Represents an Elib loan.
     */
    private static class ElibLoan {
        private final String id;
        private final Date expirationDate;

        private ElibLoan(long orderNumber, String date) {
            id = String.valueOf(orderNumber);
            final DateTime dateTime = ELIB_DATE_FORMAT.parseDateTime(date);
            expirationDate = dateTime.toDate();
        }
    }
}
