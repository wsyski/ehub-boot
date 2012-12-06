/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import java.util.Locale;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.axiell.ehub.AbstractEhubRepositoryTest;
import com.axiell.ehub.DevelopmentData;
import com.axiell.ehub.consumer.IConsumerAdminController;
import com.axiell.ehub.provider.record.format.FormatDecoration;
import com.axiell.ehub.provider.record.format.FormatDecoration.ContentDisposition;
import com.axiell.ehub.provider.record.format.FormatTextBundle;
import com.axiell.ehub.provider.record.format.IFormatAdminController;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:/com/axiell/ehub/admin-controller-context.xml")
public class ContentProviderRepositoryTest extends AbstractEhubRepositoryTest<DevelopmentData> {

    @Autowired
    private IContentProviderAdminController contentProviderAdminController;

    @Autowired
    private IFormatAdminController formatAdminController;

    @Autowired
    private IConsumerAdminController consumerAdminController;   

    /**
     * @see com.axiell.ehub.AbstractEhubRepositoryTest#initDevelopmentData()
     */
    @Override
    protected DevelopmentData initDevelopmentData() {
        return new DevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController);
    }
    
    /**
     * 
     */
    @Test
    @Rollback(true)
    public void testElibContentProvider() {
        ContentProvider contentProvider = contentProviderAdminController.getContentProvider(ContentProviderName.ELIB);
        Assert.assertEquals(ContentProviderName.ELIB, contentProvider.getName());
        Assert.assertEquals(3, contentProvider.getProperties().size());
    }

    /**
     * 
     */
    @Test
    @Rollback(true)
    public void testElibUContentProvider() {
        ContentProvider contentProvider = contentProviderAdminController.getContentProvider(ContentProviderName.ELIBU);
        Assert.assertEquals(ContentProviderName.ELIBU, contentProvider.getName());
        Assert.assertEquals(4, contentProvider.getFormatDecorations().size());

        FormatDecoration formatDecoration0 = contentProvider.getFormatDecoration(DevelopmentData.ELIBU_FORMAT_0_ID);
        Assert.assertEquals(DevelopmentData.ELIBU_FORMAT_0_ID, formatDecoration0.getContentProviderFormatId());
        Assert.assertEquals(ContentDisposition.STREAMING, formatDecoration0.getContentDisposition());
        Assert.assertEquals(1, formatDecoration0.getTextBundles().size());
        FormatTextBundle textBundle0 = formatDecoration0.getTextBundle(Locale.ENGLISH.getLanguage());
        Assert.assertEquals(Locale.ENGLISH.getLanguage(), textBundle0.getLanguage());
        Assert.assertEquals(DevelopmentData.ELIBU_FORMAT_0_NAME, textBundle0.getName());
        Assert.assertEquals(DevelopmentData.ELIBU_FORMAT_0_DESCRIPTION, textBundle0.getDescription());

        FormatDecoration formatDecoration1 = contentProvider.getFormatDecoration(DevelopmentData.ELIBU_FORMAT_1_ID);
        Assert.assertEquals(DevelopmentData.ELIBU_FORMAT_1_ID, formatDecoration1.getContentProviderFormatId());
        Assert.assertEquals(ContentDisposition.STREAMING, formatDecoration1.getContentDisposition());
        Assert.assertEquals(1, formatDecoration1.getTextBundles().size());
        FormatTextBundle textBundle1 = formatDecoration1.getTextBundle(Locale.ENGLISH.getLanguage());
        Assert.assertEquals(Locale.ENGLISH.getLanguage(), textBundle1.getLanguage());
        Assert.assertEquals(DevelopmentData.ELIBU_FORMAT_1_NAME, textBundle1.getName());
        Assert.assertEquals(DevelopmentData.ELIBU_FORMAT_1_DESCRIPTION, textBundle1.getDescription());
    }
}
