package com.axiell.ehub.provider.routing;

import java.util.List;

public interface IRoutingAdminController {

    List<RoutingRule> getRoutingRules();

    RoutingRule save(RoutingRule routingRule);

    boolean existsRoutingRuleWith(String source);

    void delete(RoutingRule routingRule);
}
