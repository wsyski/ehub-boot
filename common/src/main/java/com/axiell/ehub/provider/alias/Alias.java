package com.axiell.ehub.provider.alias;

import  jakarta.persistence.Access;
import  jakarta.persistence.AccessType;
import  jakarta.persistence.Column;
import  jakarta.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Access(AccessType.PROPERTY)
public class Alias implements Serializable {
    public static final int LENGTH = 255;
    private String value;

    public Alias() {
    }

    private Alias(String alias) {
        setValue(alias);
    }

    public static Alias newInstance(String alias) {
        return new Alias(alias);
    }

    @Column(name = "ALIAS", nullable = false, unique = true, length = LENGTH)
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
