package com.axiell.ehub.core.controller.external.v5_0.provider;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.common.controller.external.v5_0.provider.IContentProvidersResource;
import com.axiell.ehub.common.controller.external.v5_0.provider.IRecordsResource;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.ContentProviderDTO;
import com.axiell.ehub.common.controller.external.v5_0.provider.dto.ContentProvidersDTO;
import com.axiell.ehub.common.provider.alias.AliasMappings;
import com.axiell.ehub.core.provider.alias.IAliasBusinessController;
import com.axiell.ehub.core.provider.record.issue.IIssueBusinessController;

public class ContentProvidersResource implements IContentProvidersResource {

    private final IIssueBusinessController issueBusinessController;
    private final IAliasBusinessController aliasBusinessController;

    public ContentProvidersResource(final IIssueBusinessController issueBusinessController, final IAliasBusinessController aliasBusinessController) {
        this.issueBusinessController = issueBusinessController;
        this.aliasBusinessController = aliasBusinessController;
    }

    @Override
    public ContentProvidersDTO root() {
        return AliasMappings.fromAliasMappingCollection(aliasBusinessController.getAliasMappings()).toContentProvidersDTO();
    }

    @Override
    public ContentProviderDTO getContentProvider(final AuthInfo authInfo, final String contentProviderAlias) {
        AliasMappings aliasMappings = AliasMappings.fromAliasMappingCollection(aliasBusinessController.getAliasMappings());
        ContentProvidersDTO contentProvidersDTO = aliasMappings.toContentProvidersDTO();
        String contentProviderAliasUppercase = contentProviderAlias.toUpperCase();
        return contentProvidersDTO.getContentProviders().stream()
                .filter(contentProviderDTO -> (contentProviderDTO.getName().equals(contentProviderAliasUppercase) || contentProviderDTO.getAliases().contains(contentProviderAliasUppercase)))
                .findFirst()
                .orElse(null);
    }

    @Override
    public IRecordsResource getRecordsResource(final String contentProviderAlias) {
        return new RecordsResource(issueBusinessController, contentProviderAlias);
    }
}
