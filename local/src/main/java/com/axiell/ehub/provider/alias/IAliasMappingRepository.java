package com.axiell.ehub.provider.alias;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IAliasMappingRepository extends CrudRepository<AliasMapping, Long> {

    AliasMapping findOneByAlias(Alias alias);

    @Query("SELECT a FROM AliasMapping a ORDER BY a.name ASC")
    List<AliasMapping> findAllOrderByName();


    @Modifying
    @Query("DELETE FROM AliasMapping a WHERE a.name = :contentProviderName")
    void deleteByContentProviderName(@Param("contentProviderName") String contentProviderName);
}
