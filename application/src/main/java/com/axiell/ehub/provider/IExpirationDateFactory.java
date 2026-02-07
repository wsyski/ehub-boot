package com.axiell.ehub.provider;

import java.util.Date;

public interface IExpirationDateFactory {

    Date createExpirationDate(ContentProvider contentProvider);
}
