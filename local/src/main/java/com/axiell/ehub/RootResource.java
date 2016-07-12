package com.axiell.ehub;

import com.axiell.ehub.checkout.CheckoutsResource;
import com.axiell.ehub.checkout.ICheckoutsResource;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.provider.ContentProvidersResource;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.core.Response;

public class RootResource implements IRootResource {
    private ILoanBusinessController loanBusinessController;
    private IIssueBusinessController formatBusinessController;

    @Override
    public Response root() {
        throw new NotImplementedException("The root path has not been implemented yet");
    }

    @Override
    public IContentProvidersResource contentProviders() {
        return new ContentProvidersResource(formatBusinessController);
    }

    @Override
    public ICheckoutsResource checkouts() {
        return new CheckoutsResource(loanBusinessController);
    }

    @Required
    public void setLoanBusinessController(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }

    @Required
    public void setFormatBusinessController(IIssueBusinessController formatBusinessController) {
        this.formatBusinessController = formatBusinessController;
    }
}
