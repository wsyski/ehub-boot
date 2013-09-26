package com.axiell.ehub.provider.elib.elibu;

import static com.axiell.ehub.EhubAssert.thenInternalServerErrorExceptionIsThrown;
import junit.framework.Assert;

import org.junit.Test;

import com.axiell.ehub.InternalServerErrorException;

public class ElibULoanIdTest {
    private Integer licenseId = 1;
    private String recordId = "recordId";
    private String formatId = "formatID";
    private String validLoanId = licenseId + "|" + recordId + "|" + formatId;
    private String invalidLoanId = licenseId + "|" + recordId;
    private ElibULoanId actualLoanId;

    @Test
    public void create() {
	whenCreate();
	thenActualLoanIdEqualsExpectedLoanId();
    }

    private void whenCreate() {
	actualLoanId = ElibULoanId.create(licenseId, recordId, formatId);
    }
    
    private void thenActualLoanIdEqualsExpectedLoanId() {
	Assert.assertEquals(validLoanId, actualLoanId.toString());
    }
    
    @Test
    public void fromContentProviderLoanId() {
	whenFromContentProviderLoanIdWithValidLoanId();
	thenActualRecordIdEqualsExpectedRecordId();
    }

    private void whenFromContentProviderLoanIdWithValidLoanId() {
	actualLoanId = ElibULoanId.fromContentProviderLoanId(validLoanId);
    }
    
    private void thenActualRecordIdEqualsExpectedRecordId() {
	Assert.assertEquals(recordId, actualLoanId.getRecordId());
    }
    
    @Test
    public void fromContentProviderLoanIdWhenNullAsContentProviderLoanId() {
	try {
	    whenFromContentProviderLoanIdWithNullAsLoanId();
	} catch (InternalServerErrorException e) {
	    thenInternalServerErrorExceptionIsThrown(e);
	}
    }

    private void whenFromContentProviderLoanIdWithNullAsLoanId() {
	actualLoanId = ElibULoanId.fromContentProviderLoanId(null);
    }
    
    @Test
    public void fromContentProviderLoanIdWhenInvalidContentProviderLoanId() {
	try {
	    whenFromContentProviderLoanIdWithInvalidLoanId();
	} catch (InternalServerErrorException e) {
	    thenInternalServerErrorExceptionIsThrown(e);
	} 
    }

    private void whenFromContentProviderLoanIdWithInvalidLoanId() {
	actualLoanId = ElibULoanId.fromContentProviderLoanId(invalidLoanId);
    }
}
