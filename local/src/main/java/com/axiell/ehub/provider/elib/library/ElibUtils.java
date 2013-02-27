package com.axiell.ehub.provider.elib.library;

import org.apache.commons.validator.routines.ISBNValidator;

public class ElibUtils {


    private static final String CheckDigits = "0123456789X0";
    private static final  ISBNValidator validator = ISBNValidator.getInstance();

    private static int CharToInt(char a) {
        return Character.getNumericValue(a);
    }

    public static final class IsbnConversionException extends Exception {

    }

    /**
     * Generates an ISBN10 with a valid ISBN13 as an input, and generates an ISBN13 with a valid ISBN10 as an input. if something in the conversion goes wrong, we return the same value we got.
     * @param isbn the isbn to get the counterpart for
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
        if (!validator.isValidISBN10(isbn10)) {
            throw new IsbnConversionException();
        }
        return validator.convertToISBN13(isbn10);
    }


    protected static String isbn13To10(final String isbn13) throws IsbnConversionException {
        if (!validator.isValidISBN13(isbn13)) throw new IsbnConversionException();
        String s9;
        int i, n, v;
        s9 = isbn13.substring(3, 12);
        n = 0;
        for (i = 0; i < 9; i++) {
            v = CharToInt(s9.charAt(i));
            if (v == -1) {
                throw new IsbnConversionException();
            } else {
                n = n + (10 - i) * v;
            }
        }
        n = 11 - (n % 11);
        return s9 + CheckDigits.substring(n, n + 1);
    }


}