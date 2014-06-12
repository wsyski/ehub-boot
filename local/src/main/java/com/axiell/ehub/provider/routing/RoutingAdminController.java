package com.axiell.ehub.provider.routing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
class RoutingAdminController implements IRoutingAdminController {

    @Autowired
    private IRoutingRuleRepository routingRuleRepository;

    @Override
    @Transactional(readOnly = true)
    public List<RoutingRule> getRoutingRules() {
        return routingRuleRepository.findAllOrderByTarget();
    }

    @Override
    @Transactional(readOnly = false)
    public RoutingRule save(RoutingRule routingRule) {
        return routingRuleRepository.save(routingRule);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsRoutingRuleWith(String source) {
        final RoutingRule routingRule = routingRuleRepository.findBySource(new Source(source));
        return routingRule != null;
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(RoutingRule routingRule) {
        routingRuleRepository.delete(routingRule);
    }
}
