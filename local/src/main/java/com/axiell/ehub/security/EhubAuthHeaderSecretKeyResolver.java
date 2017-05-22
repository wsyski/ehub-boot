package com.axiell.ehub.security;

import com.axiell.authinfo.AbstractAuthHeaderSecretKeyResolver;
import com.axiell.authinfo.AuthInfo;
import com.axiell.authinfo.IAuthHeaderSecretKeyResolver;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.consumer.EhubConsumer;
import com.axiell.ehub.consumer.IConsumerBusinessController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.transaction.annotation.Transactional;

public class EhubAuthHeaderSecretKeyResolver extends AbstractAuthHeaderSecretKeyResolver implements IAuthHeaderSecretKeyResolver {

    private long expirationTimeInSeconds;

    private IConsumerBusinessController consumerBusinessController;

    @Transactional(readOnly = true)
    @Override
    public String getSecretKey(final AuthInfo authInfo) {
        Long ehubConsumerId = authInfo.getEhubConsumerId();
        if (ehubConsumerId == null) {
            throw new InternalServerErrorException(ErrorCause.MISSING_EHUB_CONSUMER_ID);
        }
        final EhubConsumer ehubConsumer = consumerBusinessController.getEhubConsumer(ehubConsumerId);
        final String secretKey = ehubConsumer.getSecretKey();
        if (StringUtils.isBlank(secretKey)) {
            final ErrorCauseArgument ehubConsumerIdArg = new ErrorCauseArgument(ErrorCauseArgument.Type.EHUB_CONSUMER_ID, ehubConsumerId);
            throw new InternalServerErrorException(ErrorCause.MISSING_SECRET_KEY, ehubConsumerIdArg);
        }
        return secretKey;
    }

    @Override
    public long getExpirationTimeInSeconds(final AuthInfo authInfo) {
        return expirationTimeInSeconds;
    }

    @Required
    public void setConsumerBusinessController(final IConsumerBusinessController consumerBusinessController) {
        this.consumerBusinessController = consumerBusinessController;
    }

    @Required
    public void setExpirationTimeInSeconds(final long expirationTimeInSeconds) {
        this.expirationTimeInSeconds = expirationTimeInSeconds;
    }
}
