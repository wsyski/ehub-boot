package com.axiell.ehub.provider;

import com.axiell.authinfo.AuthInfo;
import com.axiell.ehub.NotImplementedException;
import com.axiell.ehub.provider.alias.AliasMapping;
import com.axiell.ehub.provider.alias.AliasMappingDTO;
import com.axiell.ehub.provider.alias.AliasMappingsDTO;
import com.axiell.ehub.provider.alias.IAliasBusinessController;
import com.axiell.ehub.provider.record.IRecordsResource;
import com.axiell.ehub.provider.record.RecordsResource;
import com.axiell.ehub.provider.record.issue.IIssueBusinessController;

import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Set;
import java.util.stream.Collectors;

@Produces(MediaType.APPLICATION_JSON)
public class ContentProvidersResource implements IContentProvidersResource {

    private final IIssueBusinessController issueBusinessController;
    private final IAliasBusinessController aliasBusinessController;

    public ContentProvidersResource(final IIssueBusinessController issueBusinessController, final IAliasBusinessController aliasBusinessController) {
        this.issueBusinessController = issueBusinessController;
        this.aliasBusinessController = aliasBusinessController;
    }

    @Override
    public AliasMappingsDTO root() {
        return getAliasMappingsDTO(aliasBusinessController.getAliasMappings());
    }

    @Override
    public ContentProviderDTO getContentProvider(AuthInfo authInfo, String contentProviderAlias) {
        throw new NotImplementedException("Get content provider path in ContentProvidersResource has not been implemented yet");
    }

    @Override
    public IRecordsResource records(String contentProviderAlias) {
        return new RecordsResource(issueBusinessController, contentProviderAlias);
    }

    private AliasMappingsDTO getAliasMappingsDTO(final Set<AliasMapping> aliasMappings) {
        AliasMappingsDTO aliasMappingsDTO = null;
        if (aliasMappings != null) {
            aliasMappingsDTO = new AliasMappingsDTO(aliasMappings.stream()
                    .map(aliasMapping -> new AliasMappingDTO(aliasMapping.getAlias().getValue(), aliasMapping.getName())).collect(Collectors.toSet()));
        }
        return aliasMappingsDTO;
    }
}
