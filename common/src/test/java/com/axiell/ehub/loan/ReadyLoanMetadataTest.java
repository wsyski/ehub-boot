package com.axiell.ehub.loan;

import com.axiell.ehub.language.Language;
import com.axiell.ehub.provider.ContentProvider;
import com.axiell.ehub.provider.ContentProviderName;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import com.axiell.ehub.util.XjcSupport;
import com.google.common.collect.Maps;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBException;
import java.util.Collections;
import java.util.Date;
import java.util.Map;

public class ReadyLoanMetadataTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(ReadyLoanMetadataTest.class);
    public static final String LANG_CODE_EN = "en";
    public static final String LANG_CODE_SV = "sv";
    private String expXml;
    private ReadyLoanMetadata expReadyLoanMetadata;
    private ReadyLoanMetadata actReadyLoanMetadata;

    @Before
    public void setUp() {
        LmsLoan lmsLoan = new LmsLoan("lmsLoanId");
        ContentProviderLoanMetadata contentProviderLoanMetadata = initContentProviderLoanMetadata();
        expReadyLoanMetadata = new ReadyLoanMetadata(1L, lmsLoan, contentProviderLoanMetadata);
    }

    private ContentProviderLoanMetadata initContentProviderLoanMetadata() {
        ContentProvider contentProvider = initContentProvider();
        FormatDecoration formatDecoration = new FormatDecoration(contentProvider, "58", FormatDecoration.ContentDisposition.DOWNLOADABLE, 10, 100);
        Map<Language, FormatTextBundle> textBundles = initTextBundles(formatDecoration);
        formatDecoration.setTextBundles(textBundles);
        return new ContentProviderLoanMetadata.Builder(contentProvider, new Date(), "contentProviderRecordId", formatDecoration).contentProviderLoanId(
                "contentProviderLoanId").build();
    }

    private ContentProvider initContentProvider() {
        Map<ContentProvider.ContentProviderPropertyKey, String> properties = Collections.singletonMap(ContentProvider.ContentProviderPropertyKey.PRODUCT_URL, "productUrl");
        return new ContentProvider(ContentProviderName.ELIB, properties);
    }

    private Map<Language, FormatTextBundle> initTextBundles(FormatDecoration formatDecoration) {
        Language en = new Language(LANG_CODE_EN);
        FormatTextBundle textBundleEn = new FormatTextBundle(formatDecoration, en, "name1", "description1");
        Language sv = new Language(LANG_CODE_SV);
        FormatTextBundle textBundleSv = new FormatTextBundle(formatDecoration, en, "namn", "beskrivning");

        Map<Language, FormatTextBundle> textBundleMap = Maps.newHashMap();
        textBundleMap.put(en, textBundleEn);
        textBundleMap.put(sv, textBundleSv);
        return textBundleMap;
    }

    @Test
    public void unmarshalReadyLoanMetadataXml() throws JAXBException {
        givenReadyLoanMetadataAsXml();
        whenUnmarshalReadyLoanMetadataXml();
        thenActualEhubLoanEqualsExpectedEhubLoan();
    }

    private void givenReadyLoanMetadataAsXml() {
        expXml = XjcSupport.marshal(expReadyLoanMetadata);
        LOGGER.debug(expXml);
    }

    private void whenUnmarshalReadyLoanMetadataXml() throws JAXBException {
        actReadyLoanMetadata = (ReadyLoanMetadata) XjcSupport.unmarshal(expXml);
    }

    private void thenActualEhubLoanEqualsExpectedEhubLoan() {
        thenActualLmsLoanEqualsExpectedLmsLoan();
        thenActualContentProviderLoanMetadataEqualsExpectedContentProviderLoanMetadata();
    }

    private void thenActualLmsLoanEqualsExpectedLmsLoan() {
        Assert.assertEquals(expReadyLoanMetadata.getLmsLoan(), actReadyLoanMetadata.getLmsLoan());
    }

    private void thenActualContentProviderLoanMetadataEqualsExpectedContentProviderLoanMetadata() {
        ContentProviderLoanMetadata expContentProviderLoan = expReadyLoanMetadata.getContentProviderLoanMetadata();
        ContentProviderLoanMetadata actContentProviderLoan = actReadyLoanMetadata.getContentProviderLoanMetadata();
        Assert.assertEquals(expContentProviderLoan.getId(), actContentProviderLoan.getId());
        Assert.assertEquals(expContentProviderLoan.getExpirationDate(), actContentProviderLoan.getExpirationDate());
        thenActualLoanMetadataEqualsExpectedLoanMetadata(expContentProviderLoan, actContentProviderLoan);
    }

    private void thenActualLoanMetadataEqualsExpectedLoanMetadata(ContentProviderLoanMetadata expMetadata, ContentProviderLoanMetadata actMetadata) {
        Assert.assertEquals(expMetadata.getId(), actMetadata.getId());
        Assert.assertEquals(expMetadata.getExpirationDate(), actMetadata.getExpirationDate());
        Assert.assertEquals(expMetadata.getFormatDecoration().getContentProviderFormatId(), actMetadata.getFormatDecoration().getContentProviderFormatId());
        Assert.assertEquals(expMetadata.getFormatDecoration().getPlayerHeight(), actMetadata.getFormatDecoration().getPlayerHeight());
        Assert.assertEquals(expMetadata.getFormatDecoration().getPlayerWidth(), actMetadata.getFormatDecoration().getPlayerWidth());
        Assert.assertEquals(expMetadata.getFormatDecoration().getContentDisposition(), actMetadata.getFormatDecoration().getContentDisposition());
        Assert.assertEquals(expMetadata.getFormatDecoration().getTextBundle(LANG_CODE_EN).getDescription(), actMetadata.getFormatDecoration().getTextBundle(LANG_CODE_EN).getDescription());
        Assert.assertEquals(expMetadata.getFormatDecoration().getTextBundle(LANG_CODE_EN).getName(), actMetadata.getFormatDecoration().getTextBundle(LANG_CODE_EN).getName());
        Assert.assertEquals(expMetadata.getFormatDecoration().getTextBundle(LANG_CODE_SV).getDescription(), actMetadata.getFormatDecoration().getTextBundle(LANG_CODE_SV).getDescription());
        Assert.assertEquals(expMetadata.getFormatDecoration().getTextBundle(LANG_CODE_SV).getName(), actMetadata.getFormatDecoration().getTextBundle(LANG_CODE_SV).getName());
        Assert.assertNull(actMetadata.getContentProvider());
    }
}
