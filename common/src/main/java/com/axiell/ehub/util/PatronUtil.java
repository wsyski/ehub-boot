package com.axiell.ehub.util;

import com.axiell.authinfo.Patron;
import com.axiell.ehub.ErrorCause;
import com.axiell.ehub.security.UnauthorizedException;

public class PatronUtil {
    public static String getMandatoryEmail(final Patron patron) {
        if (!patron.hasEmail()) {
            throw new UnauthorizedException(ErrorCause.MISSING_EMAIL);
        }
        return patron.getEmail();
    }

    public static String getMandatoryLibraryCard(final Patron patron) {
        if (!patron.hasLibraryCard()) {
            throw new UnauthorizedException(ErrorCause.MISSING_LIBRARY_CARD);
        }
        return patron.getLibraryCard();
    }

    public static String getMandatoryPin(final Patron patron) {
        if (!patron.hasPin()) {
            throw new UnauthorizedException(ErrorCause.MISSING_PIN);
        }
        return patron.getPin();
    }
}
