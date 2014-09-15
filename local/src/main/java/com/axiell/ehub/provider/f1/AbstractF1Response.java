package com.axiell.ehub.provider.f1;

abstract class AbstractF1Response {
    protected final String value;

    protected AbstractF1Response(final String value) {
        this.value = value;
    }

    String getValue() {
        return value;
    }
}
