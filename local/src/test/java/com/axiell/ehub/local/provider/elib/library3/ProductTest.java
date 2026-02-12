package com.axiell.ehub.local.provider.elib.library3;

import com.google.common.collect.Lists;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;


public class ProductTest {
    private Product underTest;
    private List<Product.AvailableFormat> formats;
    private Product.AvailableFormat pdf = ElibFormats.PDF.toAvailableFormat();
    private Product.AvailableFormat epubOffline = ElibFormats.EPUB_OFFLINE.toAvailableFormat();
    private Product.AvailableFormat epubOnline = ElibFormats.EPUB_ONLINE.toAvailableFormat();
    private Product.AvailableFormat flash = ElibFormats.FLASH.toAvailableFormat();
    private List<Product.AvailableFormat> actualFormats;

    @BeforeEach
    public void setUpUnderTest() {
        underTest = new Product();
    }

    @Test
    public void pdfButNotEpubOffline() {
        givenFlashEpubOnlinePdf();
        givenAvailableFormatsInProduct();
        whenGetFormats();
        thenActualFormatsContainsFlash();
        thenActualFormatsContainsEpubOnline();
        thenActualFormatsContainsPdf();
        thenActualFormatsDoesNotContainEpubOffline();
    }

    @Test
    public void epubOfflineButNotPdf() {
        givenFlashEpubOnlineEpubOffline();
        givenAvailableFormatsInProduct();
        whenGetFormats();
        thenActualFormatsContainsFlash();
        thenActualFormatsContainsEpubOnline();
        thenActualFormatsContainsEpubOffline();
        thenActualFormatsDoesNotContainPdf();
    }

    @Test
    public void pdfAndEpubOffline() {
        givenFlashEpubOnlinePdfEpubOffline();
        givenAvailableFormatsInProduct();
        whenGetFormats();
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

    private void givenAvailableFormatsInProduct() {
        ReflectionTestUtils.setField(underTest, "formats", formats);
    }

    private void whenGetFormats() {
        actualFormats = underTest.getFormats();
    }

    private void thenActualFormatsDoesNotContainPdf() {
        Assertions.assertFalse(actualFormats.contains(pdf));
    }

    private void thenActualFormatsContainsEpubOffline() {
        Assertions.assertTrue(actualFormats.contains(epubOffline));
    }

    private void thenActualFormatsContainsEpubOnline() {
        Assertions.assertTrue(actualFormats.contains(epubOnline));
    }

    private void thenActualFormatsContainsFlash() {
        Assertions.assertTrue(actualFormats.contains(flash));
    }

    private void thenActualFormatsDoesNotContainEpubOffline() {
        Assertions.assertFalse(actualFormats.contains(epubOffline));
    }

    private void thenActualFormatsContainsPdf() {
        Assertions.assertTrue(actualFormats.contains(pdf));
    }
}
