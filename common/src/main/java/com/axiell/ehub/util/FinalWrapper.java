package com.axiell.ehub.util;

public class FinalWrapper<T> {
    public final T value;

    public FinalWrapper(T value) {
        this.value = value;
    }
}
