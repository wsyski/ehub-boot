package com.axiell.ehub.local.provider.alias;

import com.axiell.ehub.common.provider.alias.Alias;
import com.axiell.ehub.common.provider.alias.AliasMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
public class AliasAdminController implements IAliasAdminController {

    @Autowired
    private IAliasMappingRepository aliasMappingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AliasMapping> getRoutingRules() {
        return aliasMappingRepository.findAllOrderByName();
    }

    @Override
    @Transactional(readOnly = false)
    public AliasMapping save(AliasMapping aliasMapping) {
        return aliasMappingRepository.save(aliasMapping);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsAlias(String alias) {
        final AliasMapping aliasMapping = aliasMappingRepository.findOneByAlias(Alias.newInstance(alias));
        return aliasMapping != null;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(AliasMapping aliasMapping) {
        aliasMappingRepository.delete(aliasMapping);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteByContentProviderName(final String contentProviderName) {
        aliasMappingRepository.deleteByContentProviderName(contentProviderName);
    }

    ;
}
