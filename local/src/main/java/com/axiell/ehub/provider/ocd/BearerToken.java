package com.axiell.ehub.provider.ocd;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(value = {"result"})
public class BearerToken {
    private String bearer;

    public String getBearer() {
        return bearer;
    }

    @Override
    public String toString() {
        return "bearer " + bearer;
    }
}
