package com.axiell.ehub.provider.routing;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Access(AccessType.PROPERTY)
public class Source implements Serializable {
    public static final int LENGTH = 255;
    private String value;

    public Source() {
    }

    public Source(String value) {
        setValue(value);
    }

    @Column(name = "SOURCE", nullable = false, unique = true, length = LENGTH)
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value == null ? null : value.toUpperCase();
    }

    @Override
    public String toString() {
        return value;
    }
}
