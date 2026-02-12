package com.axiell.ehub.common.util;

import java.util.Collection;

public class CollectionFinder<T> implements IFinder<T, Collection<T>> {

    public T find(final IMatcher<T> matcher, final Collection<T> collection) throws NotFoundException {
        if (collection != null) {
            for (T object : collection) {
                if (matcher.matches(object))
                    return object;
            }
        }
        throw new NotFoundException();
    }
}
