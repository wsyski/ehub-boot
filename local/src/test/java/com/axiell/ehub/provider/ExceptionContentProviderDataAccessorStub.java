package com.axiell.ehub.provider;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.checkout.ContentLinks;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.provider.record.format.Formats;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;


class ExceptionContentProviderDataAccessorStub implements IContentProviderDataAccessor {

    @Override
    public Formats getFormats(final CommandData commandData) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        ResteasyWebTarget target = client.target("http://www.googleapis.com/calendar/v3/calendars/calendarId/events");
        Response response = target.request().get();
        throw new ClientErrorException(response);
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData commandData) {
        throw new NotImplementedException("createLoan has not been implemented");
    }

    @Override
    public ContentLinks getContent(final CommandData commandData) {
        throw new NotImplementedException("getLoan has not been implemented");
    }
}
