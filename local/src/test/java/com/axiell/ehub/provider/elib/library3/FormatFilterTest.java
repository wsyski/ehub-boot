package com.axiell.ehub.provider.elib.library3;

import com.google.common.collect.Lists;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

public class FormatFilterTest {
    private FormatFilter underTest;
    private List<Product.AvailableFormat> formats;
    private Product.AvailableFormat pdf = ElibFormats.PDF.toAvailableFormat();
    private Product.AvailableFormat epubOffline = ElibFormats.EPUB_OFFLINE.toAvailableFormat();
    private Product.AvailableFormat epubOnline = ElibFormats.EPUB_ONLINE.toAvailableFormat();
    private Product.AvailableFormat flash = ElibFormats.FLASH.toAvailableFormat();
    private List<Product.AvailableFormat> actualFormats;

    @Before
    public void setUpUnderTest() {
        underTest = new FormatFilter();
    }

    @Test
    public void pdfButNotEpubOffline() {
        givenFlashEpubOnlinePdf();
        whenApplyFilter();
        thenActualFormatsContainsFlash();
        thenActualFormatsContainsEpubOnline();
        thenActualFormatsContainsPdf();
        thenActualFormatsDoesNotContainEpubOffline();
    }

    @Test
    public void epubOfflineButNotPdf() {
        givenFlashEpubOnlineEpubOffline();
        whenApplyFilter();
        thenActualFormatsContainsFlash();
        thenActualFormatsContainsEpubOnline();
        thenActualFormatsContainsEpubOffline();
        thenActualFormatsDoesNotContainPdf();
    }

    @Test
    public void pdfAndEpubOffline() {
        givenFlashEpubOnlinePdfEpubOffline();
        whenApplyFilter();
        thenActualFormatsContainsFlash();
        thenActualFormatsContainsEpubOnline();
        thenActualFormatsContainsEpubOffline();
        thenActualFormatsDoesNotContainPdf();
    }

    private void givenFlashEpubOnlinePdf() {
        formats = Lists.newArrayList(flash, epubOnline, pdf);
    }

    private void givenFlashEpubOnlineEpubOffline() {
        formats = Lists.newArrayList(flash, epubOnline, epubOffline);
    }

    private void givenFlashEpubOnlinePdfEpubOffline() {
        formats = Lists.newArrayList(flash, epubOnline, pdf, epubOffline);
    }

    private void whenApplyFilter() {
        actualFormats = underTest.applyFilter(formats);
    }

    private void thenActualFormatsDoesNotContainPdf() {
        assertFalse(actualFormats.contains(pdf));
    }

    private void thenActualFormatsContainsEpubOffline() {
        assertTrue(actualFormats.contains(epubOffline));
    }

    private void thenActualFormatsContainsEpubOnline() {
        assertTrue(actualFormats.contains(epubOnline));
    }

    private void thenActualFormatsContainsFlash() {
        assertTrue(actualFormats.contains(flash));
    }

    private void thenActualFormatsDoesNotContainEpubOffline() {
        assertFalse(actualFormats.contains(epubOffline));
    }

    private void thenActualFormatsContainsPdf() {
        assertTrue(actualFormats.contains(pdf));
    }

}
