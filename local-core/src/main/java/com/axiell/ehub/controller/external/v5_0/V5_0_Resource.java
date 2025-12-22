package com.axiell.ehub.controller.external.v5_0;

import com.axiell.ehub.controller.external.v5_0.checkout.CheckoutsResource;
import com.axiell.ehub.controller.external.v5_0.checkout.ICheckoutsResource;
import com.axiell.ehub.controller.external.v5_0.provider.ContentProvidersResource;
import com.axiell.ehub.controller.external.v5_0.provider.IContentProvidersResource;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.provider.alias.IAliasBusinessController;
import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class V5_0_Resource implements IV5_0_Resource {

    private final IAliasBusinessController aliasBusinessController;
    private final IIssueBusinessController issueBusinessController;
    private final ILoanBusinessController loanBusinessController;

    public V5_0_Resource(final IAliasBusinessController aliasBusinessController, final IIssueBusinessController issueBusinessController, final ILoanBusinessController loanBusinessController) {
        this.aliasBusinessController = aliasBusinessController;
        this.issueBusinessController = issueBusinessController;
        this.loanBusinessController = loanBusinessController;
    }

    @Override
    public IContentProvidersResource contentProviders() {
        return new ContentProvidersResource(issueBusinessController, aliasBusinessController);
    }

    @Override
    public ICheckoutsResource checkouts() {
        return new CheckoutsResource(loanBusinessController);
    }
}
