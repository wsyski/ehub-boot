package com.axiell.ehub.provider.alias;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
class AliasAdminController implements IAliasAdminController {

    @Autowired
    private IAliasMappingRepository aliasMappingRepository;

    @Override
    @Transactional(readOnly = true)
    public List<AliasMapping> getRoutingRules() {
        return aliasMappingRepository.findAllOrderByTarget();
    }

    @Override
    @Transactional(readOnly = false)
    public AliasMapping save(AliasMapping aliasMapping) {
        return aliasMappingRepository.save(aliasMapping);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsAlias(String alias) {
        final AliasMapping aliasMapping = aliasMappingRepository.findByAlias(Alias.newInstance(alias));
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
    };
}
