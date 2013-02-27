package com.axiell.ehub.provider.elib.library;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

public class ElibUtilsTest {
    private static final String ISBN_13 = "9789173892377";
    private static final String ISBN_10 = "9173892378";
    @Test
    public void testISBN13to10() throws Exception {
        final String isbn10 = ElibUtils.generateIsbn13or10(ISBN_13);
        assertEquals(ISBN_10, isbn10);
        final String isbn13 = ElibUtils.generateIsbn13or10(ISBN_10);
        assertEquals(ISBN_13, isbn13);
        final String invalid = "0fn30i4fn3e0ifn143dp4dmo3";
        final String invalid2 = ElibUtils.generateIsbn13or10(invalid);
        assertEquals(invalid, invalid2);
        final String invalid3 = ElibUtils.generateIsbn13or10("1234567890");
        assertEquals("1234567890", invalid3);

    }
}
