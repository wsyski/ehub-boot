package com.axiell.ehub.provider.ocd;

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
