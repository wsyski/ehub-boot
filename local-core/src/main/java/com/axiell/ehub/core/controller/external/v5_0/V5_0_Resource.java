package com.axiell.ehub.core.controller.external.v5_0;

import com.axiell.ehub.common.controller.external.v5_0.IV5_0_Resource;
import com.axiell.ehub.core.controller.external.v5_0.checkout.CheckoutsResource;
import com.axiell.ehub.common.controller.external.v5_0.checkout.ICheckoutsResource;
import com.axiell.ehub.core.controller.external.v5_0.provider.ContentProvidersResource;
import com.axiell.ehub.common.controller.external.v5_0.provider.IContentProvidersResource;
import com.axiell.ehub.core.loan.ILoanBusinessController;
import com.axiell.ehub.core.provider.alias.IAliasBusinessController;
import com.axiell.ehub.core.provider.record.issue.IIssueBusinessController;

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
