package com.axiell.ehub.local.provider.alias;

import com.axiell.ehub.common.BadRequestException;
import com.axiell.ehub.common.ErrorCauseArgument;
import com.axiell.ehub.common.NotFoundException;
import com.axiell.ehub.common.provider.alias.Alias;
import com.axiell.ehub.common.provider.alias.AliasMapping;
import com.axiell.ehub.core.provider.alias.IAliasBusinessController;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

import static com.axiell.ehub.common.ErrorCause.MISSING_CONTENT_PROVIDER_NAME;
import static com.axiell.ehub.common.ErrorCause.UNKNOWN_CONTENT_PROVIDER;
import static com.axiell.ehub.common.ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME;

@Component
public class AliasBusinessController implements IAliasBusinessController {

    @Autowired
    private IAliasMappingRepository aliasMappingRepository;

    @Override
    public Set<AliasMapping> getAliasMappings() {
        Iterable<AliasMapping> aliasMappings = aliasMappingRepository.findAll();
        return Sets.newHashSet(aliasMappings);
    }

    @Override
    @Transactional(readOnly = true)
    public String getName(final String alias) {
        validateAlias(alias);
        final AliasMapping aliasMapping = aliasMappingRepository.findOneByAlias(Alias.newInstance(alias));
        validateMapping(alias, aliasMapping);
        return aliasMapping.getName();
    }

    private void validateAlias(final String source) {
        if (source == null)
            throw new BadRequestException(MISSING_CONTENT_PROVIDER_NAME);
    }

    private void validateMapping(final String alias, final AliasMapping aliasMapping) {
        if (aliasMapping == null) {
            final ErrorCauseArgument argument = new ErrorCauseArgument(CONTENT_PROVIDER_NAME, alias);
            throw new NotFoundException(UNKNOWN_CONTENT_PROVIDER, argument);
        }
    }
}
