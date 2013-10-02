package com.axiell.ehub.lms.palma;

public abstract class AbstractPalmaLoansService {

    private FileResponseUnmarshaller fileResponseUnmarshaller = new FileResponseUnmarshaller(getContextPath());

    protected abstract String getContextPath();

    public FileResponseUnmarshaller getFileResponseUnmarshaller() {
        return fileResponseUnmarshaller;
    }
}