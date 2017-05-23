package com.axiell.authinfo;

public abstract class AbstractAuthHeaderSecretKeyResolver implements IAuthHeaderSecretKeyResolver {
    private boolean isValidate = true;
    private boolean isDebug = false;

    @Override
    public boolean isValidate() {
        return isValidate;
    }

    @Override
    public boolean isDebug() {
        return isDebug;
    }


    public void setValidate(final boolean isValidate) {
        this.isValidate = isValidate;
    }

    public void setDebug(boolean isDebug) {
        this.isDebug = isDebug;
    }
}