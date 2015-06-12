package com.axiell.ehub.provider;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.checkout.ContentLink;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.provider.record.format.Formats;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ClientResponseFailure;


class LegacyExceptionContentProviderDataAccessor implements IContentProviderDataAccessor {

    @Override
    public Formats getFormats(final CommandData commandData) {
        ClientRequest request = new ClientRequest("http://www.googleapis.com/calendar/v3/calendars/calendarId/events");
        ClientResponse<?> response;
        try {
            response = request.get();
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage(), ex);
        }
        throw new ClientResponseFailure(response);
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData commandData) {
        throw new NotImplementedException("createLoan_success has not been implemented");
    }

    @Override
    public ContentLink getContent(final CommandData commandData) {
        throw new NotImplementedException("getLoan has not been implemented");
    }
}
