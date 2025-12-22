/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.util;

import com.axiell.ehub.ITimestampAware;
import lombok.extern.slf4j.Slf4j;

import  jakarta.persistence.PrePersist;
import  jakarta.persistence.PreUpdate;
import java.time.Instant;

/**
 * JPA entity listener to capture auditing information on persiting and updating entities. To get this one flying be
 * sure you configure it as entity listener in your {@code orm.xml} as follows:
 * <p/>
 * <pre>
 * &lt;persistence-unit-metadata&gt;
 *     &lt;persistence-unit-defaults&gt;
 *         &lt;entity-listeners&gt;
 *             &lt;entity-listener class="com.axiell.axiell.util.TimestampAwareEntityListener" /&gt;
 *         &lt;/entity-listeners&gt;
 *     &lt;/persistence-unit-defaults&gt;
 * &lt;/persistence-unit-metadata&gt;
 * </pre>
 */
@Slf4j
public class TimestampAwareEntityListener {

    /**
     * Sets modification and creation date and auditor on the target object in case it implements {@link ITimestampAware} on
     * persist events.
     *
     * @param target
     */
    @PrePersist
    public void touchForCreate(final Object target) {
        touch(target, true);
    }

    /**
     * Sets modification and creation date and auditor on the target object in case it implements {@link ITimestampAware} on
     * update events.
     *
     * @param target
     */
    @PreUpdate
    public void touchForUpdate(final Object target) {
        touch(target, false);
    }

    private void touch(final Object target, final boolean isNew) {
        if (!(target instanceof ITimestampAware)) {
            return;
        }
        ITimestampAware<?> timestampAware = (ITimestampAware<?>) target;
        Instant now = touchDate(timestampAware, isNew);
        logTime(isNew, timestampAware, now);
    }

    private void logTime(boolean isNew, ITimestampAware<?> timestampAware, Instant now) {
        if (isNew) {
            log.debug("Pre-persist {} - at {}", timestampAware, now);
        } else {
            log.debug("Pre-update {} - at {}", timestampAware, now);
        }
    }


    /**
     * Touches the timestamp aware regarding modification and created date. Created date is only set on new timestamp aware.
     *
     * @param timeStampAware time stamp aware entity.
     * @param isNew          true if a new entity.
     * @return the touched timestamp.
     */
    private Instant touchDate(final ITimestampAware<?> timeStampAware, final boolean isNew) {
        Instant now = Instant.now();
        if (isNew) {
            timeStampAware.setCreatedDate(now);
        }
        timeStampAware.setModifiedDate(now);
        return now;
    }
}
