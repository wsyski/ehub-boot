package com.axiell.ehub.provider.ocd;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
