package com.axiell.ehub.logging;

import com.axiell.ehub.logging.RecursiveToStringStyle;
import com.google.common.collect.Lists;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(MockitoJUnitRunner.class)
public class RecursiveToStringStyleTest {
    private RecursiveToStringStyle underTest = new RecursiveToStringStyle();
    private Map mapWithValues;
    private Collection collectionWithValues;
    private StringBuffer stringBuffer;
    private String result;

    @Before
    public void setUp() {
        collectionWithValues = new ArrayList();
        mapWithValues = new HashMap();
    }

    @Test
    public void stringMapToString() {
        givenThatMapHasOnlyStringValues();
        whenMapDetailIsAppended();
        thenResultHasThreeKeyValuePairsInCorrectFormatAndValues();
    }

    @Test
    public void objectMapToString() {
        givenThatMapHasDifferentObjectValues();
        whenMapDetailIsAppended();
        thenResultHasDifferentFormats();
    }

    @Ignore("Recursiveness (itself inside the map) not handled correctly")
    @Test
    public void mapContainingItselfToString() {
        givenMapContainsItself();
        whenMapDetailIsAppended();
        thenRecursivenessIsHandledCorrectly();
    }

    @Test
    public void emptyMapToString() {
        whenMapDetailIsAppended();
        thenBufferHasEmptyMapResult();
    }


    @Test
    public void stringCollectionToString() {
        givenCollectionHasThreeStrings();
        whenCollectionIsAppended();
        thenBufferContainsStringsInOrderInCorrectFormat();
    }

    @Test
    public void emptyCollectionToString() {
        whenCollectionIsAppended();
        thenBufferHasEmptyCollectionResult();
    }

    @SuppressWarnings("unchecked")
    @Ignore("Does not handle recursiveness with itself as it should")
    @Test
    public void collectionWithItselfToString() {
        givenCollectionContainsItself();
        whenCollectionIsAppended();
        thenCollectionHandledRecursivenessCorrectly();
    }

    private void thenRecursivenessIsHandledCorrectly() {
        assertEquals("{This map}", stringBuffer.toString());
    }

    private Object givenMapContainsItself() {
        return mapWithValues.put("key", mapWithValues);
    }

    private void thenCollectionHandledRecursivenessCorrectly() {
        assertEquals("[(this Collection)]", stringBuffer.toString());
    }

    private void givenCollectionContainsItself() {
        collectionWithValues = Lists.newArrayList();
        collectionWithValues.add(collectionWithValues);
    }


    private void thenBufferHasEmptyCollectionResult() {
        assertEquals("[]", stringBuffer.toString());
    }

    private void thenBufferHasEmptyMapResult() {
        assertEquals("{}", stringBuffer.toString());
    }

    private void thenBufferContainsStringsInOrderInCorrectFormat() {
        assertEquals("[one, two, three]", stringBuffer.toString());
    }

    private void whenCollectionIsAppended() {
        stringBuffer = new StringBuffer();
        underTest.appendDetail(stringBuffer, null, collectionWithValues);
    }

    private void givenCollectionHasThreeStrings() {
        collectionWithValues = Lists.newArrayList("one", "two", "three");
    }

    private void thenResultHasThreeKeyValuePairsInCorrectFormatAndValues() {
        assertTrue("Buffer did not match the expected format!", stringBuffer.toString().matches("\\{.{4}=.{6}, .{4}=.{6}, .{4}=.{6}\\}"));
        assertTrue(stringBuffer.toString().contains("key1=value1"));
        assertTrue(stringBuffer.toString().contains("key3=value3"));
        assertTrue(stringBuffer.toString().contains("key2=value2"));
    }

    private void thenResultHasDifferentFormats() {
        final String s = stringBuffer.toString();
        assertTrue(s.contains("key1=value1"));
    }

    private void whenMapDetailIsAppended() {
        stringBuffer = new StringBuffer();
        underTest.appendDetail(stringBuffer, null, mapWithValues);
    }

    private void givenThatMapHasOnlyStringValues() {
        mapWithValues = new HashMap();
        mapWithValues.put("key1", "value1");
        mapWithValues.put("key2", "value2");
        mapWithValues.put("key3", "value3");
    }

    private void givenThatMapHasDifferentObjectValues() {
        mapWithValues = new HashMap();
        mapWithValues.put("key1", "value1");
        mapWithValues.put("key2", new TestPojo("property1", "property2"));
        mapWithValues.put("key3", new TestPojo("property3", "property4"));
    }


}
