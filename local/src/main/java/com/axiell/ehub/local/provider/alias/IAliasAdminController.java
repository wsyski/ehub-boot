package com.axiell.ehub.local.provider.alias;

import com.axiell.ehub.common.provider.alias.AliasMapping;

import java.util.List;

public interface IAliasAdminController {

    List<AliasMapping> getRoutingRules();

    AliasMapping save(AliasMapping aliasMapping);

    boolean existsAlias(String alias);

    void delete(AliasMapping aliasMapping);

    void deleteByContentProviderName(String contentProviderName);
}
