package com.axiell.ehub.support.about;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IDatabaseChangeLogRepository extends JpaRepository<DatabaseChangeLog, String> {
}
