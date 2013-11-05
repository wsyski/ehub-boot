package com.axiell.ehub.provider.overdrive;

import java.lang.reflect.Method;

import javax.ws.rs.ext.Provider;

import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.ClientErrorInterceptor;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.provider.ContentProviderName;

@Provider
public class OverDriveClientErrorInterceptor implements ClientErrorInterceptor, AcceptedByMethod {
    
    @Override
    public boolean accept(@SuppressWarnings("rawtypes") final Class type, final Method method) {
	final Package thisPkg = getClass().getPackage();
	final Package providedPkg = type.getPackage();
	return thisPkg.equals(providedPkg);
    }

    @Override
    public void handle(final ClientResponse<?> response) throws RuntimeException {
	final ErrorCauseArgument contentProviderStatusArg = makeStatusCodeErrorCauseArgument(response);
	final ErrorCauseArgument contentProviderNameArg = new ErrorCauseArgument(Type.CONTENT_PROVIDER_NAME, ContentProviderName.OVERDRIVE);
	final String message = getErrorMessage(response);
	throw new InternalServerErrorException(message, ErrorCause.CONTENT_PROVIDER_ERROR, contentProviderNameArg, contentProviderStatusArg);
    }

    private ErrorCauseArgument makeStatusCodeErrorCauseArgument(final ClientResponse<?> response) {
	final int status = response.getStatus();
	final String statusCode = String.valueOf(status);
	return new ErrorCauseArgument(Type.CONTENT_PROVIDER_STATUS, statusCode);
    }

    private String getErrorMessage(final ClientResponse<?> response) {
	final ErrorDetails errorDetails = response.getEntity(ErrorDetails.class);
	return errorDetails.getMessage();
    }
}
