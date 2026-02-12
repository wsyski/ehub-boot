package com.axiell.ehub.common.provider.alias;

import com.axiell.ehub.common.controller.external.v5_0.provider.dto.AliasMappingDTO;
import com.axiell.ehub.common.provider.ContentProvider;

public class AliasMappingDTOBuilder {
    public static final String ALIAS = "DISTRIBUT\u00d6R ELIB";
    public static final String NAME = ContentProvider.CONTENT_PROVIDER_ELIB3;

    public static AliasMappingDTO aliasMappingDTO() {
        return new AliasMappingDTO(ALIAS, NAME);
    }
}
