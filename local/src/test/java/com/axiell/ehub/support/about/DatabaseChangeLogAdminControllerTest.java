package com.axiell.ehub.support.about;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.any;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseChangeLogAdminControllerTest {
    private IDatabaseChangeLogAdminController underTest;

    @Mock
    private IDatabaseChangeLogRepository databaseChangeLogRepository;
    private DatabaseChangeLog change1;
    private DatabaseChangeLog change2;
    private DatabaseChangeLog actualChange;

    @Before
    public void setUp() {
        underTest = new DatabaseChangeLogAdminController();
        ReflectionTestUtils.setField(underTest, "databaseChangeLogRepository", databaseChangeLogRepository);
    }

    @Test
    public void latestDatabaseChange() {
        givenChanges();
        whenGetLatestDatabaseChange();
        thenActualChangeEqualsExpectedChange();
    }

    private void whenGetLatestDatabaseChange() {
        actualChange = underTest.getLatestDatabaseChange();
    }

    private void givenChanges() {
        change1 = new DatabaseChangeLog();
        change1.setOrderExecuted(1);

        change2 = new DatabaseChangeLog();
        change2.setOrderExecuted(2);

        final List<DatabaseChangeLog> changes = new ArrayList<>();
        changes.add(change2);
        changes.add(change1);

        given(databaseChangeLogRepository.findAll(any(Sort.class))).willReturn(changes);
    }

    private void thenActualChangeEqualsExpectedChange() {
        Assert.assertEquals(change2, actualChange);
    }

    @Test
    public void testEmptyReleaseTableException() {
        givenNoChanges();
        whenGetLatestDatabaseChange();
        thenActualChangeIsNull();
    }

    private void givenNoChanges() {
        final List<DatabaseChangeLog> changes = new ArrayList<>();
        given(databaseChangeLogRepository.findAll(any(Sort.class))).willReturn(changes);
    }

    private void thenActualChangeIsNull() {
        Assert.assertNull(actualChange);
    }
}
