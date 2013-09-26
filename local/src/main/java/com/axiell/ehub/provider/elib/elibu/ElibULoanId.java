package com.axiell.ehub.provider.elib.elibu;

import java.text.MessageFormat;

import com.axiell.ehub.InternalServerErrorException;

class ElibULoanId {
    private static final String ELIBU_LOAN_ID_PATTERN = "{0}|{1}|{2}";
    private static final String ELIBU_LOAN_ID_SPLIT_PATTERN = "\\|";
    private static final int ELIBU_LOAN_ID_PARTS = 3;
    private static final int ELIBU_RECORD_ID_INDEX = 1;

    private final String recordId;
    private final String value;

    private ElibULoanId(String contentProviderLoanId) {
	recordId = findRecordId(contentProviderLoanId);
	value = contentProviderLoanId;	
    }

    private ElibULoanId(Integer licenseId, String recordId, String formatId) {
	this.recordId = recordId;
	value = MessageFormat.format(ELIBU_LOAN_ID_PATTERN, licenseId, recordId, formatId);
    }

    static ElibULoanId create(Integer licenseId, String recordId, String formatId) {
	return new ElibULoanId(licenseId, recordId, formatId);
    }

    static ElibULoanId fromContentProviderLoanId(String contentProviderLoanId) {
	return new ElibULoanId(contentProviderLoanId);
    }

    private String findRecordId(String contentProviderLoanId) {
	validateProvidedContentProviderLoanId(contentProviderLoanId);	
	final String[] parts = contentProviderLoanId.split(ELIBU_LOAN_ID_SPLIT_PATTERN);
	
	if (expectedNumberOfParts(parts))
	    return parts[ELIBU_RECORD_ID_INDEX];

	throw new InternalServerErrorException("The provided ElibU loan ID '" + contentProviderLoanId
		+ "' is in the wrong format, it should be in the format '" + ELIBU_LOAN_ID_PATTERN + "'");
    }

    private void validateProvidedContentProviderLoanId(String contentProviderLoanId) {
	if (contentProviderLoanId == null)
	    throw new InternalServerErrorException("The content provided ElibU loan ID is null");
    }

    private boolean expectedNumberOfParts(final String[] parts) {
	return parts != null && parts.length == ELIBU_LOAN_ID_PARTS;
    }

    String getRecordId() {
	return recordId;
    }

    @Override
    public String toString() {
	return value;
    }
}
