package com.axiell.ehub.lms.palma;

public abstract class AbstractPalmaLoansService {
    protected static final String PALMA_CHECK_OUT_RESPONSE_XML = "com/axiell/ehub/lms/palma/CheckOutResponse.xml";
    protected static final String PALMA_CHECK_OUT_TEST_RESPONSE_XML = "com/axiell/ehub/lms/palma/CheckOutTestResponse.xml";

    private FileResponseUnmarshaller fileResponseUnmarshaller = new FileResponseUnmarshaller(getContextPath());

    protected abstract String getContextPath();

    public FileResponseUnmarshaller getFileResponseUnmarshaller() {
        return fileResponseUnmarshaller;
    }
}