package com.axiell.ehub.provider.overdrive;

import java.io.InputStream;
import java.lang.reflect.Method;

import javax.ws.rs.ext.Provider;

import org.apache.log4j.spi.LoggerFactory;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.core.BaseClientResponse;
import org.jboss.resteasy.client.core.ClientErrorInterceptor;
import org.jboss.resteasy.spi.interception.AcceptedByMethod;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.provider.ContentProviderName;
import org.slf4j.Logger;

import static org.slf4j.LoggerFactory.*;

@Provider
public class OverDriveClientErrorInterceptor implements ClientErrorInterceptor, AcceptedByMethod {
    private static final Logger LOGGER = getLogger(OverDriveClientErrorInterceptor.class);

    @Override
    public boolean accept(@SuppressWarnings("rawtypes") final Class type, final Method method) {
//        LOGGER.info("Class = " + type);
//        final Package thisPkg = getClass().getPackage();
//        LOGGER.info("This package = " + thisPkg);
//        final Package providedPkg = type.getPackage();
//        return thisPkg.equals(providedPkg);
        return false;
    }

    @Override
    public void handle(final ClientResponse<?> response) throws RuntimeException {
        LOGGER.info("Handling error!");
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
        response.resetStream();
        final ErrorDetails errorDetails = response.getEntity(ErrorDetails.class);
        return errorDetails.getMessage();
    }
}
