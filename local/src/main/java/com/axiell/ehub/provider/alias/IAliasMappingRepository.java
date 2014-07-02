package com.axiell.ehub.provider.alias;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface IAliasMappingRepository extends CrudRepository<AliasMapping, Long> {

    AliasMapping findByAlias(Alias alias);

    @Query("SELECT r FROM AliasMapping r ORDER BY r.name ASC")
    List<AliasMapping> findAllOrderByTarget();
}
