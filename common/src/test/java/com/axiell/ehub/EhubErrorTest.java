package com.axiell.ehub;

import javax.xml.bind.JAXBException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.axiell.ehub.ErrorCauseArgument.Type;
import com.axiell.ehub.util.XjcSupport;

public class EhubErrorTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(EhubErrorTest.class);
    private String expXml;
    private EhubError expEhubError;
    private EhubError actEhubError;

    @Before
    public void setUp() {
	final ErrorCauseArgument argument = new ErrorCauseArgument(Type.LMS_LOAN_ID, "unknownLmsLoanId1");
	expEhubError = ErrorCause.LOAN_BY_LMS_LOAN_ID_NOT_FOUND.toEhubError(argument);
    }

    @Test
    public void unmarshalEhubErrorXml() throws JAXBException {
	givenEhubErrorAsXml();
	whenUnmarshalEhubErrorXml();
	thenActualEhubErrorEqualsExpectedEhubError();
    }

    private void givenEhubErrorAsXml() {
	expXml = XjcSupport.marshal(expEhubError);
	LOGGER.debug(expXml);
    }

    private void whenUnmarshalEhubErrorXml() throws JAXBException {
	actEhubError = (EhubError) XjcSupport.unmarshal(expXml);
    }

    private void thenActualEhubErrorEqualsExpectedEhubError() {
	Assert.assertEquals(expEhubError.getCause(), actEhubError.getCause());
	Assert.assertEquals(expEhubError.getMessage(), actEhubError.getMessage());
	Assert.assertNotNull(actEhubError.getArguments());
	Assert.assertEquals(expEhubError.getArguments().size(), actEhubError.getArguments().size());
    }
}
