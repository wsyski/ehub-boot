package com.axiell.ehub.common.logging;

public class TestPojo {
    private final String aProperty;
    private final String anotherProperty;

    public TestPojo(final String aProperty, final String anotherProperty) {
        this.aProperty = aProperty;
        this.anotherProperty = anotherProperty;
    }

    public String getaProperty() {
        return aProperty;
    }

    public String getAnotherProperty() {
        return anotherProperty;
    }
}
