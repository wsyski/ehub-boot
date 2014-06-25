package com.axiell.ehub.provider.alias;

import java.util.List;

public interface IAliasAdminController {

    List<AliasMapping> getRoutingRules();

    AliasMapping save(AliasMapping aliasMapping);

    boolean existsAlias(String alias);

    void delete(AliasMapping aliasMapping);
}
