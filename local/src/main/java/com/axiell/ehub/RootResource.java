package com.axiell.ehub;

import com.axiell.ehub.checkout.CheckoutsResource;
import com.axiell.ehub.checkout.ICheckoutsResource;
import com.axiell.ehub.loan.ILoanBusinessController;
import com.axiell.ehub.provider.ContentProvidersResource;
import com.axiell.ehub.provider.IContentProvidersResource;
import com.axiell.ehub.provider.record.format.IFormatBusinessController;
import com.axiell.ehub.security.AuthInfo;
import org.springframework.beans.factory.annotation.Required;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.core.Response;

public class RootResource implements IRootResource {
    private ILoanBusinessController loanBusinessController;
    private IFormatBusinessController formatBusinessController;

    @Override
    public Response root() {
        throw new NotImplementedException("The root path has not been implemented yet");
    }

    @Override
    public IContentProvidersResource contentProviders(@HeaderParam("Authorization") AuthInfo authInfo) {
        return new ContentProvidersResource(formatBusinessController, authInfo);
    }

    @Override
    public ICheckoutsResource checkouts(@HeaderParam("Authorization") AuthInfo authInfo) {
        return new CheckoutsResource(loanBusinessController, authInfo);
    }

    @Required
    public void setLoanBusinessController(ILoanBusinessController loanBusinessController) {
        this.loanBusinessController = loanBusinessController;
    }

    @Required
    public void setFormatBusinessController(IFormatBusinessController formatBusinessController) {
        this.formatBusinessController = formatBusinessController;
    }
}
