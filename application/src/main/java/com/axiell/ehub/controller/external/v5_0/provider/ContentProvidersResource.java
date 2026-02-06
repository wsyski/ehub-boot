package com.axiell.ehub.controller.external.v5_0.provider;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.controller.external.v5_0.provider.dto.ContentProviderDTO;

public class ContentProvidersResource implements IContentProvidersResource {


    @Override
    public ContentProviderDTO getContentProvider(final AuthInfo authInfo, final String contentProviderAlias) {
        return null;
    }

    @Override
    public IRecordsResource getRecordsResource(final String contentProviderAlias) {
        return null;
    }
}
