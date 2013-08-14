package com.axiell.ehub.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor;
import com.axiell.ehub.provider.elib.library.ElibDataAccessor;
import com.axiell.ehub.provider.publit.PublitDataAccessor;

@Component
public class ContentProviderDataAccessorFactory implements IContentProviderDataAccessorFactory {
    @Autowired(required = true)
    private ElibDataAccessor elibDataAccessor;

    @Autowired(required = true)
    private ElibUDataAccessor elibUDataAccessor;

    @Autowired(required = true)
    private PublitDataAccessor publitDataAccessor;

    @Override
    public IContentProviderDataAccessor getInstance(ContentProviderName contentProviderName) {
        switch (contentProviderName) {
            case ELIB:
                return elibDataAccessor;
            case ELIBU:
                return elibUDataAccessor;
            case PUBLIT:
                return publitDataAccessor;
            default:
                throw new NotImplementedException("Content provider with name '" + contentProviderName
                        + "' has not been implemented");
        }
    }
}
