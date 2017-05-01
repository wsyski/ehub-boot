package com.axiell.ehub.provider.elib.library3;

import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.auth.Patron;
import org.springframework.stereotype.Component;

import static com.axiell.ehub.consumer.ContentProviderConsumer.ContentProviderConsumerPropertyKey.ELIB_SERVICE_ID;

@Component
class Elib3Facade implements IElibFacade {

    @Override
    public BookAvailability getBookAvailability(final ContentProviderConsumer contentProviderConsumer, final String elibProductId, final Patron patron) {
        final String serviceId = contentProviderConsumer.getProperty(ELIB_SERVICE_ID);
        final String patronId = patron.getId();
        final String checksum = new ChecksumBuilder(serviceId, contentProviderConsumer).appendParameter(patronId).appendParameter(elibProductId).build();
        final IElibResource elibResource = ElibResourceFactory.create(contentProviderConsumer);
        return elibResource.getBookAvailability(serviceId, checksum, elibProductId, patronId);
    }

    @Override
    public Product getProduct(final ContentProviderConsumer contentProviderConsumer, final String elibProductId) {
        final String serviceId = contentProviderConsumer.getProperty(ELIB_SERVICE_ID);
        final String checksum = new ChecksumBuilder(serviceId, contentProviderConsumer).appendParameter(elibProductId).build();
        final IElibResource elibResource = ElibResourceFactory.create(contentProviderConsumer);
        final GetProductResponse response = elibResource.getProduct(serviceId, checksum, elibProductId);
        return response.getProduct();
    }

    @Override
    public CreatedLoan createLoan(final ContentProviderConsumer contentProviderConsumer, final String elibProductId, final Patron patron) {
        final String serviceId = contentProviderConsumer.getProperty(ELIB_SERVICE_ID);
        final String patronId = patron.getId();
        final String checksum = new ChecksumBuilder(serviceId, contentProviderConsumer).appendParameter(patronId).appendParameter(elibProductId).build();
        final IElibResource elibResource = ElibResourceFactory.create(contentProviderConsumer);
        final CreateLoanResponse response = elibResource.createLoan(serviceId, patronId, elibProductId, checksum);
        return response.getCreatedLoan();
    }

    @Override
    public LoanDTO getLoan(final ContentProviderConsumer contentProviderConsumer, final String loanId) {
        final String serviceId = contentProviderConsumer.getProperty(ELIB_SERVICE_ID);
        final String checksum = new ChecksumBuilder(serviceId, contentProviderConsumer).appendParameter(loanId).build();
        final IElibResource elibResource = ElibResourceFactory.create(contentProviderConsumer);
        final GetLoanResponse response = elibResource.getLoan(serviceId, checksum, loanId);
        return response.getLoan();
    }

    @Override
    public GetLoansResponse getLoans(ContentProviderConsumer contentProviderConsumer, Patron patron) {
        final String serviceId = contentProviderConsumer.getProperty(ELIB_SERVICE_ID);
        final String patronId = patron.getId();
        final String checksum = new ChecksumBuilder(serviceId, contentProviderConsumer).appendParameter(patronId).build();
        final IElibResource elibResource = ElibResourceFactory.create(contentProviderConsumer);
        return elibResource.getLoans(serviceId, checksum, patronId, true);
    }

    @Override
    public LibraryProduct getLibraryProduct(final ContentProviderConsumer contentProviderConsumer, final String elibProductId) {
        final String serviceId = contentProviderConsumer.getProperty(ELIB_SERVICE_ID);
        final String checksum = new ChecksumBuilder(serviceId, contentProviderConsumer).appendParameter(elibProductId).build();
        final IElibResource elibResource = ElibResourceFactory.create(contentProviderConsumer);
        final LibraryProductResponse response = elibResource.getLibraryProduct(serviceId, checksum, elibProductId);
        return response.getLibraryProduct();
    }
}
