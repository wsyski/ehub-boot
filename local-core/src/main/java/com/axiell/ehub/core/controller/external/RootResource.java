package com.axiell.ehub.core.controller.external;

import com.axiell.ehub.common.controller.external.IRootResource;
import com.axiell.ehub.common.controller.external.v5_0.IV5_0_Resource;
import com.axiell.ehub.core.controller.external.v5_0.V5_0_Resource;
import com.axiell.ehub.core.loan.ILoanBusinessController;
import com.axiell.ehub.core.provider.alias.IAliasBusinessController;
import com.axiell.ehub.core.provider.record.issue.IIssueBusinessController;
import org.springframework.beans.factory.annotation.Autowired;

public class RootResource implements IRootResource {

    @Autowired
    private IAliasBusinessController aliasBusinessController;
    @Autowired
    private IIssueBusinessController issueBusinessController;
    @Autowired
    private ILoanBusinessController loanBusinessController;

    @Override
    public IV5_0_Resource getIV5_0_Resource() {
        return new V5_0_Resource(aliasBusinessController, issueBusinessController, loanBusinessController);
    }
}
