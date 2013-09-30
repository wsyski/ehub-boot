package com.axiell.ehub.util.strings;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.*;
import java.util.Map.Entry;

import static com.axiell.ehub.util.strings.ToString.dateToString;
import static org.apache.commons.lang3.SystemUtils.LINE_SEPARATOR;

class RecursiveToStringStyle extends ToStringStyle {
    private static final String UNKNOWN_FIELD = "?";
    private static final char MAP_START_CHAR = '{';
    private static final char EQUALS_CHAR = '=';
    private static final char MAP_END_CHAR = '}';
    private static final String EMPTY_MAP = "{}";
    private static final char COLLECTION_START_CHAR = '[';
    private static final char COLLECTION_END_CHAR = ']';
    private static final char CLASS_PREFIX_CHAR = '@';


    public RecursiveToStringStyle() {
        initializeToString();
    }

    private void initializeToString() {
        setArrayContentDetail(true);
        setUseShortClassName(true);
        setUseClassName(false);
        setUseIdentityHashCode(false);
        setFieldSeparator(", " + LINE_SEPARATOR + "  ");
    }

    @Override
    public void appendDetail(final StringBuffer buffer, final String fieldName, final Object value) {
        if (isDate(value)) {
            appendDateValue(buffer, (Date) value);
        } else if (isJavaNativeClass(value)) {
            appendJavaNativeClassValue(buffer, value);
        } else {
            appendClassValue(buffer, value);
        }
    }

    @Override
    protected void appendDetail(StringBuffer buffer, String fieldName, Map<?, ?> map) {
        if (map.isEmpty()) {
            appendEmptyMap(buffer);
        } else {
            appendValuesInMap(buffer, fieldName, map);
        }
    }

    @Override
    protected void appendDetail(final StringBuffer buffer, final String fieldName, final Collection<?> collection) {
        if (collection.isEmpty()) {
            appendEmptyCollection(buffer);
        } else {
            appendValuesInCollection(buffer, fieldName, collection);
        }
    }

    private void appendEmptyCollection(final StringBuffer buffer) {
        buffer.append("[]");
    }

    private void appendValuesInCollection(final StringBuffer buffer, final String fieldName, final Collection<?> collection) {
        buffer.append(COLLECTION_START_CHAR);
        final Iterator<?> collectionIterator = collection.iterator();
        for (; ; ) {
            Collection<Object> traversedObjects = new ArrayList<>();
            addToTraversedObjects(collection, traversedObjects);
            Object value = collectionIterator.next();
            appendCurrentValue(buffer, fieldName, value, traversedObjects);
            if (iteratorHasNoMoreEntries(collectionIterator)) {
                buffer.append(COLLECTION_END_CHAR);
                break;
            }
            appendEntrySeparator(buffer);
        }
    }

    private void appendCurrentValue(final StringBuffer buffer, final String fieldName, final Object value, final Collection<Object> traversedObjects) {
        if (isTraversed(value, traversedObjects)) {
            buffer.append(CLASS_PREFIX_CHAR).append(value.getClass().getName());
        } else {
            addToTraversedObjects(value, traversedObjects);
            appendInternal(buffer, fieldName, value, true);
        }
    }

    private void appendClassValue(final StringBuffer buffer, final Object value) {
        try {
            buffer.append(ReflectionToStringBuilder.toString(value, this));
        } catch (RuntimeException ex) {
            buffer.append(UNKNOWN_FIELD);
        }
    }

    private void appendJavaNativeClassValue(final StringBuffer buffer, final Object value) {
        try {
            buffer.append(value);
        } catch (RuntimeException ex) {
            buffer.append(UNKNOWN_FIELD);
        }
    }

    private boolean isJavaNativeClass(final Object value) {
        return value.getClass().getName().startsWith("java.");
    }

    private boolean isDate(final Object value) {
        return value instanceof Date;
    }

    private void appendDateValue(final StringBuffer buffer, final Date value) {
        try {
            buffer.append(dateToString(value));
        } catch (RuntimeException ex) {
            buffer.append(UNKNOWN_FIELD);
        }
    }


    private StringBuffer appendEmptyMap(final StringBuffer buffer) {
        return buffer.append(EMPTY_MAP);
    }

    private void appendValuesInMap(final StringBuffer buffer, final String fieldName, final Map<?, ?> map) {
        buffer.append(MAP_START_CHAR);
        final Iterator<? extends Entry<?, ?>> mapIterator = map.entrySet().iterator();
        for (; ; ) {
            Collection<Object> traversedObjects = new ArrayList<>();
            addToTraversedObjects(map, traversedObjects);
            appendCurrentEntry(buffer, fieldName, mapIterator, traversedObjects);
            if (iteratorHasNoMoreEntries(mapIterator)) {
                buffer.append(MAP_END_CHAR);
                break;
            }
            appendEntrySeparator(buffer);
        }
    }

    private void appendCurrentEntry(final StringBuffer buffer, final String fieldName, final Iterator<? extends Entry<?, ?>> mapIterator,
                                    Collection<Object> traversedObjects) {
        Entry<?, ?> currentMapEntry = mapIterator.next();
        Object currentMapKey = currentMapEntry.getKey();
        Object CurrentMapValue = currentMapEntry.getValue();
        appendValue(buffer, fieldName, currentMapKey, traversedObjects);
        buffer.append(EQUALS_CHAR);
        appendValue(buffer, fieldName, CurrentMapValue, traversedObjects);
    }

    private void appendEntrySeparator(final StringBuffer buffer) {
        buffer.append(',').append(' ');
    }

    private boolean iteratorHasNoMoreEntries(final Iterator<?> mapIterator) {
        return !mapIterator.hasNext();
    }

    private void appendValue(final StringBuffer buffer, final String fieldName, final Object currentMapKey, final Collection<Object> traversedObjects) {
        if (isTraversed(currentMapKey, traversedObjects)) {
            buffer.append(CLASS_PREFIX_CHAR).append(currentMapKey.getClass().getName());
        } else {
            addToTraversedObjects(currentMapKey, traversedObjects);
            appendInternal(buffer, fieldName, currentMapKey, true);
        }
    }

    private boolean isTraversed(final Object value, final Collection<Object> traversedObjects) {
        return traversedObjects.contains(value);
    }

    private void addToTraversedObjects(final Object value, final Collection<Object> traversedObjects) {
        if (value instanceof Collection || value instanceof Map) {
            traversedObjects.add(value);
        }
    }

    public String toString(final Map<?, ?> map) {
        StringBuffer buffer = new StringBuffer();
        appendDetail(buffer, null, map);
        return buffer.toString();
    }

    public String toString(Collection<?> collection) {
        StringBuffer buffer = new StringBuffer();
        appendDetail(buffer, null, collection);
        return buffer.toString();
    }
}