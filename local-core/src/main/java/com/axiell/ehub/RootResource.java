package com.axiell.ehub;

import com.axiell.ehub.checkout.CheckoutsResource;
import com.axiell.ehub.checkout.ICheckoutsResource;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.provider.ContentProvidersResource;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Produces(MediaType.APPLICATION_JSON)
public class RootResource implements IRootResource {
    @Autowired
    private ILoanBusinessController loanBusinessController;
    @Autowired
    private IIssueBusinessController issueBusinessController;

    @Override
    public Response root() {
        throw new NotImplementedException("The root path has not been implemented yet");
    }

    @Override
    public IContentProvidersResource contentProviders() {
        return new ContentProvidersResource(issueBusinessController);
    }

    @Override
    public ICheckoutsResource checkouts() {
        return new CheckoutsResource(loanBusinessController);
    }
}
