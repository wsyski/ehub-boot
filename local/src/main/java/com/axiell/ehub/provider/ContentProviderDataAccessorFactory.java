package com.axiell.ehub.provider;

import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.provider.askews.AskewsDataAccessor;
import com.axiell.ehub.provider.borrowbox.BorrowBoxDataAccessor;
import com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor;
import com.axiell.ehub.provider.elib.library.ElibDataAccessor;
import com.axiell.ehub.provider.elib.library3.Elib3DataAccessor;
import com.axiell.ehub.provider.f1.F1DataAccessor;
import com.axiell.ehub.provider.ocd.OcdDataAccessor;
import com.axiell.ehub.provider.overdrive.OverDriveDataAccessor;
import com.axiell.ehub.provider.publit.PublitDataAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentProviderDataAccessorFactory implements IContentProviderDataAccessorFactory {
    @Autowired(required = true)
    private ElibDataAccessor elibDataAccessor;

    @Autowired(required = true)
    private Elib3DataAccessor elib3DataAccessor;

    @Autowired(required = true)
    private ElibUDataAccessor elibUDataAccessor;

    @Autowired(required = true)
    private PublitDataAccessor publitDataAccessor;
    
    @Autowired(required = true)
    private AskewsDataAccessor askewsDataAccessor;
    
    @Autowired(required = true)
    private OverDriveDataAccessor overDriveDataAccessor;

    @Autowired(required = true)
    private F1DataAccessor f1DataAccessor;

    @Autowired(required = true)
    private OcdDataAccessor ocdDataAccessor;

    @Autowired(required = true)
    private BorrowBoxDataAccessor borrowBoxDataAccessor;

    @Override
    public IContentProviderDataAccessor getInstance(final ContentProviderName contentProviderName) {
        switch (contentProviderName) {
            case ELIB:
                return elibDataAccessor;
            case ELIB3:
                return elib3DataAccessor;
            case ELIBU:
                return elibUDataAccessor;
            case PUBLIT:
                return publitDataAccessor;
            case ASKEWS:
                return askewsDataAccessor;
            case OVERDRIVE:
        	    return overDriveDataAccessor;
            case F1:
                return f1DataAccessor;
            case BORROWBOX:
                return borrowBoxDataAccessor;
            case OCD:
                return ocdDataAccessor;
            default:
                throw new NotImplementedException("Content provider with name '" + contentProviderName
                        + "' has not been implemented");
        }
    }
}
