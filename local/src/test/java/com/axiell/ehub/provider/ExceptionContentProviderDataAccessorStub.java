package com.axiell.ehub.provider;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.checkout.Content;
import com.axiell.ehub.loan.ContentProviderLoan;
import com.axiell.ehub.provider.record.issue.Issue;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import org.jboss.resteasy.client.jaxrs.internal.ResteasyClientBuilderImpl;

import javax.ws.rs.ClientErrorException;
import javax.ws.rs.core.Response;
import java.util.List;


class ExceptionContentProviderDataAccessorStub implements IContentProviderDataAccessor {

    @Override
    public List<Issue> getIssues(final CommandData commandData) {
        ResteasyClient client = new ResteasyClientBuilderImpl().build();
        ResteasyWebTarget target = client.target("http://www.googleapis.com/calendar/v3/calendars/calendarId/events");
        Response response = target.request().get();
        throw new ClientErrorException(response);
    }

    @Override
    public ContentProviderLoan createLoan(final CommandData commandData) {
        throw new NotImplementedException("createLoan has not been implemented");
    }

    @Override
    public Content getContent(final CommandData commandData) {
        throw new NotImplementedException("getLoan has not been implemented");
    }
}
