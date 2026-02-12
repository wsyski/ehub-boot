package com.axiell.ehub.mock.provider.alias;

import com.axiell.ehub.common.controller.external.v5_0.provider.dto.ContentProvidersDTO;
import com.axiell.ehub.common.provider.alias.Alias;
import com.axiell.ehub.common.provider.alias.AliasMapping;
import com.axiell.ehub.common.provider.alias.AliasMappings;
import com.axiell.ehub.core.error.IEhubExceptionFactory;
import com.axiell.ehub.core.provider.alias.IAliasBusinessController;
import com.axiell.ehub.mock.AbstractBusinessController;
import com.axiell.ehub.mock.util.EhubMessageUtility;
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
        ContentProvidersDTO contentProvidersDTO = ehubMessageUtility.getEhubMessage(ContentProvidersDTO.class, "content-providers");
        return getAliasMappings(AliasMappings.fromContentProvidersDTO(contentProvidersDTO));
    }

    private Set<AliasMapping> getAliasMappings(final AliasMappings aliasMappings) {
        return aliasMappings.entrySet().stream().flatMap(entry -> entry.getValue().stream().map(value -> {
            Alias alias = new Alias();
            alias.setValue(value);
            AliasMapping aliasMapping = new AliasMapping();
            aliasMapping.setAlias(alias);
            aliasMapping.setName(entry.getKey());
            return aliasMapping;
        })).collect(Collectors.toSet());
    }

    public String getName(final String alias) {
        return "ELIB3";
    }

}
