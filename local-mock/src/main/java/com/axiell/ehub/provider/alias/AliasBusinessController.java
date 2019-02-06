package com.axiell.ehub.provider.alias;

import com.axiell.ehub.AbstractBusinessController;
import com.axiell.ehub.error.IEhubExceptionFactory;
import com.axiell.ehub.util.EhubMessageUtility;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
class AliasBusinessController extends AbstractBusinessController implements IAliasBusinessController {
    @Autowired
    private IEhubExceptionFactory ehubExceptionFactory;

    @Autowired
    private EhubMessageUtility ehubMessageUtility;

    @Override
    public Set<AliasMapping> getAliasMappings() {
        AliasMappingsDTO aliasMappingsDTO = ehubMessageUtility.getEhubMessage(AliasMappingsDTO.class, "alias-mappings");
        return getAliasMappings(aliasMappingsDTO);
    }

    private Set<AliasMapping> getAliasMappings(final AliasMappingsDTO aliasMappingsDTO) {
        return aliasMappingsDTO.toDTO().stream().map(aliasMappingDTO -> {
            Alias alias = new Alias();
            alias.setValue(aliasMappingDTO.getAlias());
            AliasMapping aliasMapping = new AliasMapping();
            aliasMapping.setAlias(alias);
            aliasMapping.setName(aliasMappingDTO.getName());
            return aliasMapping;
        }).collect(Collectors.toSet());
    }

    public String getName(final String alias) {
        return "ELIB3";
    }

}
