package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;

import java.util.Date;

public interface IExpirationDateFactory {

    Date createExpirationDate(ContentProvider contentProvider);
}
