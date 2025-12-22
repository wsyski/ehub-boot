/*
 * Copyright 2008-2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.axiell.ehub;

import org.springframework.data.domain.Persistable;

import java.io.Serializable;
import java.time.Instant;

/**
 * Interface for timestamp aware entities. Allows storing and retrieving creation and modification information.
 *
 * @param <I> the type of the timestamp aware type's idenifier
 */
public interface ITimestampAware<I extends Serializable> extends Persistable<I> {

    /**
     * Returns the creation date of the entity.
     *
     * @return the createdDate
     */
    Instant getCreatedDate();

    /**
     * Sets the creation date of the entity.
     *
     * @param createdDate the creation date to set
     */
    void setCreatedDate(final Instant createdDate);

    /**
     * Returns the date of the last modification.
     *
     * @return the modifiedDate
     */
    Instant getModifiedDate();

    /**
     * Sets the date of the last modification.
     *
     * @param modifiedDate the date of the last modification to set
     */
    void setModifiedDate(final Instant modifiedDate);
}
