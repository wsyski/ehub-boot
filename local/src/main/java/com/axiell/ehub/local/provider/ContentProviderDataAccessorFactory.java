package com.axiell.ehub.local.provider;

import com.axiell.ehub.common.provider.ContentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class ContentProviderDataAccessorFactory implements IContentProviderDataAccessorFactory {

    @Autowired
    @Qualifier("elib3DataAccessor")
    private IContentProviderDataAccessor elib3DataAccessor;

    @Autowired
    @Qualifier("overDriveDataAccessor")
    private IContentProviderDataAccessor overDriveDataAccessor;

    @Autowired
    @Qualifier("lpfEpDataAccessor")
    private IContentProviderDataAccessor lpfEpDataAccessor;

    @Autowired
    @Qualifier("lppEpDataAccessor")
    private IContentProviderDataAccessor lppEpDataAccessor;

    @Override
    public IContentProviderDataAccessor getInstance(final ContentProvider contentProvider) {
        final String name = contentProvider.getName();
        if (ContentProvider.CONTENT_PROVIDER_ELIB3.equals(name)) {
            return elib3DataAccessor;
        } else if (ContentProvider.CONTENT_PROVIDER_OVERDRIVE.equals(name)) {
            return overDriveDataAccessor;
        } else {
            boolean isLoanPerProduct = contentProvider.isLoanPerProduct();
            return isLoanPerProduct ? lppEpDataAccessor : lpfEpDataAccessor;
        }
    }
}
