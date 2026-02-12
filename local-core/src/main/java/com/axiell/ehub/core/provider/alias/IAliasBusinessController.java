package com.axiell.ehub.core.provider.alias;

import com.axiell.ehub.common.provider.alias.AliasMapping;

import java.util.Set;

public interface IAliasBusinessController {

    String getName(String alias);

    Set<AliasMapping> getAliasMappings();
}
