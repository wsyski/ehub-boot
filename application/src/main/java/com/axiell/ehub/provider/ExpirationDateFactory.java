package com.axiell.ehub.provider;

import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.InternalServerErrorException;
import com.axiell.ehub.provider.ContentProvider.ContentProviderPropertyKey;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;

@Component
public class ExpirationDateFactory implements IExpirationDateFactory {
    private static final int HOUR_OF_DAY = 12;

    @Override
    public Date createExpirationDate(ContentProvider contentProvider) {
        final String expirationDays = contentProvider.getProperty(ContentProviderPropertyKey.LOAN_EXPIRATION_DAYS);
        validateExpirationDaysIsNotNull(contentProvider, expirationDays);
        validateExpirationDateIsNumeric(contentProvider, expirationDays);
        return toDate(expirationDays);
    }

    private void validateExpirationDaysIsNotNull(final ContentProvider contentProvider, final String expirationDays) {
        if (expirationDays == null)
            throw new InternalServerErrorException("Property 'loan expiration days' has not been set for Content Provider '" + contentProvider.getName() + "'",
                    ErrorCause.INTERNAL_SERVER_ERROR);
    }

    private void validateExpirationDateIsNumeric(final ContentProvider contentProvider, final String expirationDays) {
        if (expirationDaysIsNotNumeric(expirationDays))
            throw new InternalServerErrorException("Property 'loan expiration days' for Content Provider '" + contentProvider.getName() + "' is not numeric",
                    ErrorCause.INTERNAL_SERVER_ERROR);
    }

    private boolean expirationDaysIsNotNumeric(final String expirationDays) {
        return !StringUtils.isNumeric(expirationDays);
    }

    private Date toDate(final String expirationDays) {
        final int expirationDaysAsInt = Integer.parseInt(expirationDays);
        final Date now = Date.from(Instant.now());
        return new Date(now.getTime() + (long) expirationDaysAsInt * 86400000);
    }
}
