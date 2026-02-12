package com.axiell.ehub.local.provider.record.format;

import com.axiell.ehub.common.provider.record.format.FormatDecoration;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface IFormatDecorationRepository extends CrudRepository<FormatDecoration, Long> {

    @Query("select fd from FormatDecoration fd where fd.contentProvider.id = :contentProviderId and fd.contentProviderFormatId = :contentProviderFormatId")
    FormatDecoration findOneByContentProviderIdAndContentProviderFormatId(@Param("contentProviderId") Long ehubConsumerId, @Param("contentProviderFormatId") String contentProviderFormatId);
}
