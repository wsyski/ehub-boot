package com.axiell.ehub.provider.alias;

import com.axiell.ehub.controller.external.v5_0.provider.dto.AliasMappingDTO;
import com.axiell.ehub.controller.external.v5_0.provider.dto.ContentProviderDTO;
import com.axiell.ehub.controller.external.v5_0.provider.dto.ContentProvidersDTO;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class AliasMappings extends HashMap<String, Set<String>> {

    private AliasMappings(Map<? extends String, ? extends Set<String>> aliasMappings) {
        super(aliasMappings);
    }

    public ContentProvidersDTO toContentProvidersDTO() {
        return new ContentProvidersDTO(entrySet().stream().map(entry -> new ContentProviderDTO(entry.getKey(), entry.getValue())).collect(Collectors.toList()));
    }

    public static AliasMappings fromContentProvidersDTO(final ContentProvidersDTO contentProviders) {
        return new AliasMappings(contentProviders.getContentProviders().stream().collect(Collectors.toMap(ContentProviderDTO::getName,
                ContentProviderDTO::getAliases)));
    }

    public static AliasMappings fromAliasMappingDTOCollection(final Collection<AliasMappingDTO> aliasMappings) {
        return new AliasMappings(aliasMappings.stream().collect(Collectors.groupingBy(AliasMappingDTO::getName,
                Collectors.mapping(AliasMappingDTO::getAlias, Collectors.toSet()))));
    }

    public static AliasMappings fromAliasMappingCollection(final Collection<AliasMapping> aliasMappings) {
        return new AliasMappings(aliasMappings.stream().collect(Collectors.groupingBy(AliasMapping::getName,
                Collectors.mapping(aliasMapping -> aliasMapping.getAlias().getValue(), Collectors.toSet()))));
    }
}
