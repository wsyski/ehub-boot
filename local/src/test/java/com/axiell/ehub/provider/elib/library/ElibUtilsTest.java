package com.axiell.ehub.provider.elib.library;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ElibUtilsTest {
    private static final String ISBN_13 = "9789173892377";
    private static final String ISBN_10 = "9173892378";
    private static final String INVALID_ISBN_1 = "0fn30i4fn3e0ifn143dp4dmo3";
    private static final String INVALID_ISBN_2 = "1234567890";
    private String currentIsbn;

    @Test
    public void testValidISBN13to10() throws Exception {
        whenIsbn13IsConvertedToIsbn10();
        thenIsbn10GeneratedIsTheExpectedOne();
    }

    @Test
    public void testInvalidISBN13to10withLetters() throws Exception {
        whenIsbnGivenIsInvalidWithLetters();
        thenIsbnIsSameAsInvalidIsbn1();
    }

    @Test
    public void testInvalidISBN13to10withNumbers() throws Exception {
        whenIsbnGivenIsInvalidWithNumbers();
        thenIsbnIsSameAsInvalidIsbn2();
    }

    @Test
    public void testValidISBN10to13() throws Exception {
        whenIsbn10IsConvertedToIsbn13();
        thenIsbn13GeneratedIsTheExpectedOne();
    }

    private void thenIsbnIsSameAsInvalidIsbn1() {
        assertEquals(INVALID_ISBN_1, currentIsbn);
    }

    private void thenIsbnIsSameAsInvalidIsbn2() {
            assertEquals(INVALID_ISBN_2, currentIsbn);
        }

    private void whenIsbnGivenIsInvalidWithLetters() {
        currentIsbn = ElibUtils.generateIsbn13or10(INVALID_ISBN_1);
    }

    private void whenIsbnGivenIsInvalidWithNumbers() {
        currentIsbn = ElibUtils.generateIsbn13or10(INVALID_ISBN_2);
    }

    private void thenIsbn13GeneratedIsTheExpectedOne() {
        assertEquals(ISBN_13, currentIsbn);
    }

    private void whenIsbn10IsConvertedToIsbn13() {
        currentIsbn = ElibUtils.generateIsbn13or10(ISBN_10);
    }

    private void thenIsbn10GeneratedIsTheExpectedOne() {
        assertEquals(ISBN_10, currentIsbn);
    }

    private void whenIsbn13IsConvertedToIsbn10() {
        currentIsbn = ElibUtils.generateIsbn13or10(ISBN_13);
    }
}
