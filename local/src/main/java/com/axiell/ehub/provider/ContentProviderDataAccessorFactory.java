package com.axiell.ehub.provider;

import com.axiell.ehub.provider.askews.AskewsDataAccessor;
import com.axiell.ehub.provider.borrowbox.BorrowBoxDataAccessor;
import com.axiell.ehub.provider.elib.elibu.ElibUDataAccessor;
import com.axiell.ehub.provider.elib.library3.Elib3DataAccessor;
import com.axiell.ehub.provider.ep.EpDataAccessor;
import com.axiell.ehub.provider.f1.F1DataAccessor;
import com.axiell.ehub.provider.ocd.OcdDataAccessor;
import com.axiell.ehub.provider.overdrive.OverDriveDataAccessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ContentProviderDataAccessorFactory implements IContentProviderDataAccessorFactory {

    @Autowired
    private Elib3DataAccessor elib3DataAccessor;

    @Autowired
    private ElibUDataAccessor elibUDataAccessor;

    @Autowired
    private AskewsDataAccessor askewsDataAccessor;

    @Autowired
    private OverDriveDataAccessor overDriveDataAccessor;

    @Autowired
    private F1DataAccessor f1DataAccessor;

    @Autowired
    private OcdDataAccessor ocdDataAccessor;

    @Autowired
    private BorrowBoxDataAccessor borrowBoxDataAccessor;

    @Autowired
    private EpDataAccessor epDataAccessor;

    @Override
    public IContentProviderDataAccessor getInstance(final String contentProviderName) {
        if (ContentProvider.CONTENT_PROVIDER_ELIB3.equals(contentProviderName)) {
            return elib3DataAccessor;
        } else if (ContentProvider.CONTENT_PROVIDER_ELIBU.equals(contentProviderName)) {
            return elibUDataAccessor;
        } else if (ContentProvider.CONTENT_PROVIDER_ASKEWS.equals(contentProviderName)) {
            return askewsDataAccessor;
        } else if (ContentProvider.CONTENT_PROVIDER_OVERDRIVE.equals(contentProviderName)) {
            return overDriveDataAccessor;
        } else if (ContentProvider.CONTENT_PROVIDER_F1.equals(contentProviderName)) {
            return f1DataAccessor;
        } else if (ContentProvider.CONTENT_PROVIDER_BORROWBOX.equals(contentProviderName)) {
            return borrowBoxDataAccessor;
        } else if (ContentProvider.CONTENT_PROVIDER_OCD.equals(contentProviderName)) {
            return ocdDataAccessor;
        } else {
            return epDataAccessor;
        }
    }
}
