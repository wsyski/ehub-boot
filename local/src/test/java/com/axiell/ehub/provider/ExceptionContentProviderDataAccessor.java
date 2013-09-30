package com.axiell.ehub.provider;

import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.consumer.ContentProviderConsumer;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.loan.ContentProviderLoanMetadata;
import com.axiell.ehub.loan.IContent;
import com.axiell.ehub.loan.PendingLoan;
import com.axiell.ehub.provider.record.format.Formats;


class ExceptionContentProviderDataAccessor implements IContentProviderDataAccessor {

    @Override
    public Formats getFormats(ContentProviderConsumer contentProviderConsumer, String contentProviderRecordId, String language) {
	ClientRequest request = new ClientRequest("http://www.google.com");
	ClientResponse<?> response;
	try {
	    response = request.get();
	} catch (Exception ex) {
        throw new RuntimeException(ex.getMessage(),ex);
	}
	throw new ClientResponseFailure(response);
    }

    @Override
    public ContentProviderLoan createLoan(ContentProviderConsumer contentProviderConsumer, String libraryCard, String pin, PendingLoan pendingLoan) {
	throw new NotImplementedException("createLoan has not been implemented");
    }

    @Override
    public IContent getContent(ContentProviderConsumer contentProviderConsumer, String libraryCard, String pin,
	    ContentProviderLoanMetadata contentProviderLoanMetadata) {
	throw new NotImplementedException("getContent has not been implemented");
    }
}
