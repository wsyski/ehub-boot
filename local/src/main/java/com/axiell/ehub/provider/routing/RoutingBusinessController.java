package com.axiell.ehub.provider.routing;

import com.axiell.ehub.BadRequestException;
import com.axiell.ehub.ErrorCauseArgument;
import com.axiell.ehub.NotFoundException;
import com.axiell.ehub.provider.ContentProviderName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.axiell.ehub.ErrorCause.MISSING_CONTENT_PROVIDER_NAME;
import static com.axiell.ehub.ErrorCause.UNKNOWN_CONTENT_PROVIDER;
import static com.axiell.ehub.ErrorCauseArgument.Type.CONTENT_PROVIDER_NAME;

@Component
class RoutingBusinessController implements IRoutingBusinessController {
    @Autowired
    private IRoutingRuleRepository routingRuleRepository;

    @Override
    @Transactional(readOnly = true)
    public ContentProviderName getTarget(final String source) {
        validateSource(source);
        final RoutingRule routingRule = routingRuleRepository.findBySource(new Source(source));
        validateRoutingRule(source, routingRule);
        return routingRule.getTarget();
    }

    private void validateSource(final String source) {
        if (source == null)
            throw new BadRequestException(MISSING_CONTENT_PROVIDER_NAME);
    }

    private void validateRoutingRule(final String source, final RoutingRule routingRule) {
        if (routingRule == null) {
            final ErrorCauseArgument argument = new ErrorCauseArgument(CONTENT_PROVIDER_NAME, source);
            throw new NotFoundException(UNKNOWN_CONTENT_PROVIDER, argument);
        }
    }
}
