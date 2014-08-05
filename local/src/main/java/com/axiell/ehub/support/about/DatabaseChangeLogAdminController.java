package com.axiell.ehub.support.about;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.springframework.data.domain.Sort.Direction.DESC;

public class DatabaseChangeLogAdminController implements IDatabaseChangeLogAdminController {

    @Autowired(required = true)
    private IDatabaseChangeLogRepository databaseChangeLogRepository;

    @Override
    @Transactional(readOnly = true)
    public DatabaseChangeLog getLatestDatabaseChange() {
        final Sort sortByOrderExecutedDesc = new Sort(DESC, "orderExecuted");
        final List<DatabaseChangeLog> changeLogs = databaseChangeLogRepository.findAll(sortByOrderExecutedDesc);
        return changeLogs.isEmpty() ? null : changeLogs.iterator().next();
    }
}
