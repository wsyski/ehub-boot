package com.axiell.ehub.provider.elib.library;

import org.apache.commons.validator.routines.ISBNValidator;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public final class ElibUtils {
    private static final String CHECK_DIGITS = "0123456789X0";
    private static final ISBNValidator VALIDATOR = ISBNValidator.getInstance();
    static final int ELIB_STATUS_CODE_OK = 101;
    static final DateTimeFormatter ELIB_DATE_FORMAT = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    private static final int ISBN_10 = 10;
    private static final int ISBN_13 = 13;
    private static final int ISBN10_MAX_LENGTH = 9;
    private static final int ISBN10_START_POSITION = 0;
    private static final int ISBN10_CHECKSUM_CONSTANT = 11;

    /**
     * Private constructor that prevents direct instantiation.
     */
    private ElibUtils() {
    }

    private static int charToInt(char a) {
        return Character.getNumericValue(a);
    }


    /**
     * Generates an ISBN10 with a valid ISBN13 as an input, and generates an
     * ISBN13 with a valid ISBN10 as an input. if something in the conversion
     * goes wrong, we return the same value we got.
     *
     * @param isbn the isbn to get the counterpart for
     * @return an ISBN13 or ISBN10
     */
    public static String generateIsbn13or10(final String isbn) {
        try {
            switch (isbn.length()) {
                case ISBN_10:
                    return isbn10To13(isbn);
                case ISBN_13:
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
        if (!VALIDATOR.isValidISBN13(isbn13)) {
            throw new IsbnConversionException();
        }
        return calculateIsbn10FromIsbn13(isbn13);
    }

    private static String calculateIsbn10FromIsbn13(final String isbn13) throws IsbnConversionException {
        String isbn10WithoutCheckDigits;
        int isbn10DigitIterator, isbn10ChecksumDigest, currentIsbn10Digit;
        isbn10WithoutCheckDigits = getIsbn10FromIsbn13WithoutCheckDigits(isbn13);
        isbn10ChecksumDigest = 0;
        for (isbn10DigitIterator = ISBN10_START_POSITION; isbn10DigitIterator < ISBN10_MAX_LENGTH; isbn10DigitIterator++) {
            currentIsbn10Digit = getCurrentIsbn10Digit(isbn10WithoutCheckDigits, isbn10DigitIterator);
            if (currentIsbn10Digit == -1) {
                throw new IsbnConversionException();
            } else {
                isbn10ChecksumDigest = addDigitToisbn10ChecksumDigest(isbn10DigitIterator, isbn10ChecksumDigest, currentIsbn10Digit);
            }
        }
        return doCalculateIsbn10WithCheckDigit(isbn10WithoutCheckDigits, isbn10ChecksumDigest);
    }

    private static String doCalculateIsbn10WithCheckDigit(final String isbn10WithoutCheckDigits, int isbn10ChecksumDigest) {
        isbn10ChecksumDigest = ISBN10_CHECKSUM_CONSTANT - (isbn10ChecksumDigest % ISBN10_CHECKSUM_CONSTANT);
        return isbn10WithoutCheckDigits + CHECK_DIGITS.substring(isbn10ChecksumDigest, isbn10ChecksumDigest + 1);
    }

    private static int addDigitToisbn10ChecksumDigest(final int isbn10DigitIterator, final int n, final int currentIsbn10Digit) {
        return n + (10 - isbn10DigitIterator) * currentIsbn10Digit;
    }

    private static int getCurrentIsbn10Digit(final String isbn10WithoutCheckDigits, final int i) {
        return charToInt(isbn10WithoutCheckDigits.charAt(i));
    }

    private static String getIsbn10FromIsbn13WithoutCheckDigits(final String isbn13) {
        return isbn13.substring(3, 12);
    }

    private static class IsbnConversionException extends Exception {
    }
}