package com.axiell.ehub.logging;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class ToStringTest {

    @Test
    public void handleString() {
        String s = whenToStringExecuted("test");
        thenExpectedStringReturned("test", s);
    }


    @Test
    public void handleList() {
        String s = whenToStringExecuted(Arrays.asList("e0", "e1", "e2"));
        thenExpectedStringReturned("{e0,e1,e2}", s);
    }

    @Test
    public void handleSet() {
        String s = whenToStringExecuted(new LinkedHashSet<>(Arrays.asList("e0", "e1", "e2")));
        thenExpectedStringReturned("{e0,e1,e2}", s);
    }

    @Test
    public void handleArray() {
        String s = whenToStringExecuted(new String[]{"e0", "e1", "e2"});
        thenExpectedStringReturned("{e0,e1,e2}", s);
    }

    @Test
    public void handleMap() {
        String s = whenToStringExecuted(Collections.singletonMap("k", "v"));
        thenExpectedStringReturned("{k=v}", s);
    }

    @Test
    public void handlePojo() {
        String s = whenToStringExecuted(new TestPojo("p0", "p1"));
        thenExpectedStringReturned("[p0,p1,{p1,p0},{p0=p1}]", s);
    }

    @Test
    public void handleDate() {
        String s = whenToStringExecuted(new Date(0));
        Assert.assertThat(s, Matchers.containsString("1970"));
    }

    private String whenToStringExecuted(final Object object) {
        return ToString.toString(object);
    }

    private void thenExpectedStringReturned(final String expectedString, final String actualString) {
        Assert.assertEquals(expectedString, actualString);
    }

    private class TestPojo {
        private final String property0;
        private final String property1;
        private final Set<String> propertiesAsSet;
        private final Map<String, String> propertiesAsMap;

        public TestPojo(final String property0, final String property1) {
            this.property0 = property0;
            this.property1 = property1;
            this.propertiesAsSet = new HashSet<>(Arrays.asList(property0, property1));
            this.propertiesAsMap = Collections.singletonMap(property0, property1);
        }

        public String getProperty0() {
            return property0;
        }

        public String getProperty1() {
            return property1;
        }

        public Set<String> getPropertiesAsSet() {
            return propertiesAsSet;
        }

        public Map<String, String> getPropertiesAsMap() {
            return propertiesAsMap;
        }
    }

}
