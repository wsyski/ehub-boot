package com.axiell.ehub.provider.alias;


import java.util.Set;

public interface IAliasBusinessController {

    String getName(String alias);

    Set<String> getAliases();
}
