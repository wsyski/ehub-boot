package com.axiell.ehub.provider.elib.library;

import org.apache.commons.validator.routines.ISBNValidator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class ElibUtils {
    private static final String CHECK_DIGITS = "0123456789X0";
    private static final ISBNValidator VALIDATOR = ISBNValidator.getInstance();
    static final int ELIB_STATUS_CODE_OK = 101;
    static final DateTimeFormatter ELIB_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Private constructor that prevents direct instantiation.
     */
    private ElibUtils() {
    }

    private static int charToInt(char a) {
	return Character.getNumericValue(a);
    }

    public static final class IsbnConversionException extends Exception {
    }

    /**
     * Generates an ISBN10 with a valid ISBN13 as an input, and generates an
     * ISBN13 with a valid ISBN10 as an input. if something in the conversion
     * goes wrong, we return the same value we got.
     * 
     * @param isbn
     *            the isbn to get the counterpart for
     * @return an ISBN13 or ISBN10
     */
    public static String generateIsbn13or10(final String isbn) {
	try {
	    switch (isbn.length()) {
	    case 10:
		return isbn10To13(isbn);
	    case 13:
		return isbn13To10(isbn);
	    default:
		return isbn;
	    }
	} catch (IsbnConversionException e) {
	    return isbn;
	}
    }

    protected static String isbn10To13(final String isbn10) throws IsbnConversionException {
	if (!VALIDATOR.isValidISBN10(isbn10)) {
	    throw new IsbnConversionException();
	}
	return VALIDATOR.convertToISBN13(isbn10);
    }

    protected static String isbn13To10(final String isbn13) throws IsbnConversionException {
	if (!VALIDATOR.isValidISBN13(isbn13))
	    throw new IsbnConversionException();
	String s9;
	int i, n, v;
	s9 = isbn13.substring(3, 12);
	n = 0;
	for (i = 0; i < 9; i++) {
	    v = charToInt(s9.charAt(i));
	    if (v == -1) {
		throw new IsbnConversionException();
	    } else {
		n = n + (10 - i) * v;
	    }
	}
	n = 11 - (n % 11);
	return s9 + CHECK_DIGITS.substring(n, n + 1);
    }

}