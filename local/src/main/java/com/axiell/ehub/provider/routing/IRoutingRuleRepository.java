package com.axiell.ehub.provider.routing;

import com.axiell.ehub.provider.routing.RoutingRule;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IRoutingRuleRepository extends CrudRepository<RoutingRule, Long> {

    RoutingRule findBySource(Source source);

    @Query("SELECT r FROM RoutingRule r ORDER BY r.target ASC")
    List<RoutingRule> findAllOrderByTarget();
}
