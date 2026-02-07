package com.axiell.ehub.controller;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.controller.external.IRootResource;
import com.axiell.ehub.controller.external.v5_0.IV5_0_Resource;
import com.axiell.ehub.controller.external.v5_0.V5_0_Resource;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.provider.alias.IAliasBusinessController;
import com.axiell.ehub.provider.record.issue.IIssueBusinessController;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
