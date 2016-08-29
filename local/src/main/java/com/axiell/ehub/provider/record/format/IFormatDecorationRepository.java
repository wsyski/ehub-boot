package com.axiell.ehub.provider.record.format;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IFormatDecorationRepository extends CrudRepository<FormatDecoration, Long> {

    @Query("select fd from FormatDecoration fd where fd.contentProvider.id = :contentProviderId and fd.contentProviderFormatId = :contentProviderFormatId")
    FormatDecoration findOneByContentProviderIdAndContentProviderFormatId(@Param("contentProviderId") Long ehubConsumerId, @Param("contentProviderFormatId") String contentProviderFormatId);
}
