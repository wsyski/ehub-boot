/*
 * Copyright (c) 2012 Axiell Group AB.
 */
package com.axiell.ehub.provider;

import com.axiell.ehub.language.ILanguageAdminController;
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
import com.axiell.ehub.provider.record.format.IFormatAdminController;

/**
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/com/axiell/ehub/admin-controller-context.xml"})
public class ContentProviderRepositoryTest extends AbstractEhubRepositoryTest<DevelopmentData> {

    @Autowired
    private IContentProviderAdminController contentProviderAdminController;

    @Autowired
    private IFormatAdminController formatAdminController;

    @Autowired
    private IConsumerAdminController consumerAdminController;

    @Autowired
    private ILanguageAdminController languageAdminController;

    /**
     * @see com.axiell.ehub.AbstractEhubRepositoryTest#initDevelopmentData()
     */
    @Override
    protected DevelopmentData initDevelopmentData() {
        return new DevelopmentData(contentProviderAdminController, formatAdminController, consumerAdminController, languageAdminController);
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
}
