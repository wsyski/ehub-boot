/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.util;

import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.util.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * 
 */
public class ToStringTest {

    /**
     * Test method for {@link HashFunction#hmacSha1(byte[], byte[])}.
     * @throws java.io.UnsupportedEncodingException
     */
    @Test
    public void testMapToString() throws UnsupportedEncodingException {
        Map<String,String> map1=new LinkedHashMap<>();
        map1.put("oneKey","oneValue");
        map1.put("twoKey","twoValue");
        map1.put("threeKey","threeValue");
        String toString=ToString.fromMap(map1);
        System.out.println(toString);
        Assert.assertEquals("{oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}", toString);
        Map<String,Map<String,String>> map2=new LinkedHashMap<>();
        map2.put("oneMap", map1);
        map2.put("twoMap", map1);
        toString=ToString.fromMap(map2);
        System.out.println(toString);
        Assert.assertEquals("{oneMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}, twoMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}}", toString);
        Collection collection=Arrays.asList("col1","col2","col3");
        toString=ToString.fromCollection(collection);
        System.out.println(toString);
        Assert.assertEquals("[col1, col2, col3]", toString);
        TestBean2 testBean2 =new TestBean2();
        testBean2.setField21(map2);
        testBean2.setField22(collection);
        TestBean1 testBean1 =new TestBean1();
        testBean1.setField11(map1);
        testBean1.setField12(collection);
        testBean1.setField13(testBean2);
        testBean1.setField14(new Date(0L));
        toString=ToString.fromObject(testBean1);
        System.out.println(toString);
        Assert.assertEquals("[field11={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}, \n" +
                "  field12=[col1, col2, col3], \n" +
                "  field13=[field21={oneMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}, twoMap={oneKey=oneValue, twoKey=twoValue, threeKey=threeValue}}, \n" +
                "  field22=[col1, col2, col3]], \n" +
                "  field14=1970-01-01 01:00:00]", toString);
    }

    private static class TestBean1 {
        Map field11;
        Collection field12;
        TestBean2 field13;
        Date field14;

        public Map getField11() {
            return field11;
        }

        public void setField11(Map field11) {
            this.field11 = field11;
        }

        public Collection getField12() {
            return field12;
        }

        public void setField12(Collection field12) {
            this.field12 = field12;
        }

        public TestBean2 getField13() {
            return field13;
        }

        public void setField13(TestBean2 field13) {
            this.field13 = field13;
        }

        public Date getField14() {
            return field14;
        }

        public void setField14(Date field14) {
            this.field14 = field14;
        }
    }

    private static class TestBean2 {
        Map field21;
        Collection field22;

        public Map getField21() {
            return field21;
        }

        public void setField21(Map field21) {
            this.field21 = field21;
        }

        public Collection getField22() {
            return field22;
        }

        public void setField22(Collection field22) {
            this.field22 = field22;
        }
    }

}
