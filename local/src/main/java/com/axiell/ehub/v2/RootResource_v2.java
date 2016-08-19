package com.axiell.ehub.v2;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import com.axiell.ehub.v2.checkout.CheckoutsResource_v2;
import com.axiell.ehub.v2.checkout.ICheckoutsResource_v2;
import com.axiell.ehub.v2.provider.ContentProvidersResource_v2;
import com.axiell.ehub.v2.provider.IContentProvidersResource_v2;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.core.Response;

public class RootResource_v2 implements IRootResource_v2 {
    private ILoanBusinessController loanBusinessController;
    private IIssueBusinessController issueBusinessController;

    @Override
    public Response root() {
        throw new NotImplementedException("The root path has not been implemented yet");
    }

    @Override
    public IContentProvidersResource_v2 contentProviders() {
        return new ContentProvidersResource_v2(issueBusinessController);
    }

    @Override
    public ICheckoutsResource_v2 checkouts() {
        return new CheckoutsResource_v2(loanBusinessController);
    }

    @Required
    public void setLoanBusinessController(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }

    @Required
    public void setIssueBusinessController(IIssueBusinessController issueBusinessController) {
        this.issueBusinessController = issueBusinessController;
    }
}
